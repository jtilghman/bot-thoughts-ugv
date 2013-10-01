/*
  TinyGPS - a small GPS library for Arduino providing basic NMEA parsing
  Copyright (C) 2008-9 Mikal Hart
  All rights reserved.

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
  
  Ported to mbed by Michael Shimniok http://www.bot-thoughts.com/
*/

#include "TinyGPS.h"

#define _GPRMC_TERM   "GPRMC"
#define _GPGGA_TERM   "GPGGA"
#define _GPGSV_TERM   "GPGSV"

TinyGPS::TinyGPS()
:  _time(GPS_INVALID_TIME)
,  _date(GPS_INVALID_DATE)
,  _latitude(GPS_INVALID_ANGLE)
,  _longitude(GPS_INVALID_ANGLE)
,  _altitude(GPS_INVALID_ALTITUDE)
,  _speed(GPS_INVALID_SPEED)
,  _course(GPS_INVALID_ANGLE)
,  _hdop(0)
,  _sat_count(0)
,  _last_time_fix(GPS_INVALID_FIX_TIME)
,  _last_position_fix(GPS_INVALID_FIX_TIME)
,  _parity(0)
,  _is_checksum_term(false)
,  _sentence_type(_GPS_SENTENCE_OTHER)
,  _term_number(0)
,  _term_offset(0)
,  _gps_data_good(false)
,  _rmc_ready(false)
,  _gga_ready(false)
,  _gsv_ready(false)
#ifndef _GPS_NO_STATS
,  _encoded_characters(0)
,  _good_sentences(0)
,  _failed_checksum(0)
#endif
{
  _term[0] = '\0';
}

//
// public methods
//

bool TinyGPS::encode(char c)
{
  bool valid_sentence = false;

  ++_encoded_characters;
  switch(c)
  {
  case ',': // term terminators
    _parity ^= c;
  case '\r':
  case '\n':
  case '*':
    if (_term_offset < sizeof(_term))
    {
      _term[_term_offset] = 0;
      valid_sentence = term_complete();
    }
    ++_term_number;
    _term_offset = 0;
    _is_checksum_term = c == '*';
    return valid_sentence;

  case '$': // sentence begin
    _term_number = _term_offset = 0;
    _parity = 0;
    _sentence_type = _GPS_SENTENCE_OTHER;
    _is_checksum_term = false;
    _gps_data_good = false;
    return valid_sentence;
  }

  // ordinary characters
  if (_term_offset < sizeof(_term) - 1)
    _term[_term_offset++] = c;
  if (!_is_checksum_term)
    _parity ^= c;

  return valid_sentence;
}

#ifndef _GPS_NO_STATS
void TinyGPS::stats(unsigned long *chars, unsigned short *sentences, unsigned short *failed_cs)
{
  if (chars) *chars = _encoded_characters;
  if (sentences) *sentences = _good_sentences;
  if (failed_cs) *failed_cs = _failed_checksum;
}
#endif

//
// internal utilities
//
int TinyGPS::from_hex(char a) 
{
  if (a >= 'A' && a <= 'F')
    return a - 'A' + 10;
  else if (a >= 'a' && a <= 'f')
    return a - 'a' + 10;
  else
    return a - '0';
}

int TinyGPS::parse_int()
{
  char *p = _term;
  bool isneg = *p == '-';
  if (isneg) ++p;
  while (*p == '0') ++p;
  int ret = gpsatol(p);
  return isneg ? -ret : ret;
}

unsigned long TinyGPS::parse_decimal()
{
  char *p = _term;
  bool isneg = *p == '-';
  if (isneg) ++p;
  unsigned long ret = 100UL * gpsatol(p);
  while (gpsisdigit(*p)) ++p;
  if (*p == '.')
  {
    if (gpsisdigit(p[1]))
    {
      ret += 10 * (p[1] - '0');
      if (gpsisdigit(p[2]))
        ret += p[2] - '0';
    }
  }
  return isneg ? -ret : ret; // TODO:  can't return - when we're returning an unsigned!
}

// mes 04/27/12 increased fractional precision to 7 digits, was 5
unsigned long TinyGPS::parse_degrees()
{
  char *p;
  unsigned long left = gpsatol(_term);
  unsigned long tenk_minutes = (left % 100UL) * 1000000UL;
  for (p=_term; gpsisdigit(*p); ++p);
  if (*p == '.')
  {
    unsigned long mult = 100000;
    while (gpsisdigit(*++p))
    {
      tenk_minutes += mult * (*p - '0');
      mult /= 10;
    }
  }
  return (left / 100) * 10000000 + tenk_minutes / 6;
}

// Processes a just-completed term
// Returns true if new sentence has just passed checksum test and is validated
bool TinyGPS::term_complete()
{
    if (_is_checksum_term) {
    
        byte checksum = 16 * from_hex(_term[0]) + from_hex(_term[1]);
        if (checksum == _parity) {
        
            if (_gps_data_good) {
            
#ifndef _GPS_NO_STATS
            ++_good_sentences;
#endif
            _last_time_fix = _new_time_fix;
            _last_position_fix = _new_position_fix;

            switch(_sentence_type) {
            case _GPS_SENTENCE_GPRMC:
                _time      = _new_time;
                _date      = _new_date;
                _latitude  = _new_latitude;
                _longitude = _new_longitude;
                _speed     = _new_speed;
                _course    = _new_course;
                _rmc_ready = true;
                break;
            case _GPS_SENTENCE_GPGGA:
                _altitude  = _new_altitude;
                _time      = _new_time;
                _latitude  = _new_latitude;
                _longitude = _new_longitude;
                _gga_ready = true;
                _hdop      = _new_hdop;
                _sat_count = _new_sat_count;
                break;
            case _GPS_SENTENCE_GPGSV:
                _gsv_ready = true;
                break;
            }

            return true;
        }
    }

#ifndef _GPS_NO_STATS
    else
        ++_failed_checksum;
#endif
    return false;
    }

    // the first term determines the sentence type
    if (_term_number == 0) {
        if (!gpsstrcmp(_term, _GPRMC_TERM))
            _sentence_type = _GPS_SENTENCE_GPRMC;
        else if (!gpsstrcmp(_term, _GPGGA_TERM))
            _sentence_type = _GPS_SENTENCE_GPGGA;
        else if (!gpsstrcmp(_term, _GPGSV_TERM))
            _sentence_type = _GPS_SENTENCE_GPGSV;
        else
            _sentence_type = _GPS_SENTENCE_OTHER;
        return false;
    }

    if (_sentence_type != _GPS_SENTENCE_OTHER && _term[0])
    /*
    if (_sentence_type == _GPS_SENTENCE_GPGSV) {
        // $GPGSV,3,1,12,05,54,069,45,12,44,061,44,21,07,184,46,22,78,289,47*72<CR><LF>
        // TODO: need to maintain a list of 12 structs with sat info and update it each time
        switch (_term_number) {
        case 0 : // number of messages
            break;
        case 1 : // sequence number
            break;
        case 2 : // satellites in view 
            break;
        case 3 : // sat ID
        case 8 :
        case 12 :
        case 16 :
            break;
        case 4 : // elevation
        case 9 :
        case 13 :
        case 17 :
            break;
        case 5 : // azimuth
        case 10 :
        case 14 :
        case 18 :
            break;
        case 6 : // SNR
        case 11 :
        case 15 :
        case 19 :
            break;
        }
    } else {*/
        switch (((_sentence_type == _GPS_SENTENCE_GPGGA) ? 200 : 100) + _term_number) {
        case 101: // Time in both sentences
        case 201:
          _new_time = parse_decimal();
          _new_time_fix = millis();
          break;
        case 102: // GPRMC validity
          _gps_data_good = _term[0] == 'A';
          break;
        case 103: // Latitude
        case 202:
          _new_latitude = parse_degrees();
          _new_position_fix = millis();
          break;
        case 104: // N/S
        case 203:
          if (_term[0] == 'S')
            _new_latitude = -_new_latitude;
          break;
        case 105: // Longitude
        case 204:
          _new_longitude = parse_degrees();
          break;
        case 106: // E/W
        case 205:
          if (_term[0] == 'W')
            _new_longitude = -_new_longitude;
          break;
        case 107: // Speed (GPRMC)
          _new_speed = parse_decimal();
          break;
        case 108: // Course (GPRMC)
          _new_course = parse_decimal();
          break;
        case 109: // Date (GPRMC)
          _new_date = gpsatol(_term);
          break;
        case 206: // Fix data (GPGGA)
          _gps_data_good = _term[0] > '0';
          break;
        case 207: // Number of satelites tracked (GPGGA)
          _new_sat_count = parse_int();
          break;
        case 208: // Horizontal Dilution of Position (GPGGA)
          _new_hdop = parse_decimal();
          break;
        case 209: // Altitude (GPGGA)
          _new_altitude = parse_decimal();
          break;
        default :
          break;
        } /* switch */
    //}

  return false;
}

long TinyGPS::gpsatol(const char *str)
{
  long ret = 0;
  while (gpsisdigit(*str))
    ret = 10 * ret + *str++ - '0';
  return ret;
}

int TinyGPS::gpsstrcmp(const char *str1, const char *str2)
{
  while (*str1 && *str1 == *str2)
    ++str1, ++str2;
  return *str1;
}
