package com.example.geolocationlist;

import android.content.Context;
import android.database.Cursor;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class HistoricoAdapter extends CursorAdapter {

    private final LayoutInflater mInflater;

    public HistoricoAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
        // Get inflater
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Use the inflater to inflate the listview layout and return it
        return mInflater.inflate(R.layout.lista_layout, parent, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Set title TextView
        String title=cursor.getString(cursor.getColumnIndex("longitude"));
        TextView txttitle=(TextView)view.findViewById(R.id.textLongitude);
        txttitle.setText(title);
        // Set url to TextView
        String url=cursor.getString(cursor.getColumnIndex("latitude"));
        TextView txturl=(TextView)view.findViewById(R.id.textLatitude);
        txturl.setText(url);

        String data=cursor.getString(cursor.getColumnIndex("data"));
        TextView txtdata=(TextView)view.findViewById(R.id.textDate);
        txtdata.setText(data);
        // Make url linkable
        Linkify.addLinks(txturl, Linkify.ALL);

    }
}
