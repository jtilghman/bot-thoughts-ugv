<img width='400' src='https://lh3.googleusercontent.com/-CKUIEDf3U2c/UwKSRZ7fnMI/AAAAAAAAJ1k/QKhl_csmRLE/w1024-h682-no/IMG_9923.JPG' />

These instructions apply to Rev 0.4

[Older Rev 0.3 RoverMux Wiki](RCMultiplexer.md)

# Introduction #

When your robot is about to crash, the RoverMux lets you take control of your self-driving robot. The 3-channel multiplexer is a dedicated board that keeps running even if your MCU doesn't.

# Source #

Schematics and firmware:
  * [Rev 0.4](https://code.google.com/p/bot-thoughts-ugv/source/browse/#svn%2Ftags%2FRoverMux_0.4)

# Instructions #

  * Use #2 hardware to install
  * Pins are labeled with channel (1, 2, 3)
    * Connect "R/C" ports to the R/C Transmitter (TX)
    * Connect "MCU" ports to the microcontroller (autopilot)
    * Connect "OUT" ports to the outputs: ESC, Steering, etc.
  * Ports are color coded with familiar R/C colors.
    * Yellow: Signal
    * Red: 5-6V from BEC
    * Black: GND
  * Use servos that respond to 3.3V signals (nearly all do).
  * Want to leave your transmitter on and take over with CH3 switch?
    * Solder the CH3 jumper
  * Want the MCU to control something on CH3?
    * Connect MCU CH3 output to 3MCU
    * Connect device to be controlled to 3OUT
    * Leave 3R/C disconnected or unsolder the CH3 jumper
    * Take control by powering on your transmitter
    * Release control by powering off
  * Want to turn on your transmitter to take control?
    * Leave 3R/C disconnected or unsolder the CH3 jumper
    * Take control by powering on your transmitter
    * Release control by powering off

The board is powered by the BEC which means you must turn on your ESC to turn on the Mux board. The Mux board distributes power to everything connected; you can design your autopilot board to draw power from the BEC with no additional wires needed.

# Taking Control #

If you want to take control by turning on your transmitter, leave 3R/C unconnected -or- unsolder the CH3 jumper. Then, power on your transmitter and the RoverMux switches to RC mode. Turn it off and it switches back to MCU mode.

If you have a receiver that generates a failsafe signal when the transmitter signal is lost, this approach won't work.

Also, if your receiver takes a long time to recognize that the transmitter is on, this approach won't work very well.

For instantaneous control, you can use CH3. Solder the CH3 jumper, then connect the receiver CH3 to the 3R/C port. Turn on your transmitter and configure it so that when you press the CH3 switch it will rapidly switch the RoverMux between R/C and MCU mode.

# Programming #

The ATtiny13A onboard can be reprogrammed. Pins for ISP are labeled on the bottom of the board: VTG, GND, SCK, !RESET, GND, MISO, and MOSI. Connect an AVR programmer, use your favorite IDE, download the code (sources above), modify and reprogram to your heart's content