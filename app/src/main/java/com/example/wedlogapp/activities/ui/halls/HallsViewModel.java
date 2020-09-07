package com.example.wedlogapp.activities.ui.halls;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class HallsViewModel extends ViewModel {

    private static final String TAG = HallsViewModel.class.getName();
    private MutableLiveData<String> mData;

    public HallsViewModel() {
        mData = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mData;
    }

}