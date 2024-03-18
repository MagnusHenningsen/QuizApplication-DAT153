package com.example.quizapplication.Data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

class OptionRepository {

    private OptionDAO OptionDAO;
    private LiveData<List<Option>> AllOptions;

    OptionRepository(Application application) {
        OptionRoomDatabase db = OptionRoomDatabase.getDatabase(application);
        OptionDAO = db.optionDAO();
        AllOptions = OptionDAO.getOptions();
    }
    LiveData<List<Option>> GetAllOptions() {
        return AllOptions;
    }
    public CompletableFuture<Void> insert(Option option) {
        return CompletableFuture.runAsync(() -> {
            OptionDAO.insert(option);
        }, OptionRoomDatabase.databaseWriteExecutor);
    }

    void delete(Option option)  {
        OptionRoomDatabase.databaseWriteExecutor.execute(() -> {
            OptionDAO.delete(option);
        });
    }
}