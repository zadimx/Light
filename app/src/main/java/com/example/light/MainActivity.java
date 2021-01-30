package com.example.light;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static StringBuilder setText;
    SensorManager sensorManager;
    List<Sensor> sensors;
    Sensor sensorLight;

    Fragment1 frag1;
    Fragment2 frag2;
    FragmentTransaction fTrans;
    CheckBox chbStack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        frag1 = new Fragment1();
        frag2 = new Fragment2();

        chbStack = (CheckBox)findViewById(R.id.chbStack);
    }

    public void onClickSensList(View v) {
        sensorManager.unregisterListener(listenerLight, sensorLight);
        StringBuilder sb = new StringBuilder();

        for (Sensor sensor : sensors) {
            sb.append("name = ").append(sensor.getName())
                    .append(", type = ").append(sensor.getType())
                    .append("\nvendor = ").append(sensor.getVendor())
                    .append(" ,version = ").append(sensor.getVersion())
                    .append("\nmax = ").append(sensor.getMaximumRange())
                    .append(", resolution = ").append(sensor.getResolution())
                    .append("\n--------------------------------------\n");
        }
        setText = sb;
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.frgmCont, frag2);
        if (chbStack.isChecked()) fTrans.addToBackStack(null);
        fTrans.commit();
    }

    public void onClickSensLight(View v) {
        sensorManager.registerListener(listenerLight, sensorLight,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerLight, sensorLight);
    }

    SensorEventListener listenerLight = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
//            tvText.setText(String.valueOf(event.values[0]));
            if (event.values[0] < 5) {
                fTrans = getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, frag1);
                if (chbStack.isChecked()) fTrans.addToBackStack(null);
                fTrans.commit();
            }
        }
    };

}
