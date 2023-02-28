package com.example.spring_android_project.Fragments;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.spring_android_project.Adapters.DownloadedBooksAdapter;
import com.example.spring_android_project.Apis.Api;
import com.example.spring_android_project.R;
import com.example.spring_android_project.Services.UserService;
import com.example.spring_android_project.Utils.Book;
import com.example.spring_android_project.Utils.User;
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
    private User user;
    private List<Book> bookList;
    private RecyclerView recyclerView_downloaded_books;
    DownloadedBooksAdapter adapter;
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

    // initialize
    private void init() {
        bookList = new ArrayList<>();
        recyclerView_downloaded_books = view.findViewById(R.id.recyclerView_downloaded_books);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 1);
        recyclerView_downloaded_books.setLayoutManager(mng);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh_layout);

    }

    private void getCurrentUserId() {

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userId;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("email").getValue().toString().equals(firebaseUser.getEmail())) {

                        userId = dataSnapshot.getKey();
                        user = new User(Integer.parseInt(userId));
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

        userService = Api.getUserService();
        Call<List<Book>> call = userService.getAllBooksForUser(userId);

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                bookList = response.body();
                adapter = new DownloadedBooksAdapter(bookList, getActivity(), getContext());
                recyclerView_downloaded_books.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

            }
        });


    }

    // set swipe to remove feature, also possible to undo deletion by using SnackBar
    private void swipeToRemove() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Book deletedBook = bookList.get(position);

                bookList.remove(position);
                adapter.notifyItemRemoved(position);
                Snackbar snackbar = Snackbar.make(recyclerView_downloaded_books, deletedBook.getBookName()
                                , Snackbar.LENGTH_LONG)
                        .setAction("undo", view -> {
                            bookList.add(position, deletedBook);
                            adapter.notifyItemInserted(position);

                        });
                snackbar.show();
                // remove book from db as well if user didnt undo deletion
                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            // Snackbar closed on its own
                            deleteBookForUser(user.getId(), deletedBook.getId());

                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                });
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

    // set search feature
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //Toast.makeText(getContext(),"FRAGMENT METHODUNDAYIZ",Toast.LENGTH_LONG).show();
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

    private void deleteBookForUser(int userId, int bookId) {

        userService = Api.getUserService();
        Call<String> call = userService.deleteBookForUser(userId,bookId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}