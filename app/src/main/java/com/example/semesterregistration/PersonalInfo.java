package com.example.semesterregistration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.semesterregistration.StudentActivity.istask1completed;
import static com.example.semesterregistration.StudentActivity.studentDetails;

public class PersonalInfo extends AppCompatActivity implements View.OnKeyListener {

    EditText name, dob, mob, email;

    Calendar myCalendar;
    DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener date;

    private void updateDOB() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,R.string.onBackPressMessage, Toast.LENGTH_SHORT).show();
    }

    public void onNextClick(View view) {

        String Name = name.getText().toString();
        String DOB = dob.getText().toString();
        String MOB = mob.getText().toString();
        String Email = email.getText().toString();

        if (!Name.isEmpty() && !DOB.isEmpty() && !MOB.isEmpty() && !Email.isEmpty()) {

            studentDetails.put("name",Name);
            studentDetails.put("dob",DOB);
            studentDetails.put("mob",MOB);
            studentDetails.put("email",Email);

            istask1completed = true;
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.semesterregistration", Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("task1", istask1completed).apply();

            try {
                sharedPreferences.edit().putString("studentDetails",ObjectSerializer.serialize(studentDetails)).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, AcademicInfo.class);
            startActivity(intent);
        } else {
            Toast.makeText(this,R.string.onEmptyFieldMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);

        setTitle("Semester Registration Form");

        myCalendar = Calendar.getInstance();

        date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDOB();
        };

        datePickerDialog = new DatePickerDialog(PersonalInfo.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        myCalendar.add(Calendar.YEAR,-24);
        datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        myCalendar.add(Calendar.YEAR,7);
        datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());

        name = findViewById(R.id.nameTextView);
        dob = findViewById(R.id.dobTextView);
        mob = findViewById(R.id.mobTextView);
        email = findViewById(R.id.emailTextView);

        mob.setOnKeyListener(this);

        Intent intent = getIntent();
        String mail = intent.getStringExtra("email");

        email.setText(mail);
        email.setEnabled(false);

        dob.setOnClickListener(v -> datePickerDialog.show());
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null && getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

        return false;
    }
}