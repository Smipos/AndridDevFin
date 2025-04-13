package ru.mirea.mironovsp.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import javax.crypto.SecretKey;

public class MyLoader extends AsyncTaskLoader<String> {
    public static final String ARG_ENCRYPTED_TEXT = "encrypted_text";
    public static final String ARG_KEY = "key";

    private final Bundle args;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try {
            byte[] encryptedText = args.getByteArray(ARG_ENCRYPTED_TEXT);
            String keyString = args.getString(ARG_KEY);

            SecretKey secretKey = AESUtils.stringToKey(keyString);

            return AESUtils.decrypt(encryptedText, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка дешифрования: " + e.getMessage();
        }
    }
}