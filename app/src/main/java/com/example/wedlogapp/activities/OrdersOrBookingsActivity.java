package com.example.wedlogapp.activities;

import android.content.Context;
import android.os.Bundle;

import com.example.wedlogapp.R;
import com.example.wedlogapp.adapters.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OrdersOrBookingsActivity extends AppCompatActivity {

    private static final String TAG = OrdersOrBookingsActivity.class.getName();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_or_bookings);
        mContext = this;

        final TabLayout tabLayout = findViewById(R.id.tabs);
        final ViewPager viewPager = findViewById(R.id.view_pager);

        List<String> tabsList = new ArrayList<>();
        if (getIntent().getBooleanExtra("isOrders", true)) {
            tabsList.add("Hall Orders");
            tabsList.add("Event Orders");
        }else {
            tabsList.add("Hall Bookings");
            tabsList.add("Event Bookings");
        }

        for (int i = 0; i < tabsList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabsList.get(i)));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryDark));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Toast.makeText(mContext, tab.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), getIntent().getBooleanExtra("isOrders", true)));
        //viewPager.setPageTransformer(true, new BackgroundToForegroundTransformer());

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}