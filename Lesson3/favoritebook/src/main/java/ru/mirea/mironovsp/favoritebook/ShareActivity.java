package ru.mirea.mironovsp.favoritebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {
    private EditText bookNameEditText;
    private EditText quoteEditText;
    private Button sendDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        bookNameEditText = findViewById(R.id.bookNameEditText);
        quoteEditText = findViewById(R.id.quoteEditText);
        sendDataButton = findViewById(R.id.sendDataButton);

        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = bookNameEditText.getText().toString();
                String quote = quoteEditText.getText().toString();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("bookName", bookName);
                resultIntent.putExtra("quote", quote);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
