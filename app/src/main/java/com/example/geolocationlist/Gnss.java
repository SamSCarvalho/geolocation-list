package com.example.geolocationlist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import org.xmlpull.v1.XmlPullParser;

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
        String[] formatList = getResources().getStringArray(R.array.coordOpt);

        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("Config",0);

        BancoController crud = new BancoController(getContext());

        crud.insereDado(location.getLongitude(),location.getLatitude(),location.getAltitude());


        if(sharedPref.getString("formatoApresent", "error").equals(formatList[0])){
            altitudeText.setText(Location.convert(location.getAltitude(),Location.FORMAT_DEGREES));
            longitudeText.setText(Location.convert(location.getLongitude(),Location.FORMAT_DEGREES));
            latitudeText.setText(Location.convert(location.getLatitude(),Location.FORMAT_DEGREES));

        }
        else if(sharedPref.getString("formatoApresent", "error").equals(formatList[1])){
            altitudeText.setText(Location.convert(location.getAltitude(),Location.FORMAT_MINUTES));
            longitudeText.setText(Location.convert(location.getLongitude(),Location.FORMAT_MINUTES));
            latitudeText.setText(Location.convert(location.getLatitude(),Location.FORMAT_MINUTES));

        }
        else if(sharedPref.getString("formatoApresent", "error").equals(formatList[2])){
            altitudeText.setText(Location.convert(location.getAltitude(),Location.FORMAT_SECONDS));
            longitudeText.setText(Location.convert(location.getLongitude(),Location.FORMAT_SECONDS));
            latitudeText.setText(Location.convert(location.getLatitude(),Location.FORMAT_SECONDS));

        }
    }

    @Override
    public void onProviderDisabled(String provider) { }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }


    @Override
    public void onGpsStatusChanged(int i) {
        try {
            GpsStatus gpsStatus = locationManager.getGpsStatus(null);
            if (gpsStatus != null) {
                Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
                try{
                    SkyView info = (SkyView) getView().findViewById(R.id.skyView);
                    info.setSats(sats);
                    info.postInvalidate();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

}
