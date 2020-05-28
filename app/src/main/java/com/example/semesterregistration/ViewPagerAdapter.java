package com.example.semesterregistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return PaymentStatus.newInstance();
        else if (position == 1)
            return RegisteredCourses.newInstance();
        else
            return CourseWise.newInstance();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}



