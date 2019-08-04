package com.example.geolocationlist;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssNavigationMessage;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.List;

public class Gnss extends Fragment implements LocationListener, GpsStatus.Listener{
    private LocationManager locationManager;
    private LocationProvider locationProvider;
    private static final int REQUEST_LOCATION = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return inflater.inflate(R.layout.gnss, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            ativaGPS();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED) {
                ativaGPS();
            }
        } else {
            Log.d("ERRO", "onRequestPermissionsResult: FALSE");
        }
    }

    public void ativaGPS() {
        try {
            locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(locationProvider.getName(),3000, 1, this);
            locationManager.addGpsStatusListener(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void desativaGPS() {
        try {
            locationManager.removeUpdates(this);
            locationManager.removeGpsStatusListener(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        desativaGPS();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onLocationChanged(Location location){
        TextView altitudeText = (TextView) getActivity().findViewById(R.id.altitudeValue);
        TextView longitudeText = (TextView) getActivity().findViewById(R.id.longitudeValue);
        TextView latitudeText = (TextView) getActivity().findViewById(R.id.latitudeValue);

        altitudeText.setText(location.getAltitude()+"");
        longitudeText.setText(location.getLongitude()+"");
        latitudeText.setText(location.getLatitude()+"");
    }
    @Override
    public void onProviderDisabled(String provider) { }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }


    @Override
    public void onGpsStatusChanged(int i) {

    }
}
