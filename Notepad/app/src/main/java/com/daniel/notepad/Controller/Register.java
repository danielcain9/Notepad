package com.daniel.notepad.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.daniel.notepad.R;

public class Register extends AppCompatActivity {

    public String pin;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView password;

    /** Note: The retrieval and storing of the pin is not secure. Do not use a pin you use elsewhere. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        View registerButton = findViewById(R.id.registerSubmit); // Get register button.

        password = findViewById(R.id.passwordFieldRegister);     // Get password field.

        // Bring up keypad
        if (password.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        password.requestFocus();

        sharedPreferences = this.getSharedPreferences("login", this.MODE_PRIVATE); // This is where the password will be kept.
        editor = sharedPreferences.edit();                                                // Used to save password.

        /* Set function for when register button is clicked. */
        registerButton.setOnClickListener(e -> {
            pin = password.getText().toString(); // Get password.
            password.setText("");
            editor.putString("password", pin).apply(); // Save password in shared preferences.

            /* Start activity which displays the notes. */
            Intent intent = new Intent(this, NoteHandler.class);
            startActivity(intent);
            finish();
        });

    }
}