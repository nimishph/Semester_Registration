package com.example.semesterregistration;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

class Student {

    private String mRollNo;
    private String mName;
    private String mEmail;
    private String mPaymentStatus;
    private ArrayList<String> mCoursesRegistered;

    private HashMap<String, Object> studentDetails = new HashMap<>();

    Student(String rollNo, String name, String email, String paymentStatus, ArrayList<String> coursesRegistered) {

        mRollNo = rollNo;
        mName = name;
        mEmail = email;
        mPaymentStatus = paymentStatus;
        mCoursesRegistered = coursesRegistered;

        studentDetails.put("name",mName);
        studentDetails.put("rollNo",mRollNo);
        studentDetails.put("email", mEmail);
        studentDetails.put("paymentStatus",mPaymentStatus);
        studentDetails.put("courses", mCoursesRegistered);
    }

    HashMap getStudentDetails() {
        return studentDetails;
    }

}