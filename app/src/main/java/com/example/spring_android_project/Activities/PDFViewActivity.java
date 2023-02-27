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
        String file_name = intent.getStringExtra("book_name");
        String book_name = "sample";
//        viewPDF(file_name);
//        pdfView.fromFile("/sdcard/Download/sample2.pdf").show();
//        pdfView.fromFile(file_name).show();
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




    private void viewPDF(String fileName){
        File pdfFile = new File("/sdcard/Download/sample2.pdf");
//        File pdfFile = new File("/sdcard/Download/"+fileName+".pdf");
//        File temp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
//        String filePath = getExternalFilesDir(fileName).getAbsolutePath()+".pdf";
//        System.out.println("path: "+filePath);
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

//        int pageNumber = 0;
//
//        page = pdfRenderer.openPage(pageNumber);

//        Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
//        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//        // Do something with the bitmap, like displaying it in an ImageView
//        imageView_pdf.setImageBitmap(bitmap);

        for(int i = 0; i < pageCount; i++){
            page = pdfRenderer.openPage(i);
            bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            pageList.add(bitmap);
//            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//            // Do something with the bitmap, like displaying it in an ImageView
//            imageView_pdf.setImageBitmap(bitmap);
            page.close();
        }

//        page.close();
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