package com.example.spring_android_project.Services;

import com.example.spring_android_project.Utils.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {

    @GET("/users/{userId}")
    Call<User> getUser(@Path("userId") int userId);
}
