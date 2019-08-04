package com.example.geolocationlist;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 2;
    private static final String PREFER_NAME = "Config";
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context contexto = getApplicationContext();
        //int duracao = Toast.LENGTH_SHORT;

        sharedPref = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (!sharedPref.contains("formatoApresent"))
        {
            editor.putString("formatoApresent", "Grau Decimal");

        }

        //editor.putString("formatoApresent", "Grau Decimal");
        //editor.putString("UnidadeApresent", R.string.km);
        //editor.putString("OrientMapa", R.string.none);
        //editor.putString("TipoMapa", R.string.vet);
        //editor.putString("InfoTrafego", 0);
        editor.commit();


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
        List<String> list = locationManager.getAllProviders();
        Log.d("get_location", list.toString());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("GNSS"));
        tabLayout.addTab(tabLayout.newTab().setText("Configurações"));
        tabLayout.addTab(tabLayout.newTab().setText("Créditos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
