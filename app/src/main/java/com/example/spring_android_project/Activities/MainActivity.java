package com.example.spring_android_project.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.spring_android_project.Fragments.AvailableBooksFragment;
import com.example.spring_android_project.Fragments.DowloadedBooksFragment;
import com.example.spring_android_project.R;
import com.example.spring_android_project.Services.UserService;
import com.example.spring_android_project.Utils.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    UserService userService;
    List<User> listUser = new ArrayList<>();
    ListView listView;
    private BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        replaceFragments(new AvailableBooksFragment());

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.page1){
                    replaceFragments(new AvailableBooksFragment());
                }else if(item.getItemId() == R.id.page2) {
                    replaceFragments(new DowloadedBooksFragment());
                }
                return false;
            }
        });
    }

    private void init(){
        navigationView = findViewById(R.id.bottom_navigation);
    }


    public void replaceFragments(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_main,fragment);
        fragmentTransaction.commit();
    }

}