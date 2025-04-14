package ru.mirea.mironovsp.buttonclicker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView textViewStudent;
    private Button btnWhoAmI;
    private Button btnItIsNotMe;
    private CheckBox checkBox;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewStudent = findViewById(R.id.textViewStudent);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe);
        checkBox = findViewById(R.id.checkBox);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewStudent.setText("Мой номер из списка № 21");
                checkBox.setChecked(!checkBox.isChecked());
            }
        };
        btnWhoAmI.setOnClickListener(onClickListener);


        btnItIsNotMe.setOnClickListener(this::onButtonClick);
    }
    public  void  onButtonClick(View v) {
        textViewStudent.setText("Это не я сделал!");
        checkBox.setChecked(!checkBox.isChecked());
    }
}