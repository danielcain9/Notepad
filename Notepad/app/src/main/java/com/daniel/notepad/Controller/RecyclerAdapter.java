package com.daniel.notepad.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.daniel.notepad.Model.Note;
import com.daniel.notepad.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Note> noteList;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;
    private ItemLongClickListener itemLongClickListener;
    private boolean allSelected = false;


    RecyclerAdapter(Context context, List<Note> data) {
        this.inflater = LayoutInflater.from(context);
        this.noteList = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);

        return new ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = noteList.get(position);
        String title = note.getTitle();
        holder.titleView.setText(title);
        holder.dateView.setText("Last edited: " + note.getDate());
        if (allSelected) {
            holder.row.callOnClick();
        }
    }


    // total number of rows
    @Override
    public int getItemCount() {
        if (noteList == null) {
            noteList = new ArrayList<>();
        }
        return noteList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView titleView;
        TextView dateView;
        View row;

        ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.noteTitle);
            dateView = itemView.findViewById(R.id.noteDateView);
            row = itemView.findViewById(R.id.idRow);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }


        @Override
        public boolean onLongClick(View v) {

            if (itemLongClickListener != null)
                itemLongClickListener.onItemLongClick(v, getAdapterPosition());

            return true;
        }

    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return noteList.get(id).getTitle();
    }

    Note getNote(int id) {
        return noteList.get(id);
    }

    public boolean isAllSelected() {
        return allSelected;
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface ItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setAllSelected(boolean state) {
        allSelected = state;
    }

}
