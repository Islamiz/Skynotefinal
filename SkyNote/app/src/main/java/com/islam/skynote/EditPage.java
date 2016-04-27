package com.islam.skynote;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.islam.skynote.db.NoteDb;
import com.islam.skynote.utils.noteObj;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by islam  .
 */

public class EditPage extends AppCompatActivity {

    protected Button btnAnnuler , btnEnregistrer , btnDelete;
    protected Boolean modeCreate;

    protected EditText editTitre , editDescription ;
    protected TextView editDate;
    protected CheckBox editRappel;

    protected NoteDb _dateBase ;

    protected DatePickerDialog fromDatePickerDialog;
    protected TimePickerDialog fromTimePickerDialog;

    protected SimpleDateFormat dateFormatter;
    protected  int _ID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

        btnAnnuler = (Button)findViewById(R.id.btnAnnuler);
        btnEnregistrer = (Button)findViewById(R.id.btnEnregistrer);
        btnDelete = (Button)findViewById(R.id.btnDelete);


        editTitre = (EditText)findViewById(R.id.editTitre);
        editDescription = (EditText)findViewById(R.id.editDescription);
        editDate = (TextView)findViewById(R.id.editDate);

        editRappel = (CheckBox)findViewById(R.id.editRappel);

        // A ce que qu'il sagit d'une creation ou d'une modification
        Intent intent = getIntent();
        modeCreate = intent.getBooleanExtra("CREATE",true);

        editRappel.setChecked(false);

        _dateBase = new NoteDb(this);

        if ( modeCreate)
        {
            // le boutton suprimer et invisible
            btnDelete.setVisibility(Button.INVISIBLE);
            btnDelete.setEnabled(false);
        }else
        {
            btnDelete.setVisibility(Button.VISIBLE);
            btnDelete.setEnabled(true);
            // On remplit les champs
            _ID= intent.getIntExtra("ID",0);
            noteObj no = _dateBase.GetOne(_ID);
            if ( no != null )
            {
                editTitre.setText(no.Titre);
                editDescription.setText(no.Description);

                if ( no.Notif_at > 0)
                {
                    editRappel.setChecked(true);
                    Date dt = new Date();
                    dt.setTime(no.Notif_at*1000);

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    editDate.setText(df.format(dt));
                }
            }else
            {
                modeCreate = true;
            }
        }

        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( modeCreate)
                    InsertionDansBD();
                else
                    Updatedb();

                setResult(RESULT_OK);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dateBase.DeleteOne(_ID);
                setResult(RESULT_OK);
                finish();
            }
        });

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDateAndTime();
            }
        });
    }


    protected void SetDateAndTime()
    {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editDate.setText(dateFormatter.format(newDate.getTime()));
                SetTime();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.show();
    }

    protected void SetTime()
    {
        Calendar newCalendar = Calendar.getInstance();
        fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editDate.setText(editDate.getText().toString()+ " " + hourOfDay+ ":"+minute);
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true);

        fromTimePickerDialog.show();
    }

    protected void InsertionDansBD()
    {
        String _titre = editTitre.getText().toString(),
                _description = editDescription.getText().toString();
        long date = 0;
        if ( editRappel.isChecked() )
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date1 = dateFormat.parse(editDate.getText().toString());
                date = (date1.getTime()/1000);

            } catch (ParseException e) {
                Log.e("RIADHEUR",e.getMessage());
                date = 0;
            }
        }
        Log.d("RIAD","On va enregistrer " + _titre);
        _dateBase.Insertion(_titre,_description,editRappel.isChecked(),date);

    }


    protected void Updatedb()
    {
        String _titre = editTitre.getText().toString(),
                _description = editDescription.getText().toString();
        long date = 0;
        if ( editRappel.isChecked() )
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date1 = dateFormat.parse(editDate.getText().toString());
                date = (date1.getTime()/1000);

            } catch (ParseException e) {
                Log.e("RIADHEUR",e.getMessage());
                date = 0;
            }
        }

        _dateBase.Update(_ID,_titre,_description,editRappel.isChecked(),date);
    }

}
