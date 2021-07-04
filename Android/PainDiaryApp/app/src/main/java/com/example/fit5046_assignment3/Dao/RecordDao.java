package com.example.fit5046_assignment3.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.fit5046_assignment3.Entity.DateConverter;
import com.example.fit5046_assignment3.Entity.Record;

import java.util.Date;
import java.util.List;

@Dao
public interface RecordDao {
    @Query("SELECT * FROM record ORDER BY date ASC")
    LiveData<List<Record>> getAllRecord();

    @Query("SELECT * FROM record ORDER BY date ASC")
    List<Record> getAllRecordList();

    @Query("SELECT * FROM record WHERE userEmail = :email LIMIT 1")
    List<Record> findRecordByEmail(String email);
    @Query("SELECT * FROM record WHERE userEmail = :email and date = :date")
    Record findRecordByEmailAndDate(String email, long date);
    @Insert
    void insertRecord(Record customer);
    @Delete
    void deleteRecord(Record customer);
    @Update
    void updateRecord(Record customer);
    @Query("DELETE FROM record")
    void deleteAllRecord();
    @Query("SELECT * FROM record WHERE date BETWEEN :start AND :end")
    @TypeConverters(DateConverter.class)
    List<Record> findRecordBetweenDate(Date start, Date end);
}
