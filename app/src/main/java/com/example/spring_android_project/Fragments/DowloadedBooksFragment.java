package com.example.spring_android_project.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spring_android_project.Adapters.AvailableBooksAdapter;
import com.example.spring_android_project.Adapters.DownloadedBooksAdapter;
import com.example.spring_android_project.R;
import com.example.spring_android_project.Utils.Book;

import java.util.ArrayList;
import java.util.List;

public class DowloadedBooksFragment extends Fragment {

    private List<Book> bookList;
    private RecyclerView recyclerView_downloaded_books;
    DownloadedBooksAdapter adapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dowloaded_books, container, false);

        init();
        getBooks();

        return view;
    }

    private void init(){
        bookList = new ArrayList<>();
        recyclerView_downloaded_books = view.findViewById(R.id.recyclerView_downloaded_books);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 1);
        recyclerView_downloaded_books.setLayoutManager(mng);


    }

    private void getBooks(){

        //TODO db den kitap bilgilerini çekip listeye at aynı şekilde, AvailableBooksFragment da
        // yaptığımız işlemin aynısı

        adapter = new DownloadedBooksAdapter(bookList,getActivity(),getContext());
        recyclerView_downloaded_books.setAdapter(adapter);
    }

}