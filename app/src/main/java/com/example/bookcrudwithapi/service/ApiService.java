package com.example.bookcrudwithapi.service;

import com.example.bookcrudwithapi.model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("book")
    Call<Book> saveBook(@Body Book book);

    @GET("book")
    Call<List<Book>> getAllBook();

    @PUT("book/{id}")
    Call<Book> updateBook(@Path("id") int id, @Body Book book);

    @DELETE("book/{id}")
    Call<Void> deleteBook(@Path("id") int id);
}