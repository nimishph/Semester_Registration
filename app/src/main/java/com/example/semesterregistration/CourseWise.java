package com.example.semesterregistration;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import static java.util.Arrays.asList;

public class CourseWise extends Fragment {

    private View layoutView;
    private LinearLayout semButtonLayout;
    private LinearLayout branchButtonLayout;
    private LinearLayout courseButtonLayout;
    private ConstraintLayout infoListLayout;
    private TextView errorMessage;

    private int semester = 0;
    private String branch = "";
    private String course = "";
    private ArrayList<Button> courseButtons = new ArrayList<>();

    public CourseWise() {
    }

    static CourseWise newInstance() {

        Bundle args = new Bundle();

        CourseWise fragment = new CourseWise();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.course_wise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutView = view;
        semButtonLayout = view.findViewById(R.id.semButtonLayout3);
        branchButtonLayout = view.findViewById(R.id.branchButtonLayout3);
        courseButtonLayout = view.findViewById(R.id.courseButtonLayout);
        infoListLayout = view.findViewById(R.id.infoListLayout);

        courseButtons.add(view.findViewById(R.id.courseButton1));
        courseButtons.add(view.findViewById(R.id.courseButton2));
        courseButtons.add(view.findViewById(R.id.courseButton3));
        courseButtons.add(view.findViewById(R.id.courseButton4));
        courseButtons.add(view.findViewById(R.id.courseButton5));
        courseButtons.add(view.findViewById(R.id.courseButton6));

        errorMessage = view.findViewById(R.id.errorTV3);

        view.findViewById(R.id.semButton1c).setOnClickListener(this::onSemButtonClick);
        view.findViewById(R.id.semButton2c).setOnClickListener(this::onSemButtonClick);
        view.findViewById(R.id.semButton3c).setOnClickListener(this::onSemButtonClick);
        view.findViewById(R.id.semButton4c).setOnClickListener(this::onSemButtonClick);

        view.findViewById(R.id.cseButtonC).setOnClickListener(this::onBranchButtonClick);
        view.findViewById(R.id.eceButtonC).setOnClickListener(this::onBranchButtonClick);

        for (Button button : courseButtons) {
            button.setOnClickListener(this::onCourseButtonClick);
        }
    }

    private void onSemButtonClick(View view) {

        switch (view.getId()) {

            case R.id.semButton1c:
                semester = 1;
                break;
            case R.id.semButton2c:
                semester = 2;
                break;
            case R.id.semButton3c:
                semester = 3;
                break;
            case R.id.semButton4c:
                semester = 4;
                break;
            default:
                break;
        }

        semButtonLayout.setVisibility(View.GONE);
        branchButtonLayout.setVisibility(View.VISIBLE);
    }

    private void onBranchButtonClick(View view) {

        ArrayList<String> cseSem1Courses = new ArrayList<>(asList("MAL 101: Mathematics-I (4)", "BEL 102: Elements of Electrical Engineering (4)", "BSL 101: Applied Sciences (4)", "CSL 101: Computer Programming (4)", "ECL 101: Analog Electronics (4)", "HUL 102: Environmental Studies (2)", "SAP 101: Health, Sports and Safety (0)"));
        ArrayList<String> eceSem1Courses = new ArrayList<>(asList("MAL 101: Mathematics-I (4)", "BEL 102: Elements of Electrical Engineering (4)", "CSL 101: Computer Programming (4)", "ECL 101: Analog Electronics (4)", "HUL 101: Communication Skills (3)", "BEL 101: Mechanics and Graphics (4)"));
        ArrayList<String> cseSem2Courses = new ArrayList<>(asList("MAL 102: Mathematics-II (4)", "ECL 102: Digital Electronics (4)", "CSL 102: Data Structures (4)", "CSL 103: Application Programming (4)", "HUL 101: Communication Skills (3)", "BEL 101: Mechanics and Graphics (4)"));
        ArrayList<String> eceSem2Courses = new ArrayList<>(asList("MAL 102: Mathematics-II (4)", "ECL 102: Digital Electronics (4)", "CSL 102: Data Structures (4)", "CSL 103: Application Programming (4)",  "BSL 101: Applied Sciences (4)", "HUL 102: Environmental Studies (2)", "SAP 101: Health, Sports and Safety (0)"));
        ArrayList<String> cseSem3Courses = new ArrayList<>(asList("MAL 201: Mathematics-III (4)", "CSL 210: Data Structures with Applications (3)", "CSL 202: Object Oriented Programming (4)", "CSL 203: Computer System Organisation (3)", "CSP 201: IT Workshop-I (2)", "ECL 202: Microprocessors and Interfacing (4)"));
        ArrayList<String> eceSem3Courses = new ArrayList<>(asList("MAL 201: Mathematics-III (4)", "ECL 201: Signals and Systems (4)", "ECL 202: Microprocessors and Interfacing (4)", "ECL 203: Analog ICs (4)", "ECL 204: Network Theory (4)", "CSP 201: IT Workshop-I (2)"));
        ArrayList<String> cseSem4Courses = new ArrayList<>(asList("CSL 205: Design and Analysis of Algorithms (4)", "CSL 206: Software Engineering (3)", "CSL 207: Operating Systems (4)", "CSL 208: Design Principles of Programming Languages (4)", "CSL 204: Discrete Maths and Graph Theory (4)", "CSP 202: IT Workshop-II (2)"));
        ArrayList<String> eceSem4Courses = new ArrayList<>(asList("ECL 301: Digital Signal Processing (4)", "ECL 303: Hardware Description Languages (4)", "ECL 304: Control Systems (4)", "ECL 305: Electromagnetics (3)", "ECL 306: Computer Architecture % Organisation (3)", "CSP 202: IT Workshop-II (2)"));

        if (view.getId() == R.id.cseButtonC)
            branch = "CSE";
        else if (view.getId() == R.id.eceButtonC)
            branch = "ECE";

        switch (semester) {

            case 1:
                if (branch.equals("CSE")) {
                    for (int i=0; i<courseButtons.size(); i++) {
                        courseButtons.get(i).setText(cseSem1Courses.get(i));
                    }
                } else {
                    for (int i=0; i<courseButtons.size(); i++) {
                        courseButtons.get(i).setText(eceSem1Courses.get(i));
                    }
                }
                break;

            case 2:
                if (branch.equals("CSE")) {
                    for (int i=0; i<courseButtons.size(); i++) {
                        courseButtons.get(i).setText(cseSem2Courses.get(i));
                    }
                } else {
                    for (int i=0; i<courseButtons.size(); i++) {
                        courseButtons.get(i).setText(eceSem2Courses.get(i));
                    }
                }
                break;
            case 3:
                if (branch.equals("CSE")) {
                    for (int i=0; i<courseButtons.size(); i++) {
                        courseButtons.get(i).setText(cseSem3Courses.get(i));
                    }
                } else {
                    for (int i=0; i<courseButtons.size(); i++) {
                        courseButtons.get(i).setText(eceSem3Courses.get(i));
                    }
                }
                break;
            case 4:
                if (branch.equals("CSE")) {
                    for (int i=0; i<courseButtons.size(); i++) {
                        courseButtons.get(i).setText(cseSem4Courses.get(i));
                    }
                } else {
                    for (int i=0; i<courseButtons.size(); i++) {
                        courseButtons.get(i).setText(eceSem4Courses.get(i));
                    }
                }
                break;

        }

        branchButtonLayout.setVisibility(View.GONE);
        courseButtonLayout.setVisibility(View.VISIBLE);
    }

    private void onCourseButtonClick(View view) {

        ListView listView = layoutView.findViewById(R.id.listView);
        String message = "Error ! Couldn't fetch the data";

        errorMessage.setText(message);
        switch (view.getId()) {

            case R.id.courseButton1:
                course = courseButtons.get(0).getText().toString();
                break;
            case R.id.courseButton2:
                course = courseButtons.get(1).getText().toString();
                break;
            case R.id.courseButton3:
                course = courseButtons.get(2).getText().toString();
                break;
            case R.id.courseButton4:
                course = courseButtons.get(3).getText().toString();
                break;
            case R.id.courseButton5:
                course = courseButtons.get(4).getText().toString();
                break;
            case R.id.courseButton6:
                course = courseButtons.get(6).getText().toString();
                break;

        }

        SQLiteDatabase database = Objects.requireNonNull(this.getContext()).openOrCreateDatabase("Students", Context.MODE_PRIVATE, null);

        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> email = new ArrayList<>();

        if (database != null) {

            try {
                String query = "SELECT * FROM students WHERE semester = " + semester + " AND branch = '" + branch + "'";

                Cursor c = database.rawQuery(query, null);

                int idIndex = c.getColumnIndex("id");
                int nameIndex = c.getColumnIndex("name");
                int emailIndex = c.getColumnIndex("email");
                int coursesIndex = c.getColumnIndex("courses");

                c.moveToFirst();
                while (!c.isAfterLast()) {
                    ArrayList<String> courses = new ArrayList<>(asList(c.getString(coursesIndex).split(", ")));
                    if (courses.contains(course)) {
                        id.add(c.getString(idIndex));
                        name.add(c.getString(nameIndex));
                        email.add(c.getString(emailIndex));
                    }
                    c.moveToNext();
                }
                c.close();

            } catch (Exception e) {
                e.printStackTrace();
                errorMessage.setVisibility(View.VISIBLE);
            } finally {
                database.close();
            }
        }

        ArrayList<Student> studentArrayList = new ArrayList<>();

        for (int i = 0; i < id.size(); i++) {
            studentArrayList.add(new Student(id.get(i), name.get(i), email.get(i), "", new ArrayList<>()));
        }

        listView.setAdapter(new ListAdapter(Objects.requireNonNull(this.getContext()), studentArrayList, "courseWise"));

        courseButtonLayout.setVisibility(View.GONE);
        infoListLayout.setVisibility(View.VISIBLE);
    }

}
