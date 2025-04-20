package ru.mirea.mironovsp.camera;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru.mirea.mironovsp.camera.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String currentPhotoPath;
    private static final String TAG = "CameraApp";

    // Новый способ обработки результата (более современный)
    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    setPic();
                } else {
                    Toast.makeText(this, "Фото не было сделано", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.captureButton.setOnClickListener(v -> {
            Log.d(TAG, "Кнопка нажата");
            dispatchTakePictureIntent();
        });
    }

    private void dispatchTakePictureIntent() {
        // 1. Проверяем наличие камеры на устройстве
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(this, "На устройстве нет камеры", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Создаем Intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 3. Альтернативная проверка доступности камеры
        List<ResolveInfo> cameraApps = getPackageManager()
                .queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if (cameraApps.isEmpty()) {
            Toast.makeText(this, "Не найдено приложение камеры", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Доступные приложения камеры: " + cameraApps.size());
            return;
        }

        // 4. Создаем файл для сохранения
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.e(TAG, "Ошибка создания файла", ex);
            Toast.makeText(this, "Ошибка создания файла", Toast.LENGTH_SHORT).show();
        }

        // 5. Если файл создан, продолжаем
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "ru.mirea.mironovsp.camera.fileprovider",
                    photoFile);

            Log.d(TAG, "URI фото: " + photoURI);

            // 6. Предоставляем временные разрешения
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            // 7. Явно выбираем приложение камеры
            String packageName = cameraApps.get(0).activityInfo.packageName;
            takePictureIntent.setPackage(packageName);

            // 8. Запускаем камеру
            try {
                takePictureLauncher.launch(takePictureIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Ошибка запуска камеры", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Ошибка запуска камеры", e);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Создаем папку, если она не существует
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "Путь к фото: " + currentPhotoPath);
        return image;
    }

    private void setPic() {
        try {
            // Получаем размеры ImageView
            int targetW = binding.imageView.getWidth();
            int targetH = binding.imageView.getHeight();

            // Получаем размеры bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Определяем коэффициент масштабирования
            int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

            // Декодируем изображение с правильным масштабом
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            binding.imageView.setImageBitmap(bitmap);
            Log.d(TAG, "Фото успешно отображено");
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при отображении фото", e);
            Toast.makeText(this, "Ошибка при отображении фото", Toast.LENGTH_SHORT).show();
        }
    }
}