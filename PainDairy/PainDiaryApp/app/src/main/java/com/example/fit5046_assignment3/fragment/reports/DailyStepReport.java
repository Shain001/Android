package com.example.fit5046_assignment3.fragment.reports;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.databinding.ReportStepBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * This is the task (b): draw today's step actual and step goal
 */
public class DailyStepReport extends Fragment {
    private ReportStepBinding binding;
    private LiveData<List<Record>> allRecord;
    private RecordViewModel recordViewModel;
    private long stepGoal = 1;
    private long stepAcual = 1;
    PieChart chart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = ReportStepBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        recordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(RecordViewModel.class);
        allRecord = recordViewModel.getAllRecord();

        chart = binding.stepReport;
        setupPieChart(chart, "Daily Step");
        try {
            stepGoal = recordViewModel.getCurRecord().getStepGoal();
            stepAcual = recordViewModel.getCurRecord().getStepActual();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        allRecord.observe(getViewLifecycleOwner(), new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                try {
                    stepGoal = recordViewModel.getCurRecord().getStepGoal();
                    stepAcual = recordViewModel.getCurRecord().getStepActual();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        loadPieChartData(chart, "daily step");
        return view;
    }

    public void setupPieChart(PieChart chart, String title) {
        chart.setDrawHoleEnabled(true);
        chart.setUsePercentValues(false);
        chart.setEntryLabelTextSize(12);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setCenterText(title);
        chart.setCenterTextSize(24);
        chart.getDescription().setEnabled(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    public void loadPieChartData(PieChart chart, String label) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(stepAcual, "Step Taken"));
        entries.add(new PieEntry(stepGoal, "Step Goal"));


        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, label);
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(chart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        chart.setData(pieData);
        chart.invalidate();

        chart.animateY(1400, Easing.EaseInOutQuad);
    }


}
