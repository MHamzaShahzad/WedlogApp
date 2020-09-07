package com.example.wedlogapp.activities.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ProfileViewModel extends ViewModel {

    private static final String TAG = ProfileViewModel.class.getName();
    private MutableLiveData<String> mData;

    public ProfileViewModel() {
        mData = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mData;
    }

}