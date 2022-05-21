package com.example.trackinjectv2.medicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.trackinjectv2.Injections.Injections;
import com.example.trackinjectv2.Injections.InjectionsActivity;
import com.example.trackinjectv2.Injections.InjectionsListView;
import com.example.trackinjectv2.R;
import com.example.trackinjectv2.authentication.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddMedicineActivity extends AppCompatActivity {

    private EditText name;
    private EditText nOfPillsPerPackage;
    private EditText nOfPillsPerDose;
    private EditText nOfDosesPerDay;
    private SwitchCompat notifications;
    private Button setTime;

    private List<Medicine> medicineList;

    private FirebaseUser user;
    private DatabaseReference database;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        //Background Animation code
        ConstraintLayout constraintLayout = findViewById(R.id.addMedicineLayout);
        AnimationDrawable animationDrawable =  (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        //graphic components find view by Id
        name = (EditText) findViewById(R.id.setMedicineNameEditText);
        nOfPillsPerPackage = findViewById(R.id.setNumberOfPillsPerPackageEditText);
        nOfPillsPerDose = findViewById(R.id.setNumberOfPillsPerDose);
        nOfDosesPerDay = findViewById(R.id.setNumberOfDosesPerDay);
        notifications = findViewById(R.id.notificationsSwitch);
        setTime = findViewById(R.id.setTimeButton);

        notifications.setChecked(false);
        setTime.setVisibility(View.GONE);

        medicineList = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        //set repeat and setTime buttons to invisible while notifications switch is off and the
        //opposite when the switch is set to on
        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    setTime.setVisibility(View.VISIBLE);
                } else {
                    setTime.setVisibility(View.GONE);
                }
            }
        });

        ImageButton addMedicine = findViewById(R.id.addMedicineButton);
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedicine();
            }
        });

        Button setTime = (Button) findViewById(R.id.setTimeButton);
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!verifyInputs()) return;
                String nameString = name.getText().toString();
                int nOfPillsPerDoseString = Integer.parseInt(nOfPillsPerDose.getText().toString());
                int nOfPillsPerPackageString = Integer.parseInt(nOfPillsPerPackage.getText().toString());
                int nOfDosesPerDayString = Integer.parseInt(nOfDosesPerDay.getText().toString());
                boolean notificationsIsChecked = notifications.isChecked();


                Intent intent = new Intent(AddMedicineActivity.this, SetTimeActivity.class);
                intent.putExtra("name",nameString);
                intent.putExtra("nOfPillsPerPackageString",nOfPillsPerPackageString);
                intent.putExtra("nOfDosesPerDayString",nOfDosesPerDayString);
                intent.putExtra("notificationsIsChecked",notificationsIsChecked);
                intent.putExtra("nOfPillsPerDoseString",nOfPillsPerDoseString);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        database.child(userID).child("medicine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicineList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Medicine medicine = postSnapshot.getValue(Medicine.class);
                    medicineList.add(medicine);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addMedicine() {
        if (!verifyInputs()) return;
        boolean notificationsIsChecked = notifications.isChecked();
        if (notificationsIsChecked) {
            Toast.makeText(AddMedicineActivity.this, "Please set your notifications.", Toast.LENGTH_LONG).show();
            return;
        }

        String nameString = name.getText().toString();
        int nOfPillsPerDoseString = Integer.parseInt(nOfPillsPerDose.getText().toString());
        int nOfPillsPerPackageString = Integer.parseInt(nOfPillsPerPackage.getText().toString());
        int nOfDosesPerDayString = Integer.parseInt(nOfDosesPerDay.getText().toString());


        String id = database.child(userID).child("medicine").push().getKey();
        Medicine medicine = new Medicine(id,nameString,nOfPillsPerDoseString,nOfPillsPerPackageString,nOfDosesPerDayString,false);

        medicineList.add(medicine);

        database.child(userID).child("medicine").setValue(medicineList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()) {
                   Toast.makeText(AddMedicineActivity.this, "Successfully added", Toast.LENGTH_LONG).show();
                   startActivity(new Intent(AddMedicineActivity.this, MedicineActivity.class));
               } else {
                   Toast.makeText(AddMedicineActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
               }
            }
        });
    }

    private boolean verifyInputs() {
        String nameString = name.getText().toString();
        int nOfPillsPerDoseString = Integer.parseInt(nOfPillsPerDose.getText().toString());
        int nOfPillsPerPackageString = Integer.parseInt(nOfPillsPerPackage.getText().toString());
        int nOfDosesPerDayString = Integer.parseInt(nOfDosesPerDay.getText().toString());


        if(nameString.isEmpty()) {
            name.setError("Please set medicine name.");
            name.requestFocus();
            return false;
        }
        if (String.valueOf(nOfPillsPerDoseString).isEmpty()) {
            nOfPillsPerDose.setError("Please set the number of pills per dose.");
            nOfPillsPerDose.requestFocus();
            return false;
        }
        if (String.valueOf(nOfPillsPerPackageString).isEmpty()) {
            nOfPillsPerPackage.setError("Please set the number of pills per package.");
            nOfPillsPerPackage.requestFocus();
            return false;
        }
        if (String.valueOf(nOfDosesPerDayString).isEmpty()) {
            nOfDosesPerDay.setError("Please set the number of doses per day.");
            nOfDosesPerDay.requestFocus();
            return false;
        }

        return true;
    }


}