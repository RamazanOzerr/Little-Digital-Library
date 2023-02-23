package com.example.spring_android_project.Apis;

import com.example.spring_android_project.Services.BookService;
import com.example.spring_android_project.Services.UserService;

public class Api {

    static final String URL = "http://10.0.2.2:8080/api/";

    public static UserService getUserService() {
        return Client.getClient(URL).create(UserService.class);
    }

    public static BookService getBookService() {

        return Client.getClient(URL).create(BookService.class);

    }
}
