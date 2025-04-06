package ru.mirea.mironovsp.multiactivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String text = getIntent().getStringExtra("key");

        TextView textView = findViewById(R.id.textView);
        textView.setText(text);
    }
}