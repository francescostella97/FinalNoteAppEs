package com.example.utente.finalnoteapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.utente.finalnoteapp.R;
import com.example.utente.finalnoteapp.activities.MainActivity;
import com.example.utente.finalnoteapp.activities.NoteActivity;
import com.example.utente.finalnoteapp.model.Note;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by ${Francesco} on 11/03/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteVH> {
    Context context;
    public ArrayList<Note> dataSet = new ArrayList<>();

    public void setDataSet(ArrayList<Note> dataSet){
        this.dataSet = dataSet;
    }
    public void deleteNote(int position){
        dataSet.remove(position);
        notifyItemRemoved(position);
    }
    public Note getNote(int position){
        return dataSet.get(position);
    }
    public void insertAtTop(Note note){
        dataSet.add(0,note);
        notifyItemInserted(0);
    }
    public void insertNote(Note note){
        if(getItemCount()!=0) {
            dataSet.add(getItemCount(), note);
            notifyItemInserted(getItemCount());
        }
        else {
            dataSet.add(0, note);
            notifyItemInserted(0);
        }
    }
    public NoteAdapter(Context context){
        this.context = context;
    }
    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.note_layout,parent,false);
        return new NoteVH(view);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {
        //UI update--------------------------------------
        Note currentNote = dataSet.get(position);
        System.out.println(currentNote);
        holder.titleTv.setText(currentNote.getTitle());
        holder.bodyTv.setText(currentNote.getBody());
        holder.creationDateTv.setText(currentNote.getCreationDate());
        holder.editDateTv.setText(currentNote.getEditDate());
        holder.dueDateTv.setText(currentNote.getDueDate());

        holder.titleTv.getRootView().setBackgroundColor(currentNote.getColor());
        holder.titleTv.getRootView().getBackground().setAlpha(150);

        if(currentNote.isSpecial()) {
            holder.specialBtn.setVisibility(View.VISIBLE);
            holder.specialBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bookmark_white_24dp, 0, 0, 0);
            holder.specialBtn.setDrawingCacheBackgroundColor(Color.BLUE);
        }
        else
            holder.specialBtn.setVisibility(View.GONE);
    }

    public class NoteVH extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
        TextView titleTv, bodyTv, dueDateTv, editDateTv, creationDateTv;
        Button specialBtn;
        public NoteVH(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            //UI control---------------------------------------------------------------
            titleTv = (TextView) itemView.findViewById(R.id.note_title);
            bodyTv = (TextView) itemView.findViewById(R.id.note_body);
            dueDateTv = (TextView) itemView.findViewById(R.id.note_dueDate);
            editDateTv = (TextView) itemView.findViewById(R.id.note_editedDate);
            creationDateTv = (TextView) itemView.findViewById(R.id.note_creationDate);
            specialBtn = (Button) itemView.findViewById(R.id.note_specialbtn);
            //-------------------------------------------------------------------------
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(((MainActivity)v.getContext()), NoteActivity.class);
            intent.putExtra("position",String.valueOf(getAdapterPosition()));
            intent.putExtra("title",titleTv.getText().toString());
            intent.putExtra("body",bodyTv.getText().toString());
            ((MainActivity)itemView.getContext()).startActivityForResult(intent,2);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MainActivity.currentPosition = getAdapterPosition();
        }
    }
}
