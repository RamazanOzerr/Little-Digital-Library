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

import com.example.spring_android_project.Apis.Api;
import com.example.spring_android_project.R;
import com.example.spring_android_project.Services.UserService;
import com.example.spring_android_project.Utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

     EditText passwordSignUp, passwordSignUp_1, emailSignUp, name, editText_last_name;
     TextView loginText;
     Button signUpbutton;
     ImageView ImageViewSignUp;
     ProgressBar progressBarSignUp;
     FirebaseAuth auth;
     DatabaseReference reference;
     FirebaseUser user;
     UserService userService;

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
        reference = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();
        name = findViewById(R.id.name);
        editText_last_name = findViewById(R.id.editText_last_name);
    }

    private void signUp(){
        signUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String namee = name.getText().toString().trim();
                String last_name = editText_last_name.getText().toString().trim();
                String email = emailSignUp.getText().toString().trim();
                String password = passwordSignUp.getText().toString().trim();
                String password2 = passwordSignUp_1.getText().toString().trim();


                if(TextUtils.isEmpty(namee)){
                    name.setError("name is required");
                    return;
                }
                if(TextUtils.isEmpty(last_name)){
                    name.setError("last name is required");
                    return;
                }

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
                            setUserInfoIntoDb(namee,last_name, email);
                            //TODO BURASI

                            User user = new User(namee, last_name, email); // create user
                            System.out.println(user);
                            postAddedUserToApi(user);


                            Toast.makeText(SignUpActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LogInActivity.class));

                        } else {
                            Toast.makeText(SignUpActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBarSignUp.setVisibility(View.GONE);
                        }
                    }
                });
            }
    });
}

    private void postAddedUserToApi(User user) {

        userService = Api.getUserService();
        Call<User> userCall = userService.addUser(user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {
                    System.out.println("SUCCESFULLY ADDED");
                    System.out.println(response.code());
                    return;
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("FAILED");
            }
        });


    }

    private void setUserInfoIntoDb( String namee, String last_name, String email){
//        reference.child("Users").child(user.getUid());
//        Map<String, String> map = new HashMap<>();
//        map.put("id","1");
//        map.put("email",email); //alternatif olarak user.getEmail() de kullanılabilir
//        reference.setValue(map);


        reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                DatabaseReference databaseReference = reference.child("Users").child(String.valueOf(count+1));
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, String> map = new HashMap<>();
                        map.put("name",namee);
                        map.put("last name",last_name);
                        map.put("email",email);
                        databaseReference.setValue(map);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        reference.child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int count = (int) snapshot.getChildrenCount();
//                DatabaseReference databaseReference = reference.child("Users").child(user.getUid());
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Map<String, String> map = new HashMap<>();
//                        map.put("id",String.valueOf(count));
//                        map.put("email",email);
//                        databaseReference.setValue(map);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

}