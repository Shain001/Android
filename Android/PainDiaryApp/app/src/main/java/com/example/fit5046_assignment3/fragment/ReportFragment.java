package com.example.fit5046_assignment3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fit5046_assignment3.R;
import com.example.fit5046_assignment3.databinding.NavDataEntryBinding;
import com.example.fit5046_assignment3.databinding.NavReportBinding;

public class ReportFragment extends Fragment {

    private NavReportBinding binding;

    public ReportFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = NavReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_report_fragment_to_painLocationReportFragment);
            }
        });

        binding.stepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.report_to_step);
            }
        });

        binding.painWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.report_to_weather);
            }
        });

        binding.stepBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.report_to_step_bar);
            }
        });

        binding.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.report_to_location);
            }
        });



        return view;
    }
}
