package com.example.fit5046_assignment3.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.fit5046_assignment3.Dao.RecordDao;
import com.example.fit5046_assignment3.Database.RecordDatabase;
import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.authentication.Login;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class RecordRepository {
    private RecordDao recordDao;
    private LiveData<List<Record>> allRecord;
    public RecordRepository(Application application){
        RecordDatabase db = RecordDatabase.getInstance(application);
        recordDao =db.recordDao();
        allRecord= recordDao.getAllRecord();

    }

    public LiveData<List<Record>> getAllRecord() {
        return allRecord;
    }

    public List<Record> findRecordByEmail(final String email){
        return recordDao.findRecordByEmail(email);
    }

    public Record findRecordByEmailAndDate(final String email, final long date){
        return recordDao.findRecordByEmailAndDate(email, date);
    }

    public CompletableFuture<List<Record>> findRecordBetweenDate(final Date start, final Date end){
        return CompletableFuture.supplyAsync(new Supplier<List<Record>>() {
            @Override
            public List<Record> get() {
                return recordDao.findRecordBetweenDate(start, end);
            }
        }, RecordDatabase.databaseWriteExecutor);
    }



    public boolean insertRecord(final Record record) throws ExecutionException, InterruptedException {
        Boolean flag = true;
        if (checkDate(record.getDate())){
            RecordDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    recordDao.insertRecord(record);
                    try {
                        List<Record> records = test(record);
                        for (Record r : records){

                            recordDao.insertRecord(r);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        else {
            flag = false;
        }

        return flag;
    }

    public void deleteRecord(final Record record){
        RecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                recordDao.deleteRecord(record);
            }
        });
    }

    public void updateRecord(final Record record){
        RecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                recordDao.updateRecord(record);
            }
        });
    }

    public void deleteAllRecord(){
        RecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                recordDao.deleteAllRecord();
            }
        });
    }

    public List<Record> getAllRecordList() throws ExecutionException, InterruptedException {
        return new GetUsersAsyncTask(recordDao).execute().get();
    }

    public HashMap<String,Integer> getPainAndFrequency() throws ExecutionException, InterruptedException {
        List<Record> allRecords = getAllRecordList();
        HashMap<String,Integer> freq = new HashMap<>();
        if (allRecords.size() != 0){
            for (Record record : allRecords){
                String painLocation = record.getPainLocation();
                if (freq.containsKey(painLocation)){
                    int currentCount = freq.get(painLocation);
                    freq.remove(painLocation);
                    freq.put(painLocation,currentCount+1);
                }
                else{
                    freq.put(painLocation,1);
                }
            }
        }
        return freq;
    }

    // This is to avoid error"cannot access database in main thread since it may cause long block for UI"
    // Doing this because in the Recycler Review Adapter, it requires List type instead of liveData
    private class GetUsersAsyncTask extends AsyncTask<Void, Void, List<Record>>
    {
        private final RecordDao recordDao;

        @Override
        protected List<Record> doInBackground(Void... url) {
            return recordDao.getAllRecordList();
        }

        public GetUsersAsyncTask(RecordDao recordDao) {
            this.recordDao = recordDao;
        }
    }

    /**
     *
     * @param date
     * @return return true if current date has not been inserted a record, otherwise return false
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Boolean checkDate(Date date) throws ExecutionException, InterruptedException {
        Boolean flag = true;
        List<Record> allRecords = getAllRecordList();
        for (Record record : allRecords){
            System.out.println(record.getDate());
            if (record.getDate().compareTo(date) == 0){

                flag = false;
            }
        }
        return flag;
    }

    public Record getCurRecord() throws ExecutionException, InterruptedException {
        List<Record> allRecord = getAllRecordList();
        Record record = null;
        for (Record r : allRecord){
            if (record == null){
                record = r;
            }
            else if (r.getDate().compareTo(record.getDate()) > 0) {
                record = r;
            }
        }
        return record;
    }

    public List<Record> getRecordBetweenDate(Date start, Date end) throws ExecutionException, InterruptedException {
        List<Record> allRecord = getAllRecordList();
        List<Record> result = new ArrayList<>();


        if (allRecord.size() != 0){
            for ( Record r : allRecord){
                if (r.getDate().before(end) && r.getDate().after(start)){
                    result.add(r);
                }
            }
        }

        return result;
    }

    public List<Record> test(Record record) throws ExecutionException, InterruptedException {

        Date date = record.getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,-11);
        Date date1 = c.getTime();

        double temp = record.getTemperature();
        double humidity = record.getHumidity();
        double pressure = record.getPressure();
        long stepac = record.getStepActual();
        long stepgoal = record.getStepGoal();
        String Painloc1 = "back";
        String Painloc2 = "neck";
        String Painloc3 = "twist";
        List<Record> list= new ArrayList<Record>();
        Record r1 = new Record(date1, record.getUserEmail(), record.getPainLevel(), record.getMoodLevel(), Painloc1, stepgoal,stepac, record.getTimeReminder(),
                temp, humidity, pressure);

        list.add(r1);

        for (int j = 1; j < 10; j ++){
            c.add(Calendar.DATE, -j);
            Date newDate = c.getTime();
            temp += Math.random() * 20;
            humidity += Math.random() * 20;
            pressure += Math.random() * 20;
            stepac += Math.random() * 2000;
            stepgoal += Math.random() * 2000;
            Record r = new Record(newDate, record.getUserEmail(), record.getPainLevel(), record.getMoodLevel(), record.getPainLocation(), stepgoal,stepac, record.getTimeReminder(),
                    temp, humidity, pressure);
            list.add(r);
        }
        return list;

    }

}
