package ru.mirea.mironovsp.dialoglasttask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity
        implements MyTimeDialogFragment.TimeDialogListener,
        MyDateDialogFragment.DateDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TimePickerDialog button
        Button btnTime = findViewById(R.id.btnTimeDialog);
        btnTime.setOnClickListener(v -> {
            MyTimeDialogFragment timeDialog = new MyTimeDialogFragment();
            timeDialog.show(getSupportFragmentManager(), "timePicker");
        });

        // DatePickerDialog button
        Button btnDate = findViewById(R.id.btnDateDialog);
        btnDate.setOnClickListener(v -> {
            MyDateDialogFragment dateDialog = new MyDateDialogFragment();
            dateDialog.show(getSupportFragmentManager(), "datePicker");
        });

        // ProgressDialog button
        Button btnProgress = findViewById(R.id.btnProgressDialog);
        btnProgress.setOnClickListener(v -> {
            MyProgressDialogFragment progressDialog = new MyProgressDialogFragment();
            progressDialog.show(getSupportFragmentManager(), "progressDialog");
        });
    }

    // TimeDialog callback
    @Override
    public void onTimeSet(int hour, int minute) {
        String time = String.format("Selected time: %02d:%02d", hour, minute);
        Toast.makeText(this, time, Toast.LENGTH_SHORT).show();
        showSnackbar(time);
    }

    // DateDialog callback
    @Override
    public void onDateSet(int year, int month, int day) {
        String date = String.format("Selected date: %02d/%02d/%d", day, month+1, year);
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        showSnackbar(date);
    }

    // Snackbar example
    private void showSnackbar(String message) {
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("OK", v -> {})
                .show();
    }
}