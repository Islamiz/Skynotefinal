package com.islam.skynote;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.islam.skynote.utils.noteObj;

import java.util.List;

//CLASSE QUI NE MA SERVIE A RIEN FINALMENENT
/**
 * Created by islam  .
 */

public class notePage extends ArrayAdapter<noteObj> {

    private int res;
    public notePage(Context context, int resource, List<noteObj> objects) {
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

        TextView txtTitre = (TextView)convertView.findViewById(R.id.TitleNote);
        txtTitre.setText(getItem(position).Titre);

        TextView txtDescr = (TextView)convertView.findViewById(R.id.DescriptionNote);
        txtDescr.setText(getItem(position).Description);
        return convertView;
    }

}
