package ru.mirea.mironovsp.intentapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String currentTime = getIntent().getStringExtra("currentTime");


        int yourNumber = 21;
        int square = yourNumber * yourNumber;

        String displayText = "Квадрат значения моего номера по списку в группе составляет " +
                square + ", а текущее время " + currentTime;

        TextView textView = findViewById(R.id.textView);
        textView.setText(displayText);
    }
}
