package com.example.wedlogapp.activities.ui.event_order;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventOrdersViewModel extends ViewModel {

    private static final String TAG = EventOrdersViewModel.class.getName();
    private MutableLiveData<String> mData;

    public EventOrdersViewModel() {
        mData = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mData;
    }

}