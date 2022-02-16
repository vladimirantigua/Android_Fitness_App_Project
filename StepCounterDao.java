package com.example.fitnessapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StepCounterDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TableStepCounter tableStepCounter);

    @Query("DELETE FROM step_counter")
    void deleteAll();

    @Query("SELECT * FROM step_counter")
    LiveData<List<TableStepCounter>> getTableUserInfo();

    @Update
    void update(TableStepCounter tableStepCounter);


    @Delete
    void delete(TableStepCounter tableStepCounter);


    // From Contact Room example -- Use for multiple users
//    @Query("SELECT * FROM contact_table WHERE contact_table.id == :id")
//    LiveData<Contact> get(int id);

}