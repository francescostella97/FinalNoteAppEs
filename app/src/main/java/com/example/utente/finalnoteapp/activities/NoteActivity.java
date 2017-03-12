package com.example.utente.finalnoteapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.utente.finalnoteapp.R;

import org.w3c.dom.Text;

/**
 * Created by ${Francesco} on 11/03/2017.
 */

public class NoteActivity extends AppCompatActivity {
    //type of action
    static int action;
    static final int ACTION_ADD = 1;
    static final int ACTION_EDIT = 2;
    static final int DELETE_FLAG = 200;
    Toolbar toolbar;
    int position;
    //declaring UI controls
    TextView titleTv, bodyTv, dueDateTv, editDateTv, creationDateTv;

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(action==ACTION_ADD) createAddIntent();
            else if(action==ACTION_EDIT) createEditIntent();
            finish();
            return true;
        }

        return super.onKeyDown( keyCode, event );
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        action = ACTION_ADD;
        //toolbar--------------------------------------------
        toolbar = (Toolbar)findViewById(R.id.toolbar_layout);
        ViewCompat.setElevation(toolbar,0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //---------------------------------------------------
        //UI control
        titleTv = (TextView) findViewById(R.id.view_note_title);
        bodyTv = (TextView) findViewById(R.id.view_note_body);
        //----------------------------------------------------
        action = ACTION_ADD;
        //managing intent
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringExtra("title")!=null){
                //editing a note
                action = ACTION_EDIT;
                position = Integer.parseInt(intent.getStringExtra("position"));
                titleTv.setText(intent.getStringExtra("title"));
                bodyTv.setText(intent.getStringExtra("body"));
            }
        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(action == ACTION_ADD){
            inflater.inflate(R.menu.add_note_menu,menu);
        }
        else {
            inflater.inflate(R.menu.view_note_menu,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.view_note_delete:
                Intent intent = new Intent();
                intent.putExtra("position", String.valueOf(position));
                intent.addFlags(DELETE_FLAG);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.add_note_save: createAddIntent (); finish(); break;
            case R.id.view_note_save: createEditIntent(); finish(); break;
            case android.R.id.home :
                if(action==ACTION_ADD) createAddIntent();
                else if(action==ACTION_EDIT) createEditIntent();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createAddIntent(){
        Log.d("MAIN ACTIVITY ","going to add ");
        Intent intent = new Intent();
        intent.putExtra("position",String.valueOf(position));
        intent.putExtra("title",titleTv.getText().toString());
        intent.putExtra("body",bodyTv.getText().toString());
        setResult(RESULT_OK,intent);
    }
    public void createEditIntent(){
        Log.d("MAIN ACTIVITY ","going to edit ");

        Intent intent = new Intent();
        intent.putExtra("position",String.valueOf(position));
        intent.putExtra("title",titleTv.getText().toString());
        intent.putExtra("body",bodyTv.getText().toString());
        intent.setFlags(DELETE_FLAG);
        setResult(RESULT_OK,intent);
    }
}
