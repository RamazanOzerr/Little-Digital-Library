package com.example.spring_android_project.Utils;

public class Book {

    private int id;
    private String bookName;
    private String title;
    private int pages;
    private String link;
    private String photoPath;

    public Book(String photoPath, String bookName, String link, String title, int pages) {
        this.bookName = bookName;
        this.title = title;
        this.pages = pages;
        this.link = link;
        this.photoPath = photoPath;
    }
    public Book(String photoPath, String bookName, String link) {
        this.photoPath = photoPath;
        this.bookName = bookName;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
