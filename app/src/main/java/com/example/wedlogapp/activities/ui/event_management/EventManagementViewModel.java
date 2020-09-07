package com.example.wedlogapp.activities.ui.event_management;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventManagementViewModel extends ViewModel {

    private static final String TAG = EventManagementViewModel.class.getName();
    private MutableLiveData<String> mData;

    public EventManagementViewModel() {
        mData = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mData;
    }

}