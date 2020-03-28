package com.example.semesterregistration;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        final TextView messageTextView = findViewById(R.id.messageTextView);

        new CountDownTimer(11000, 1000) {
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
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(resultActivity.this,"Thank you for rating the app!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(resultActivity.this, StudentActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(resultActivity.this, StudentActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        }.start();
    }
}
