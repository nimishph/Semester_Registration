package com.example.semesterregistration;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.util.TypedValue;
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
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class PaymentStatus extends Fragment {

    private int semester = 0;

    private LinearLayout semButtonLayout;
    private LinearLayout branchButtonLayout;
    private ConstraintLayout infoLayout;
    private ListView listView;
    private TextView errorMessage;

    public PaymentStatus() {

    }

    static PaymentStatus newInstance() {
        Bundle args = new Bundle();
        PaymentStatus fragment = new PaymentStatus();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.payment_status,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        semButtonLayout = view.findViewById(R.id.semButtonLayout1);
        branchButtonLayout = view.findViewById(R.id.branchButtonLayout1);
        infoLayout = view.findViewById(R.id.paymentInfoListLayout);

        listView = view.findViewById(R.id.listView);

        errorMessage = view.findViewById(R.id.errorTV1);

        view.findViewById(R.id.semButton1a).setOnClickListener(this::onSemButtonClick);
        view.findViewById(R.id.semButton2a).setOnClickListener(this::onSemButtonClick);
        view.findViewById(R.id.semButton3a).setOnClickListener(this::onSemButtonClick);
        view.findViewById(R.id.semButton4a).setOnClickListener(this::onSemButtonClick);

        view.findViewById(R.id.cseButtonA).setOnClickListener(this::onBranchButtonClick);
        view.findViewById(R.id.eceButtonA).setOnClickListener(this::onBranchButtonClick);
    }

    private void onSemButtonClick(View view) {

        switch (view.getId()) {

            case R.id.semButton1a:
                semester = 1;
                break;
            case R.id.semButton2a:
                semester = 2;
                break;
            case R.id.semButton3a:
                semester = 3;
                break;
            case R.id.semButton4a:
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
        if (view.getId() == R.id.cseButtonA)
            branch = "CSE";
        else
            branch = "ECE";

        String message = "Error ! Couldn't fetch the data";

        errorMessage.setText(message);

        ArrayList<Student> studentArrayList = new ArrayList<>();
        SQLiteDatabase database = null;
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> feeStatus = new ArrayList<>();

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
                int feeStatusIndex = c.getColumnIndex("feestatus");

                c.moveToFirst();
                while (!c.isAfterLast()) {

                    id.add(c.getString(idIndex));
                    name.add(c.getString(nameIndex));
                    feeStatus.add(c.getString(feeStatusIndex));
                    c.moveToNext();
                }
                c.close();

                for (int i = 0; i < id.size(); i++) {
                    studentArrayList.add(new Student(id.get(i), name.get(i), "", feeStatus.get(i), new ArrayList<>()));
                }

                listView.setAdapter(new ListAdapter(Objects.requireNonNull(this.getContext()), studentArrayList, "paymentTab"));
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage.setVisibility(View.VISIBLE);
            } finally {
                database.close();
            }
        }

        branchButtonLayout.setVisibility(View.GONE);
        infoLayout.setVisibility(View.VISIBLE);

    }
}

