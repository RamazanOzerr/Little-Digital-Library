package com.example.spring_android_project.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spring_android_project.Apis.Api;
import com.example.spring_android_project.R;
import com.example.spring_android_project.Services.BookService;
import com.example.spring_android_project.Services.UserService;
import com.example.spring_android_project.Utils.Book;
import com.example.spring_android_project.Utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailableBooksAdapter extends RecyclerView.Adapter<AvailableBooksAdapter.ViewHolder> {

    private List<Book> bookList;
    Activity activity;
    Context context;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    UserService userService;
    BookService bookService;

    public AvailableBooksAdapter(List<Book> bookList, Activity activity, Context context) {
        this.bookList = bookList;
        this.activity = activity;
        this.context = context;
    }


    @NonNull
    @Override
    public AvailableBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.available_books_recyclerview_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableBooksAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(bookList.get(position).getPhotoPath()).into(holder.book_image_available);
//        holder.book_name_available.setText(bookList.get(position).getBookName());
        holder.text_name_view.setText(bookList.get(position).getBookName());
        holder.text_page_view.setText(String.valueOf(bookList.get(position).getPages()));
        holder.text_title_view.setText(bookList.get(position).getTitle());

        holder.downloadbtn.setOnClickListener(view -> {
            downloadBook(bookList.get(position).getLink());
            System.out.println(bookList.get(position).getLink());

            getCurrentUserId(bookList.get(position).getId());
        });
        System.out.println("*****************************");
        System.out.println(bookList.get(position).getBookName());
        System.out.println((bookList.get(position).getId()));
        System.out.println(bookList.get(position).getPhotoPath());


    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView book_name_available, text_name_view, text_page_view, text_title_view;
        CircleImageView book_image_available;
        CardView cardview_available_books;
        Button downloadbtn;

        ViewHolder(View itemView) {
            super(itemView);

            book_name_available = itemView.findViewById(R.id.book_name_available);
            cardview_available_books = itemView.findViewById(R.id.cardview_available_books);
            book_image_available = itemView.findViewById(R.id.book_image_available);
            downloadbtn = itemView.findViewById(R.id.download_btn);
            text_name_view = itemView.findViewById(R.id.text_name_view);
            text_page_view = itemView.findViewById(R.id.text_page_view);
            text_title_view = itemView.findViewById(R.id.text_title_view);

        }
    }

    private void downloadBook(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

//        String title = URLUtil.guessFileName(url, null, null);
        String title = "sample2.pdf";
        request.setTitle(title);
        request.setDescription("book is downloading...");
        String cookie = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(context, "download started", Toast.LENGTH_SHORT).show();

    }

    private void getCurrentUserId(int bookId) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //TODO db den kitap bilgilerini çekip listeye at aynı şekilde, AvailableBooksFragment da
        // yaptığımız işlemin aynısı

        // todo: firebase current user -- returns user id.
        // todo: post (/books/userid/bookid)

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userId;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    System.out.println("****************************************************************");
                    System.out.println(firebaseUser.toString());
                    if (dataSnapshot.child("email").getValue().toString().equals(firebaseUser.getEmail())) {

                        userId = dataSnapshot.getKey();
                        addBookForUser(Integer.parseInt(userId),bookId);
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addBookForUser(int userId, int bookId) {
        System.out.println("*******************" + userId);

        bookService = Api.getBookService();
        Call<Book> call = bookService.addBookForUser(userId,bookId);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {

                System.out.println(response.code());
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });

    }
}
