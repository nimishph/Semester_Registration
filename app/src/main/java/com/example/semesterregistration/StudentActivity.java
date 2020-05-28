package com.example.semesterregistration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.example.semesterregistration.MainActivity.currentUserMail;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {

    String mail;

    static HashMap<String, Object> registeredStudents = new HashMap<>();

    static ImageView imageView1, imageView2, imageView3, imageView4;

    FloatingActionButton newRegButton, popUp;
    TextView newRegTextView;

    ListView coursesListView;

    SharedPreferences sharedPreferences;
    static HashMap<String, String> studentDetails = new HashMap<>();

    static Boolean istask1completed;
    static Boolean istask2completed;
    static Boolean istask3completed;
    static Boolean istask4completed;

    Boolean isRegistered;
    static ArrayList<String> selectedCourses = new ArrayList<>(Collections.singletonList("No courses selected"));
    private final String TAG = "Student Activity";

    private void updateRegisteredStudentsList() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("task1", istask1completed);
        hashMap.put("task2", istask2completed);
        hashMap.put("task3", istask3completed);
        hashMap.put("task4", istask4completed);
        hashMap.put("isRegistered", isRegistered);
        hashMap.put("student_details", studentDetails);
        hashMap.put("selected_courses", selectedCourses);
        registeredStudents.put(studentDetails.get("email"), hashMap);

        try {
            sharedPreferences.edit().putString("registeredStudents", ObjectSerializer.serialize(registeredStudents)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
            sharedPreferences.edit().putString("currentUserMail", "").apply();
            sharedPreferences.edit().putString("currentUserPassword", "").apply();
            sharedPreferences.edit().putString("currentUserType", "").apply();

            updateRegisteredStudentsList();
            istask1completed = false;
            sharedPreferences.edit().putBoolean("task1", istask1completed).apply();
            istask2completed = false;
            sharedPreferences.edit().putBoolean("task2", istask2completed).apply();
            istask3completed = false;
            sharedPreferences.edit().putBoolean("task3", istask3completed).apply();
            istask4completed = false;
            sharedPreferences.edit().putBoolean("task4", istask4completed).apply();
            isRegistered = false;
            sharedPreferences.edit().putBoolean("isRegistered", isRegistered).apply();
            studentDetails.clear();
            try {
                sharedPreferences.edit().putString("studentDetails", ObjectSerializer.serialize(studentDetails)).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
            selectedCourses.clear();

            finishAffinity();
        }

        if (item.getItemId() == R.id.courses) {
            Button closeButton = findViewById(R.id.closeButton);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedCourses);
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

    void fillDatabase() {

        AsyncTask.execute(() -> {
            SQLiteDatabase database = this.openOrCreateDatabase("Students", MODE_PRIVATE, null);

            database.execSQL("CREATE TABLE IF NOT EXISTS students (id VARCHAR, name VARCHAR, email VARCHAR, semester VARCHAR, branch VARCHAR, feestatus VARCHAR, backlog VARCHAR, courses VARHCAR)");

            String Id = studentDetails.get("id");
            String Name = studentDetails.get("name");
            String Email = studentDetails.get("email");
            String Semester = studentDetails.get("semester");
            String Branch = studentDetails.get("branch");
            String FeeStatus = studentDetails.get("feeStatus");
            String Backlog = studentDetails.get("backlog");
            String Courses = studentDetails.get("courseList");

            if (Courses != null)
                Log.i("Courses", Courses);
            else
                Log.e("Courses", "NULL");

            String insertQuery = "INSERT INTO students VALUES (" +
                    "'" + Id +
                    "'" + ", " +
                    "'" + Name +
                    "'" + ", " +
                    "'" + Email +
                    "'" + ", " +
                    "'" + Semester +
                    "'" + ", " +
                    "'" + Branch +
                    "'" + ", " +
                    "'" + FeeStatus +
                    "'" + ", " +
                    "'" + Backlog +
                    "'" + ", " +
                    "'" + Courses + "'" + ")";
            database.execSQL(insertQuery);
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("id", Id);
//            contentValues.put("name", Name);
//            contentValues.put("email", Email);
//            contentValues.put("semester", Semester);
//            contentValues.put("branch", Branch);
//            contentValues.put("feestatus", FeeStatus);
//            contentValues.put("backlog", Backlog);
//            contentValues.put("courses", Courses);
//
//            database.insert("students", null, contentValues);

        });
    }

    public void setRegistrationDetails() {

        updateRegisteredStudentsList();

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

        ConstraintLayout layout = findViewById(R.id.studentActivityLayout);
        layout.setOnClickListener(this);

        TextView emailview = findViewById(R.id.emailView);
        emailview.setOnClickListener(v -> Toast.makeText(StudentActivity.this, emailview.getText().toString(), Toast.LENGTH_SHORT).show());

        mail = currentUserMail;

        sharedPreferences = getSharedPreferences("com.example.semesterregistration",Context.MODE_PRIVATE);

        Log.i("Registered-Students", String.valueOf(registeredStudents.containsKey(mail)));

        if (!registeredStudents.isEmpty() && registeredStudents.containsKey(mail)) {
            try {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) registeredStudents.get(mail);
                if (hashMap != null) {
                    istask1completed = (Boolean) hashMap.get("task1");
                    istask2completed = (Boolean) hashMap.get("task2");
                    istask3completed = (Boolean) hashMap.get("task3");
                    istask4completed = (Boolean) hashMap.get("task4");
                    isRegistered = (Boolean) hashMap.get("isRegistered");
                    studentDetails = (HashMap<String, String>) hashMap.get("student_details");
                    selectedCourses = (ArrayList<String>) hashMap.get("selected_courses");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            istask1completed = sharedPreferences.getBoolean("task1", false);
            istask2completed = sharedPreferences.getBoolean("task2", false);
            istask3completed = sharedPreferences.getBoolean("task3", false);
            istask4completed = sharedPreferences.getBoolean("task4", false);
            isRegistered = false;

            try {
                studentDetails = (HashMap<String, String>) ObjectSerializer.deserialize(sharedPreferences.getString("studentDetails", ObjectSerializer.serialize(new HashMap<String, String>())));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);

        coursesListView = findViewById(R.id.coursesListView);

        newRegButton = findViewById(R.id.newRegOptionButton);
        newRegTextView = findViewById(R.id.newRegistrationOption);

/*
        // Logs Start
        Log.i("task1 - " + TAG, istask1completed.toString());
        Log.i("task2 - " + TAG, istask2completed.toString());
        Log.i("task3 - " + TAG, istask3completed.toString());
        Log.i("task4 - " + TAG, istask4completed.toString());

        if (!studentDetails.isEmpty())
            Log.i(TAG, studentDetails.toString());
        else
            Log.i(TAG, "studentDetails is empty!");
        // Logs Finish
*/

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
            if (!isRegistered)
                fillDatabase();
            isRegistered = true;
            sharedPreferences.edit().putBoolean("isRegistered", true).apply();
            setRegistrationDetails();
        } else if (istask1completed || istask2completed || istask3completed || istask4completed) {
            newRegTextView.setText("Continue Registration");
        }

        popUp  = findViewById(R.id.popUpButton);
        popUp.setOnClickListener(view -> {

            if (newRegButton.getVisibility() == View.VISIBLE) {
                newRegButton.animate().alpha(0).setDuration(600).start();
                newRegTextView.animate().alpha(0).setDuration(600).start();
                view.animate().rotationBy(-360).setDuration(800).start();

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    popUp.setImageResource(R.drawable.add);
                    popUp.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30, 99)));
                    newRegButton.setVisibility(View.GONE);
                    newRegTextView.setVisibility(View.GONE);
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
        });
    }

    @Override
    public void onClick(View v) {

        if (newRegButton.getVisibility() == View.VISIBLE) {
            popUp.callOnClick();
        }
    }
}

