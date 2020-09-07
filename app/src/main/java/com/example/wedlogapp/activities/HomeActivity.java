package com.example.wedlogapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wedlogapp.R;
import com.example.wedlogapp.SessionManagement;
import com.example.wedlogapp.activities.ui.event_order.EventOrdersFragment;
import com.example.wedlogapp.activities.ui.hall_order.HallOrdersFragment;
import com.example.wedlogapp.activities.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getName();
    private Context mContext;

    private SessionManagement sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        sessionManagement = new SessionManagement(mContext);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_halls, R.id.navigation_event_management, R.id.navigation_transportation)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_bar_menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Intent intent = new Intent(mContext, OrdersOrBookingsActivity.class);
        switch (item.getItemId()) {
            case R.id.menu_profile:
                fragmentTransaction.replace(R.id.nav_host_fragment, new ProfileFragment()).addToBackStack("Profile").commit();
                break;
            case R.id.menu_orders:
                intent.putExtra("isOrders", true);
                startActivity(intent);
                break;
            case R.id.menu_bookings:
                intent.putExtra("isOrders", false);
                startActivity(intent);
                break;
            case R.id.menu_logout:
                sessionManagement.clearPreferences();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                break;
            default:
                Log.i(TAG, "onOptionsItemSelected: UNKNOWN_MENU_ITEM_CLICKED");
        }

        return super.onOptionsItemSelected(item);
    }
}