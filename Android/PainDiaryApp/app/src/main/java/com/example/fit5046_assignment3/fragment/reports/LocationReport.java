package com.example.fit5046_assignment3.fragment.reports;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.databinding.ReportLocationBinding;
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

public class LocationReport extends Fragment {

    private ReportLocationBinding binding;
    private PieChart chart;
    private RecordViewModel recordViewModel;
    private HashMap<String, Integer> freq;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = ReportLocationBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        chart = binding.painChart;
        recordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(RecordViewModel.class);
        try {
            freq = recordViewModel.getPainAndFrequency();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setupPieChart(chart, "Pain Location Analysis");

        loadPieChartData(chart, "Pain Location", freq);


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

    final static public void loadPieChartData(PieChart chart, String label, HashMap<String, Integer> data) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : data.entrySet() ){
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));

        }


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
