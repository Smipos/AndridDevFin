package ru.mirea.mironovsp.mireaproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.UUID;

import ru.mirea.mironovsp.mireaproject.MyWorker;
import ru.mirea.mironovsp.mireaproject.R;

public class WorkerFragment extends Fragment {
    private TextView statusText;
    private UUID workId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker, container, false);

        statusText = view.findViewById(R.id.status_text);
        Button startButton = view.findViewById(R.id.start_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        startButton.setOnClickListener(v -> startWork());
        cancelButton.setOnClickListener(v -> cancelWork());

        return view;
    }

    private void startWork() {
        Data inputData = new Data.Builder()
                .putString("TASK_DESCRIPTION", "Completing a background task")
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(inputData)
                .setConstraints(constraints)
                .build();

        workId = workRequest.getId();

        WorkManager.getInstance(requireContext())
                .getWorkInfoByIdLiveData(workId)
                .observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo != null) {
                        String status = workInfo.getState().name();
                        statusText.setText("Status: " + status);

                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            String result = workInfo.getOutputData().getString("RESULT");
                            Toast.makeText(getContext(), "Task ended: " + result,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        WorkManager.getInstance(requireContext()).enqueue(workRequest);
        Toast.makeText(getContext(), "Task started", Toast.LENGTH_SHORT).show();
    }

    private void cancelWork() {
        if (workId != null) {
            WorkManager.getInstance(requireContext()).cancelWorkById(workId);
            statusText.setText("Status: Cancelled");
        }
    }
}