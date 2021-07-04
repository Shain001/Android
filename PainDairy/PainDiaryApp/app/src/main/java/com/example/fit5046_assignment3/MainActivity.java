package com.example.fit5046_assignment3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fit5046_assignment3.Entity.Record;
import com.example.fit5046_assignment3.ViewModel.RecordViewModel;
import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.authentication.Login;
import com.example.fit5046_assignment3.databinding.ActivityMainBinding;
import com.example.fit5046_assignment3.util.BackgroundRecordUploader;
import com.example.fit5046_assignment3.weatherApi.ApiInterface;
import com.example.fit5046_assignment3.weatherApi.ResponseConvert;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private RecordViewModel recordViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initiate view
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Set Navigation
        setSupportActionBar(binding.appBar.toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment,
                R.id.nav_data_entry_fragment,
                R.id.nav_map_fragment,
                R.id.nav_report_fragment,
                R.id.nav_record_fragment).setOpenableLayout(binding.drawerLayout)
                .build();

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);

        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.appBar.toolbar, navController,
                mAppBarConfiguration);

        // logout button listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(MenuItem -> {
            logout(view);
            return true;
        });


        // FirebBase Upload
        recordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RecordViewModel.class);

        try {
            List<Record> allRecordList = recordViewModel.getAllRecordList();
            BackgroundRecordUploader.allRecords = allRecordList;
            Log.i("aaaa", "data update to Back");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LiveData<List<Record>> allRecords = recordViewModel.getAllRecord();
        allRecords.observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                BackgroundRecordUploader.allRecords = records;
            }
        });
        Calendar c = Calendar.getInstance();


        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        try {
            String currentTimeString =sdf.format(c.getTime());//convert format first
            Date currTime=sdf.parse(currentTimeString);//get the Date object of current time
            Date uploadTime = sdf.parse("14:43:00");  // upload time
            long diffInMs=uploadTime.getTime()-currTime.getTime();//million seconds,  this is to get the delaty time
            long diffInSec=TimeUnit.MILLISECONDS.toSeconds(diffInMs);//seconds
            Log.d("aaaaa", "uploadTime:"+String.valueOf(uploadTime.getTime()));
            Log.d("aaaaa", "currentTime:"+String.valueOf(currTime.getTime()));
            Log.d("aaaaa", "duration:"+String.valueOf(diffInMs)+"ms");
            Log.d("aaaaa", "duration:"+String.valueOf(diffInSec)+"s");
            Log.d("aaaaa", "Work Manager starts working");

            // Set the work manager. Set as repeat every 24 hours
            PeriodicWorkRequest.Builder myWorkBuilder =
                    new PeriodicWorkRequest.Builder(BackgroundRecordUploader.class, 24, TimeUnit.HOURS);
            myWorkBuilder.setInitialDelay(diffInSec,TimeUnit.SECONDS).addTag("uploadRecord");//set delay for next task

            PeriodicWorkRequest myWork = myWorkBuilder.build();
            WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("uploadRecord",ExistingPeriodicWorkPolicy.REPLACE,myWork);
        } catch (ParseException e) {
            e.printStackTrace();
        }



//        // Get location info
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
//            SharedViewModel shareModel = new ViewModelProvider(this).get(SharedViewModel.class);
//            shareModel.setLatAltitude(latitude);
//            shareModel.setLongAltitude(longitude);


    }

    //logout function
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }



}