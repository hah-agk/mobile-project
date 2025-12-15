package com.example.currencyconverter.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
}
