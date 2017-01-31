package org.emot.emotcontrol;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.emot.libcontrol.ArmActions;
import org.emot.libcontrol.Arms;
import org.emot.libcontrol.EmotControl;
import org.emot.libcontrol.Emotions;
import org.emot.libcontrol.LedColors;
import org.emot.libcontrol.Leds;

import java.lang.ref.WeakReference;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    private TextView aValue;
    private TextView bValue;
    private TextView statusDisplay;
    private Handler handler = new TestAppIncomingDataHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmotControl.onCreate(this, handler);

        aValue = (TextView) findViewById(R.id.aValue);
        bValue = (TextView) findViewById(R.id.bValue);
        statusDisplay = (TextView) findViewById(R.id.statusView);
        statusDisplay.setMovementMethod(new ScrollingMovementMethod());

        // Four spinners to set up
        Spinner leftArmSpinner = (Spinner) findViewById(R.id.leftArmSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.arm_actions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leftArmSpinner.setAdapter(adapter);
        leftArmSpinner.setOnItemSelectedListener(new LeftArmSpinnerListener());

        Spinner rightArmSpinner = (Spinner) findViewById(R.id.rightArmSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.arm_actions_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rightArmSpinner.setAdapter(adapter2);
        rightArmSpinner.setOnItemSelectedListener(new RightArmSpinnerListener());

        Spinner leftLedSpinner = (Spinner) findViewById(R.id.leftLedSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this, R.array.led_colors_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leftLedSpinner.setAdapter(adapter3);
        leftLedSpinner.setOnItemSelectedListener(new LeftLedSpinnerListener());

        Spinner rightLedSpinner = (Spinner) findViewById(R.id.rightLedSpinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(
                this, R.array.led_colors_array, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rightLedSpinner.setAdapter(adapter4);
        rightLedSpinner.setOnItemSelectedListener(new RightLedSpinnerListener());

        Spinner emotionSpinner = (Spinner) findViewById(R.id.emotionSpinner);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(
                this, R.array.emotions_array, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emotionSpinner.setAdapter(adapter5);
        emotionSpinner.setOnItemSelectedListener(new EmotionSpinnerListener());

    }

    @Override
    public void onPause() {
        super.onPause();
        EmotControl.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        EmotControl.onResume();
    }

    private static class TestAppIncomingDataHandler extends Handler {
        private final WeakReference<MainActivity> activity;

        TestAppIncomingDataHandler(MainActivity owner) {
            activity = new WeakReference<>(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EmotControl.MESSAGE_FROM_SERIAL_PORT:
                    Scanner scanner = new Scanner((String) msg.obj);
                    activity.get().aValue.setText(String.format("%6.2f", scanner.nextFloat()));
                    activity.get().bValue.setText(String.format("%6.2f", scanner.nextFloat()));
                    break;
                case EmotControl.CTS_CHANGE:
                    Toast.makeText(activity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case EmotControl.DSR_CHANGE:
                    Toast.makeText(activity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private class LeftArmSpinnerListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // pos: 0-stable 1-up 2-down
            EmotControl.setArm(Arms.LEFT, ArmActions.values()[pos]);
            statusDisplay.append(String.format("Left Arm: %s\n", parent.getItemAtPosition(pos)));
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private class RightArmSpinnerListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // pos: 0-stable 1-up 2-down
            EmotControl.setArm(Arms.RIGHT, ArmActions.values()[pos]);
            statusDisplay.append(String.format("Right Arm: %s\n", parent.getItemAtPosition(pos)));
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private class LeftLedSpinnerListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // BLACK, WHITE, GREEN, RED, YELLOW, BLUE, DARK_BLUE
            EmotControl.setLed(Leds.LEFT, LedColors.values()[pos]);
            statusDisplay.append(String.format("Left LED: %s\n", parent.getItemAtPosition(pos)));
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private class RightLedSpinnerListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            EmotControl.setLed(Leds.RIGHT, LedColors.values()[pos]);
            statusDisplay.append(String.format("Right LED: %s\n", parent.getItemAtPosition(pos)));
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private class EmotionSpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            EmotControl.setEmotion(Emotions.values()[pos]);
            statusDisplay.append(String.format("Emotion: %s\n", parent.getItemAtPosition(pos)));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
