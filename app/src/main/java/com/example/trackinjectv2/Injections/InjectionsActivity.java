package com.example.trackinjectv2.Injections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.trackinjectv2.MainMenuActivity;
import com.example.trackinjectv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InjectionsActivity extends AppCompatActivity {

    private Button map;
    private ImageView addInjection;
    private ListView listView;

    private FirebaseUser user;
    private DatabaseReference database;
    private String userID;

    private ArrayAdapter<Injections> adapter;

    private List<Injections> injectionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injections_section_actitvity);

        //Background Animation code
        RelativeLayout relativeLayout = findViewById(R.id.injectionsSectionLayout);
        AnimationDrawable animationDrawable =  (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        map = (Button) findViewById(R.id.mapButton);
        addInjection = (ImageView) findViewById(R.id.addInjectionSiteImageButton);
        listView = (ListView) findViewById(R.id.listViewInjections);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        injectionsList = new ArrayList<>();

        addInjection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Injections injection = injectionsList.get(i);
                showUpdateDeleteDialog(injection.getId(),injection.getLocationName(),injection.getInjectionLocationNumber());
                return true;
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InjectionsActivity.this, InjectionsMapActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(InjectionsActivity.this, MainMenuActivity.class));
        finish();
    }

    protected void onStart() {
        super.onStart();
        database.child(userID).child("injections").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                injectionsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Injections injection = postSnapshot.getValue(Injections.class);
                    injectionsList.add(injection);
                }
                adapter = new InjectionsListView(InjectionsActivity.this, injectionsList);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InjectionsActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_injection_dialog, null);
        dialogBuilder.setView(dialogView);

       EditText locationName = (EditText) dialogView.findViewById(R.id.addInjectionLocation);
       EditText locationNumber = (EditText) dialogView.findViewById(R.id.addInjectionLocationNumber);
       Button addButton = (Button) dialogView.findViewById(R.id.addInjectionButton);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationNameString = locationName.getText().toString();
                String locationNumberString = locationNumber.getText().toString();

                if (locationNameString.isEmpty()) {
                    locationName.setError("Please enter site name.");
                    locationName.requestFocus();
                    return;
                }
                if (locationNumberString.isEmpty()) {
                    locationNumber.setError("Please enter site number.");
                    locationNumber.requestFocus();
                    return;
                }

                String id = database.push().getKey();
                Injections injection = new Injections(id, locationNameString, Integer.parseInt(locationNumberString));

                database.child(userID).child("injections").child(id).setValue(injection);
                Toast.makeText(InjectionsActivity.this, "Injection site added.", Toast.LENGTH_LONG).show();

                database.child(userID).child("injections").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        injectionsList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Injections injection = postSnapshot.getValue(Injections.class);
                            injectionsList.add(injection);
                        }
                        adapter = new InjectionsListView(InjectionsActivity.this, injectionsList);
                        listView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(InjectionsActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.dismiss();
            }
        });
    }

    private void showUpdateDeleteDialog(String id, String name, int number) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_delete_injection_dialog, null);
        dialogBuilder.setView(dialogView);

        EditText locationName = (EditText) dialogView.findViewById(R.id.updateInjectionLocation);
        EditText locationNumber  = (EditText) dialogView.findViewById(R.id.updateInjectionLocationNumber);
        Button updateButton = (Button) dialogView.findViewById(R.id.updateInjectionButton);
        Button deleteButton = (Button) dialogView.findViewById(R.id.deleteInjectionButton);

        locationName.setText(name);
        locationNumber.setText(String.valueOf(number));


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationNameString = locationName.getText().toString();
                String locationNumberString = locationNumber.getText().toString();

                if (locationNameString.isEmpty()) {
                    locationName.setError("Please enter site name.");
                    locationName.requestFocus();
                    return;
                }
                if (locationNumberString.isEmpty()) {
                    locationNumber.setError("Please enter site number.");
                    locationNumber.requestFocus();
                    return;
                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("injections").child(id);

                Injections injection = new Injections(id,locationNameString,Integer.parseInt(locationNumberString));
                databaseReference.setValue(injection);
                database.child(userID).child("injections").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        injectionsList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Injections injection = postSnapshot.getValue(Injections.class);
                            injectionsList.add(injection);
                        }
                        adapter = new InjectionsListView(InjectionsActivity.this, injectionsList);
                        listView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(InjectionsActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Injection site updated.", Toast.LENGTH_LONG).show();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("injections").child(id);
                databaseReference.removeValue();
                database.child(userID).child("injections").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        injectionsList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Injections injection = postSnapshot.getValue(Injections.class);
                            injectionsList.add(injection);
                        }
                        adapter = new InjectionsListView(InjectionsActivity.this, injectionsList);
                        listView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(InjectionsActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Injection site deleted.", Toast.LENGTH_LONG).show();
            }
        });
    }



}