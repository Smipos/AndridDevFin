package ru.mirea.mironovsp.audiorecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean isWork = false;
    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;

    private MediaRecorder recorder;
    private MediaPlayer player;
    private String recordFilePath;

    private Button recordButton;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordButton = findViewById(R.id.recordButton);
        playButton = findViewById(R.id.playButton);

        playButton.setEnabled(false);

        // Устанавливаем путь к .mp4 файлу
        recordFilePath = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.mp4").getAbsolutePath();

        // Проверка разрешений
        int audioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        isWork = audioPermission == PackageManager.PERMISSION_GRANTED;

        if (!isWork) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_CODE_PERMISSION);
        }

        // Кнопка записи
        recordButton.setOnClickListener(v -> {
            if (isStartRecording) {
                startRecording();
                recordButton.setText("Stop Recording");
                playButton.setEnabled(false);
            } else {
                stopRecording();
                recordButton.setText("Start Recording");
                playButton.setEnabled(true);
            }
            isStartRecording = !isStartRecording;
        });

        // Кнопка воспроизведения
        playButton.setOnClickListener(v -> {
            if (isStartPlaying) {
                startPlaying();
                playButton.setText("Stop Playing");
                recordButton.setEnabled(false);
            } else {
                stopPlaying();
                playButton.setText("Start Playing");
                recordButton.setEnabled(true);
            }
            isStartPlaying = !isStartPlaying;
        });
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // MP4 формат
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);    // AAC кодек

        try {
            recorder.prepare();
            recorder.start();
            Toast.makeText(this, "Recording started (mp4)", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;

            File file = new File(recordFilePath);
            if (file.exists()) {
                Toast.makeText(this, "Файл создан: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Файл НЕ создан!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
            Toast.makeText(this, "Playing started", Toast.LENGTH_SHORT).show();

            player.setOnCompletionListener(mp -> {
                Toast.makeText(this, "Playback complete", Toast.LENGTH_SHORT).show();
                playButton.setText("Play Recording");
                recordButton.setEnabled(true);
                isStartPlaying = true;
            });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Playback failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            Toast.makeText(this, "Playback stopped", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (!isWork) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}