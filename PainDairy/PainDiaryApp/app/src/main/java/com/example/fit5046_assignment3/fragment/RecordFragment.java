package com.example.fit5046_assignment3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.databinding.NavRecordBinding;
import com.example.fit5046_assignment3.databinding.NavReportBinding;
import com.example.fit5046_assignment3.recyclerView.RecyclerViewAdapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecordFragment extends Fragment {

    private NavRecordBinding binding;
    private List<Record> allRecordsList;
    private RecordViewModel rvm;
    //recycler view components
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedViewModel sharedViewModel;

    public RecordFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = NavRecordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        rvm =  ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(RecordViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        // get all records and send to the adapter, since in the adapter it requires list instead of live data, therefore
        // when newing the adapter, send list allRecords and then if anything changes, using observer to update list in adapter.
        try {
            allRecordsList = rvm.getAllRecordList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Initialize the recycler view
        adapter = new RecyclerViewAdapter(allRecordsList,rvm,view, sharedViewModel);
        //set layout of recycler view
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        // Set the describer for the allRecords
        rvm.getAllRecord().observe(getViewLifecycleOwner(), new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                adapter.setRecords(records);
            }
        });


        return view;
    }
}
