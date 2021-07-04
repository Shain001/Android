package com.example.fit5046_assignment3.fragment.reports;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.R;
import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.authentication.Login;
import com.example.fit5046_assignment3.databinding.ReportPainWeatherBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PainWeatherReportFragment extends Fragment {
    private ReportPainWeatherBinding binding;
    // private LiveData<List<Record>> allRecord;
    private RecordViewModel recordViewModel;
    private List<Record> recordBetweenDate;
    private EditText startDateText;
    private EditText endDateText;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    private Date startDate = null;
    private Date endDate = null;
    private String factor;
    private LineChart chart;
    private SharedViewModel sharedViewModel;
    private LineChart lineChart;
    private String pValue;
    private String correlation;
    private List<Record> allRecords;



//    private  plotDate;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ReportPainWeatherBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        chart = binding.weatherChart;
        startDateText = binding.startDate;
        endDateText = binding.endDate;
        recordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(RecordViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        // Set Spinner Selection List
        List<String> spinnerList = new ArrayList<String>();
        spinnerList.add("humidity");
        spinnerList.add("temperature");
        spinnerList.add("pressure");

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        binding.factorSpinner.setAdapter(spinnerAdapter);


        // For start date, set text and update field value
        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), AlertDialog.THEME_TRADITIONAL,
                        startDateSetListener, year,month,day );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String strDate = year + "-" + (month+1) + "-" + dayOfMonth;
                startDateText.setText(strDate);
                startDate = getDate(year,month,dayOfMonth);
                Log.i("startDate", "start: " +startDate);
            }
        };

        // For end date,  set text and update field value
        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(), AlertDialog.THEME_TRADITIONAL,
                        endDateSetListener, year,month,day );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String strDate = year + "-" + (month+1) + "-" + dayOfMonth;
                endDateText.setText(strDate);
                endDate = getDate(year,month,dayOfMonth);
                Log.i("endate", "end: " +endDate);
            }
        };


        // Search Btn Button
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {
                    if (!(startDate != null && endDate != null)){

                        Toast.makeText(getContext(), "enter date!", Toast.LENGTH_SHORT);

                    }
                    else{

                        allRecords = recordViewModel.getRecordBetweenDate(startDate, endDate);
                    }

                    factor = binding.factorSpinner.getSelectedItem().toString();
                    HashMap<String, Integer> painDate = new HashMap<String, Integer>();
                    HashMap<String, Double> painFactor = new HashMap<String, Double>();
                    if (allRecords != null) {

                        // build correlation data
                        double[][] cor = new double[allRecords.size()][2];
                        for (Record r : allRecords) {
                            for (int i = 0; i < allRecords.size(); i++) {
                                cor[i][0] = r.getPainLevel();
                                cor[i][1] = r.getFactor(factor);
                            }
                        }
                        sharedViewModel.setCor(cor);
                        if (allRecords.size() >=2){
                            correlation(cor);
                            binding.correlationBtn.setVisibility(View.VISIBLE);

                        }

                         ArrayList<Date> dates = new ArrayList<>();
                         ArrayList<Integer> painData = new ArrayList<>();
                         ArrayList<Float> factorData = new ArrayList<>();
                        for (Record i : allRecords) {
                            Log.i("setENtries", "onClick: " + i.getDate());
                            dates.add(i.getDate());
                            painData.add(i.getPainLevel());
                            factorData.add(i.getFactor(factor));
                        }

                        for (Record r : allRecords) {

                                Log.i("painAndDates", "pain and dates and fact: " + dates.get(0) + " /" + painData.get(0));

                                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                List<Entry> painEntry = new ArrayList<Entry>();
                                List<Entry> factorEntry = new ArrayList<Entry>();
                                int i = 0;
                                for (Integer p : painData) {
                                    painEntry.add(new Entry(i, p));
                                    i++;
                                }
                                int j = 0;
                                for (Float p : factorData) {
                                    factorEntry.add(new Entry(j, p));
                                    j++;
                                }

                                LineDataSet dataSet1 = new LineDataSet(painEntry, "Pain Level");
                                LineDataSet dataSet2 = new LineDataSet(factorEntry, factor);
                                dataSet1.setColor(Color.RED);
                                dataSet1.setLineWidth(1.6f);
                                dataSet2.setColor(ColorTemplate.getHoloBlue());
                                dataSet2.setLineWidth(1.6f);
                                List<String> xAxisValues = new ArrayList<>();
                                for (Date d : dates) {
//                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                    String strDate = d.toString();
                                    xAxisValues.add(strDate.substring(4,10));
                                }
                                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
                                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

                                dataSets.add(dataSet1);
                                dataSets.add(dataSet2);

                                LineData lineData = new LineData(dataSets);
                                chart.setData(lineData);
                                chart.invalidate();
                            }}
                    else{
                        Toast.makeText(getContext(), "No Record", Toast.LENGTH_SHORT);
                    }

                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                } catch (ExecutionException executionException) {
                    executionException.printStackTrace();
                }


            }
        });






        binding.correlationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toShow = "p value = " + pValue + "; correlation = " + correlation;
                binding.correlation.setText(toShow);
            }
        });


        return view;
    }

    public Date getDate(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH,day);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.YEAR, year);
        Date dateWithoutTime = cal.getTime();
        return dateWithoutTime;
    }

    private void configureLineChart() {
        Description desc = new Description();
        desc.setText("Stock Price History");
        desc.setTextSize(28);
        lineChart.setDescription(desc);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value * 1000L;
                return mFormat.format(new Date(millis));
            }
        });
    }

    public void correlation(double[][] data){
        RealMatrix m = MatrixUtils.createRealMatrix(data);
        for (int i = 0; i < m.getColumnDimension(); i++) for (int j = 0; j < m.getColumnDimension(); j++) {
            PearsonsCorrelation pc = new PearsonsCorrelation();
            double cor = pc.correlation(m.getColumn(i), m.getColumn(j));
            System.out.println(i + "," + j + "=[" + String.format(".%2f", cor) + "," + "]");
        }

        PearsonsCorrelation pc = new PearsonsCorrelation(m);
        RealMatrix corM = pc.getCorrelationMatrix();
        RealMatrix pM = pc.getCorrelationPValues();
//        return("p value:" + pM.getEntry(0, 1)+ "\n" + " correlation: " + corM.getEntry(0, 1));
        pValue = "" + pM.getEntry(0, 1);
        correlation = "" + corM.getEntry(0, 1);
    }
}
