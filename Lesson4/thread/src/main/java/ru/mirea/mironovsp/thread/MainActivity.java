package ru.mirea.mironovsp.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.mironovsp.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", String.format(
                                "Запущен поток № %d студентом группы № %s номер по списку № %d",
                                numberThread, "БИСО-02-20", 21));

                        try {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        int totalPairs = Integer.parseInt(binding.totalPairsInput.getText().toString());
                                        int days = Integer.parseInt(binding.daysInput.getText().toString());

                                        if (days <= 0) {
                                            Toast.makeText(MainActivity.this,
                                                    "Количество дней не может быть 0 или ты болт забил на институт?!",
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        long endTime = System.currentTimeMillis() + 10 * 1000;
                                        while (System.currentTimeMillis() < endTime) {
                                            synchronized (this) {
                                                try {
                                                    wait(endTime - System.currentTimeMillis());
                                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        }

                                        double average = (double) totalPairs / days;

                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.resultText.setText(String.format("Среднее: %.2f пар/день", average));
                                            }
                                        });

                                        Log.d("ThreadProject", "Выполнен поток № " + numberThread);

                                    } catch (NumberFormatException e) {
                                        Toast.makeText(MainActivity.this,
                                                "Введи норм число тупень",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            Log.e("ThreadProject", "Ошибка в потоке", e);
                        }
                    }
                }).start();
            }
        });
    }
}