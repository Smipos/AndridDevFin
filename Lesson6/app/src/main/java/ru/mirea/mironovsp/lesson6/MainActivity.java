package ru.mirea.mironovsp.lesson6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText groupNumberEditText;
    private EditText listNumberEditText;
    private EditText favoriteMovieEditText;
    private Button saveButton;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String GROUP_KEY = "groupNumber";
    private static final String LIST_KEY = "listNumber";
    private static final String MOVIE_KEY = "favoriteMovie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        groupNumberEditText = findViewById(R.id.groupNumberEditText);
        listNumberEditText = findViewById(R.id.listNumberEditText);
        favoriteMovieEditText = findViewById(R.id.favoriteMovieEditText);
        saveButton = findViewById(R.id.saveButton);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        loadSavedData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GROUP_KEY, groupNumberEditText.getText().toString());
        editor.putString(LIST_KEY, listNumberEditText.getText().toString());
        editor.putString(MOVIE_KEY, favoriteMovieEditText.getText().toString());
        editor.apply();
    }

    private void loadSavedData() {
        groupNumberEditText.setText(sharedPreferences.getString(GROUP_KEY, ""));
        listNumberEditText.setText(sharedPreferences.getString(LIST_KEY, ""));
        favoriteMovieEditText.setText(sharedPreferences.getString(MOVIE_KEY, ""));
    }
}