package com.example.wedlogapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.wedlogapp.activities.ui.event_order.EventOrdersFragment;
import com.example.wedlogapp.activities.ui.hall_order.HallOrdersFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int NumberOfTabs;
    boolean isOrders;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int NumberOfTabs, boolean isOrders) {
        super(fm);
        this.NumberOfTabs = NumberOfTabs;
        this.isOrders = isOrders;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (isOrders)
                    return new HallOrdersFragment("accounts/orders/halls");
                else
                    return new HallOrdersFragment("accounts/halls/clients/orders");
            case 1:
                if (isOrders)
                    return new EventOrdersFragment("accounts/orders/events");
                else
                    return new EventOrdersFragment("accounts/events/clients/orders");
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return NumberOfTabs;
    }

}
