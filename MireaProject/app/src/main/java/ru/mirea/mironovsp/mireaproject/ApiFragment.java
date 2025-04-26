package ru.mirea.mironovsp.mireaproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.mironovsp.mireaproject.models.Quote;
import ru.mirea.mironovsp.mireaproject.models.QuoteApi;

public class ApiFragment extends Fragment {
    private TextView resultTextView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_api, container, false);
        resultTextView = root.findViewById(R.id.resultTextView);
        progressBar = root.findViewById(R.id.progressBar);

        fetchRussianQuotes();

        return root;
    }

    private void fetchRussianQuotes() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.forismatic.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuoteApi api = retrofit.create(QuoteApi.class);

        // Параметры: method=getQuote, format=json, lang=ru
        Call<Quote> call = api.getQuote("getQuote", "json", "ru");

        call.enqueue(new Callback<Quote>() {
            @Override
            public void onResponse(Call<Quote> call, Response<Quote> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Quote quote = response.body();
                    String result = "Цитата: " + quote.getQuoteText() + "\n\nАвтор: " + quote.getQuoteAuthor();
                    resultTextView.setText(result);
                } else {
                    resultTextView.setText("Ошибка: не удалось получить данные");
                }
            }

            @Override
            public void onFailure(Call<Quote> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                resultTextView.setText("Ошибка: " + t.getMessage());
            }
        });
    }
}