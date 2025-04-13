package ru.mirea.mironovsp.mireaproject;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("MyWorker", "Фоновая задача начата");
        String taskDesc = getInputData().getString("TASK_DESCRIPTION");

        try {
            Thread.sleep(10000);

            Data outputData = new Data.Builder()
                    .putString("RESULT", "Успешно выполнено: " + taskDesc)
                    .build();

            return Result.success(outputData);
        } catch (InterruptedException e) {
            return Result.failure();
        }
    }
}
