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
    private static final int REQUEST_LOCATION = 2;
    private static final String PREFER_NAME = "Config";
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context contexto = getApplicationContext();
        //int duracao = Toast.LENGTH_SHORT;

        //cria o shared preferences e atribui default value para campos que não existirem

        sharedPref = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (!sharedPref.contains("formatoApresent")) {
            editor.putString("formatoApresent", "Grau Decimal");

        }
        if (!sharedPref.contains("unidadeApresent")) {
            editor.putString("unidadeApresent", "Km/h(quilometro por hora)");

        }
        if (!sharedPref.contains("orientMapa")) {
            editor.putString("orientMapa", "Nenhuma");

        }
        if (!sharedPref.contains("tipoMapa")) {
            editor.putString("tipoMapa", "Vetorial");

        }
        if (!sharedPref.contains("infoTrafego")) {
            editor.putInt("infoTrafego", 0);

        }

        editor.commit();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.ic_astronaut2);

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
