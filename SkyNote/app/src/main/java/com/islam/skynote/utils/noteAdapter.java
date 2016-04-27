package com.islam.skynote.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.islam.skynote.R;

import java.util.List;

/**
 * Created by islam  .
 */
// classe qui permet d'afficher les items dans la liste

public class noteAdapter extends ArrayAdapter<noteObj> {

    private int res;
    public noteAdapter(Context context, int resource, List<noteObj> objects) {
        super(context, resource, 0, objects);
        res = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(res, parent, false);
        }

        TextView txtTitre = (TextView)convertView.findViewById(R.id.txtTitre);
        txtTitre.setText(getItem(position).Titre);

        TextView txtDescr = (TextView)convertView.findViewById(R.id.txtDescription);
        txtDescr.setText(getItem(position).Description);
        return convertView;
    }

}
