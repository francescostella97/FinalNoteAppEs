package com.example.utente.finalnoteapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.utente.finalnoteapp.R;
import com.example.utente.finalnoteapp.adapters.NoteAdapter;
import com.example.utente.finalnoteapp.db.DBHandler;
import com.example.utente.finalnoteapp.model.Note;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by ${Francesco} on 11/03/2017.
 */

public class MainActivity extends AppCompatActivity{
    //declaring recycler view items
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    RecyclerView.LayoutManager layoutManager;

    //declaring data handlers
    DBHandler db;

    Toolbar toolbar;

    //declaring UI controls
    FloatingActionButton fab;

    //declaring private constants
    private String KEY_LAYOUT_TYPE = "layout";
    private final int STRAGGERED = 1;
    private final int LINEAR  =0;

    public static int currentPosition;
    //campo per tipo di layout
    private int layoutType = STRAGGERED;
    public int getLayoutType() {
        return layoutType;
    }
    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }
    public RecyclerView.LayoutManager getLayoutManager(){
        SharedPreferences layoutPref = getSharedPreferences(getString(R.string.preferred_layout),Context.MODE_PRIVATE);
        int type = layoutPref.getInt(KEY_LAYOUT_TYPE,-1);
        //impostazione layout delle sharedPreferences
        if (type == STRAGGERED){
            setLayoutType(STRAGGERED);
            return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        else if(type == LINEAR){
            setLayoutType(LINEAR);
            return new LinearLayoutManager(this);
        }
        return new LinearLayoutManager(this);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.context_delete:
                createActionDeleteIntent(); break;
            case R.id.context_open:
                createActionEditIntent(); break;

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        //setta icona in base al layout delle sharedPreferences
        if(getLayoutType() == STRAGGERED)
            toolbar.getMenu().findItem(R.id.menu_layout_type).setIcon(R.drawable.ic_view_list_white_24dp);
        else if(getLayoutType() == LINEAR)
            toolbar.getMenu().findItem(R.id.menu_layout_type).setIcon(R.drawable.ic_view_quilt_white_24dp);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //aggiorna icona e layout in base al layout delle sharedPreferences
        if(id == R.id.menu_layout_type){
            if(getLayoutType() == STRAGGERED){
                setLayoutType(LINEAR);
                item.setIcon(R.drawable.ic_view_quilt_white_24dp);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            else if(getLayoutType() == LINEAR){
                item.setIcon(R.drawable.ic_view_list_white_24dp);
                setLayoutType(STRAGGERED);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            //action adding
            if(resultCode == RESULT_OK){
                boolean isSpecial = false;
                int position = Integer.valueOf(data.getStringExtra("position"));
                String title = data.getStringExtra("title");
                String body = data.getStringExtra("body");

                Note note = new Note();
                note.setTitle(title);
                note.setBody(body);
                if(isSpecial) noteAdapter.insertAtTop(note);
                else noteAdapter.insertNote(note);
                db.insertNote(note);
                recyclerView.scrollToPosition(0);

            }
        }
        else if(requestCode == 2){

            if(resultCode == RESULT_OK){
                if(data.getFlags() == 200){
                    //action editing
                    boolean isSpecial = false;
                    int position = Integer.valueOf(data.getStringExtra("position"));
                    String title = data.getStringExtra("title");
                    String body = data.getStringExtra("body");
                    Note note = noteAdapter.getNote(position);
                    note.setTitle(title);
                    note.setBody(body);
                    db.updateNote(note);
                    if(isSpecial) {
                        noteAdapter.deleteNote(position);
                        noteAdapter.insertAtTop(note);
                        recyclerView.scrollToPosition(0);
                    }
                    noteAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(position);
                    System.out.println("Edited note \n"+note);
                }
                else{
                    //action deleting
                    System.out.println("deleting noew....................");
                    int position = Integer.valueOf(data.getStringExtra("position"));
                    currentPosition = position;
                    createActionDeleteIntent();

                }
            }

        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar--------------------------------------------
        toolbar = (Toolbar)findViewById(R.id.toolbar_layout);
        toolbar.setTitle(R.string.app_name);
        ViewCompat.setElevation(toolbar,0);
        setSupportActionBar(toolbar);
        //---------------------------------------------------
        //data handlers--------------------------
        db = new DBHandler(this);
        //recyler view items-----------------------------------
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        layoutManager = getLayoutManager();
        noteAdapter = new NoteAdapter(this);
        noteAdapter.setDataSet(db.getAllNotes());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter);
        registerForContextMenu(recyclerView);
        //-------------------------------------------------------
        //UI control
        fab = (FloatingActionButton) findViewById(R.id.add_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActionInsertIntent();
            }
        });
    }
    @Override
    public void onStop() {
        //salvataggio sharedPreferences
        SharedPreferences layoutPref = getSharedPreferences(getString(R.string.preferred_layout), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = layoutPref.edit();
        editor.putInt(KEY_LAYOUT_TYPE,getLayoutType());
        editor.commit();
        super.onStop();
    }
    //methods that create different types of intents
    public void createActionInsertIntent(){
        Intent intent = new Intent(MainActivity.this,NoteActivity.class);
        startActivityForResult(intent,1);
    }
    public void createActionDeleteIntent(){
        db.deleteNote(noteAdapter.getNote(currentPosition));
        noteAdapter.deleteNote(currentPosition);
    }
    public void createActionEditIntent() {
        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        intent.putExtra("position", String.valueOf(currentPosition));
        intent.putExtra("title", noteAdapter.getNote(currentPosition).getTitle());
        intent.putExtra("body", noteAdapter.getNote(currentPosition).getBody());
        startActivityForResult(intent, 2);
    }
}
