package ru.mirea.mironovsp.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tvIp, tvCity, tvRegion, tvCountry, tvLoc, tvWeather, tvTemperature, tvWindspeed;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvIp = findViewById(R.id.tvIp);
        tvCity = findViewById(R.id.tvCity);
        tvRegion = findViewById(R.id.tvRegion);
        tvCountry = findViewById(R.id.tvCountry);
        tvLoc = findViewById(R.id.tvLoc);
        tvWeather = findViewById(R.id.tvWeather);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvWindspeed = findViewById(R.id.tvWindspeed);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnGetInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetAndGetData();
            }
        });
    }

    private void checkInternetAndGetData() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = null;
        if (connectivityManager != null) {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkinfo != null && networkinfo.isConnected()) {
            new DownloadInfoTask().execute("https://ipinfo.io/json");
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadInfoTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject responseJson = new JSONObject(result);

                String ip = responseJson.getString("ip");
                String city = responseJson.optString("city", "Неизвестно");
                String region = responseJson.optString("region", "Неизвестно");
                String country = responseJson.optString("country", "Неизвестно");
                String loc = responseJson.optString("loc", "0,0");

                tvIp.setText("IP: " + ip);
                tvCity.setText("Город: " + city);
                tvRegion.setText("Регион: " + region);
                tvCountry.setText("Страна: " + country);
                tvLoc.setText("Координаты: " + loc);

                String[] coordinates = loc.split(",");
                if (coordinates.length == 2) {
                    String latitude = coordinates[0];
                    String longitude = coordinates[1];
                    String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                            "&longitude=" + longitude + "&current_weather=true";
                    new DownloadWeatherTask().execute(weatherUrl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Ошибка парсинга данных", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject responseJson = new JSONObject(result);
                JSONObject currentWeather = responseJson.getJSONObject("current_weather");

                double temperature = currentWeather.getDouble("temperature");
                double windspeed = currentWeather.getDouble("windspeed");
                int weathercode = currentWeather.getInt("weathercode");

                String weatherDescription = getWeatherDescription(weathercode);

                tvWeather.setText("Погода: " + weatherDescription);
                tvTemperature.setText("Температура: " + temperature + "°C");
                tvWindspeed.setText("Скорость ветра: " + windspeed + " км/ч");

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Ошибка получения погоды", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getWeatherDescription(int weathercode) {
        switch (weathercode) {
            case 0: return "Ясно";
            case 1: return "Преимущественно ясно";
            case 2: return "Переменная облачность";
            case 3: return "Пасмурно";
            case 45: case 48: return "Туман";
            case 51: case 53: case 55: return "Морось";
            case 56: case 57: return "Ледяная морось";
            case 61: case 63: case 65: return "Дождь";
            case 66: case 67: return "Ледяной дождь";
            case 71: case 73: case 75: return "Снег";
            case 77: return "Снежные зерна";
            case 80: case 81: case 82: return "Ливень";
            case 85: case 86: return "Снегопад";
            case 95: return "Гроза";
            case 96: case 99: return "Гроза с градом";
            default: return "Неизвестно";
        }
    }

    private String downloadIpInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                data = stringBuilder.toString();
            } else {
                data = connection.getResponseMessage() + ". Error Code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }
}