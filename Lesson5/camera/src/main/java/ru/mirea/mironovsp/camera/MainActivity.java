package ru.mirea.mironovsp.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 100;
    private boolean isPermissionGranted = false;

    private ImageView imageView;
    private Uri imageUri;
    private File photoFile;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                imageView.setImageURI(imageUri);
                                Toast.makeText(MainActivity.this, "Фото сохранено", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        // Проверка разрешения
        int camPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        isPermissionGranted = camPermission == PackageManager.PERMISSION_GRANTED;

        if (!isPermissionGranted) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_PERMISSION);
        }

        imageView.setOnClickListener(v -> {
            if (isPermissionGranted) {
                openCamera();
            } else {
                Toast.makeText(this, "Нет разрешения на камеру", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openCamera() {
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка создания файла", Toast.LENGTH_SHORT).show();
            return;
        }

        String authority = getPackageName() + ".fileprovider";
        imageUri = FileProvider.getUriForFile(this, authority, photoFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraLauncher.launch(intent);
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String filename = "IMG_" + timestamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(filename, ".jpg", storageDir);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isPermissionGranted = grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (!isPermissionGranted) {
                Toast.makeText(this, "Камера запрещена", Toast.LENGTH_SHORT).show();
            }
        }
    }
}