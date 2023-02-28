package com.example.spring_android_project.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import com.example.spring_android_project.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.pdfview.PDFView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFViewActivity extends AppCompatActivity {

    private AppCompatImageView imageView_pdf;
    private PdfRenderer.Page page;
    Bitmap bitmap;
    private MaterialToolbar toolbar;
    private RecyclerView recyclerView_pdf;
    private List<Bitmap> pageList;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        init();
        Intent intent = getIntent();
        String book_name = intent.getStringExtra("book_name");
        pdfView.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),book_name+".pdf")).show();
    }

    private void init(){
//        imageView_pdf = findViewById(R.id.imageView_pdf_view1);
        toolbar = findViewById(R.id.toolBar_pdfView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView_pdf = findViewById(R.id.recyclerView_pdf);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView_pdf.setLayoutManager(mng);
        pageList = new ArrayList<>();
        pdfView = findViewById(R.id.pdfView);
    }

}