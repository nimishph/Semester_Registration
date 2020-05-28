package com.example.semesterregistration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.semesterregistration.StudentActivity.istask4completed;
import static com.example.semesterregistration.StudentActivity.studentDetails;
import static java.util.Arrays.asList;

public class CourseSelection extends AppCompatActivity {

    private final String TAG = "CourseSelectionActivity";

    ArrayAdapter<String> arrayAdapter1;
    ArrayAdapter<String> arrayAdapter2;
    ArrayList<String> selectedCoursesList;

    @Override
    public void onBackPressed() {
        Toast.makeText(this,R.string.onBackPressMessage, Toast.LENGTH_SHORT).show();
    }

    public void onNextClick (View view) {

        if (selectedCoursesList.size() >= 6) {

            istask4completed = true;

            StringBuilder courses = new StringBuilder();

            for (int i = 0; i < selectedCoursesList.size(); i++) {
                courses.append(selectedCoursesList.get(i));
                if (i != selectedCoursesList.size() - 1) {
                    courses.append(", ");
                }
            }

            studentDetails.put("courseList", courses.toString());

            Log.i("COURSES", courses.toString());

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

        ArrayList<String> cseSem1Courses = new ArrayList<>(asList("MAL 101: Mathematics-I (4)", "BEL 102: Elements of Electrical Engineering (4)", "BSL 101: Applied Sciences (4)", "CSL 101: Computer Programming (4)", "ECL 101: Analog Electronics (4)", "HUL 102: Environmental Studies (2)", "SAP 101: Health, Sports and Safety (0)"));
        ArrayList<String> eceSem1Courses = new ArrayList<>(asList("MAL 101: Mathematics-I (4)", "BEL 102: Elements of Electrical Engineering (4)", "CSL 101: Computer Programming (4)", "ECL 101: Analog Electronics (4)", "HUL 101: Communication Skills (3)", "BEL 101: Mechanics and Graphics (4)"));
        ArrayList<String> cseSem2Courses = new ArrayList<>(asList("MAL 102: Mathematics-II (4)", "ECL 102: Digital Electronics (4)", "CSL 102: Data Structures (4)", "CSL 103: Application Programming (4)", "HUL 101: Communication Skills (3)", "BEL 101: Mechanics and Graphics (4)"));
        ArrayList<String> eceSem2Courses = new ArrayList<>(asList("MAL 102: Mathematics-II (4)", "ECL 102: Digital Electronics (4)", "CSL 102: Data Structures (4)", "CSL 103: Application Programming (4)", "BSL 101: Applied Sciences (4)", "HUL 102: Environmental Studies (2)", "SAP 101: Health, Sports and Safety (0)"));
        ArrayList<String> cseSem3Courses = new ArrayList<>(asList("MAL 201: Mathematics-III (4)", "CSL 210: Data Structures with Applications (3)", "CSL 202: Object Oriented Programming (4)", "CSL 203: Computer System Organisation (3)", "CSP 201: IT Workshop-I (2)", "ECL 202: Microprocessors and Interfacing (4)"));
        ArrayList<String> eceSem3Courses = new ArrayList<>(asList("MAL 201: Mathematics-III (4)", "ECL 201: Signals and Systems (4)", "ECL 202: Microprocessors and Interfacing (4)", "ECL 203: Analog ICs (4)", "ECL 204: Network Theory (4)", "CSP 201: IT Workshop-I (2)"));
        ArrayList<String> cseSem4Courses = new ArrayList<>(asList("CSL 205: Design and Analysis of Algorithms (4)", "CSL 206: Software Engineering (3)", "CSL 207: Operating Systems (4)", "CSL 208: Design Principles of Programming Languages (4)", "CSL 204: Discrete Maths and Graph Theory (4)", "CSP 202: IT Workshop-II (2)"));
        ArrayList<String> eceSem4Courses = new ArrayList<>(asList("ECL 301: Digital Signal Processing (4)", "ECL 303: Hardware Description Languages (4)", "ECL 304: Control Systems (4)", "ECL 305: Electromagnetics (3)", "ECL 306: Computer Architecture % Organisation (3)", "CSP 202: IT Workshop-II (2)"));

        selectedCoursesList  = new ArrayList<>();

        arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedCoursesList);

        ListView availableCourses = findViewById(R.id.availableList);
        availableCourses.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        ListView selectedCourses = findViewById(R.id.selectedList);

        selectedCourses.setAdapter(arrayAdapter2);

        String backlog = studentDetails.get("backlog");

        int semester = Integer.parseInt(studentDetails.get("semester"));
        String branch = studentDetails.get("branch");

        if (semester == 1) {
            if (branch.equals("CSE"))
                arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, cseSem1Courses);
            else
                arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, eceSem1Courses);
            availableCourses.setAdapter(arrayAdapter1);
        }

        if (semester == 2) {
            if (branch.equals("CSE")) {
                arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, cseSem2Courses);
                if (backlog.equals("Yes")) {
                    cseSem2Courses.addAll(cseSem1Courses);
                }
            } else {
                arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, eceSem2Courses);
                if (backlog.equals("Yes")) {
                    eceSem2Courses.addAll(eceSem1Courses);
                }
            }

            availableCourses.setAdapter(arrayAdapter1);
        }

        if (semester == 3) {
            if (branch.equals("CSE")) {
                arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, cseSem3Courses);
                if (backlog.equals("Yes")) {
                    cseSem3Courses.addAll(cseSem2Courses);
                }
            } else {
                arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, eceSem3Courses);
                if (backlog.equals("Yes")) {
                    eceSem3Courses.addAll(eceSem2Courses);
                }
            }
            availableCourses.setAdapter(arrayAdapter1);
        }

        if (semester == 4) {
            if (branch.equals("CSE")) {
                arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, cseSem4Courses);
                if (backlog.equals("Yes")) {
                    cseSem4Courses.addAll(cseSem3Courses);
                }
            } else {
                arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, eceSem4Courses);
                if (backlog.equals("Yes")) {
                    eceSem4Courses.addAll(eceSem3Courses);
                }
            }
            availableCourses.setAdapter(arrayAdapter1);
        }

        availableCourses.setOnItemClickListener((parent, view, position, id) -> {

            CheckedTextView checkedTextView = (CheckedTextView) view;

            if (checkedTextView.isChecked()) {
                selectedCoursesList.add(arrayAdapter1.getItem(position));
                arrayAdapter2.notifyDataSetChanged();
                Toast.makeText(CourseSelection.this, "Registered !", Toast.LENGTH_SHORT).show();
            } else {
                selectedCoursesList.remove(arrayAdapter1.getItem(position));
                arrayAdapter2.notifyDataSetChanged();
                Toast.makeText(CourseSelection.this, "Unregistered !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
