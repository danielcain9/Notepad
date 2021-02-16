package com.daniel.notepad.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.daniel.notepad.Controller.CreateNote;
import com.daniel.notepad.Model.Note;
import com.daniel.notepad.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;

import com.google.gson.reflect.TypeToken;

import org.threeten.bp.LocalDateTime;

import java.lang.reflect.Type;


public class NoteHandler extends AppCompatActivity implements RecyclerAdapter.ItemClickListener, RecyclerAdapter.ItemLongClickListener {

    private RecyclerAdapter adapter;
    private ArrayList<Note> noteList;
    private Map<Integer, Note> selectedNotes;  // List of notes user is selecting.
    private List<View> views;                  // List of the views user is selecting.
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String json;
    private Gson gson;
    private boolean selecting;                 // If the user is currently selecting notes.
    private boolean allSelected;               // If all notes have been selected.
    private int originalPosition;              // Edited note's original position.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler);    // Set recycler view layout.
        selectedNotes = new HashMap<>();      // Initialise empty HashMap of selected notes.
        views = new ArrayList<>();            // Initialise empty ArrayList of Views.
        setupNotes();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this, noteList);

        adapter.setClickListener(this);  // Set click listener for each note.
        adapter.setLongClickListener(this); // Set listener for a long click on each note.

        recyclerView.setAdapter(adapter);   // Set the RecyclerView's adapter.

        // No notes being selected so set to false.
        selecting = false;
        allSelected = false;

        // Set click function for add notes button.
        findViewById(R.id.addNote).setOnClickListener(e -> {
            onAdd();
        });

        // Set click function for back arrow.
        findViewById(R.id.backRecycler).setOnClickListener(e -> onBack());

        // Set click function for select all button.
        findViewById(R.id.selectAll).setOnClickListener(e ->
                onSelectAll());

        // Set click function for delete note(s) button.
        findViewById(R.id.delete).setOnClickListener(e -> onDelete());

        // If a note has been passed on this activity's creation.
        if (this.getIntent().hasExtra("note")) {
            boolean edited = false;

            // If the note has been edited, set edited to true.
            if (this.getIntent().getBooleanExtra("edited", false)) {
                edited = true;
            }
            onSave(this.getIntent().getStringArrayExtra("note"), edited);  // Pass the note and value of edited to onSave.
        }

    }


    /* When a note is clicked normally. */
    @Override
    public void onItemClick(View view, int position) {
        Note curNote = adapter.getNote(position); // Get the notes position in the recyclerview list.

        if (selecting == true) { // If notes are being selected.

            // If note is selected already. Deselect it.
            if (selectedNotes.containsKey(position)) {
                view.setBackgroundColor(Color.TRANSPARENT); // Set the notes view to its normal colour.
                selectedNotes.remove(position, curNote);
                views.remove(view);
                allSelected = false;
            }
            // Select note.
            else {
                view.setBackgroundColor(Color.BLACK); // Change background colour so it is clear note is selected.
                selectedNotes.put(position, curNote);
                views.add(view);
            }
        } else { // Not selecting.
            onEdit(curNote);             // Edit note.
            originalPosition = position; // Save original position in case note is not changed.
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Note curNote = adapter.getNote(position);

        // Make select all, delete and back button visible.
        findViewById(R.id.delete).setVisibility(View.VISIBLE);
        findViewById(R.id.selectAll).setVisibility(View.VISIBLE);
        findViewById(R.id.backRecycler).setVisibility(View.VISIBLE);

        selectedNotes.put(position, curNote);
        views.add(view);

        view.setBackgroundColor(Color.BLACK); // Set the notes view to black so it is clear note is selected.
        selecting = true;                     // Notes are now being selected.
    }

    public void onBack() {
        /* Deselect all notes. */
        selectedNotes.clear();
        for (View view : views
        ) {
            view.setBackgroundColor(Color.TRANSPARENT);
        }
        views.clear();
        selecting = false;
        adapter.setAllSelected(false);

        // Make select all, delete and back button invisible.
        findViewById(R.id.backRecycler).setVisibility(View.INVISIBLE);
        findViewById(R.id.delete).setVisibility(View.INVISIBLE);
        findViewById(R.id.selectAll).setVisibility(View.INVISIBLE);
    }

    private void onSelectAll() {
        if (allSelected) { // deselect all.
            allSelected = false;
        } else {          // select all.
            allSelected = true;
        }

        // deselect all.
        if (allSelected) {
            selectedNotes.clear();
            views.clear();
        }

        adapter.setAllSelected(true);
        adapter.notifyDataSetChanged(); // Update recyclerview appearance.

    }

    private void onDelete() {

        // Remove selected notes.
        for (Map.Entry<Integer, Note> object : selectedNotes.entrySet()) {
            Note note = (Note) object.getValue();
            noteList.remove(note);
        }

        // Update and save list of notes.
        editor = sharedPreferences.edit();
        json = gson.toJson(noteList);
        editor.putString("notes", json);
        editor.commit();

        // Notify recycler view.
        adapter.notifyDataSetChanged();
        onBack();
    }


    private void setupNotes() {

        // Get all the notes.
        sharedPreferences = this.getSharedPreferences("notes", this.MODE_PRIVATE);
        gson = new Gson();
        json = sharedPreferences.getString("notes", "");
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        noteList = gson.fromJson(json, type);
        if (noteList == null) {
            noteList = new ArrayList<>();
        }
        sortList();

    }

    private void sortList() {
        // Sort notes by date edited. Latest to oldest.
        if (noteList != null) {
            if (noteList.size() > 1) {
                noteList.sort((o1, o2) -> {
                    LocalDateTime localDateTime01 = o1.getLocalDateTime();
                    LocalDateTime localDateTime02 = o2.getLocalDateTime();
                    if (localDateTime01.isBefore(localDateTime02)) {
                        return 1;
                    } else if (localDateTime01.equals(localDateTime02)) {
                        return 0;
                    } else {
                        return -1;
                    }
                });
            }
        }

    }

    public void onAdd() {
        Intent intent = new Intent(this, CreateNote.class);
        startActivity(intent);
        finish();
    }

    public void onSave(String[] noteArray, boolean edited) {

        int insertIndex = 0;

        // If note not changed, place in original place.
        if (!edited) {
            insertIndex = originalPosition;
        }

        // Add note to list and save. Update recyclerview.
        Note note = new Note(noteArray[0], noteArray[1], noteArray[2]);
        noteList.add(insertIndex, note);
        editor = sharedPreferences.edit();
        json = gson.toJson(noteList);
        editor.putString("notes", json);
        editor.commit();
        adapter.notifyItemInserted(insertIndex);
    }

    public void onEdit(Note note) {
        String[] noteArray = new String[3];
        noteArray[0] = note.getTitle();
        noteArray[1] = note.getBody();
        noteArray[2] = note.getDate();
        Intent intent = new Intent(this, CreateNote.class);
        intent.putExtra("note", noteArray); // Pass content of note being edited.
        noteList.remove(note);                     // Delete note to avoid duplicates.
        editor = sharedPreferences.edit();

        json = gson.toJson(noteList);

        editor.putString("notes", json);
        editor.commit();

        adapter.notifyDataSetChanged();
        startActivity(intent);
        finish();
    }


}