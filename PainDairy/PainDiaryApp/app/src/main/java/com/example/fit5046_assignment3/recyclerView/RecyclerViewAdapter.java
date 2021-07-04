package com.example.fit5046_assignment3.recyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.R;
import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.databinding.RecordRowBinding;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Record> records;
    private RecordViewModel rvm;
    private View view;
    private SharedViewModel sharedViewModel;


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecordRowBinding binding;

        public ViewHolder(RecordRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public RecyclerViewAdapter(List<Record> records, RecordViewModel rvm, View view, SharedViewModel sharedViewModel){
        this.records = records;
        this.rvm = rvm;
        this.view = view;
        this.sharedViewModel = sharedViewModel;
    }

    // This is to create the view holder and connect it with the view,
    // View holder is like a container for a view, i.e. the row in table
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecordRowBinding binding = RecordRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    // This is to fill in the data to the view holder
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        final Record record = records.get(position);

        // convert timestamp in record to date string
//        long timestamp = record.getDate();
        Date date = record.getDate();
//        String recordDate = new SimpleDateFormat("yyyy-MM-dd").format(timestamp);
        String recordDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String toShow = "Pain Level: " + record.getPainLevel() + " Pain Location: " + record.getPainLocation() + " Mood Level: " +
                record.getMoodLevel() + " Step Taken: " + (int) record.getStepActual() + " Humidity: " + record.getHumidity() +
                " Pressure: " + record.getPressure() + " Temperature: " + record.getTemperature();
        holder.binding.recordDate.setText(recordDate);
//        holder.binding.recordMoodLevel.setText(Integer.toString(record.getMoodLevel()));
        holder.binding.recordPainLevel.setText(toShow);
//        holder.binding.recordPainLocation.setText(record.getPainLocation());
//        holder.binding.recordStepActual.setText(Integer.toString((int) record.getStepActual()));
//        holder.binding.recordHumidity.setText(Double.toString(record.getHumidity()));
//        holder.binding.recordPressure.setText(Double.toString(record.getPressure()));
//        holder.binding.recordTemp.setText(Double.toString(record.getTemperature()));




        holder.binding.ivItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvm.deleteRecord(record);
                notifyDataSetChanged();
            }
        });

        holder.binding.ivItemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.setEditRecord(record);
                Navigation.findNavController(view).navigate(R.id.record_to_edit);
            }
        });

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
