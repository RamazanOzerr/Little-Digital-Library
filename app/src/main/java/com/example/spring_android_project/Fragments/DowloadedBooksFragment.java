package com.example.spring_android_project.Fragments;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spring_android_project.Activities.MainActivity;
import com.example.spring_android_project.Adapters.AvailableBooksAdapter;
import com.example.spring_android_project.Adapters.DownloadedBooksAdapter;
import com.example.spring_android_project.Apis.Api;
import com.example.spring_android_project.R;
import com.example.spring_android_project.Services.BookService;
import com.example.spring_android_project.Services.UserService;
import com.example.spring_android_project.Utils.Book;
import com.example.spring_android_project.Utils.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DowloadedBooksFragment extends Fragment {

    private UserService userService;
    private BookService bookService;
    private List<Book> bookList;
    private RecyclerView recyclerView_downloaded_books;
    DownloadedBooksAdapter adapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    View view;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dowloaded_books, container, false);

        init();
        getCurrentUserId();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            bookList.clear();
            getCurrentUserId();
            swipeRefreshLayout.setRefreshing(false);
        });

        swipeToRemove();

        return view;
    }

    private void init(){
        bookList = new ArrayList<>();
        recyclerView_downloaded_books = view.findViewById(R.id.recyclerView_downloaded_books);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 1);
        recyclerView_downloaded_books.setLayoutManager(mng);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh_layout);

    }

    private void getCurrentUserId(){

        //TODO db den kitap bilgilerini çekip listeye at aynı şekilde, AvailableBooksFragment da
        // yaptığımız işlemin aynısı

        // todo: firebase current user -- returns user id.
        // todo: post (/books/userid/bookid)
        String photoPath = "https://firebasestorage.googleapis.com/v0/b/libraryproject-1c015.appspot.com/o/book4.png?alt=media&token=f08c44fe-02f4-467e-869d-6b51fc774978";
        String name = "name";
        String link = "link";


        bookList.add(new Book(photoPath,name,link));
        bookList.add(new Book(photoPath,"1",link));
        bookList.add(new Book(photoPath,"2",link));
        bookList.add(new Book(photoPath,"3",link));
        bookList.add(new Book(photoPath,"4",link));
        adapter = new DownloadedBooksAdapter(bookList,getActivity(),getContext());
        recyclerView_downloaded_books.setAdapter(adapter);

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userId;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("email").getValue().toString().equals(firebaseUser.getEmail())) {

                        userId = dataSnapshot.getKey();
                        displayBooksForUser(Integer.parseInt(userId));
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void displayBooksForUser(int userId) {
        System.out.println("*******************" + userId);


        userService = Api.getUserService();
        Call<List<Book>> call = userService.getAllBooksForUser(userId);

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                bookList = response.body();
                adapter = new DownloadedBooksAdapter(bookList,getActivity(),getContext());
                recyclerView_downloaded_books.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

            }
        });


    }

    private void swipeToRemove(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Book deletedBook = bookList.get(position);
                //TODO: recyclerView üzerinde silme işlemini burada yapıyoruz, db den silme de burada olmalı
                //TODO: removeFromDb();
                bookList.remove(position);
                adapter.notifyItemRemoved(position);
                Snackbar.make(recyclerView_downloaded_books, deletedBook.getBookName()
                        , Snackbar.LENGTH_LONG)
                        .setAction("undo", view -> {
                            bookList.add(position,deletedBook);
                            adapter.notifyItemInserted(position);
                            //TODO: undoFromDb();
                        }).show();

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                        .addActionIcon(R.drawable.baseline_delete_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_downloaded_books);
    }
}