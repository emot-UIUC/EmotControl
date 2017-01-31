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

class EmotArm {
public:
    EmotArm(int upper_pin_num, int lower_pin_num);
    void up();
    void stable();
    void down();
    void char_control(char c);
private:
    int upper_pin;
    int lower_pin;
};

EmotArm::EmotArm(int up, int low) {
    this->upper_pin = up;
    this->lower_pin = low;
}

void EmotArm::up() {
    digitalWrite(this->upper_pin, HIGH);
    digitalWrite(this->lower_pin, LOW);
}

void EmotArm::stable() {1
    digitalWrite(this->upper_pin, LOW);
    digitalWrite(this->lower_pin, LOW);
}

void EmotArm::down() {
    digitalWrite(this->upper_pin, LOW);
    digitalWrite(this->lower_pin, HIGH);
}

void EmotArm::char_control(char c) {
    switch (c) {
    case 'u':
        this->up();
        break;
    case 's':
        this->stable();
        break;
    case 'd':
        this->down();
        break;
    default:
        break;
    }
}
