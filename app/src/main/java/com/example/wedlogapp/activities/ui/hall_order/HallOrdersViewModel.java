package com.example.wedlogapp.activities.ui.hall_order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class HallOrdersViewModel extends ViewModel {

    private static final String TAG = HallOrdersViewModel.class.getName();
    private MutableLiveData<String> mData;

    public HallOrdersViewModel() {
        mData = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mData;
    }

}