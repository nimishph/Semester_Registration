package com.example.semesterregistration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.semesterregistration.StudentActivity.imageView2;
import static com.example.semesterregistration.StudentActivity.istask2completed;
import static com.example.semesterregistration.StudentActivity.studentDetails;
import static java.util.Arrays.asList;

public class AcademicInfo extends AppCompatActivity {

    Spinner semesters;
    EditText enrollmentIdView;
    CheckBox cseBox, eceBox;
    Switch backlogSwitch;

    String branch;
    static int semester;
    String backlog = "No";

    @Override
    public void onBackPressed() {
        Toast.makeText(this,R.string.onBackPressMessage, Toast.LENGTH_SHORT).show();
    }

    public String generateID() {
        String ID = "BT" + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2) + branch + "001";
        return ID;
    }

    public void onNextClick(View view) {

        if ((!enrollmentIdView.getText().toString().isEmpty() || !enrollmentIdView.isEnabled()) && (cseBox.isChecked() || eceBox.isChecked()) && semesters.getSelectedItemPosition() > 0) {

            studentDetails.put("branch", branch);
            studentDetails.put("backlog", backlog);

            if (semester == 1) {
                studentDetails.put("id", generateID());
            } else {
                studentDetails.put("id", enrollmentIdView.getText().toString());
            }

            istask2completed = true;

            SharedPreferences sharedPreferences = getSharedPreferences("com.example.semesterregistration", Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("task2", istask2completed).apply();

            Intent intent = new Intent(this, FeesStatus.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.onEmptyFieldMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.academic_info);

        semesters = findViewById(R.id.semesterSelector);
        enrollmentIdView  = findViewById(R.id.enrollmentIdView);
        cseBox = findViewById(R.id.cseCheckBox);
        eceBox = findViewById(R.id.eceCheckBox);
        backlogSwitch = findViewById(R.id.backlogSelector);

        final ArrayList<String> sems = new ArrayList<>(asList("Select semester","1","2","3","4"));
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,sems);
        semesters.setAdapter(arrayAdapter);

        semesters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (position == 1) {
                        enrollmentIdView.setText("");
                        enrollmentIdView.setEnabled(false);
                        backlogSwitch.setEnabled(false);
                    } else {
                        enrollmentIdView.setEnabled(true);
                        backlogSwitch.setEnabled(true);
                    }
                    semester = Integer.parseInt(sems.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cseBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    eceBox.setChecked(false);
                    branch = "CSE";
                }
            }
        });

        eceBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cseBox.setChecked(false);
                    branch = "ECE";
                }
            }
        });

        backlogSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    backlog = backlogSwitch.getTextOn().toString();
                } else {
                    backlog = backlogSwitch.getTextOff().toString();
                }
            }
        });
    }
}
