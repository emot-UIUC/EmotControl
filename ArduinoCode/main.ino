/*
The MIT License (MIT)

Copyright (c) 2016-2017 emot-UIUC

Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

#include <FastLED.h>
#include <Wire.h>
#include <Adafruit_MPL115A2.h>
#include "arm.h"

#define RIGHT_BOTTOM 5
#define RIGHT_UPPER 6
#define LEFT_BOTTOM 9
#define LEFT_UPPER 10
#define LED_PIN     4

#define NUM_LEDS    2
#define CHIPSET     WS2812
#define COLOR_ORDER GRB
#define BRIGHTNESS  64

#define REPORT_INTERVAL 100

float pressure1 = 0.0;
float pressure2 = 0.0;
unsigned char msg[4];
unsigned long time;
unsigned long last;

Adafruit_MPL115A2 mpl115a2;
CRGB leds[NUM_LEDS];

EmotArm left_arm(LEFT_UPPER, LEFT_BOTTOM);
EmotArm right_arm(RIGHT_UPPER, RIGHT_BOTTOM);

char pressure_1_buf[8];
char pressure_2_buf[8];
char uplink_buf[20];

void setup() {
    delay(3000); // power-up safety delay

    // It's important to set the color correction for your LED strip here,
    // so that colors can be more accurately rendered through the 'temperature' profiles
    FastLED.addLeds<CHIPSET, LED_PIN, COLOR_ORDER>(leds, NUM_LEDS).setCorrection( TypicalSMD5050 );
    FastLED.setBrightness( BRIGHTNESS );

    mpl115a2.begin();    // Initiate Pressure sensors

    pinMode(7, OUTPUT);  //RESET SENSOR
    pinMode(8, OUTPUT);  //RESET SENSOR

    pinMode(RIGHT_BOTTOM, OUTPUT);
    pinMode(RIGHT_UPPER, OUTPUT);
    pinMode(LEFT_BOTTOM, OUTPUT);
    pinMode(LEFT_UPPER, OUTPUT);

    Serial.begin(9600);
    Serial.setTimeout(0);  // set non-blocking IO
}


void loop() {
	time = millis();
	Serial.readBytes(msg, 4);

    if (msg[0] == 0) {
        // arm
        if (msg[1] == 'l') {
            left_arm.char_control(msg[2]);
        } else if (msg[1] == 'r') {
            right_arm.char_control(msg[2]);
        }
    } else {
        leds[(int) msg[0]-1].red   = (int) msg[1];
        leds[(int) msg[0]-1].green = (int) msg[2];
        leds[(int) msg[0]-1].blue  = (int) msg[3];
        FastLED.show();
    }

	if (time - last > REPORT_INTERVAL) {
		// Report sensor readings
		digitalWrite(7, HIGH);
		digitalWrite(8, LOW);
		pressure1 = mpl115a2.getPressure();

		digitalWrite(7, LOW);
		digitalWrite(8, HIGH);
		pressure2 = mpl115a2.getPressure();

        dtostrf(pressure1, 6, 2, pressure_1_buf);
        dtostrf(pressure2, 6, 2, pressure_2_buf);
        sprintf(uplink_buf, "%s %s", pressure_1_buf, pressure_2_buf);
		Serial.write(uplink_buf, 13);

		last = time;
	}
}
