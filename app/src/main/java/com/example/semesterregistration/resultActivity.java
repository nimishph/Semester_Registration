package com.example.semesterregistration;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.semesterregistration.StudentActivity.istask1completed;
import static com.example.semesterregistration.StudentActivity.istask2completed;
import static com.example.semesterregistration.StudentActivity.istask3completed;
import static com.example.semesterregistration.StudentActivity.istask4completed;

public class resultActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Toast.makeText(this,R.string.onBackPressMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ImageView resultImageView = findViewById(R.id.resultImageView);
        TextView registrationTextView = findViewById(R.id.registrationTextView);

        if (!istask3completed && !istask4completed) {
            resultImageView.setImageResource(R.drawable.payment);
            registrationTextView.setText(R.string.onUnsuccessfulRegistration);
        } else {
            resultImageView.setImageResource(R.drawable.register);
            registrationTextView.setText(R.string.onSuccessfulRegistration);
        }
        Log.i("task1", istask1completed.toString());
        Log.i("task2", istask2completed.toString());
        Log.i("task3", istask3completed.toString());
        Log.i("task4", istask4completed.toString());
        final TextView messageTextView = findViewById(R.id.messageTextView);

        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String message = "Redirecting to home page in " + millisUntilFinished / 1000 + " s ...";
                messageTextView.setText(message);
            }

            @Override
            public void onFinish() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(resultActivity.this);
                alertDialog
                        .setTitle("Please rate the app")
                        .setView(R.layout.activity_rating)
                        .setPositiveButton("Done", (dialog, which) -> {
                            Toast.makeText(resultActivity.this, "Thank you for rating the app!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(resultActivity.this, StudentActivity.class);
                            startActivity(intent);
                        })
                        .setNegativeButton("No thanks", (dialog, which) -> {
                            Intent intent = new Intent(resultActivity.this, StudentActivity.class);
                            startActivity(intent);
                        }).show();
            }
        }.start();
    }
}
