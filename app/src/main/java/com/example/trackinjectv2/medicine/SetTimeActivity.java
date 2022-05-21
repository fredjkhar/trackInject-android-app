package com.example.trackinjectv2.medicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.trackinjectv2.R;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class SetTimeActivity extends AppCompatActivity {


    private int hour;
    private int minute;


    private Button addTime;
    private ListView timeListView;
    private Button addMedicine;

    private ToggleButton monday;
    private ToggleButton tuesday;
    private ToggleButton wednesday;
    private ToggleButton thursday;
    private ToggleButton friday;
    private ToggleButton saturday;
    private ToggleButton sunday;

    private List<Time> timeList;
    private List<Integer> daysList;
    private List<Medicine> medicineList;
    private List<Integer> newAlarmIDs;


    private FirebaseUser user;
    private DatabaseReference database;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

        //Background Animation code
        ConstraintLayout constraintLayout = findViewById(R.id.setTimeLayout);
        AnimationDrawable animationDrawable =  (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        addTime = findViewById(R.id.addTimesButton);
        timeListView = findViewById(R.id.selectedTimesListView);
        addMedicine = findViewById(R.id.finalAddMedicine);

        monday = findViewById(R.id.mondayToggleButton);
        tuesday = findViewById(R.id.tuesdayToggleButton);
        wednesday = findViewById(R.id.wednesdayToggleButton);
        thursday = findViewById(R.id.thursdayToggleButton);
        friday = findViewById(R.id.fridayToggleButton);
        saturday = findViewById(R.id.saturdayToggleButton);
        sunday = findViewById(R.id.sundayToggleButton);


        timeList = new ArrayList<>();
        daysList = new ArrayList<>();
        medicineList = new ArrayList<>();

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetTimeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;

                        timeList.add(new Time(hour,minute));
                        ArrayAdapter<Time> adapter = new NotificationsTimeListView(SetTimeActivity.this, timeList);
                        timeListView.setAdapter(adapter);
                    }
                },12,0,false
                );

                Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour,minute);
                timePickerDialog.show();
            }
        });

        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = Objects.requireNonNull(getIntent().getExtras()).getString("name");
                int nOfPillsPerDoseString = getIntent().getExtras().getInt("nOfPillsPerDoseString");
                int nOfPillsPerPackageString = getIntent().getExtras().getInt("nOfPillsPerPackageString");
                int nOfDosesPerDayString = getIntent().getExtras().getInt("nOfDosesPerDayString");
                boolean notificationsIsChecked = getIntent().getExtras().getBoolean("notificationsIsChecked");

                if (!toggleButtonsCheck()) return;
                newAlarmIDs = new ArrayList<>();
                for (int i = 0; i < timeList.size(); i++) {
                    int hour = timeList.get(i).getHour();
                    int minute = timeList.get(i).getMinute();
                    for (int j = 0; j < daysList.size(); j++) {
                        int Uid = (int) (SystemClock.uptimeMillis() % 99999999);
                        newAlarmIDs.add(Uid);
                        setAlarm(daysList.get(j),hour,minute,Uid);
                    }
                }
                String id = database.child(userID).child("medicine").push().getKey();
                Medicine medicine = new Medicine(id,nameString,nOfPillsPerDoseString,nOfPillsPerPackageString,nOfDosesPerDayString,
                        notificationsIsChecked,newAlarmIDs,timeList,daysList,"medicine");

                medicineList.add(medicine);

                database.child(userID).child("medicine").setValue(medicineList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SetTimeActivity.this, "Successfully added", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SetTimeActivity.this, MedicineActivity.class));
                        } else {
                            Toast.makeText(SetTimeActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

    private boolean toggleButtonsCheck() {
        if (!monday.isChecked() && !tuesday.isChecked() && !wednesday.isChecked()
                && !thursday.isChecked() && !friday.isChecked() && !saturday.isChecked() && !sunday.isChecked()) {
            Toast.makeText(SetTimeActivity.this, "Please pick a least one day of the week.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (monday.isChecked()) daysList.add(Calendar.MONDAY);
        if (tuesday.isChecked()) daysList.add(Calendar.TUESDAY);
        if (wednesday.isChecked()) daysList.add(Calendar.WEDNESDAY);
        if (thursday.isChecked()) daysList.add(Calendar.THURSDAY);
        if (friday.isChecked()) daysList.add(Calendar.FRIDAY);
        if (saturday.isChecked()) daysList.add(Calendar.SATURDAY);
        if (sunday.isChecked()) daysList.add(Calendar.SUNDAY);

        return true;
    }



    private void setAlarm(int dayOfTheWeek, int hour, int minute, int alarmID) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, dayOfTheWeek);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar calendarNow = Calendar.getInstance();

        if(calendar.before(calendarNow)) calendar.add(Calendar.DATE, 7);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(SetTimeActivity.this,Alarm.class);
        intent.putExtra("id",alarmID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(SetTimeActivity.this, alarmID, intent,0);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

    }

     /*public void setExactAlarm(int alarmID) {
        Intent intent = new Intent(SetTimeActivity.this,Alarm.class);
        intent.putExtra("id",alarmID);
        PendingIntent broadcast =PendingIntent.getBroadcast(SetTimeActivity.this, alarmID, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,AlarmManager.INTERVAL_FIFTEEN_MINUTES,broadcast);
    }*/





}