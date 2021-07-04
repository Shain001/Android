package com.example.fit5046_assignment3.fragment;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.R;
import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.databinding.DataEntryConfirmBinding;
import com.example.fit5046_assignment3.databinding.NavDataEntryBinding;

import com.example.fit5046_assignment3.util.AlertReciever;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/*
Version 1.0: Validation needed to be added;
             UI need to be updated;
 */

public class DataEntryFragment extends Fragment {
    private NavDataEntryBinding binding;
    private FirebaseAuth fAuth;
    private TimePicker timePicker;
    private RecordViewModel recordViewModel;
    private SharedViewModel sharedViewModel;
    private Record record;
    private int painLevel = 0;
    private int moodLevel = 0;
    private long stepGoal;
    private long stepActual;

    public DataEntryFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = NavDataEntryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        fAuth = FirebaseAuth.getInstance();
        timePicker = binding.timepicker;
        timePicker.setHour(0);
        timePicker.setMinute(0);
        timePicker.setIs24HourView(true);

        recordViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(RecordViewModel.class);

        sharedViewModel= new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        // Set Spinner Selection List
        List<String> spinnerList = new ArrayList<String>();
        spinnerList.add("back");
        spinnerList.add("neck");
        spinnerList.add("head");
        spinnerList.add("knees");
        spinnerList.add("hips");
        spinnerList.add("abdomen");
        spinnerList.add("elbows");
        spinnerList.add("shoulders");
        spinnerList.add("shins");
        spinnerList.add("jaw");
        spinnerList.add("facial");

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        binding.painLocation.setAdapter(spinnerAdapter);

        binding.edit.setVisibility(View.INVISIBLE);

        // Listener for save button
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // unenable the input
                binding.stepActual.setEnabled(false);
                binding.stepGoal.setEnabled(false);
                binding.painLevel.setEnabled(false);
                binding.moodLevel.setEnabled(false);
                binding.timepicker.setEnabled(false);
                binding.painLocation.setEnabled(false);
                binding.saveBtn.setEnabled(false);



                // read input
                Date currentDateWithoutTime = getDateWithoutTime();


                painLevel = (int) binding.painLevel.getValue();

                moodLevel = (int) binding.moodLevel.getValue();


                String painLocation = binding.painLocation.getSelectedItem().toString();
                if (binding.stepGoal.getText().toString().equals("")){
                    stepGoal = 10000;
                }
                else {
                    stepGoal = Integer.parseInt(binding.stepGoal.getText().toString());

                }





                if (binding.stepActual.getText().toString().equals("")){
                    binding.stepActual.setError("Please Enter the Step Has Taken");
                }
                else {
                    stepActual = Integer.parseInt(binding.stepActual.getText().toString());

                }
                // build date object for reminder
                int hour = binding.timepicker.getHour();
                int minute = binding.timepicker.getMinute();
                minute -= 2;
                Log.i("timepicker", "hour: " + hour);
                Log.i("timepicker", "min: " + minute);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hour);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.SECOND, 0);
                // Notification Set
                startAlarm(c);


                // convert current datewithout time to string and concat with reminder time and hour
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                String strDate = dateFormat.format(currentDateWithoutTime);
                String timeReminderStr = strDate + " " + hour +":" + minute;

                // get user email since we use it as part of primary key
                String email = fAuth.getCurrentUser().getEmail();
                //                // get current date timestamp without time
//              long date = new Date().getTime();

                        // convert concated string time to date

                Date timeReminder= null;
                try {
                    timeReminder = new SimpleDateFormat("yyyy-mm-dd HH:MM").parse(timeReminderStr);

                    //long timeReminderTimeStamp = timeReminder.getTime();
                    // new the record entity object
                    double temperature = Double.parseDouble(sharedViewModel.getTemperature());
                    double humidity = Double.parseDouble(sharedViewModel.getHumidity());
                    double pressure = Double.parseDouble(sharedViewModel.getPressure());

                    System.out.println(currentDateWithoutTime.toString());
                    record = new Record(currentDateWithoutTime, email, painLevel, moodLevel, painLocation, stepGoal, stepActual, timeReminder,
                            temperature,humidity,pressure);
                    // call the dialog confirm dialog
                    DataEntryConfirmation confirm = new DataEntryConfirmation(record,recordViewModel,view,
                            sharedViewModel,binding.saveBtn, binding.edit,binding.painLevel,
                            binding.moodLevel,binding.stepGoal,binding.stepActual,binding.painLocation, binding.timepicker);
                    binding.edit.setVisibility(View.VISIBLE);
                    if (!binding.stepActual.getText().toString().equals("")){
                        confirm.show(getParentFragmentManager(),"confirmation");

                    }



//                    RecordFragment recordFragment= new RecordFragment();
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.nav_data_entry_fragment, recordFragment, "record_fragment")
//                            .addToBackStack(null)
//                            .commit();
                    //recordViewModel.insertRecord(record);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enable input
                binding.painLevel.setEnabled(true);
                binding.moodLevel.setEnabled(true);
                binding.timepicker.setEnabled(true);
                binding.painLocation.setEnabled(true);
                binding.saveBtn.setEnabled(true);
                binding.stepActual.setEnabled(true);
                binding.stepGoal.setEnabled(true);
                binding.edit.setVisibility(View.INVISIBLE);

                // read in input
                painLevel = (int) binding.painLevel.getValue();
                moodLevel = (int) binding.moodLevel.getValue();
                if (binding.stepGoal.getText().toString().equals("")){
                    stepGoal = 10000;
                }
                else {
                    stepGoal = Integer.parseInt(binding.stepGoal.getText().toString());

                }

                if (binding.stepActual.getText().toString().equals("")){
                    binding.stepActual.setError("Please Enter the Step Has Taken");
                }
                else {
                    stepActual = Integer.parseInt(binding.stepActual.getText().toString());
                }

                String painLocation = binding.painLocation.getSelectedItem().toString();
//                long stepGoal = Integer.parseInt(binding.stepGoal.getText().toString());
//                long stepActual = Integer.parseInt(binding.stepActual.getText().toString());
                // build date object for reminder
                int hour = binding.timepicker.getHour();
                int minute = binding.timepicker.getMinute();

                record.setMoodLevel(moodLevel);
                record.setPainLevel(painLevel);
                record.setStepActual(stepActual);
                record.setPainLocation(painLocation);
                record.setStepGoal(stepGoal);

//                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//                String strDate = dateFormat.format(record.getDate());
//                String timeReminderStr = strDate + " " + hour +":" + minute;
//                try {
//                    Date timeReminder = new SimpleDateFormat("yyyy-mm-dd HH:MM").parse(timeReminderStr);
//                    record.setTimeReminder(timeReminder);
//                    recordViewModel.updateRecord(record);
//                    Toast.makeText(getContext(),"update success", Toast.LENGTH_LONG).show();
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

            }
        });

        return view;
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

    public void startAlarm(Calendar calendar){
        AlarmManager alarm  = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReciever.class);
//        intent.putExtra("notificationId", 1);
//        intent.putExtra("todo", "Time to Care your Health");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarm.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }


}
