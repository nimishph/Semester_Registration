package com.example.semesterregistration;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter extends ArrayAdapter<Student> {

    private ArrayList<String> extraInfo = new ArrayList<>();
    private String mTab = "";
    private Context mContext;

    ListAdapter(@NonNull Context context, ArrayList<Student> studentArrayList, String tab) {
        super(context, 0, studentArrayList);
        mTab = tab;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }


        Student currentStudent = getItem(position);
        assert currentStudent != null;

        HashMap currentStudentDetails = currentStudent.getStudentDetails();

        CoordinatorLayout coordinatorLayout = listItemView.findViewById(R.id.coordinatorLayout);
        TextView rollNo = listItemView.findViewById(R.id.rollNo);
        TextView name = listItemView.findViewById(R.id.name);
        Spinner extra_info =  listItemView.findViewById(R.id.extra_info);

        switch (mTab) {
            case "paymentTab":
                if (extraInfo.size() == 0) {
                    String paymentStatus = (String) currentStudentDetails.get("paymentStatus");
                    extraInfo.add(paymentStatus);
                    Log.i("payment status", extraInfo.get(0));
                }
                break;
            case "registeredCourses":
                ArrayList<String> courses = (ArrayList<String>) currentStudentDetails.get("courses");
                assert courses != null;
                if (extraInfo.size() < courses.size()) {
                    extraInfo.addAll(courses);
                    Log.i("courses", courses.toString());
                }
                break;
            case "courseWise":
                if (extraInfo.size() == 0) {
                    String email = (String) currentStudentDetails.get("email");
                    extraInfo.add(email);
                    Log.i("email", extraInfo.get(0));
                }
                break;

            default:
                break;
        }

        rollNo.setText(currentStudentDetails.get("rollNo").toString());
        name.setText(currentStudentDetails.get("name").toString());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item, extraInfo);

        extra_info.setAdapter(arrayAdapter);

        name.setOnClickListener(v -> {
            if(name.getText() != null || !name.getText().toString().equals("")) {
                Toast.makeText(mContext, name.getText().toString(), Toast.LENGTH_SHORT).show();
                Snackbar.make(coordinatorLayout, name.getText().toString(), Snackbar.LENGTH_SHORT).show();
            }
        });

        return listItemView;
    }
}
