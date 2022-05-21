package com.example.trackinjectv2.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trackinjectv2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Background Animation code
        ConstraintLayout constraintLayout = findViewById(R.id.registerLayout);
        AnimationDrawable animationDrawable =  (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        mAuth = FirebaseAuth.getInstance();

        //find view by id for UI elements
        firstName = findViewById(R.id.firstNameEditText);
        lastName = findViewById(R.id.lastNameEditText);
        email = findViewById(R.id.emailEditTextRegister);
        password = findViewById(R.id.passwordEditTextRegister);
        repeatPassword = findViewById(R.id.repeatPasswordEditText);

        Button register = findViewById(R.id.registerButtonRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String firstNameString = firstName.getText().toString();
        String lastNameString = lastName.getText().toString();
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        String repeatPasswordString = repeatPassword.getText().toString();

        //First Name Verification
        if (firstNameString.isEmpty() || firstNameString.equals("first name")) {
            firstName.setError("Please enter your First Name");
            firstName.requestFocus();
            return;
        }else if(!firstNameString.matches("^[A-Za-z]+$")){
            firstName.setError("First Name cannot contain symbols or digits");
            firstName.requestFocus();
            return;
        }

        //Last Name Verification
        if (lastNameString.isEmpty() || lastNameString.equals("last name")) {
            lastName.setError("Please enter your Last Name");
            lastName.requestFocus();
            return;
        }else if(!firstNameString.matches("^[A-Za-z]+$")){
            lastName.setError("Last Name cannot contain symbols or digits");
            lastName.requestFocus();
            return;
        }

        //Email Verification
        if (emailString.isEmpty() || emailString.equals("email")) {
            email.setError("Please enter your Email");
            email.requestFocus();
            return;
        } else if (!emailString.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        //Password Verification
        if (passwordString.isEmpty() || passwordString.equals("password")) {
            password.setError("Please enter your Password");
            password.requestFocus();
            return;
        } else if (passwordString.length()< 6) {
            password.setError("Password must contain at least six elements");
            password.requestFocus();
            return;
        }

        //Repeat Password Verification
        if (repeatPasswordString.isEmpty() || passwordString.equals("repeat password")) {
            repeatPassword.setError("Please repeat your password");
            repeatPassword.requestFocus();
            return;
        } else if (!passwordString.equals(repeatPasswordString)  ) {
            repeatPassword.setError("Passwords don't match");
            repeatPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(firstNameString, lastNameString, emailString, passwordString);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
                                        auth.sendEmailVerification();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        Toast.makeText(RegisterActivity.this, "Successfully registered. Please proceed to your email to verify your account", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register. Please try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Email you entered already exists", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}