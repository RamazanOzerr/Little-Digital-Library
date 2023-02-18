package com.example.spring_android_project.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spring_android_project.Activities.MainActivity;
import com.example.spring_android_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView signUpTextView;
    private Button loginButton;
    private ImageView imageViewLogIn;
    private ProgressBar progressBarLogin;
    FirebaseAuth auth;
    String emailOfuser, passwordOfuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        init();
        login();
        signUp();

    }

    private void init(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signUpTextView = findViewById(R.id.signUptextView);
        loginButton = findViewById(R.id.loginButton);
        imageViewLogIn = findViewById(R.id.imageViewLogIn);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        auth = FirebaseAuth.getInstance();
    }

    private void login(){
       loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               emailOfuser = email.getText().toString().trim();
               passwordOfuser = password.getText().toString().trim();
               if(TextUtils.isEmpty(emailOfuser)){
                   email.setError("email is required");
               }
               if(TextUtils.isEmpty(passwordOfuser)){
                   password.setError("Password is required");
               }
               if(password.length() < 8){
                   password.setError("Password must be at least 8 characters");
               }
               progressBarLogin.setVisibility(View.VISIBLE);
               try{
                   auth.signInWithEmailAndPassword(emailOfuser, passwordOfuser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(LogInActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(getApplicationContext(), MainActivity.class));
                           } else {
                               Toast.makeText(LogInActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                           }
                       }
                   });
               } catch (Exception e) {
                   Toast.makeText(LogInActivity.this, "You must enter the password",
                           Toast.LENGTH_LONG).show();
               }
           }
       });
    }

    private void signUp(){
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });
    }
}