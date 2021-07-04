package com.example.fit5046_assignment3.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.databinding.NavHomeBinding;
import com.example.fit5046_assignment3.databinding.NavMapBinding;
import com.example.fit5046_assignment3.weatherApi.ApiInterface;
import com.example.fit5046_assignment3.weatherApi.ResponseConvert;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private NavHomeBinding binding;
    final String WEATHER_API_KEY = "ba69309cafcb21314e5478827a0607af";
    final String BaseUrl = "https://api.openweathermap.org/";
    private String temperature = "123456";
    private String pressure;
    private String humidity;
    private String city;
    private SharedViewModel sharedViewModel;

    public HomeFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = NavHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
// get weather info and store in shared model
        getWeather(WEATHER_API_KEY, BaseUrl);


        return view;
    }

    // Get Weather API
    public void getWeather(String key, String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface service = retrofit.create(ApiInterface.class);
        Call<ResponseConvert> call = service.getCurrentWeatherData("Melbourne,au", "metric",key);
        call.enqueue(new Callback<ResponseConvert>() {

            @Override
            public void onResponse(@NonNull Call<ResponseConvert> call, @NonNull Response<ResponseConvert> response) {
                Log.i("code", String.valueOf(response.code()));
                if (response.code() == 200) {
                    ResponseConvert weatherResponse = response.body();
                    assert weatherResponse != null;
//                    City.setText("City: "+weatherResponse.name);
                    java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
//                    sharedViewModel.setHumidity("Humidity: " + df.format(weatherResponse.getMain().getHumidity().toString()));
//                    sharedViewModel.setPressure("Pressure: " + df.format(weatherResponse.getMain().getPressure().toString()));
//                    sharedViewModel.setTemperature("Temperature: " + df.format(weatherResponse.getMain().getTemp().toString()));
//
//
//                    binding.temp.setText(df.format(weatherResponse.getMain().getTemp().toString()));
//                    binding.hum.setText(df.format(weatherResponse.getMain().getHumidity().toString()));
//                    binding.pressure.setText(df.format(weatherResponse.getMain().getPressure().toString()));

                    sharedViewModel.setHumidity(String.format("%.2f", Double.parseDouble(weatherResponse.getMain().getHumidity())));
                    sharedViewModel.setPressure(String.format("%.2f", Double.parseDouble(weatherResponse.getMain().getPressure())));
                    sharedViewModel.setTemperature(String.format("%.2f", Double.parseDouble(weatherResponse.getMain().getTemp())));

                    binding.temp.setText("Humidity: " + String.format("%.2f", Double.parseDouble(weatherResponse.getMain().getHumidity())));
                    binding.hum.setText("Pressure: " + String.format("%.2f", Double.parseDouble(weatherResponse.getMain().getPressure())));
                    binding.pressure.setText("Temperature: " + String.format("%.2f", Double.parseDouble(weatherResponse.getMain().getTemp())));
                    Log.i("12345", ""+ temperature);
                }


            }

            @Override
            public void onFailure(@NonNull Call<ResponseConvert> call, @NonNull Throwable t) {

                city = "Mel";
                temperature = "16";
                humidity = "2";
                pressure = "1";
            }
        });
    }

}
