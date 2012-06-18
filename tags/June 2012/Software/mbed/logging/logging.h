#ifndef __LOGGING_H
#define __LOGGING_H

#include "mbed.h"
#include "SystemState.h"

FILE *openlog(char *prefix);
bool initLogfile(void);
void clearState( SystemState *s );
void logData( SystemState s );
void closeLogfile(void);

#endif