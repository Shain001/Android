package com.example.fit5046_assignment3.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fit5046_assignment3.R;
import com.example.fit5046_assignment3.ViewModel.SharedViewModel;
import com.example.fit5046_assignment3.databinding.NavMapBinding;
import com.example.fit5046_assignment3.databinding.NavRecordBinding;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.utils.BitmapUtils;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import static androidx.core.content.ContextCompat.getSystemService;

public class MapFragment extends Fragment {

    private NavMapBinding binding;
    private MapView mapView;
    private MarkerViewManager markerViewManager;
    private Symbol symbol;
    private static final String ID_ICON_AIRPORT = "airport";
    private Geocoder geocoder;
    public MapFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        geocoder = new Geocoder(getActivity());
        final LatLng[] latLng = {new LatLng(model.getLatAltitude(), model.getLongAltitude())};
        String token = getString(R.string.mapbox_access_token);
        Mapbox.getInstance(getActivity(), token);
        binding = NavMapBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mapView = (MapView) binding.mapView;


        // initialize the map

//        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//        }
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        CameraPosition position = new CameraPosition.Builder().target(latLng[0]).zoom(13).build();

                        mapboxMap.setCameraPosition(position);
                        mapboxMap.addMarker(new MarkerOptions().setPosition(latLng[0]));

//                        // Create a SymbolManager.
//                        GeoJsonOptions geoJsonOptions = new GeoJsonOptions().withTolerance(0.4f);
//                        SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style,null, geoJsonOptions);
//                        addAirplaneImageToStyle(style);
//                        // Set non-data-driven properties.
//                        symbolManager.setIconAllowOverlap(true);
//                        symbolManager.setTextAllowOverlap(true);
//
//                        // Create a symbol at the specified location.
//                        SymbolOptions symbolOptions = new SymbolOptions()
//                                .withLatLng(new LatLng(model.getLatAltitude(), model.getLongAltitude()))
//                                .withIconImage("ID_ICON_AIRPORT")
//                                .withIconSize(1.3f);

                        // Use the manager to draw the symbol.
//                        symbol = symbolManager.create(symbolOptions);
                    }
                });
            }
        });

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Address> addresses =  geocoder.getFromLocationName(binding.address.getText().toString().trim(),1);

                    if (addresses.size() >0){
                        Address address = addresses.get(0);
                        latLng[0] = new LatLng(address.getLatitude(), address.getLongitude());

                        mapView.onCreate(savedInstanceState);
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull final MapboxMap mapboxMap) {


                                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {

                                    @Override

                                    public void onStyleLoaded(@NonNull Style style) {
                                        MarkerOptions markerOptions = new MarkerOptions()
                                                .position(new LatLng(address.getLatitude(), address.getLongitude()))
                                                .title(binding.address.getText().toString().trim());
                                        CameraPosition position = new CameraPosition.Builder().target(latLng[0]).zoom(13).build();
                                        mapboxMap.addMarker(markerOptions);
                                        mapboxMap.setCameraPosition(position);

                                    }
                                });


                            }
                        });
                    }
                    else{
                        Toast.makeText(getActivity(), "Address doesn't exist", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }


    // Override the lifecycle method to adapt MapBox lifecycle requirements
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void addAirplaneImageToStyle(Style style) {
        style.addImage(ID_ICON_AIRPORT,
                BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_launcher_background)),
                true);
    }


}

