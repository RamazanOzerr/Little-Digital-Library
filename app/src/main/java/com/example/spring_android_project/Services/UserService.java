package com.example.spring_android_project.Services;

import  com.example.spring_android_project.Utils.Book;
import com.example.spring_android_project.Utils.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("users/{userId}")  //todo : return the user
    Call<User> getUser(@Path("userId") int userId);

    @POST("users") // todo: add the user when signed up
    Call<User> addUser(@Body User user);

    // todo: delete the user by given user id

    @GET("users/{userId}/books")  // todo: get all books for a user (downloaded books)
    Call<Book> getAllBooksForUser(@Path("userId") int userId);
}
