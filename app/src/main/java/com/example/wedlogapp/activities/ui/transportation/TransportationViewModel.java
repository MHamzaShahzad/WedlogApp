package com.example.wedlogapp.activities.ui.transportation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransportationViewModel extends ViewModel {

    private static final String TAG = TransportationViewModel.class.getName();
    private MutableLiveData<String> mData;

    public TransportationViewModel() {
        mData = new MutableLiveData<>();
    }

    public LiveData<String> getData() {
        return mData;
    }

}