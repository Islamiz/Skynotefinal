package com.islam.skynote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.islam.skynote.utils.noteObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by islam  .
 */
public class NoteDb extends SQLiteOpenHelper {
    public static final int SORT_LAST_INSERT = 0;
    public static final int SORT_LAST_MODIF = 1;


    public static final int DEBUG_MODE = 1;

    //nom de la base de bdd et sa version
    protected static String DATABASE_NAME = "notes.db";
    protected static int DATABASE_VERSION = 2;


    public static String TABLE_NOTES = "notes";
    public static String NOTE_ID = "_id";
    public static String NOTE_TITLE = "title";
    public static String NOTE_DESCR = "description";
    public static String NOTE_CREATE = "create_at";
    public static String NOTE_MODIF = "modif_at";
    public static String NOTE_NOTIF = "notif_at";
    public static String NOTE_STATUS = "status";

    public static final String[] ALL_COLUMNS =
            {NOTE_ID, NOTE_TITLE, NOTE_DESCR,NOTE_CREATE,NOTE_MODIF,NOTE_NOTIF,NOTE_STATUS};

    public NoteDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //création d'une table
    @Override
    public void onCreate(SQLiteDatabase db) {

            String strCreate = "CREATE TABLE "+ TABLE_NOTES + " ( "
                    + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + NOTE_TITLE + " TEXT,"
                    + NOTE_DESCR + " TEXT,"
                    + NOTE_CREATE+ " INTEGER,"
                    + NOTE_MODIF + " INTEGER,"
                    + NOTE_NOTIF + " INTEGER NULL,"
                    + NOTE_STATUS + " INTEGER )";

        db.execSQL(strCreate);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String strDrop = "DROP TABLE IF EXISTS " + TABLE_NOTES;
        db.execSQL(strDrop);
        onCreate(db);
    }


    // insersion d'une note dans la base de donné
    public void Insertion(String titre , String Description, Boolean Rappel , long date)
    {
        ContentValues values = new ContentValues();
        values.put(NOTE_TITLE, titre);
        values.put(NOTE_DESCR, Description);
        values.put(NOTE_CREATE, System.currentTimeMillis() / 1000);
        values.put(NOTE_MODIF, System.currentTimeMillis() / 1000);

        if ( Rappel )
            values.put(NOTE_NOTIF,date);

        long insert = getWritableDatabase().insert(TABLE_NOTES, null, values);

    }

    //update d'une note
    public void Update(int ID, String titre , String Descr , Boolean Rappel , long date )
    {
        ContentValues values = new ContentValues();
        values.put(NOTE_TITLE, titre);
        values.put(NOTE_DESCR, Descr);
        values.put(NOTE_NOTIF, date);
        values.put(NOTE_MODIF, System.currentTimeMillis() / 1000);


        getWritableDatabase().update(TABLE_NOTES,values,NOTE_ID + " = "+ ID,null);
    }


    //delete d'une note en particulier
    public void DeleteOne(int ID)
    {
        getWritableDatabase().delete(TABLE_NOTES,NOTE_ID + "="+ID,null);
    }


    public noteObj GetOne(int id)
    {
        Cursor _readAll = getReadableDatabase().query(TABLE_NOTES,ALL_COLUMNS,NOTE_ID + " = " + id,null,null,null,null);
        ArrayList<noteObj> no = new ArrayList<>();

        while(_readAll.moveToNext()) {

            noteObj p = new noteObj();
            p.Titre = _readAll.getString(_readAll.getColumnIndex(NOTE_TITLE));
            p.Description = _readAll.getString(_readAll.getColumnIndex(NOTE_DESCR));
            p.ID = _readAll.getInt(_readAll.getColumnIndex(NOTE_ID));
            p.Notif_at = _readAll.getLong(_readAll.getColumnIndex(NOTE_NOTIF));

            return p;
        }
        return null;
    }

    public List<noteObj> getAll(int sort)
    {
        String strsort = "";
        switch (sort)
        {
            case  SORT_LAST_INSERT:
                strsort = NOTE_CREATE + " DESC";
                break;
            case  SORT_LAST_MODIF:
                strsort = NOTE_MODIF + " DESC";
                break;
        }
        Cursor _readAll = getReadableDatabase().query(TABLE_NOTES,ALL_COLUMNS,null,null,null,null,strsort);
        ArrayList<noteObj> no = new ArrayList<>();

        Debug("-----------");
        while(_readAll.moveToNext())
        {
            noteObj p = new noteObj();
            p.Titre = _readAll.getString(_readAll.getColumnIndex(NOTE_TITLE));
            p.Description = _readAll.getString(_readAll.getColumnIndex(NOTE_DESCR));
            p.ID = _readAll.getInt(_readAll.getColumnIndex(NOTE_ID));
            p.Notif_at = _readAll.getLong(_readAll.getColumnIndex(NOTE_NOTIF));

            int h = _readAll.getInt(_readAll.getColumnIndex(NOTE_CREATE));
            long notfi = _readAll.getLong(_readAll.getColumnIndex(NOTE_NOTIF));
            Debug(p.Titre+" -- " + h + " -- " + notfi);

            no.add(p);
        }
        Debug("-----------");
        return no;
    }


    public List<noteObj> GetWhere(String where)
    {
        Cursor _readAll = getReadableDatabase().query(TABLE_NOTES,ALL_COLUMNS,where,null,null,null,null);
        ArrayList<noteObj> no = new ArrayList<>();

        Debug("-----------");
        while(_readAll.moveToNext())
        {
            noteObj p = new noteObj();
            p.Titre = _readAll.getString(_readAll.getColumnIndex(NOTE_TITLE));
            p.Description = _readAll.getString(_readAll.getColumnIndex(NOTE_DESCR));
            p.Notif_at = _readAll.getLong(_readAll.getColumnIndex(NOTE_NOTIF));
            p.ID = _readAll.getInt(_readAll.getColumnIndex(NOTE_ID));

            int h = _readAll.getInt(_readAll.getColumnIndex(NOTE_CREATE));
            long notfi = _readAll.getLong(_readAll.getColumnIndex(NOTE_NOTIF));
            Debug(p.Titre+" -- " + h + " -- " +  notfi );

            no.add(p);
        }
        Debug("-----------");
        return no;
    }

    public void DeleteALl()
    {
        getWritableDatabase().delete(TABLE_NOTES,null,null);
    }

    public void Debug(String msg)
    {
        if (DEBUG_MODE==1 )
        {
            Log.d("DBRIAD",msg);
        }
    }
}
