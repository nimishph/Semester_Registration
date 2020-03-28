package com.example.semesterregistration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.semesterregistration.AcademicInfo.semester;
import static com.example.semesterregistration.StudentActivity.imageView4;
import static com.example.semesterregistration.StudentActivity.istask4completed;
import static com.example.semesterregistration.StudentActivity.studentDetails;
import static java.util.Arrays.asList;

public class CourseSelection extends AppCompatActivity {

    ArrayAdapter arrayAdapter1;
    ArrayAdapter arrayAdapter2;
    ArrayList<String> selectedCoursesList;

    @Override
    public void onBackPressed() {
        Toast.makeText(this,R.string.onBackPressMessage, Toast.LENGTH_SHORT).show();
    }

    public void onNextClick (View view) {

        if (selectedCoursesList.size() >= 6) {

            istask4completed = true;

            try {
                String selectedCourses = ObjectSerializer.serialize(selectedCoursesList);
                studentDetails.put("selectedCourses",selectedCourses);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences sharedPreferences = getSharedPreferences("com.example.semesterregistration", Context.MODE_PRIVATE);

            sharedPreferences.edit().putBoolean("task4", istask4completed).apply();

            try {
                sharedPreferences.edit().putString("studentDetails",ObjectSerializer.serialize(studentDetails)).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, resultActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Select more courses !", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_selection);

        ArrayList<String> sem1Courses = new ArrayList<>(asList("MAL 101: Mathematics-I (4)", "BEL 102: Elements of Electrical Engineering (4)", "BSL 101: Applied Sciences (4)", "CSL 101: Computer Programming (4)", "ECL 101: Analog Electronics (4)", "SAP 101: Health, Sports and Safety (0)", "HUL 102: Environmental Studies (2)"));
        ArrayList<String> sem2Courses = new ArrayList<>(asList("MAL 102: Mathematics-II (4)", "ECL 102: Digital Electronics (4)", "CSL 102: Data Structures (4)", "CSL 103: Application Programming (4)", "HUL 101: Communication Skills (3)", "BEL 101: Mechanics and Graphics (4)"));
        ArrayList<String> sem3Courses = new ArrayList<>(asList("MAL 201: Mathematics-III (4)", "CSL 210: Data Structures with Applications (3)", "CSL 202: Object Oriented Programming (4)", "CSL 203: Computer System Organisation (3)", "CSP 201: IT Workshop-I (2)", "ECL 202: Microprocessors and Interfacing (4)"));
        ArrayList<String> sem4Courses = new ArrayList<>(asList("CSL 205: Design and Analysis of Algorithms (4)", "CSL 206: Software Engineering (3)", "CSL 207: Operating Systems (4)", "CSL 208: Design Principles of Programming Languages (4)", "CSl 204: Discrete Maths and Graph Theory (4)", "CSP 202: IT Workshop-II (2)"));

        selectedCoursesList  = new ArrayList<>();

        arrayAdapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_1, selectedCoursesList);

        ListView availableCourses = findViewById(R.id.availableList);
        availableCourses.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        ListView selectedCourses = findViewById(R.id.selectedList);

        selectedCourses.setAdapter(arrayAdapter2);

        String backlog = StudentActivity.studentDetails.get("backlog");

        if (semester == 1) {
            arrayAdapter1 = new ArrayAdapter(this,android.R.layout.simple_list_item_checked, sem1Courses);
            availableCourses.setAdapter(arrayAdapter1);
        }

        if (semester == 2) {
            arrayAdapter1 = new ArrayAdapter(this,android.R.layout.simple_list_item_checked, sem2Courses);
            if (backlog.equals("Yes")) {
                sem2Courses.addAll(sem1Courses);
            }
            availableCourses.setAdapter(arrayAdapter1);
        }

        if (semester == 3) {
            arrayAdapter1 = new ArrayAdapter(this,android.R.layout.simple_list_item_checked, sem3Courses);
            if (backlog.equals("Yes")) {
                sem3Courses.addAll(sem2Courses);
            }
            availableCourses.setAdapter(arrayAdapter1);
        }

        if (semester == 4) {
            arrayAdapter1 = new ArrayAdapter(this,android.R.layout.simple_list_item_checked, sem4Courses);
            if (backlog.equals("Yes")) {
                sem4Courses.addAll(sem3Courses);
            }
            availableCourses.setAdapter(arrayAdapter1);
        }

        availableCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView checkedTextView = (CheckedTextView) view;

                if (checkedTextView.isChecked()) {
                    selectedCoursesList.add(arrayAdapter1.getItem(position).toString());
                    arrayAdapter2.notifyDataSetChanged();
                    Toast.makeText(CourseSelection.this,"Registered !", Toast.LENGTH_SHORT).show();
                } else {
                    selectedCoursesList.remove(arrayAdapter1.getItem(position).toString());
                    arrayAdapter2.notifyDataSetChanged();
                    Toast.makeText(CourseSelection.this,"Unregistered !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
