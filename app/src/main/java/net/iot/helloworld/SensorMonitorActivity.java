package net.iot.helloworld;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class SensorMonitorActivity extends AppCompatActivity implements SensorEventListener{
    SensorManager manager = (SensorManager)getSystemService(SENSOR_SERVICE);
    List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);
    int position =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_monitor);
        Intent intent = getIntent();


    }

    @Override
    protected void onResume() {
        manager.registerListener(this,sensors.get(position),
                SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    protected void onPause() {
        manager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String message = "timestamp"+event.timestamp+"\n";
        for(int i =0;i<event.values.length;i++){
            message +="#"+(i+1)+":"+event.values[i]+"\n";
        }
        Toast.makeText(SensorMonitorActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Toast.makeText(SensorMonitorActivity.this,"accuracy:"+accuracy,Toast.LENGTH_SHORT).show();
    }
}
