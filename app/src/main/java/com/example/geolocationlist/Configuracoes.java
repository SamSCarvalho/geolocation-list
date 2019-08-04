package com.example.geolocationlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;
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

        final Spinner coord = (Spinner) getView().findViewById(R.id.coordenadaInput);
        final String[] formatList = getResources().getStringArray(R.array.coordOpt);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                 android.R.layout.simple_spinner_item, formatList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        coord.setAdapter(adapter);

        final SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences("Config",0);


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


}
