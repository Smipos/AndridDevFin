package ru.mirea.mironovsp.activitylifecycle;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivityLifecycle"; // Уникальный тег для фильтрации в Logcat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate() - Activity создается");

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editText = findViewById(R.id.editText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() - Activity становится видимым");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() - Activity получает фокус");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() - Activity теряет фокус");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() - Activity больше не видно");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() - Activity уничтожается");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() - Activity перезапускается");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() - Сохранение состояния");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState() - Восстановление состояния");
    }
}