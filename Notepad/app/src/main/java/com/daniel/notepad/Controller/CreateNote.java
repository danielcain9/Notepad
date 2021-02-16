package com.daniel.notepad.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.daniel.notepad.R;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.time.Instant;


public class CreateNote extends AppCompatActivity {

    private String originalTitle; // Original title of note being edited.
    private String originalBody;  // Original body of note being edited.
    private String originalDate;  // Original date of note being edited.
    private String[] noteArray;   // Array to store put Note as extra for intent.
    private boolean editing = false; // True if editing note, else false.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        AndroidThreeTen.init(this);  // Initialise ThreeTen Android Backport.

        /*Check if editing a note, if so set editing to true and call loadNote.
         * Else, initialise new noteArray and set editing to false.*/
        if (this.getIntent().hasExtra("note")) {
            editing = true;
            loadNote();
        } else {
            noteArray = new String[3];
            editing = false;
        }
        findViewById(R.id.saveNote).setOnClickListener(e -> onSave()); // Set click listener on save button.
        findViewById(R.id.backCreateNote).setOnClickListener(e -> onBack()); // Set click listener on back button.
    }

    public void loadNote() {

        noteArray = new String[3]; // Initialise noteArray.

        noteArray = this.getIntent().getStringArrayExtra("note"); // Set noteArray equal to note being edited.

        /* Save details of current note in case back is clicked and changes need to be reverted. */
        originalTitle = noteArray[0];
        originalBody = noteArray[1];
        originalDate = noteArray[2];

        /* Find and set the title/body field to the note being edited's title/body. */
        EditText editText = findViewById(R.id.createTitle);
        editText.setText(originalTitle);
        editText = findViewById(R.id.createBody);
        editText.setText(originalBody);


    }

    public void onSave() {

        // Get title field and save as string
        TextInputEditText textInputEditText = (TextInputEditText) findViewById(R.id.createTitle);
        String title = textInputEditText.getEditableText().toString();

        // Get body field and save as string.
        textInputEditText = findViewById(R.id.createBody);
        String body = textInputEditText.getEditableText().toString();

        // Set title to Untitled if no title set.
        if (title == null || title.equals(" ") || title.equals("")) {
            title = "Untitled";
        }

        /* Get current instant.
         *  Create formatter of form: Day-Month-Year Time.
         *  Create and set string equal to the current date/time using the formatter. */
        org.threeten.bp.Instant instant = org.threeten.bp.Instant.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu kk:mm");
        String localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(dateTimeFormatter);

        Intent intent = new Intent(this, NoteHandler.class);

        // Avoid null pointer exceptions.
        if (body == null || body.equals("")) {
            body = " ";
        }

        // Store the note in noteArray.
        noteArray[0] = title;
        noteArray[1] = body;
        noteArray[2] = localDateTime;

        // If note is being edited, put extra so new activity knows.
        if (editing) {
            intent.putExtra("edited", editing);
        }

        Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();

        // Place edited note as extra for new activity to receive.
        intent.putExtra("note", noteArray);

        startActivity(intent);
        finish();
    }

    public void onBack() {

        Intent intent = new Intent(this, NoteHandler.class);

        if (editing) {

            // Set noteArray title/body/date to notes original title/body/date
            noteArray[0] = originalTitle;
            noteArray[1] = originalBody;
            noteArray[2] = originalDate;


            intent.putExtra("note", noteArray);  // Pass original note to activity
            intent.putExtra("edited", false); // Let new activity know note wasn't changed.

        }
        startActivity(intent);
        finish();
    }
}