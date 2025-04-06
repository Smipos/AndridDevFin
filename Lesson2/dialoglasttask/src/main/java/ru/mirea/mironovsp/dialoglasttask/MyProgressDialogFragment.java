package ru.mirea.mironovsp.dialoglasttask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.DialogFragment;

public class MyProgressDialogFragment extends DialogFragment {

    private ProgressDialog progressDialog;
    private Handler handler;
    private Runnable runnable;

    @Override
    public ProgressDialog onCreateDialog(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        // Имитация прогресса
        handler = new Handler();
        runnable = new Runnable() {
            int progress = 0;
            @Override
            public void run() {
                if (progress < 100) {
                    progress += 10;
                    progressDialog.setProgress(progress);
                    handler.postDelayed(this, 300);
                } else {
                    progressDialog.dismiss();
                }
            }
        };

        return progressDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 300);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}