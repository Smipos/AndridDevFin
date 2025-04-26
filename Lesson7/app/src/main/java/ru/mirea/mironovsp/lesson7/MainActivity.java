package ru.mirea.mironovsp.lesson7;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SocketTime";
    private final String host = "time.nist.gov"; // или time-a.nist.gov
    private final int port = 13;

    private TextView timeTextView;
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeTextView = findViewById(R.id.timeTextView);
        dateTextView = findViewById(R.id.dateTextView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTimeTask timeTask = new GetTimeTask();
                timeTask.execute();
            }
        });
    }
    public static class SocketUtils {
        public static BufferedReader getReader(Socket s) throws IOException {
            return new BufferedReader(new InputStreamReader(s.getInputStream()));
        }

        public static PrintWriter getWriter(Socket s) throws IOException {
            return new PrintWriter(s.getOutputStream(), true);
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class GetTimeTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            String[] timeAndDate = new String[]{"", ""};
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                reader.readLine();
                String timeResult = reader.readLine();
                Log.d(TAG, "Ответ сервера: " + timeResult);

                String[] parts = timeResult.trim().split("\\s+");
                if (parts.length >= 3) {
                    timeAndDate[0] = parts[2];
                    timeAndDate[1] = parts[1];
                }

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                timeAndDate[0] = "Ошибка";
                timeAndDate[1] = e.getMessage();
            }
            return timeAndDate;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            timeTextView.setText("Time: " + result[0]);
            dateTextView.setText("Date: " + formatDate(result[1]));
        }

        private String formatDate(String dateStr) {
            String[] parts = dateStr.split("-");
            if (parts.length == 3) {
                return "20" + parts[0] + "." + parts[1] + "." + parts[2];
            }
            return dateStr;
        }
    }
}