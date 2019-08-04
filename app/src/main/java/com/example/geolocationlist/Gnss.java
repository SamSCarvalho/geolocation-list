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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.List;

public class Gnss extends Fragment implements LocationListener{
    private LocationManager locationManager;
    private LocationListener locationListener = new DummyLocationListener();
    private GpsListener gpsListener = new GpsListener();
    private Location location;
    private GpsStatus gpsStatus;
    private static final int REQUEST_LOCATION = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gnss, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        gpsStatus = locationManager.getGpsStatus(null);
        locationManager.addGpsStatusListener(gpsListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2*1000, 0, locationListener);

    }


    private void getSatData(){
        Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
        for(GpsSatellite sat : sats){
        }

        if(ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }

        gpsStatus = locationManager.getGpsStatus(gpsStatus);

    }

    @Override
    public void onLocationChanged(Location location){ }
    @Override
    public void onProviderDisabled(String provider) { }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    class GpsListener implements GpsStatus.Listener{
        @Override
        public void onGpsStatusChanged(int event) {
            getSatData();
        }
    }

    class DummyLocationListener implements LocationListener {
        //Empty class just to ease instatiation
        @Override
        public void onLocationChanged(Location location) {
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
    }
}
