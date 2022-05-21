package com.example.trackinjectv2.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackinjectv2.MainMenuActivity;
import com.example.trackinjectv2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        //Background Animation code
        ConstraintLayout constraintLayout = findViewById(R.id.loginLayout);
        AnimationDrawable animationDrawable =  (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        email = findViewById(R.id.emailOrUserNameEditTextLogin);
        password = findViewById(R.id.passwordEditTextLogin);

        //Register Button
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        //Forgot password button
        TextView forgotPasswordEditText = findViewById(R.id.forgotPasswordEditText);
        forgotPasswordEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class)); }
        });

        //Login button
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser(){
        String emailOrUsernameString = email.getText().toString();
        String passwordString = password.getText().toString();
        //Email Verification
        if (emailOrUsernameString.isEmpty() || emailOrUsernameString.equals("email")) {
            email.setError("Please enter your Email.");
            email.requestFocus();
            return;
        } else if (emailOrUsernameString.contains("@") || emailOrUsernameString.contains(".")) {

            if (!emailOrUsernameString.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                email.setError("Please enter a valid email or Username.");
                email.requestFocus();
                return;
            }
        } else if (emailOrUsernameString.length() < 6) {
            email.setError("Please enter a valid email or Username.");
            email.requestFocus();
            return;
        }

        //Password Verification
        if (passwordString.isEmpty() || passwordString.equals("password")) {
            password.setError("Please enter your Password.");
            password.requestFocus();
            return;
        } else if (passwordString.length()< 6) {
            password.setError("Password must contain at least six element.");
            password.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(emailOrUsernameString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    assert user != null;
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Please proceed to your email to verify your account.", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(MainActivity.this, "Failed to Login. Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}