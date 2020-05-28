package com.example.semesterregistration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FacultyActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.log_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.semesterregistration", Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
            sharedPreferences.edit().putString("currentUserMail", "").apply();
            sharedPreferences.edit().putString("currentUserPassword", "").apply();
            sharedPreferences.edit().putString("currentUserType", "").apply();
            finishAffinity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        TabLayout tab_layout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(
                tab_layout,
                viewPager2,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Payment\nStatus");
                    } else if (position == 1) {
                        tab.setText("Registered\nCourses");
                    } else if (position == 2) {
                        tab.setText("Course\nWise");
                    }
                }
        ).attach();
    }

}
