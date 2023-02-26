package com.example.spring_android_project.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spring_android_project.Activities.PDFViewActivity;
import com.example.spring_android_project.R;
import com.example.spring_android_project.Utils.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DownloadedBooksAdapter extends RecyclerView.Adapter<DownloadedBooksAdapter.ViewHolder> {

    private List<Book> bookList;
    Activity activity;
    Context context;

    public DownloadedBooksAdapter(List<Book> bookList, Activity activity, Context context) {
        this.bookList = bookList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.downloaded_books_recyclerview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(bookList.get(position).getPhotoPath()).into(holder.book_image_downloaded);
        holder.book_name_downloaded.setText(bookList.get(position).getBookName());

        holder.displayBtn.setOnClickListener(view -> {
            //TODO display method
            displayBook(bookList.get(position).getBookName());
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView book_name_downloaded;
        CircleImageView book_image_downloaded;
        CardView cardview_downloaded_books;
        Button displayBtn;

        ViewHolder(View itemView){
            super(itemView);

            book_name_downloaded = itemView.findViewById(R.id.book_name_downloaded);
            cardview_downloaded_books = itemView.findViewById(R.id.cardview_downloaded_books);
            book_image_downloaded = itemView.findViewById(R.id.book_image_downloaded);
            displayBtn = itemView.findViewById(R.id.display_btn);

        }
    }

    //TODO method sonra doldurulacak
    private void displayBook(String book_name){
        Intent intent = new Intent(context, PDFViewActivity.class);
        intent.putExtra("book_name",book_name);
        activity.startActivity(intent);
    }
}




