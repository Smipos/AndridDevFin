package ru.mirea.mironovsp.mireaproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.mirea.mironovsp.mireaproject.R;

public class FilesFragment extends Fragment {
    private EditText etOriginalText, etKey, etResult;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_files, container, false);

        etOriginalText = root.findViewById(R.id.etOriginalText);
        etKey = root.findViewById(R.id.etKey);
        etResult = root.findViewById(R.id.etResult);

        Button btnEncrypt = root.findViewById(R.id.btnEncrypt);
        Button btnDecrypt = root.findViewById(R.id.btnDecrypt);

        btnEncrypt.setOnClickListener(v -> encryptText());
        btnDecrypt.setOnClickListener(v -> decryptText());

        return root;
    }

    public void clearFields() {
        if (getView() != null) {
            etOriginalText.setText("");
            etKey.setText("");
            etResult.setText("");
        }
    }

    private void encryptText() {
        String text = etOriginalText.getText().toString();
        String keyStr = etKey.getText().toString();

        if (text.isEmpty() || keyStr.isEmpty()) {
            Toast.makeText(getContext(), "Введите текст и ключ", Toast.LENGTH_SHORT).show();
            return;
        }

        int key = Integer.parseInt(keyStr);
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            encrypted.append((char) (c + key));
        }

        etResult.setText(encrypted.toString());
    }

    private void decryptText() {
        String text = etOriginalText.getText().toString();
        String keyStr = etKey.getText().toString();

        if (text.isEmpty() || keyStr.isEmpty()) {
            Toast.makeText(getContext(), "Введите текст и ключ", Toast.LENGTH_SHORT).show();
            return;
        }

        int key = Integer.parseInt(keyStr);
        StringBuilder decrypted = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            decrypted.append((char) (c - key));
        }

        etResult.setText(decrypted.toString());
    }
}