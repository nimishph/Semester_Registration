package com.example.semesterregistration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.semesterregistration.StudentActivity.istask3completed;
import static com.example.semesterregistration.StudentActivity.studentDetails;

public class FeesStatus extends AppCompatActivity {

    CheckBox paidBox, xtensionBox, unpaidBox;

    String feeStatus;

    @Override
    public void onBackPressed() {
        Toast.makeText(this,R.string.onBackPressMessage, Toast.LENGTH_SHORT).show();
    }

    public void onNextClick(View view) {

        if (unpaidBox.isChecked() || paidBox.isChecked() || xtensionBox.isChecked()) {
            if (!unpaidBox.isChecked()) {

                istask3completed = true;
                studentDetails.put("feeStatus", feeStatus);

                SharedPreferences sharedPreferences = getSharedPreferences("com.example.semesterregistration", Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("task3", istask3completed).apply();
                sharedPreferences.edit().putBoolean("feesPaid",true).apply();

                Intent intent = new Intent(this, CourseSelection.class);
                startActivity(intent);

            } else {
                Intent intent = new Intent(this, resultActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, R.string.onEmptyFieldMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fees_status);

        paidBox = findViewById(R.id.paidCheckBox);
        xtensionBox = findViewById(R.id.xtensionCheckBox);
        unpaidBox = findViewById(R.id.unpaidCheckBox);

        paidBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    feeStatus = "Paid";
                    xtensionBox.setChecked(false);
                    unpaidBox.setChecked(false);
                }
            }
        });

        xtensionBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    feeStatus = "Extension requested";
                    paidBox.setChecked(false);
                    unpaidBox.setChecked(false);
                }
            }
        });

        unpaidBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    feeStatus = "Unpaid";
                    paidBox.setChecked(false);
                    xtensionBox.setChecked(false);
                }
            }
        });
    }
}
