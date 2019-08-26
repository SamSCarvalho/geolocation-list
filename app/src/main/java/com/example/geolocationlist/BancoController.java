package com.example.geolocationlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BancoController {
    private SQLiteDatabase db;
    private CriarBanco banco;

    public BancoController(Context context){
        banco = new CriarBanco(context);
    }

    public String insereDado(double longitude, double latitude, double altitude){
        ContentValues valores;
        long resultado;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriarBanco.LONGITUDE, longitude);
        valores.put(CriarBanco.LATITUDE, latitude);
        valores.put(CriarBanco.ALTITUDE, altitude);
        valores.put(CriarBanco.DATA, dateFormat.format(date));

        resultado = db.insert(CriarBanco.TABELA, null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso";
    }

    public Cursor carregaDados(){
        Cursor cursor;
        String[] campos =  { banco.ID ,banco.LONGITUDE, banco.LATITUDE, banco.ALTITUDE, banco.DATA};
        db = banco.getReadableDatabase();
        cursor = db.query(
                banco.TABELA,
                campos,
                null,
                null,
                null,
                null,
                banco.ID+" DESC",
                null
        );

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public void delete(){
        db = banco.getWritableDatabase();
        db.delete(banco.TABELA, null, null);
        db.close();
    }
}
