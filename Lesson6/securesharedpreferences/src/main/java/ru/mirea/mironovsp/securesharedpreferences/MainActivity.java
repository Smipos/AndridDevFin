package ru.mirea.mironovsp.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    private TextView poetNameTextView;
    private ImageView poetImageView;
    private SharedPreferences secureSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poetNameTextView = findViewById(R.id.poetNameTextView);
        poetImageView = findViewById(R.id.poetImageView);

        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            if (!secureSharedPreferences.contains("poet_name")) {
                secureSharedPreferences.edit()
                        .putString("poet_name", "Сергей Есенин")
                        .apply();
            }

            String poetName = secureSharedPreferences.getString("poet_name", "");
            poetNameTextView.setText(poetName);

            poetImageView.setImageResource(R.drawable.esenin);

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            poetNameTextView.setText("Ошибка при инициализации безопасного хранилища");
        }
    }
}