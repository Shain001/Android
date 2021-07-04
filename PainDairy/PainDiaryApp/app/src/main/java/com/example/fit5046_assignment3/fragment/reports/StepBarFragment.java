package com.example.fit5046_assignment3.fragment.reports;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.databinding.ReportStepBarBinding;
import com.example.fit5046_assignment3.databinding.ReportStepBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class StepBarFragment extends Fragment {
    private ReportStepBarBinding binding;
    private RecordViewModel recordViewModel;
    private Date startDate;
    private Date endDate;
    private List<Record> allRecords = new ArrayList<>();
    private List<String> dates = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ReportStepBarBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        recordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(RecordViewModel.class);
        List<BarEntry> entries = new ArrayList<>();
        try {
            allRecords = recordViewModel.getAllRecordList();
            int i = 0;
            for ( Record r : allRecords){
                entries.add(new BarEntry(i, r.getStepActual()));
                dates.add(r.getDate().toString().substring(4,10));
                i++;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



//                for (Long s : stepsActual){
//                    entries.add(new BarEntry(i, s));
//                    i ++;
//                }



        BarDataSet barDataSet = new BarDataSet(entries, "Steps");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();


        binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dates));
        binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        BarData barData = new BarData(barDataSet);
        binding.barChart.setData(barData);
        barData.setBarWidth(1.0f);
        binding.barChart.setVisibility(View.VISIBLE);
        binding.barChart.animateY(4000);
        //description will be displayed as "Description Label" if not provided
        Description description = new Description();
        description.setText("Daily Steps");
        binding.barChart.setDescription(description);
        //refresh the chart
        binding.barChart.invalidate();

        return view;
    }

    public Date getDateWithoutTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date dateWithoutTime = cal.getTime();
        return dateWithoutTime;
    }

    public Date getDateOneWeekBefore(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateWithoutTime());
        calendar.add(Calendar.DAY_OF_YEAR, -1000);
        Date newDate = calendar.getTime();
        return newDate;
    }
}
