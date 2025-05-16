package ru.mirea.mironovsp.mireaproject.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ru.mirea.mironovsp.mireaproject.R;

public class MicrophoneFragment extends Fragment {
    private static final int SAMPLE_RATE = 44100;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private Thread recordingThread;
    private Handler handler = new Handler(Looper.getMainLooper());

    private TextView statusText;
    private TextView volumeText;
    private ProgressBar volumeProgress;
    private Button toggleButton;
    private Button analyzeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microphone, container, false);

        statusText = view.findViewById(R.id.statusText);
        volumeText = view.findViewById(R.id.volumeText);
        volumeProgress = view.findViewById(R.id.volumeProgress);
        toggleButton = view.findViewById(R.id.toggleButton);
        analyzeButton = view.findViewById(R.id.analyzeButton);

        toggleButton.setOnClickListener(v -> toggleRecording());
        analyzeButton.setOnClickListener(v -> analyzeNoise());

        // Проверка разрешений
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }

        return view;
    }

    private void toggleRecording() {
        if (isRecording) {
            stopRecording();
            toggleButton.setText("Начать запись");
            statusText.setText("Статус: неактивно");
            analyzeButton.setEnabled(true);
        } else {
            if (startRecording()) {
                toggleButton.setText("Остановить запись");
                statusText.setText("Статус: запись...");
                analyzeButton.setEnabled(false);
            }
        }
    }

    private boolean startRecording() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Разрешение на запись не предоставлено", Toast.LENGTH_SHORT).show();
            return false;
        }

        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            Toast.makeText(getContext(), "Ошибка инициализации микрофона", Toast.LENGTH_SHORT).show();
            return false;
        }

        audioRecord.startRecording();
        isRecording = true;

        recordingThread = new Thread(() -> {
            short[] audioBuffer = new short[bufferSize];
            while (isRecording) {
                int samplesRead = audioRecord.read(audioBuffer, 0, bufferSize);
                if (samplesRead > 0) {
                    double amplitude = calculateAmplitude(audioBuffer, samplesRead);
                    double db = 20 * Math.log10(amplitude);

                    handler.post(() -> {
                        volumeText.setText(String.format("Уровень звука: %.1f dB", db));
                        volumeProgress.setProgress((int) Math.min(db, 100));
                    });
                }
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
        return true;
    }

    private double calculateAmplitude(short[] buffer, int length) {
        double sum = 0;
        for (int i = 0; i < length; i++) {
            sum += buffer[i] * buffer[i];
        }
        return Math.sqrt(sum / length);
    }

    private void stopRecording() {
        if (audioRecord != null) {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;

            try {
                recordingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void analyzeNoise() {
        // Здесь можно добавить анализ записанного звука
        Toast.makeText(getContext(), "Анализ шума выполнен", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Разрешение получено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Разрешение отклонено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording) {
            stopRecording();
        }
    }
}