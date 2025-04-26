package ru.mirea.mironovsp.firebaseauth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseAuth";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Настройка обработчиков кликов
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(v -> {
            String email = ((android.widget.EditText) findViewById(R.id.fieldEmail)).getText().toString();
            String password = ((android.widget.EditText) findViewById(R.id.fieldPassword)).getText().toString();
            createAccount(email, password);
        });

        findViewById(R.id.emailSignInButton).setOnClickListener(v -> {
            String email = ((android.widget.EditText) findViewById(R.id.fieldEmail)).getText().toString();
            String password = ((android.widget.EditText) findViewById(R.id.fieldPassword)).getText().toString();
            signIn(email, password);
        });

        findViewById(R.id.signOutButton).setOnClickListener(v -> signOut());
        findViewById(R.id.verifyEmailButton).setOnClickListener(v -> sendEmailVerification());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Проверяем, вошел ли пользователь (не-null) и обновляем UI соответственно
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Пользователь вошел
            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);

            ((android.widget.TextView) findViewById(R.id.statusTextView)).setText(
                    getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
            ((android.widget.TextView) findViewById(R.id.detailTextView)).setText(
                    getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
        } else {
            // Пользователь вышел
            ((android.widget.TextView) findViewById(R.id.statusTextView)).setText(R.string.signed_out);
            ((android.widget.TextView) findViewById(R.id.detailTextView)).setText(null);

            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }
    }

    private boolean validateForm() {
        String email = ((android.widget.EditText) findViewById(R.id.fieldEmail)).getText().toString();
        String password = ((android.widget.EditText) findViewById(R.id.fieldPassword)).getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "Enter email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Успешная регистрация, обновляем UI с информацией о пользователе
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // Если регистрация не удалась, показываем сообщение пользователю
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Успешный вход, обновляем UI с информацией о пользователе
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // Если вход не удался, показываем сообщение пользователю
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Отключаем кнопку
        findViewById(R.id.verifyEmailButton).setEnabled(false);

        // Отправляем письмо с подтверждением
        final FirebaseUser user = mAuth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    // Включаем кнопку обратно
                    findViewById(R.id.verifyEmailButton).setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this,
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(MainActivity.this,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}