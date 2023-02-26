package com.example.spring_android_project.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.spring_android_project.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PDFViewActivity extends AppCompatActivity {

    private AppCompatImageView imageView_pdf;
    private PdfRenderer.Page page;
    private MaterialToolbar toolbar;
    private Button button_next, button_prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        init();
        Intent intent = getIntent();
        String file_name = intent.getStringExtra("book_name");
        viewPDF(file_name);


//        button.setOnClickListener(view -> {
//            button.setVisibility(View.GONE);
//            viewPDF("sample");
//        });
////        viewPDF("sample");
    }

    private void init(){
        imageView_pdf = findViewById(R.id.imageView_pdf_view1);
        toolbar = findViewById(R.id.toolBar_pdfView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button_next = findViewById(R.id.button_next);
        button_prev = findViewById(R.id.button_prev);
    }

    private void viewPDF(String fileName){
        File pdfFile = new File("/sdcard/Download/sample2.pdf");
//        File pdfFile = new File("/sdcard/Download/"+fileName+".pdf");
//        File temp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
        String filePath = getExternalFilesDir(fileName).getAbsolutePath()+".pdf";
        System.out.println("path: "+filePath);
//        File pdfFile = new File(filePath);
        PdfRenderer pdfRenderer = null;
        try {
            pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int pageCount = pdfRenderer.getPageCount();

        // TODO: şu an muhtemel en sonu gösteriyor, scrollView koyabiliriz buraya, ya da next,
        // prev gibi butonlar koyabiliriz


        page = pdfRenderer.openPage(0);
        Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // Do something with the bitmap, like displaying it in an ImageView
        imageView_pdf.setImageBitmap(bitmap);

        button_prev.setOnClickListener(view -> {

        });

        button_next.setOnClickListener(view -> {

        });
//        for(int i = 0; i < pageCount; i++){
//            page = pdfRenderer.openPage(i);
//            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
//            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//            // Do something with the bitmap, like displaying it in an ImageView
//            imageView_pdf.setImageBitmap(bitmap);
//            page.close();
//        }
        page.close();
        pdfRenderer.close();
    }

//        // open the PDF file
//        ParcelFileDescriptor fileDescriptor = null;
//        try {
//            fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        PdfRenderer renderer = null;
//        try {
//            renderer = new PdfRenderer(fileDescriptor);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//// render the first page as a bitmap
//        PdfRenderer.Page page = renderer.openPage(0);
//        Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
//        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//
//// display the bitmap in an ImageView
//        imageView_pdf.setImageBitmap(bitmap);
//
//// clean up resources
//        page.close();
//        renderer.close();
//        try {
//            fileDescriptor.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//


//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void initRenderer(String fileName) throws IOException {
//
//        try {
//            File temp = new File(getCacheDir(),fileName);
//            FileOutputStream fos = new FileOutputStream(temp);
//            InputStream is = getAssets().open(fileName);
//
//            byte[] buffer = new byte[1024];
//            int readBytes;
//            while ((readBytes = is.read(buffer)) != -1){
//                fos.write(buffer,0,readBytes);
//            }
//            fos.close();
//            is.close();
//
//
//
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
}