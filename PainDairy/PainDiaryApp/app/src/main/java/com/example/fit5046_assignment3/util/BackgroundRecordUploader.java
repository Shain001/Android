package com.example.fit5046_assignment3.util;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.ViewModel.SharedViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackgroundRecordUploader extends Worker{
    public static List<Record> allRecords;
    private FirebaseAuth fAuth=FirebaseAuth.getInstance();;
    private FirebaseFirestore dbFire=FirebaseFirestore.getInstance();;
    private Date curDate;


    public BackgroundRecordUploader(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("aaaa", "start uploading record");
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strCurDate = sdf.format(c.getTime());

        curDate = getDateWithoutTime();
        Map<String, String> dateToPush = new HashMap<>();

        for (Record r : allRecords){
            Date dbDate = r.getDate();
            if (dbDate.compareTo(curDate) == 0){
                dateToPush.put("pain_location",r.getPainLocation() );
                dateToPush.put("pain_Level",Integer.toString(r.getPainLevel()) );
                dateToPush.put("mood_level",Integer.toString(r.getMoodLevel()) );
                dateToPush.put("temperature",Double.toString(r.getTemperature()) );
                dateToPush.put("pressure",Double.toString(r.getPressure()) );
                dateToPush.put("humidity",Double.toString(r.getHumidity())  );
                dateToPush.put("step_actual",Long.toString(r.getStepActual()));
                dateToPush.put("step_goal",Long.toString(r.getStepGoal()));
                dbFire.collection(fAuth.getCurrentUser().getEmail()).document(strCurDate).set(dateToPush);
                Log.d("aaaa", "end uploading record");
                Log.d("aaaa", dateToPush.get("pain_location"));
                break;
            }

        }

        return Result.success();
    }

    public Date getDateWithoutTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date dateWithoutTime = cal.getTime();
        Log.i("TAG", "getDateWithoutTime: " + dateWithoutTime);
        return dateWithoutTime;
    }


}
