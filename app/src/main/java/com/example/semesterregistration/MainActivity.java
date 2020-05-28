package com.example.semesterregistration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.semesterregistration.StudentActivity.registeredStudents;
import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

/*

PASSWORD POLICY:

    At least 8 chars

    Contains at least one digit

    Contains at least one lower alpha char and one upper alpha char

    Contains at least one char within a set of special chars (@#%$^ etc.)

    Does not contain space, tab, etc.
*/

    static String currentUserMail;

    AutoCompleteTextView emailID;
    EditText password;
    Button button;
    Spinner spinner;
    ProgressBar loading;

    SharedPreferences sharedPreferences;
    private final String TAG = "Main Activity";
    String email, pwd, userType, currentUserPassword, currentUserType;
    Boolean isLoggedIn;
    ArrayList<String> emails = new ArrayList<>();
    ArrayList<String> passwords = new ArrayList<>();
    ArrayList<String> userTypes = new ArrayList<>();

    private void afterValidation() {
        Toast.makeText(MainActivity.this, "Welcome, to IIIT Nagpur's registration portal !", Toast.LENGTH_SHORT).show();
        if (userType.equals("Faculty")) {
            // open faculty intent

            Intent intent = new Intent(MainActivity.this, FacultyActivity.class);
            startActivity(intent);
        }

        if (userType.equals("Student")) {
            // open student intent
            try {
                registeredStudents = (HashMap<String, Object>) ObjectSerializer.deserialize(sharedPreferences.getString("registeredStudents", ObjectSerializer.serialize(new HashMap<String, Object>())));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(MainActivity.this, StudentActivity.class);
            startActivity(intent);
        }

    }

    private void incorrectInput() {
        Toast.makeText(this, "Incorrect Fields - Please check your login info and try LOGIN again.", Toast.LENGTH_SHORT).show();
        emailID.setText("");
        password.setText("");
        spinner.setSelection(0);
    }


    public void onClickButton(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        view.setEnabled(false);

        if (isLoggedIn) {
            afterValidation();
        } else {

            boolean incorrectInputFlag = false;

            userType = spinner.getSelectedItem().toString();

            if (emails.contains(email)) {
                if (!pwd.equals(passwords.get(emails.indexOf(email))) || !userType.equals(userTypes.get(emails.indexOf(email)))) {
                    incorrectInput();
                    incorrectInputFlag = true;
                }
            }

            if (!incorrectInputFlag) {
                currentUserMail = email;
                currentUserPassword = pwd;
                currentUserType = userType;

                emailID.setText("");
                password.setText("");
                spinner.setSelection(0);

                loading.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                // Actions to do after 02 seconds
                if (email.isEmpty() || pwd.isEmpty()) {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Empty fields not allowed !", Toast.LENGTH_SHORT).show();
                } else {
                    loading.setVisibility(View.GONE);
                    if ((email.matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$") && pwd.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"))) {

                        isLoggedIn = true;
                        if (!emails.contains(currentUserMail)) {
                            emails.add(currentUserMail);
                            passwords.add(currentUserPassword);
                            userTypes.add(currentUserType);
                        }

                        try {
                            sharedPreferences.edit().putString("userTypes", ObjectSerializer.serialize(userTypes)).apply();
                            sharedPreferences.edit().putString("emails", ObjectSerializer.serialize(emails)).apply();
                            sharedPreferences.edit().putString("passwords", ObjectSerializer.serialize(passwords)).apply();
                            sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply();
                            sharedPreferences.edit().putString("currentUserMail", currentUserMail).apply();
                            sharedPreferences.edit().putString("currentUserPassword", currentUserPassword).apply();
                            sharedPreferences.edit().putString("currentUserType", currentUserType).apply();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        afterValidation();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid email or password !", Toast.LENGTH_LONG).show();
                    }
                }
                }, 2000);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailID = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        button = findViewById(R.id.button);
        spinner = findViewById(R.id.spinner);
        loading = findViewById(R.id.loading);

        LinearLayout passwordPolicyCard = findViewById(R.id.passwordPolicy);
        TextView passwordPolicy = findViewById(R.id.passwordPolicyTV);

        passwordPolicyCard.setOnClickListener(v -> {
            if (passwordPolicy.getVisibility() == View.GONE) {
                passwordPolicy.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
            } else if (passwordPolicy.getVisibility() == View.VISIBLE) {
                passwordPolicy.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }
        });

        sharedPreferences = getSharedPreferences("com.example.semesterregistration", Context.MODE_PRIVATE);

        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        currentUserMail = sharedPreferences.getString("currentUserMail", "");
        currentUserPassword = sharedPreferences.getString("currentUserPassword", "");
        currentUserType = sharedPreferences.getString("currentUserType", "");

        Log.i("Logged In", isLoggedIn.toString());

        if (isLoggedIn) {
            email = currentUserMail;
            pwd = currentUserPassword;
            userType = currentUserType;

            button.callOnClick();
        } else {
            try {
                emails = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("emails", ObjectSerializer.serialize(new ArrayList<String>())));
                passwords = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("passwords", ObjectSerializer.serialize(new ArrayList<String>())));
                userTypes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("userTypes", ObjectSerializer.serialize(new ArrayList<String>())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        password.setOnKeyListener(this);
        spinner.setOnKeyListener(this);

        final ArrayList<String> userTypesList = new ArrayList<>(asList("Select the type of user ...", "Faculty", "Student"));
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userTypesList);
        spinner.setAdapter(arrayAdapter1);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, emails);

        emailID.setAdapter(arrayAdapter2);
        emailID.setThreshold(1);

        emailID.setOnItemClickListener((parent, view, position, id) -> {
            password.setText(passwords.get(emails.indexOf(emailID.getText().toString())));
            spinner.setSelection(userTypesList.indexOf(userTypes.get((emails.indexOf(emailID.getText().toString())))));

/*
            //Logs Start

            Log.i(TAG + " - Password", password.getText().toString());
            Log.i(TAG + " - UserType", userTypes.get(position));
            //Logs End
*/

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null && getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    email = emailID.getText().toString();
                    pwd = password.getText().toString();

                    if (!email.isEmpty() && !pwd.isEmpty()) {
                        button.setEnabled(true);
                        button.setBackgroundColor(Color.rgb(244, 67, 54));
                        button.setTextColor(Color.rgb(255, 255, 255));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null && getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }
}
