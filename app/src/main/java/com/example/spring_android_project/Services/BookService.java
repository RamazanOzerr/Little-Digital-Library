package com.example.spring_android_project.Services;

import com.example.spring_android_project.Utils.Book;
import com.example.spring_android_project.Utils.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BookService {

    @GET("books")  // todo : get all books in the db  (DONE)
    Call<List<Book>> getBooks();

    @POST("books/{userId}/{bookId}")  // todo : add a book for a user
    Call<User> addBookForUser(@Path("userId") int userId,
                              @Path("bookId") int bookdId);



}
