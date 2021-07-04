package com.example.fit5046_assignment3.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.R;
import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.databinding.EditRecordBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditFragment extends Fragment {

    private EditRecordBinding binding;
    private KeyListener keyListener;
    private RecordViewModel recordViewModel;
    private SharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EditRecordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        recordViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(RecordViewModel.class);
        Record record = sharedViewModel.getEditRecord();


        // Set default value
        binding.moodLevel.setValue(record.getMoodLevel());
        binding.painLevel.setValue(record.getPainLevel());

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

        int recordedPosition = spinnerList.indexOf(record.getPainLocation());
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        binding.painLocation.setAdapter(spinnerAdapter);
        binding.painLocation.setSelection(recordedPosition);
        binding.stepGoal.setText(Long.toString(record.getStepGoal()));
        binding.stepActual.setText(Long.toString(record.getStepActual()));
        binding.timepicker.setMinute(record.getTimeReminder().getMinutes());
        binding.timepicker.setHour(record.getTimeReminder().getHours());


        // Make input text uneditable before click edit button, also include the save button
        binding.stepActual.setEnabled(false);
        binding.stepGoal.setEnabled(false);
        binding.painLevel.setEnabled(false);
        binding.moodLevel.setEnabled(false);
        binding.timepicker.setEnabled(false);
        binding.painLocation.setEnabled(false);
        binding.saveBtn2.setEnabled(false);

        // edit button listener
        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enable edit
                binding.painLevel.setEnabled(true);
                binding.moodLevel.setEnabled(true);
                binding.timepicker.setEnabled(true);
                binding.painLocation.setEnabled(true);
                binding.saveBtn2.setEnabled(true);
                binding.stepActual.setEnabled(true);
                binding.stepGoal.setEnabled(true);
            }
        });

        binding.saveBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // disable edit button
                binding.editBtn.setEnabled(false);

                // read in input
                int painLevel = (int) binding.painLevel.getValue();
                int moodLevel = (int) binding.moodLevel.getValue();
                String painLocation = binding.painLocation.getSelectedItem().toString();
                long stepGoal = Integer.parseInt(binding.stepGoal.getText().toString());
                long stepActual = Integer.parseInt(binding.stepActual.getText().toString());
                // build date object for reminder
                int hour = binding.timepicker.getHour();
                int minute = binding.timepicker.getMinute();

                record.setMoodLevel(moodLevel);
                record.setPainLevel(painLevel);
                record.setStepActual(stepActual);
                record.setPainLocation(painLocation);
                record.setStepGoal(stepGoal);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                String strDate = dateFormat.format(record.getDate());
                String timeReminderStr = strDate + " " + hour +":" + minute;
                try {
                    Date timeReminder = new SimpleDateFormat("yyyy-mm-dd HH:MM").parse(timeReminderStr);
                    record.setTimeReminder(timeReminder);
                    recordViewModel.updateRecord(record);
                    Toast.makeText(getContext(),"update success", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(view).navigate(R.id.nav_record_fragment);


                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });



        return view;
    }


}
