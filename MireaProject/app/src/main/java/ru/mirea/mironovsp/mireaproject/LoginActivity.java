package ru.mirea.mironovsp.mireaproject;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;
import ru.mirea.mironovsp.mireaproject.MainActivity;
import ru.mirea.mironovsp.mireaproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            signIn(email, password);
        });

        binding.registerButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            createAccount(email, password);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //FirebaseUser currentUser = mAuth.getCurrentUser();
       // if (currentUser != null) {
           // startMainActivity();
       // }
    }

    private void createAccount(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startMainActivity();
                    } else {
                        Toast.makeText(this, "Ошибка регистрации: " +
                                        Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startMainActivity();
                    } else {
                        Toast.makeText(this, "Ошибка входа: " +
                                        Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

