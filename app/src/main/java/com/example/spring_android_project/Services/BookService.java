package com.example.spring_android_project.Services;

import com.example.spring_android_project.Utils.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BookService {

    @GET("books")
    Call<List<Book>> getBooks();

}
