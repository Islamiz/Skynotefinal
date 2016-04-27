package com.islam.skynote;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.islam.skynote.db.NoteDb;
import com.islam.skynote.utils.noteAdapter;
import com.islam.skynote.utils.noteNotif;
import com.islam.skynote.utils.noteObj;

/**
 * Created by islam  .
 */

public class Accueil extends AppCompatActivity {

    private static final int EDIT_PAGE_CODE = 814;
    private static final int EDIT_PAGE_NOTE = 824;


    protected NoteDb _datebase;
    protected ListView _listNote;
    protected noteAdapter _listAdapter;
    protected noteNotif _noteNofif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _datebase = new NoteDb(this);

        _noteNofif = new noteNotif(this,_datebase);
        _noteNofif.checkNotif();
        setContentView(R.layout.activity_accueil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _listNote = (ListView)findViewById(R.id.listNote);
        _listAdapter = new noteAdapter(this,R.layout.itemlist,_datebase.getAll(NoteDb.SORT_LAST_INSERT));
        _listNote.setAdapter(_listAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accueil.this, EditPage.class);
                intent.putExtra("CREATE",true);
                // On ouvert la page d'edition avec le mode Create
                startActivityForResult(intent,EDIT_PAGE_CODE);
            }
        });

        _listNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                noteObj no = _listAdapter.getItem(position);

                Intent intent = new Intent(Accueil.this, EditPage.class);
                intent.putExtra("CREATE",false);
                intent.putExtra("ID",no.ID);
                // On ouvert la page d'edition avec le mode Create
                startActivityForResult(intent,EDIT_PAGE_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_accueil, menu);
        return true;
    }


    // classe qui gère a assimlier la fonction deleteALL et setting au bouton dans le menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_deleteALL)
        {
            _datebase.DeleteALl();
            RefrecheList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent a)
    {
        if ( requestCode == EDIT_PAGE_CODE)
        {
            if ( resultCode == RESULT_OK )
            {
                    RefrecheList();
            }
        }
    }

// rafraichir la liste afin de ne paq quitter l'aplication a chaque foi
    protected void RefrecheList()
    {
        _listAdapter.clear();
        _listAdapter.addAll(_datebase.getAll(NoteDb.SORT_LAST_INSERT));
        _listAdapter.notifyDataSetChanged();
        _noteNofif.checkNotif();
    }
}
