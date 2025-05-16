package ru.mirea.mironovsp.mireaproject.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.mirea.mironovsp.mireaproject.R;

public class AltitudeFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private boolean isMonitoring = false;

    private TextView altitudeValue;
    private TextView pressureValue;
    private TextView healthImpact;
    private TextView healthAdvice;
    private Button startButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_altitude, container, false);

        // Инициализация UI элементов
        altitudeValue = view.findViewById(R.id.altitudeValue);
        pressureValue = view.findViewById(R.id.pressureValue);
        healthImpact = view.findViewById(R.id.healthImpact);
        healthAdvice = view.findViewById(R.id.healthAdvice);
        startButton = view.findViewById(R.id.startButton);

        // Получение сервиса сенсоров
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        // Проверка наличия датчика
        if (sensorManager != null) {
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        }

        if (pressureSensor == null) {
            Toast.makeText(getContext(), "Датчик давления не доступен", Toast.LENGTH_LONG).show();
            startButton.setEnabled(false);
            altitudeValue.setText("Датчик недоступен");
            pressureValue.setText("Датчик недоступен");
        }

        startButton.setOnClickListener(v -> toggleMonitoring());

        return view;
    }

    private void toggleMonitoring() {
        if (!isMonitoring) {
            if (sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)) {
                startButton.setText("Остановить мониторинг");
                isMonitoring = true;
                Toast.makeText(getContext(), "Мониторинг начат", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Ошибка запуска датчика", Toast.LENGTH_SHORT).show();
            }
        } else {
            sensorManager.unregisterListener(this);
            startButton.setText("Начать мониторинг");
            isMonitoring = false;
            Toast.makeText(getContext(), "Мониторинг остановлен", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float pressure = event.values[0];
            float altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure);

            requireActivity().runOnUiThread(() -> {
                pressureValue.setText(String.format("Атм. давление: %.1f hPa", pressure));
                altitudeValue.setText(String.format("Высота: %.0f метров", altitude));
                updateHealthInfo(altitude);
            });
        }
    }

    private void updateHealthInfo(float altitude) {
        String impact, advice;

        if (altitude < 1000) {
            impact = "Нормальные условия";
            advice = "Без особых рекомендаций";
        } else if (altitude < 2000) {
            impact = "Легкая гипоксия";
            advice = "Избегайте резких физических нагрузок";
        } else if (altitude < 3000) {
            impact = "Умеренная гипоксия";
            advice = "Требуется акклиматизация, пейте больше воды";
        } else if (altitude < 4000) {
            impact = "Высокий риск горной болезни";
            advice = "Используйте кислородные баллоны при необходимости";
        } else {
            impact = "Опасная для жизни высота";
            advice = "Требуется специальное снаряжение и подготовка";
        }

        healthImpact.setText("Влияние на организм: " + impact);
        healthAdvice.setText("Рекомендации: " + advice);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Можно добавить обработку изменения точности
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isMonitoring) {
            sensorManager.unregisterListener(this);
            isMonitoring = false;
            startButton.setText("Начать мониторинг");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMonitoring && pressureSensor != null) {
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}