package ru.mirea.mironovsp.lesson5;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        List<String> sensorDetails = new ArrayList<>();

        for (Sensor sensor : sensors) {
            String info =
                            sensor.getName() + "\n" +
                            "Тип: " + getSensorType(sensor.getType()) + "\n" +
                            "Производитель: " + sensor.getVendor() + "\n" +
                            "Версия: " + sensor.getVersion();

            sensorDetails.add(info);
        }

        if (sensorDetails.isEmpty()) {
            Toast.makeText(this, "На устройстве нет датчиков", Toast.LENGTH_LONG).show();
        } else {
            ListView listView = findViewById(R.id.sensor_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    sensorDetails
            );
            listView.setAdapter(adapter);
        }
    }

    private String getSensorType(int type) {
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER: return "Акселерометр";
            case Sensor.TYPE_GYROSCOPE: return "Гироскоп";
            case Sensor.TYPE_LIGHT: return "Датчик освещенности";
            case Sensor.TYPE_MAGNETIC_FIELD: return "Магнитометр";
            case Sensor.TYPE_PROXIMITY: return "Датчик приближения";
            case Sensor.TYPE_STEP_COUNTER: return "Счетчик шагов";
            case Sensor.TYPE_HEART_RATE: return "Датчик пульса";
            default: return "Неизвестный (" + type + ")";
        }
    }
}