package com.example.fit5046_assignment3.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.Repository.RecordRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RecordViewModel extends AndroidViewModel {

    private RecordRepository rRepository;
    private LiveData<List<Record>> allRecord;



    public RecordViewModel(@NonNull Application application) {
        super(application);
        // What does this application mean? How is the process?
        rRepository = new RecordRepository(application);
        allRecord = rRepository.getAllRecord();
    }

    public LiveData<List<Record>> getAllRecord() {
        return allRecord;
    }

    public List<Record> getAllRecordList() throws ExecutionException, InterruptedException {return rRepository.getAllRecordList();}

    public Boolean insertRecord(Record record) throws ExecutionException, InterruptedException {
        return rRepository.insertRecord(record);
    }

    public void deleteRecord(Record record){
        rRepository.deleteRecord(record);
    }

    public void deleteAllRecord(){
        rRepository.deleteAllRecord();
    }

    public void updateRecord(Record record){
        rRepository.updateRecord(record);


    }

    public List<Record> findRecordByEmail(String email){
        return rRepository.findRecordByEmail(email);
    }

    public Record findRecordByEmailAndDate(String email, long date){
        return rRepository.findRecordByEmailAndDate(email, date);
    }

    // for report pain location
    public HashMap<String,Integer> getPainAndFrequency() throws ExecutionException, InterruptedException {
        return rRepository.getPainAndFrequency();
    }

    public Record getCurRecord() throws ExecutionException, InterruptedException {
        return rRepository.getCurRecord();
    }

    public List<Record> getRecordBetweenDate(Date start, Date end) throws ExecutionException, InterruptedException {
        return rRepository.getRecordBetweenDate(start,end);
    }

    public CompletableFuture<List<Record>>  findRecordBetweenDate(Date start, Date end){
        return rRepository.findRecordBetweenDate(start,end);
    }


}
