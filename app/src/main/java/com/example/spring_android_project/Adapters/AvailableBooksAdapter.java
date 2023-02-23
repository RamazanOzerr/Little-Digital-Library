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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spring_android_project.R;
import com.example.spring_android_project.Utils.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvailableBooksAdapter extends RecyclerView.Adapter<AvailableBooksAdapter.ViewHolder> {

    private List<Book> bookList;
    Activity activity;
    Context context;

    public AvailableBooksAdapter(List<Book> bookList, Activity activity, Context context) {
        this.bookList = bookList;
        this.activity = activity;
        this.context = context;
    }


    @NonNull
    @Override
    public AvailableBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.available_books_recyclerview_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableBooksAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(bookList.get(position).getPhotoPath()).into(holder.book_image_available);
        holder.book_name_available.setText(bookList.get(position).getBookName());
        holder.downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadBook(bookList.get(position).getLink());
                System.out.println(bookList.get(position).getLink());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView book_name_available;
        CircleImageView book_image_available;
        CardView cardview_available_books;
        Button downloadbtn;

        ViewHolder(View itemView){
            super(itemView);

            book_name_available = itemView.findViewById(R.id.book_name_available);
            cardview_available_books = itemView.findViewById(R.id.cardview_available_books);
            book_image_available = itemView.findViewById(R.id.book_image_available);
            downloadbtn = itemView.findViewById(R.id.download_btn);

        }
    }

    private void downloadBook(String url){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        String title = URLUtil.guessFileName(url,null,null);
        request.setTitle(title);
        request.setDescription("book is downloading...");
        String cookie = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie",cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Toast.makeText(context,"download started",Toast.LENGTH_SHORT).show();

    }
}
