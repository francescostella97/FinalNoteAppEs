package com.example.utente.finalnoteapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.ColorRes;

import com.example.utente.finalnoteapp.model.Note;
import com.example.utente.finalnoteapp.model.State;

import java.util.ArrayList;

/**
 * Created by ${Francesco} on 11/03/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FinalNoteApp.db";
    private static final String NOTE_TABLE = "Note";
    private static final String NOTE_COLUMN_ID = "Id";
    private static final String NOTE_COLUMN_TITLE = "Title";
    private static final String NOTE_COLUMN_BODY = "Body";
    private static final String NOTE_COLUMN_CREATION = "Creation";
    private static final String NOTE_COLUMN_EDITED = "Edited";
    private static final String NOTE_COLUMN_DUE = "Due";
    private static final String NOTE_COLUMN_COLOR = "Color";
    private static final String NOTE_COLUMN_SPECIAL = "Special";
    private static final String NOTE_COLUMN_STATE = "State";

    public static int version = 1;

    public DBHandler(Context context){
        super(context,DATABASE_NAME,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + NOTE_TABLE + "("+
                NOTE_COLUMN_ID + " integer primary key, " +
                NOTE_COLUMN_TITLE + " text, " +
                NOTE_COLUMN_BODY + " text, "+
                NOTE_COLUMN_CREATION + " text, " +
                NOTE_COLUMN_EDITED + " text, " +
                NOTE_COLUMN_DUE + " text, "+
                NOTE_COLUMN_COLOR + " integer, " +
                NOTE_COLUMN_SPECIAL + " integer, "+
                NOTE_COLUMN_STATE + " text) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS note");
        onCreate(db);
    }
    public boolean insertNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_TITLE,note.getTitle());
        values.put(NOTE_COLUMN_BODY,note.getBody());
        values.put(NOTE_COLUMN_CREATION,note.getCreationDate());
        values.put(NOTE_COLUMN_EDITED,note.getEditDate());
        values.put(NOTE_COLUMN_DUE,note.getDueDate());
        values.put(NOTE_COLUMN_COLOR,note.getColor());
        boolean special = note.isSpecial();
        values.put(NOTE_COLUMN_SPECIAL,(special == true)? 1 : 0);
        State state = note.getState();
        values.put(NOTE_COLUMN_STATE, (state == State.TODO) ? State.TODO.name() : State.DONE.name());
        long id = db.insert(NOTE_TABLE,null,values);
        db.close();
        note.setId((int)id);
        return true;
    }
    public boolean deleteNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTE_TABLE, "id = ? ",new String[]{String.valueOf(note.getId())});
        db.close();
        return true;
    }
    public boolean updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_TITLE,note.getTitle());
        values.put(NOTE_COLUMN_BODY,note.getBody());
        values.put(NOTE_COLUMN_CREATION,note.getCreationDate());
        values.put(NOTE_COLUMN_EDITED,note.getEditDate());
        values.put(NOTE_COLUMN_DUE,note.getDueDate());
        values.put(NOTE_COLUMN_COLOR,note.getColor());
        boolean special = note.isSpecial();
        values.put(NOTE_COLUMN_SPECIAL,(special == true)? 1 : 0);
        State state = note.getState();
        values.put(NOTE_COLUMN_STATE, (state == State.TODO) ? State.TODO.name() : State.DONE.name());
        db.update(NOTE_TABLE,values,"id = ? ", new String[]{String.valueOf(note.getId())});
        db.close();
        return true;
    }
    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "+NOTE_TABLE, null );
        res.moveToFirst();
        while (!res.isAfterLast()){
            Note note = new Note();
            note.setId(res.getInt(res.getColumnIndex(NOTE_COLUMN_ID)));
            note.setTitle(res.getString(res.getColumnIndex(NOTE_COLUMN_TITLE)));
            note.setBody(res.getString(res.getColumnIndex(NOTE_COLUMN_BODY)));
            note.setCreationDate(res.getString(res.getColumnIndex(NOTE_COLUMN_CREATION)));
            note.setEditDate(res.getString(res.getColumnIndex(NOTE_COLUMN_EDITED)));
            note.setDueDate(res.getString(res.getColumnIndex(NOTE_COLUMN_DUE)));
            note.setColor(res.getInt(res.getColumnIndex(NOTE_COLUMN_COLOR)));
            int special = res.getInt(res.getColumnIndex(NOTE_COLUMN_SPECIAL));
            note.setSpecial((special==1)? true:false);
            String state = res.getString(res.getColumnIndex(NOTE_COLUMN_STATE));
            note.setState(State.valueOf(state.toUpperCase()));
            if(note.isSpecial())
                notes.add(0,note);
            else
                notes.add(note);
            res.moveToNext();
            System.out.println(note);
        }
        db.close();
        return notes;
    }
}
