package ru.mirea.mironovsp.data_thread;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView logTextView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logTextView = findViewById(R.id.logTextView);
        handler = new Handler();

        findViewById(R.id.startButton).setOnClickListener(v -> startProcesses());
    }

    private void startProcesses() {
        logTextView.append("\n--- Запуск процессов ---\n");

        logTextView.append("выполнение в UI\n");

        handler.post(() -> {
            logTextView.append("handler.post()\n");
        });

        handler.postDelayed(() -> {
            logTextView.append("handler.postDelayed(1000)\n");
        }, 1000);

        runOnUiThread(() -> {
            logTextView.append("runOnUiThread()\n");
        });

        new Thread(() -> {
            String threadInfo = "новый поток non-ui\n";
            runOnUiThread(() -> {
                logTextView.append(threadInfo);
            });
        }).start();
    }
}