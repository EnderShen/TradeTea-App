package com.example.tradetea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private ImageView RBackIcon;
    private FirebaseAuth fAuth;
    private EditText REmail;
    private  EditText RPassword;
    private  Button RegisterBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //hide progress bar
        getSupportActionBar().hide();

        RBackIcon = findViewById(R.id.mBackIcon);
        REmail = findViewById(R.id.REmail);
        RPassword = findViewById(R.id.RPassword);
        RegisterBT = findViewById(R.id.RRegisterBT);
        fAuth = FirebaseAuth.getInstance();

        Register(REmail,RPassword,RegisterBT);
        BackToLogin(RBackIcon);
    }

    private void Register(EditText Email, EditText Password, Button RegisterBT)
    {
        RegisterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if (TextUtils.isEmpty(password)) {
                    Password.setError("Password is required");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Email.setError("Email is required");
                    return;
                }

                if (password.length() < 6) {
                    Password.setError("Password must >= 6 characters");
                    return;
                }

                //Crate a account with email and password
                //if registered successful, user will turn to home page
                //if fail to registered, display error message
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();

                        } else {
                            Toast.makeText(RegisterActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void BackToLogin(ImageView mBackIcon)
    {
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}