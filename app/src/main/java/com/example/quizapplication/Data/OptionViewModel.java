package com.example.quizapplication.Data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class OptionViewModel extends AndroidViewModel {

    private OptionRepository oRepository;
    private final LiveData<List<Option>> allOptions;
    public OptionViewModel(@NonNull Application application) {
        super(application);
        oRepository = new OptionRepository(application);
        allOptions = oRepository.GetAllOptions();
    }

    public LiveData<List<Option>> getAllOptions() { return allOptions; }
    public void insert(Option option) { oRepository.insert((option));}
    public void delete(Option option) { oRepository.delete(option);}
}
