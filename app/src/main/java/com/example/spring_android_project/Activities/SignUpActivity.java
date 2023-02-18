package com.example.spring_android_project.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spring_android_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText passwordSignUp, passwordSignUp_1, emailSignUp;
    private TextView loginText;
    private Button signUpbutton;
    private ImageView ImageViewSignUp;
    private ProgressBar progressBarSignUp;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        signUp();
    }

    private void init(){
        passwordSignUp = findViewById(R.id.passwordSignUp);
        passwordSignUp_1 = findViewById(R.id.passwordSignUp_1);
        emailSignUp = findViewById(R.id.emailSignUp);
        loginText = findViewById(R.id.loginText);
        signUpbutton = findViewById(R.id.signUpbutton);
        ImageViewSignUp = findViewById(R.id.ImageViewSignUp);
        progressBarSignUp = findViewById(R.id.progressBarSignUp);
        auth = FirebaseAuth.getInstance();
    }

    private void signUp(){
        signUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailSignUp.getText().toString().trim();
                String password = passwordSignUp.getText().toString().trim();
                String password2 = passwordSignUp_1.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    emailSignUp.setError("Email is required");
                    return;

                }
                if (TextUtils.isEmpty(password)) {
                    passwordSignUp.setError("Password is required");
                    return;
                }
                if (password.length() < 8) {
                    passwordSignUp_1.setError("Password must be at least 8 characters");
                    return;
                }
                if (password.equals(password2) == false) {
                    Toast.makeText(SignUpActivity.this, "Passwords must be the same!", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressBarSignUp.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(SignUpActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LogInActivity.class));

                        } else {
                            Toast.makeText(SignUpActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
    });
}
}