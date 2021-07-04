package com.example.fit5046_assignment3.fragment.reports;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.databinding.ReportCorrelationBinding;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class CorrelationReportFragment extends Fragment {
    private ReportCorrelationBinding binding;
    private SharedViewModel sharedViewModel;
    private double[][] cor;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ReportCorrelationBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        cor = sharedViewModel.getCor();
        Log.i("correlationView", "onCreateView: " + cor[0][0]);
//        binding.textView20.setText((int) cor[0][0]);

        String a = correlation();
        Log.i("cortest", a);
        Log.i("corvalue", String.valueOf(cor[0][0]));
        return view;
    }

    public String correlation(){
        RealMatrix m = MatrixUtils.createRealMatrix(cor);
        for (int i = 0; i < m.getColumnDimension(); i++) for (int j = 0; j < m.getColumnDimension(); j++) {
            PearsonsCorrelation pc = new PearsonsCorrelation();
            double cor = pc.correlation(m.getColumn(i), m.getColumn(j));
            System.out.println(i + "," + j + "=[" + String.format(".%2f", cor) + "," + "]");
            }

        PearsonsCorrelation pc = new PearsonsCorrelation(m);
        RealMatrix corM = pc.getCorrelationMatrix();
        RealMatrix pM = pc.getCorrelationPValues();
        return("p value:" + pM.getEntry(0, 1)+ "\n" + " correlation: " + corM.getEntry(0, 1));
    }
}
