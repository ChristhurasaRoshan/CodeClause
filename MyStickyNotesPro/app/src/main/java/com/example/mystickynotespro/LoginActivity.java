package com.example.mystickynotespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText;
    Button loginButton;
    ProgressBar progressBar;
    TextView createAccountButtonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton= findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        createAccountButtonTextView= findViewById(R.id.create_account_text_button);

        loginButton.setOnClickListener((v)-> loginUser());
        createAccountButtonTextView.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class)));

    }



    void loginUser(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validateData(email,password);
        if(!isValidated){
            return;
        }
        loginAccountInFirebase(email,password);
    }
    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"Error"+task.isSuccessful(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility((View.GONE));
            loginButton.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email,String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is Invalid");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password length is Invalid");
            return false;
        }
        return true;
    }
}