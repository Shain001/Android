package com.example.fit5046_assignment3.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.R;
import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.databinding.DataEntryConfirmBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class DataEntryConfirmation extends DialogFragment {
    private Record record;
    private RecordViewModel recordViewModel;
    private View entryView;
    private SharedViewModel sharedViewModel;
    private Button saveBtn;
    private Button editBtn;
    private Slider painLevel;
    private Slider moodLevel;
    private EditText stepGoal;
    private EditText stepActual;
    private Spinner location;
    private TimePicker reminder;

    public void setRecord(Record record) {
        this.record = record;
    }

    public DataEntryConfirmation(Record record, RecordViewModel recordViewModel, View entryView, SharedViewModel sharedViewModel, Button saveBtn, Button editBtn, Slider painLevel, Slider moodLevel, EditText stepGoal, EditText stepActual, Spinner location, TimePicker reminder) {
        this.record = record;
        this.recordViewModel = recordViewModel;
        this.entryView = entryView;
        this.sharedViewModel = sharedViewModel;
        this.saveBtn = saveBtn;
        this.editBtn = editBtn;
        this.painLevel = painLevel;
        this.moodLevel = moodLevel;
        this.stepGoal = stepGoal;
        this.stepActual = stepActual;
        this.location = location;
        this.reminder = reminder;
    }

    public void setRecordViewModel(RecordViewModel recordViewModel, View view, SharedViewModel sharedViewModel) {
        this.recordViewModel = recordViewModel;
        this.entryView = view;
        this.sharedViewModel = sharedViewModel;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new AlertDialog.Builder(getContext()).setTitle("Confirmation").setMessage("Confirm?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (recordViewModel.insertRecord(record)){

                                sharedViewModel.setRecord(record);

                                Toast.makeText(getContext(),"Add Success!",Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(entryView).navigate(R.id.nav_record_fragment);
//                                RecordFragment recordFragment= new RecordFragment();
                                //System.out.println(getActivity().getSupportFragmentManager());

                                // jump to record page, manully jumping since here cannot use fragment manager
//                                NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
//                                navigationView.getMenu().performIdentifierAction(R.id.nav_record_fragment,0);



    //                        getActivity().getSupportFragmentManager().beginTransaction()
    ////                                .replace(R.id.nav_data_entry_fragment, recordFragment, "record_fragment")
    ////                                .addToBackStack(null)
    ////                                .commit();
                            }
                            else{
                                DataEntryOneTimeWarning warning = new DataEntryOneTimeWarning(entryView);
                                warning.show(getParentFragmentManager(),"warning");
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        editBtn.setVisibility(View.VISIBLE);
                    }
                }).create();


    }



    public void setRecordViewModel(RecordViewModel recordViewModel) {
        this.recordViewModel = recordViewModel;
    }

    public void setEntryView(View entryView) {
        this.entryView = entryView;
    }

    public void setSharedViewModel(SharedViewModel sharedViewModel) {
        this.sharedViewModel = sharedViewModel;
    }

    public void setSaveBtn(Button saveBtn) {
        this.saveBtn = saveBtn;
    }

    public void setPainLevel(Slider painLevel) {
        this.painLevel = painLevel;
    }

    public void setMoodLevel(Slider moodLevel) {
        this.moodLevel = moodLevel;
    }

    public void setStepGoal(EditText stepGoal) {
        this.stepGoal = stepGoal;
    }

    public void setStepActual(EditText stepActual) {
        this.stepActual = stepActual;
    }

    public void setLocation(Spinner location) {
        this.location = location;
    }

    public void setReminder(TimePicker reminder) {
        this.reminder = reminder;
    }

    public void setEditBtn(Button editBtn) {
        this.editBtn = editBtn;
    }
}
