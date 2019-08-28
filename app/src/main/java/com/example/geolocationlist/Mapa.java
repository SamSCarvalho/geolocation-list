package com.example.geolocationlist;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.getSystemService;

public class Mapa extends Fragment implements OnMapReadyCallback, LocationListener {
    private LocationManager locationManager;
    private LocationProvider locationProvider;
    private static final int REQUEST_LOCATION = 2;
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    Marker nowLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mView = inflater.inflate(R.layout.mapa, container, false);
        return mView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) getView().findViewById(R.id.map);

        if (mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
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
            locationManager.requestLocationUpdates(locationProvider.getName(),1000, 1, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void desativaGPS() {
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        desativaGPS();
    }


    public ArrayList<PositionModel> getPositions(){
        BancoController crud = new BancoController(getContext());
        Cursor cursor = crud.carregaDados();
        cursor.moveToFirst();
        ArrayList<PositionModel> position = new ArrayList<PositionModel>();
        while(!cursor.isAfterLast()) {
            PositionModel pos = new PositionModel(
                    cursor.getDouble(cursor.getColumnIndex("latitude")),
                    cursor.getDouble(cursor.getColumnIndex("longitude")),
                    cursor.getString(cursor.getColumnIndex("data"))
            );
            position.add(pos);
            cursor.moveToNext();
        }
        cursor.close();
        return position;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        int MapType = GoogleMap.MAP_TYPE_NORMAL;
        boolean Traffic = false;
        mGoogleMap = googleMap;
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("Config",0);

        if(sharedPref.getString("tipoMapa", "error").equals("Imagem de Satélite")){
            MapType = GoogleMap.MAP_TYPE_SATELLITE;
        } else if(sharedPref.getString("tipoMapa", "error").equals("Vetorial")) {
            MapType = GoogleMap.MAP_TYPE_NORMAL;
        }

        if(sharedPref.getInt("infoTrafego", -1) == 1){
            Traffic = true;
        } else if(sharedPref.getInt("infoTrafego", -1) == 0){
            Traffic = false;
        }


        ArrayList<PositionModel> positions = getPositions();
        PositionModel lastPosition = null;
        if (positions.size() > 0){
            lastPosition = positions.get(0);
        } else {
            lastPosition = new PositionModel(
                    13.32323,
                    42.244242,
                    "Sem atualização"
            );
        }

        mGoogleMap.setMapType(MapType);
        mGoogleMap.setTrafficEnabled(Traffic);


        nowLocation = mGoogleMap.addMarker(
                new MarkerOptions().position(
                        new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude())
                ).title("Estamos aqui")
        );
        nowLocation.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
      

        CameraPosition Liberty = CameraPosition.builder().target(
                new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude())
        ).zoom(16).bearing(0).tilt(45).build();

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(Location location) {
        float bearing = 0;
        TextView velocidadeText = (TextView) getActivity().findViewById(R.id.velocidadeValueMap);
        TextView longitudeText = (TextView) getActivity().findViewById(R.id.longitudeValueMap);
        TextView latitudeText = (TextView) getActivity().findViewById(R.id.latitudeValueMap);
        String[] formatList = getResources().getStringArray(R.array.coordOpt);
        String[] velocidadeList = getResources().getStringArray(R.array.velocidadeOpt);
        String[] bearingType = getResources().getStringArray(R.array.orientOpt);
        BancoController crud = new BancoController(getContext());


        Toast.makeText(getContext(), "Bearing" + location.getBearing(), Toast.LENGTH_LONG).show();

        crud.insereDado(location.getLongitude(),location.getLatitude(),location.getAltitude());

        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("Config",0);

        if(sharedPref.getString("orientMapa", "error").equals(bearingType[0])){
            bearing = 0;
        } else if(sharedPref.getString("orientMapa", "error").equals(bearingType[1])) {
            bearing = 0;
        }else if(sharedPref.getString("orientMapa", "error").equals(bearingType[2])) {
            bearing = location.getBearing();
        }

        nowLocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        nowLocation.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        nowLocation.setTitle("Estamos aqui");

        CameraPosition Liberty = CameraPosition.builder().target(
                new LatLng(location.getLatitude(), location.getLongitude())
        ).zoom(16).bearing(bearing).tilt(45).build();

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));


        if(sharedPref.getString("formatoApresent", "error").equals(formatList[0])){
            longitudeText.setText(Location.convert(location.getLongitude(),Location.FORMAT_DEGREES));
            latitudeText.setText(Location.convert(location.getLatitude(),Location.FORMAT_DEGREES));

        }
        else if(sharedPref.getString("formatoApresent", "error").equals(formatList[1])){
            longitudeText.setText(Location.convert(location.getLongitude(),Location.FORMAT_MINUTES));
            latitudeText.setText(Location.convert(location.getLatitude(),Location.FORMAT_MINUTES));

        }
        else if(sharedPref.getString("formatoApresent", "error").equals(formatList[2])){
            longitudeText.setText(Location.convert(location.getLongitude(),Location.FORMAT_SECONDS));
            latitudeText.setText(Location.convert(location.getLatitude(),Location.FORMAT_SECONDS));

        }

        if(sharedPref.getString("unidadeApresent", "error").equals(velocidadeList[1])){
            velocidadeText.setText(location.getSpeed()+" Mph");
        } else if(sharedPref.getString("unidadeApresent", "error").equals(velocidadeList[0])){
            velocidadeText.setText((location.getSpeed()*1.609)+" Km/h");
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        nowLocation.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        nowLocation.setTitle("Estamos aqui");
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onProviderDisabled(String s) {
        nowLocation.setIcon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_question));
        nowLocation.setTitle("GPS desativado");
    }
}
