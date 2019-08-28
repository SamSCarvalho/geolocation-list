package com.example.geolocationlist;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class Historico extends Fragment implements OnMapReadyCallback {
    private ListView lista;
    View mView;
    MapView mapHist;
    GoogleMap mGoogleMap;
    Polyline polyLineHist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.historico, container, false);
        this.loadDate();

        return mView;
    }



    public void loadDate(){
        BancoController crud = new BancoController(getContext());
        Cursor cursor = crud.carregaDados();

        HistoricoAdapter adapter = new HistoricoAdapter(getContext(), cursor);

        lista = (ListView) mView.findViewById(R.id.listaView);
        lista.setAdapter(adapter);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapHist = (MapView) getView().findViewById(R.id.mapHist);

        if (mapHist != null){
            mapHist.onCreate(null);
            mapHist.onResume();
            mapHist.getMapAsync(this);
        }

        final Button button = (Button) mView.findViewById(R.id.apagarHist);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BancoController crud = new BancoController(getContext());
                crud.delete();
                polyLineHist.remove();
                loadDate();
            }
        });

        final Switch mostrarMapa = (Switch) getView().findViewById(R.id.mostrarMapa);

        mostrarMapa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    lista.setVisibility(mView.INVISIBLE);
                    mapHist.setVisibility(mView.VISIBLE);


                } else {

                    lista.setVisibility(mView.VISIBLE);
                    mapHist.setVisibility(mView.INVISIBLE);
                }
            }
        });

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

        // Desenhar em outra tela o polyline com as rotas

        PolylineOptions options = new PolylineOptions();

        options.color( Color.parseColor( "#8e24aa" ) );
        options.width( 5 );
        options.visible( true );

        ArrayList<PositionModel> positions = getPositions();

        for ( PositionModel pos : positions )
        {
            options.add( new LatLng( pos.getLatitude(),
                    pos.getLongitude() ) );
        }

        polyLineHist = mGoogleMap.addPolyline( options );

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

        CameraPosition Liberty = CameraPosition.builder().target(
                new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude())
        ).zoom(20).bearing(0).tilt(45).build();

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
    }
}
