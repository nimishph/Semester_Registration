package com.example.semesterregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Arrays.asList;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {

    String mail;

    static ImageView imageView1, imageView2, imageView3, imageView4;

    FloatingActionButton newRegButton, popUp;
    TextView newRegTextView;

    ListView coursesListView;

    SharedPreferences sharedPreferences;

    static SQLiteDatabase sqLiteDatabase;
    static Boolean istask1completed;
    static Boolean istask2completed;
    static Boolean istask3completed;
    static Boolean istask4completed;
    Boolean isRegistered;

    static HashMap<String, String> studentDetails;

    ArrayList<String> selectedCourses = new ArrayList<>(asList("No courses selected"));

    public void closeList(View view) {
        if (coursesListView.getVisibility() == View.VISIBLE) {
            coursesListView.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() { finishAffinity(); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.log_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            sharedPreferences = getSharedPreferences("com.example.semesterregistration", Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("isLoggedIn",false).apply();
            finishAffinity();
        }

        if (item.getItemId() == R.id.courses) {
            Button closeButton = findViewById(R.id.closeButton);
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, selectedCourses);
            coursesListView.setAdapter(arrayAdapter);
            coursesListView.setVisibility(View.VISIBLE);
            closeButton.setVisibility(View.VISIBLE);
        }

        if (item.getItemId() == R.id.feePayment) {

            if (istask1completed && istask2completed) {
                if (istask3completed) {
                    Toast.makeText(this, "Already paid !", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(this, FeesStatus.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Previous stages incomplete !", Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void newRegistration(View view) {
        isRegistered =  sharedPreferences.getBoolean("isRegistered", false);
        if (!isRegistered && !istask1completed) {
            Intent intent = new Intent(this, PersonalInfo.class);
            intent.putExtra("email",mail);
            startActivity(intent);
        } else {
            Toast.makeText(this,"Registration in progress!\nTap on \"Continue registration\" to complete registration", Toast.LENGTH_SHORT).show();
            if (!istask2completed) {
                Intent intent = new Intent (this, AcademicInfo.class);
                startActivity(intent);
            } else if (!istask3completed) {
                Intent intent = new Intent(this, FeesStatus.class);
                startActivity(intent);
            } else if (!istask4completed) {
                Intent intent = new Intent(this, CourseSelection.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Already registered !", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setRegistrationDetails() {

        TextView name = findViewById(R.id.nameView);
        TextView id = findViewById(R.id.enrollmentIdView);
        TextView email = findViewById(R.id.emailView);
        TextView branch = findViewById(R.id.branchView);
        TextView feeStatus = findViewById(R.id.feeStatusView);
        TextView backlog = findViewById(R.id.backlogView);

        name.setText(studentDetails.get("name"));
        id.setText(studentDetails.get("id"));
        email.setText(studentDetails.get("email"));
        branch.setText(studentDetails.get("branch"));
        feeStatus.setText(studentDetails.get("feeStatus"));
        backlog.setText(studentDetails.get("backlog"));

        try {
            selectedCourses = (ArrayList<String>) ObjectSerializer.deserialize(studentDetails.get("selectedCourses"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ConstraintLayout regProfileLayout = findViewById(R.id.regProfileLayout);
        regProfileLayout.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        setTitle("Profile");

        sqLiteDatabase = this.openOrCreateDatabase("Students",MODE_PRIVATE, null);

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS events (name VARCHAR, )");

        ConstraintLayout layout = findViewById(R.id.studentActivityLayout);
        layout.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("com.example.semesterregistration",Context.MODE_PRIVATE);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);

        coursesListView = findViewById(R.id.coursesListView);

        newRegButton = findViewById(R.id.newRegOptionButton);
        newRegTextView = findViewById(R.id.newRegistrationOption);

        istask1completed = sharedPreferences.getBoolean("task1",false);
        istask2completed = sharedPreferences.getBoolean("task2",false);
        istask3completed = sharedPreferences.getBoolean("task3",false);
        istask4completed = sharedPreferences.getBoolean("task4",false);

        try {
            studentDetails = (HashMap<String, String>) ObjectSerializer.deserialize(sharedPreferences.getString("studentDetails", ObjectSerializer.serialize(new HashMap<String, String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (istask1completed) {
            imageView1.setImageResource(R.drawable.complete);
        }
        if (istask2completed) {
            imageView2.setImageResource(R.drawable.complete);
        }

        if (istask3completed) {
            imageView3.setImageResource(R.drawable.complete);
        }

        if (istask4completed) {
            imageView4.setImageResource(R.drawable.complete);
        }

        if (istask1completed && istask2completed && istask3completed && istask4completed) {
            sharedPreferences.edit().putBoolean("isRegistered",true).apply();
            setRegistrationDetails();
        } else if (istask1completed || istask2completed || istask3completed || istask4completed) {
            newRegTextView.setText("Continue Registration");
        }


        Intent intent = getIntent();
        mail = intent.getStringExtra("email");

        popUp  = findViewById(R.id.popUpButton);
        popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (newRegButton.getVisibility() == View.VISIBLE) {
                    newRegButton.animate().alpha(0).setDuration(600).start();
                    newRegTextView.animate().alpha(0).setDuration(600).start();
                    view.animate().rotationBy(-360).setDuration(800).start();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            popUp.setImageResource(R.drawable.add);
                            popUp.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233,30,99)));
                            newRegButton.setVisibility(View.GONE);
                            newRegTextView.setVisibility(View.GONE);
                        }
                    }, 600);
                } else {
                    popUp.setImageResource(R.drawable.incomplete);
                    popUp.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)));
                    view.animate().rotationBy(360).setDuration(800).start();

                    newRegButton.setVisibility(View.VISIBLE);
                    newRegTextView.setVisibility(View.VISIBLE);
                    newRegButton.animate().alpha(1).setDuration(600).start();
                    newRegTextView.animate().alpha(1).setDuration(600).start();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (newRegButton.getVisibility() == View.VISIBLE) {
            popUp.callOnClick();
        }
    }
}