package com.example.trackinjectv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackinjectv2.Injections.InjectionsActivity;
import com.example.trackinjectv2.authentication.MainActivity;
import com.example.trackinjectv2.authentication.User;
import com.example.trackinjectv2.medicine.MedicineActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMenuActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference database;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Background Animation code
        ConstraintLayout constraintLayout = findViewById(R.id.mainMenuLayout);
        AnimationDrawable animationDrawable =  (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.greetingTextView);


        TextView medicineButton = findViewById(R.id.medicineEditText);
        medicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, MedicineActivity.class));
            }
        });

        Button logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainMenuActivity.this, MainActivity.class));
            }
        });

        TextView injectionsButton = (TextView) findViewById(R.id.injectionsEditText);
        injectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, InjectionsActivity.class));
            }
        });


    }
}