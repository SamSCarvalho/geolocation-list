package com.example.geolocationlist;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.content.Context;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Configuracoes extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.configuracoes, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("Config",0);

        final Spinner coord = (Spinner) getView().findViewById(R.id.coordenadaInput);
        final Spinner velocidade = (Spinner) getView().findViewById(R.id.velocidadeInput);
        final Spinner orient = (Spinner) getView().findViewById(R.id.orientInput);
        final Spinner tipo = (Spinner) getView().findViewById(R.id.tipoInput);
        final Switch info = (Switch) getView().findViewById(R.id.infoSwitch);

        coordenadasSpinner(coord, sharedPref);
        velocidadeSpinner(velocidade,sharedPref);
        orientSpinner(orient, sharedPref);
        tipoSpinner(tipo, sharedPref);
        infoSwitch(info, sharedPref);

        final Context contexto = getActivity();
        final int duracao = Toast.LENGTH_SHORT;


    }


    public void coordenadasSpinner(final Spinner coord, final SharedPreferences sharedPref ){

        final String[] formatList = getResources().getStringArray(R.array.coordOpt);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.color_spinner_layout, formatList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coord.setAdapter(adapter);

        if(sharedPref.getString("formatoApresent", "error").equals(formatList[0])){
            coord.setSelection(0);
        }
        else if(sharedPref.getString("formatoApresent", "error").equals(formatList[1])){
            coord.setSelection(1);
        }
        else if(sharedPref.getString("formatoApresent", "error").equals(formatList[2])){
            coord.setSelection(2);
        }

        coord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (coord.getSelectedItem().toString().trim().equals(formatList[0])){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("formatoApresent", formatList[0]);
                    editor.commit();
                }
                else if (coord.getSelectedItem().toString().trim().equals(formatList[1])){

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("formatoApresent", formatList[1]);
                    editor.commit();
                }
                else if (coord.getSelectedItem().toString().trim().equals(formatList[2])){

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("formatoApresent", formatList[2]);
                    editor.commit();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void velocidadeSpinner(final Spinner velocidade, final SharedPreferences sharedPref){

        final String[] unidadeList = getResources().getStringArray(R.array.velocidadeOpt);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.color_spinner_layout, unidadeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        velocidade.setAdapter(adapter);


        if(sharedPref.getString("unidadeApresent", "error").equals(unidadeList[0])){
            velocidade.setSelection(0);
        }
        else if(sharedPref.getString("unidadeApresent", "error").equals(unidadeList[1])){
            velocidade.setSelection(1);
        }

        velocidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (velocidade.getSelectedItem().toString().trim().equals(unidadeList[0])){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("unidadeApresent", unidadeList[0]);
                    editor.commit();
                }
                else if (velocidade.getSelectedItem().toString().trim().equals(unidadeList[1])){

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("unidadeApresent", unidadeList[1]);
                    editor.commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void orientSpinner(final Spinner orient, final SharedPreferences sharedPref){

        final String[] orientList = getResources().getStringArray(R.array.orientOpt);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.color_spinner_layout, orientList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orient.setAdapter(adapter);


        if(sharedPref.getString("orientMapa", "error").equals(orientList[0])){
            orient.setSelection(0);
        }
        else if(sharedPref.getString("orientMapa", "error").equals(orientList[1])){
            orient.setSelection(1);
        }
        else if(sharedPref.getString("orientMapa", "error").equals(orientList[2])){
            orient.setSelection(2);
        }

        orient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (orient.getSelectedItem().toString().trim().equals(orientList[0])){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("orientMapa", orientList[0]);
                    editor.commit();
                }
                else if (orient.getSelectedItem().toString().trim().equals(orientList[1])){

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("orientMapa", orientList[1]);
                    editor.commit();
                }
                else if (orient.getSelectedItem().toString().trim().equals(orientList[2])){

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("orientMapa", orientList[2]);
                    editor.commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void tipoSpinner(final Spinner tipo, final SharedPreferences sharedPref){

        final String[] tipoList = getResources().getStringArray(R.array.tipoOpt);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.color_spinner_layout, tipoList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adapter);


        if(sharedPref.getString("tipoMapa", "error").equals(tipoList[0])){
            tipo.setSelection(0);
        }
        else if(sharedPref.getString("tipoMapa", "error").equals(tipoList[1])){
            tipo.setSelection(1);
        }

        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (tipo.getSelectedItem().toString().trim().equals(tipoList[0])){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("tipoMapa", tipoList[0]);
                    editor.commit();
                }
                else if (tipo.getSelectedItem().toString().trim().equals(tipoList[1])){

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("tipoMapa", tipoList[1]);
                    editor.commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void infoSwitch(final Switch info, final SharedPreferences sharedPref){

        if(sharedPref.getInt("infoTrafego", 2) == 1){
            info.setChecked(true);
        }
        else {
            info.setChecked(false);
        }

        info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("infoTrafego", 1);
                    editor.commit();
                    System.out.println(sharedPref.getInt("infoTrafego", 2));

                } else {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("infoTrafego", 0);
                    editor.commit();
                    System.out.println(sharedPref.getInt("infoTrafego", 2));
                }
            }
        });
    }

}
