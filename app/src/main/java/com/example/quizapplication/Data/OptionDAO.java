package com.example.quizapplication.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OptionDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Option option);

    @Delete
    void delete(Option option);

    @Query("DELETE FROM options_table")
    void deleteAll();

    @Query("Select * FROM options_table ORDER BY name ASC")
    LiveData<List<Option>> getOptions();
}
