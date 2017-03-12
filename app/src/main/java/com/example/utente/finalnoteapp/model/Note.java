package com.example.utente.finalnoteapp.model;

import com.example.utente.finalnoteapp.R;
import com.example.utente.finalnoteapp.utilities.Utilities;

import javax.sql.StatementEvent;

/**
 * Created by ${Francesco} on 11/03/2017.
 */

public class Note {
    private static int counter = 0;
    private int id;
    private String title = "";
    private String body = "";
    private String creationDate;
    private String editDate;
    private String dueDate = "";
    private int color;
    private boolean isSpecial;
    private State state;

    public Note(){
        this.id = counter;
        counter++;
        this.creationDate = Utilities.getCurrentDate();
        this.editDate = this.creationDate;
        this.state = State.TODO;
        this.color = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getEditDate() {
        return editDate;
    }

    public void setEditDate(String editDate) {
        this.editDate = editDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", editDate='" + editDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", color=" + color +
                ", isSpecial=" + isSpecial +
                ", state=" + state +
                '}';
    }
}
