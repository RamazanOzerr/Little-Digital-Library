package com.example.spring_android_project.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.spring_android_project.Adapters.AvailableBooksAdapter;
import com.example.spring_android_project.Apis.Api;
import com.example.spring_android_project.R;
import com.example.spring_android_project.Services.BookService;
import com.example.spring_android_project.Utils.Book;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailableBooksFragment extends Fragment {
    BookService bookService;

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

    private void init() {
        bookList = new ArrayList<>();
        recyclerView_available_books = view.findViewById(R.id.recyclerView_available_books);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 1);
        recyclerView_available_books.setLayoutManager(mng);
        adapter = new AvailableBooksAdapter(bookList, getActivity(), getContext());
        recyclerView_available_books.setAdapter(adapter);

    }

    // get books from db
   private void getBooks() {

       bookService = Api.getBookService();
       Call<List<Book>> call =
               bookService.getBooks();

       call.enqueue(new Callback<List<Book>>() {
           @Override
           public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
               bookList = response.body();
               adapter = new AvailableBooksAdapter(bookList, getActivity(), getContext());
               recyclerView_available_books.setAdapter(adapter);

           }

           @Override
           public void onFailure(Call<List<Book>> call, Throwable t) {
               System.out.println("error");

           }
       });
   }

   // set search bar
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

}