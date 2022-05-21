package com.example.trackinjectv2.medicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.List;

public class MedicineActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference database;
    private String userID;

    private List<Medicine> medicineList;

    private ListView listView;

    private ArrayAdapter<Medicine> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicne);

        //Background Animation code
        RelativeLayout relativeLayout = findViewById(R.id.medicineLayout);
        AnimationDrawable animationDrawable =  (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        medicineList = new ArrayList<>();
        listView = findViewById(R.id.listViewMedicine);



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               showDeleteDialog(medicineList.get(i));
               return true;
            }
        });

        ImageButton addMedicine = findViewById(R.id.addMedicineImageButton);
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MedicineActivity.this, AddMedicineActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MedicineActivity.this, MainMenuActivity.class));
        finish();
    }

    protected void onStart() {
        super.onStart();
        database.child(userID).child("medicine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicineList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Medicine product = postSnapshot.getValue(Medicine.class);
                    medicineList.add(product);
                }
                adapter = new MedicineListView(MedicineActivity.this, medicineList);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MedicineActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDeleteDialog(Medicine medicine) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_update_medicine_dialog, null);
        dialogBuilder.setView(dialogView);

        TextView medicineName = dialogView.findViewById(R.id.medicineNameTV);
        TextView nOfPillsPerPackage = dialogView.findViewById(R.id.nOfPillsPerPackage);
        TextView nOfPillsPerDose = dialogView.findViewById(R.id.nOfPillsPerDose);
        TextView nOfDosesPerDay = dialogView.findViewById(R.id.nOfDosesPerDay);
        TextView notifications = dialogView.findViewById(R.id.notificationsTV);

        ListView listView1 = dialogView.findViewById(R.id.listViewTimes);

        Button deleteButton = dialogView.findViewById(R.id.deleteMedicineButton);

        medicineName.setText(medicine.getName());
        nOfPillsPerPackage.setText("Number of pills per package : ".concat(String.valueOf(medicine.getNbrOfPillsPerPackage())));
        nOfPillsPerDose.setText("Number of pills per dose : ".concat(String.valueOf(medicine.getNbrOfPillsPerDose())));
        nOfDosesPerDay.setText("Number of doses per day : ".concat(String.valueOf(medicine.getNbrOfDosesPerDay())));

        String days = "";
        for (Integer day : medicine.getPickedDays()) {
            if (day.equals(Calendar.MONDAY)) days = days.concat(", Monday");
            if (day.equals(Calendar.TUESDAY)) days = days.concat(", Tuesday");
            if (day.equals(Calendar.WEDNESDAY)) days = days.concat(", Wednesday");
            if (day.equals(Calendar.THURSDAY)) days = days.concat(", Thursday");
            if (day.equals(Calendar.FRIDAY)) days = days.concat(", Friday");
            if (day.equals(Calendar.SATURDAY)) days = days.concat(", Saturday");
            if (day.equals(Calendar.SUNDAY)) days = days.concat(", Sunday");
        }
        days = days.substring(1);
        notifications.setText("Notifications days : ".concat(days));

        ArrayAdapter<Time> adp = new NotificationsTimeListView(MedicineActivity.this, medicine.getPickedTimes());
        listView1.setAdapter(adp);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").
                        child(userID).child("medicine");
                medicineList.remove(medicine);
                databaseReference.setValue(medicineList);

                database.child(userID).child("medicine").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        medicineList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Medicine product = postSnapshot.getValue(Medicine.class);
                            medicineList.add(product);
                        }
                        adapter = new MedicineListView(MedicineActivity.this, medicineList);
                        listView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MedicineActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });

                alertDialog.dismiss();

                Toast.makeText(MedicineActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
            }
        });




    }


}