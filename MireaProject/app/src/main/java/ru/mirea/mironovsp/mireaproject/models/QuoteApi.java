package ru.mirea.mironovsp.mireaproject.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuoteApi {
    @GET("1.0/")
    Call<Quote> getQuote(
            @Query("method") String method,
            @Query("format") String format,
            @Query("lang") String language
    );
}