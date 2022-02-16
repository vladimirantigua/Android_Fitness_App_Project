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
public interface UserInfoDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TableUserInfo tableUserInfo);

    @Query("DELETE FROM user_info")
    void deleteAll();

    @Query("SELECT * FROM user_info")
    LiveData<List<TableUserInfo>> getTableUserInfo();

    @Update
    void update(TableUserInfo tableUserInfo);


    @Delete
    void delete(TableUserInfo tableUserInfo);


    // From Contact Room example -- Use for multiple users
//    @Query("SELECT * FROM contact_table WHERE contact_table.id == :id")
//    LiveData<Contact> get(int id);

}