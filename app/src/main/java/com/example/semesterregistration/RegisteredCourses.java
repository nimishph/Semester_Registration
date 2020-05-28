package com.example.semesterregistration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static java.util.Arrays.asList;

public class RegisteredCourses extends Fragment {

    private int semester = 0;
    private TextView errorMessage;

    private LinearLayout semButtonLayout;
    private LinearLayout branchButtonLayout;
    private ConstraintLayout infoLayout;
    private ListView listView;

    public RegisteredCourses() {

    }

    static RegisteredCourses newInstance() {

        Bundle args = new Bundle();

        RegisteredCourses fragment = new RegisteredCourses();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registered_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        semButtonLayout = view.findViewById(R.id.semButtonLayout2);
        branchButtonLayout = view.findViewById(R.id.branchButtonLayout2);
        infoLayout = view.findViewById(R.id.coursesInfoListLayout);

        listView = view.findViewById(R.id.listView);

        errorMessage = view.findViewById(R.id.errorTV2);

        view.findViewById(R.id.semButton1b).setOnClickListener(this::onSemButtonClick);
        view.findViewById(R.id.semButton2b).setOnClickListener(this::onSemButtonClick);
        view.findViewById(R.id.semButton3b).setOnClickListener(this::onSemButtonClick);
        view.findViewById(R.id.semButton4b).setOnClickListener(this::onSemButtonClick);

        view.findViewById(R.id.cseButtonB).setOnClickListener(this::onBranchButtonClick);
        view.findViewById(R.id.eceButtonB).setOnClickListener(this::onBranchButtonClick);

    }

    private void onSemButtonClick(View view) {

        switch (view.getId()) {

            case R.id.semButton1b:
                semester = 1;
                break;
            case R.id.semButton2b:
                semester = 2;
                break;
            case R.id.semButton3b:
                semester = 3;
                break;
            case R.id.semButton4b:
                semester = 4;
                break;
            default:
                break;
        }

        semButtonLayout.setVisibility(View.GONE);
        branchButtonLayout.setVisibility(View.VISIBLE);
    }

    private void onBranchButtonClick(View view) {

        String branch = "";
        if (view.getId() == R.id.cseButtonB)
            branch = "CSE";
        else
            branch = "ECE";

        String message = "Error ! Couldn't fetch the data";

        errorMessage.setText(message);

        ArrayList<Student> studentArrayList = new ArrayList<>();
        SQLiteDatabase database = null;
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> courses = new ArrayList<>();

        try {
            database = this.getContext().openOrCreateDatabase("Students", MODE_PRIVATE, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (database != null) {
            try {
                String query = "SELECT * FROM students WHERE semester = " + semester + " AND branch = '" + branch + "'";

                Cursor c = database.rawQuery(query, null);

                int idIndex = c.getColumnIndex("id");
                int nameIndex = c.getColumnIndex("name");
                int coursesIndex = c.getColumnIndex("courses");

                c.moveToFirst();
                while (!c.isAfterLast()) {
                    id.add(c.getString(idIndex));
                    name.add(c.getString(nameIndex));
                    courses = new ArrayList<>(asList(c.getString(coursesIndex).split(", ")));
                    c.moveToNext();
                }

                c.close();

//            if (database.inTransaction()) {
//                database.endTransaction();
//            }
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage.setVisibility(View.VISIBLE);

            } finally {
                database.close();
            }
        }

        for (int i = 0; i < id.size(); i++) {
            studentArrayList.add(new Student(id.get(i), name.get(i), "", "", courses));
        }

        listView.setAdapter(new ListAdapter(Objects.requireNonNull(this.getContext()), studentArrayList, "registeredCourses"));

        branchButtonLayout.setVisibility(View.GONE);
        infoLayout.setVisibility(View.VISIBLE);

    }
}
