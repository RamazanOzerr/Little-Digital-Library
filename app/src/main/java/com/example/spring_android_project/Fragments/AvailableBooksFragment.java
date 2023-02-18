package com.example.spring_android_project.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spring_android_project.Adapters.AvailableBooksAdapter;
import com.example.spring_android_project.R;
import com.example.spring_android_project.Utils.Book;

import java.util.ArrayList;
import java.util.List;

public class AvailableBooksFragment extends Fragment {

    private List<Book> bookList;
    private RecyclerView recyclerView_available_books;
    AvailableBooksAdapter adapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_available_books, container, false);

        init();
        getBooks();

        return view;
    }

    private void init(){
        bookList = new ArrayList<>();
        recyclerView_available_books = view.findViewById(R.id.recyclerView_available_books);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(),1);
        recyclerView_available_books.setLayoutManager(mng);
        adapter = new AvailableBooksAdapter(bookList,getActivity(),getContext());
        recyclerView_available_books.setAdapter(adapter);
    }

    // TODO KITAP BILGILERINI DB DEN BURDA CEKIYORUZ
    private void getBooks(){

        //TODO DB DEN PHOTO PATH VE NAME BILGILERINI CEK, LISTEYE EKLE
        String photoPath = "https://firebasestorage.googleapis.com/v0/b/libraryproject-1c015.appspot.com/o/book4.png?alt=media&token=f08c44fe-02f4-467e-869d-6b51fc774978";
        String name = "name";
        String link = "link";

        bookList.add(new Book(photoPath,name,link));

    }

}