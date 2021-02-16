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

public class Login extends AppCompatActivity {

    public String pin;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView password;

    /**
     * Note: The retrieval and storing of the pin is not secure. Do not use a pin you use elsewhere.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View loginButton = findViewById(R.id.loginSubmit); // Get login button.
        password = findViewById(R.id.passwordFieldLogin);  // Get password field.


        sharedPreferences = this.getSharedPreferences("login", this.MODE_PRIVATE); // Get password

        // Bring up keypad
        if (password.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        password.requestFocus();


        /* Set function for when login button is clicked. */
        loginButton.setOnClickListener(e -> {
            pin = password.getText().toString(); // Get password from password field.

            /* Call login method, pass pin inputted by user. */
            if (login(pin)) {  // If password is correct. Start activity with list of notes.
                Intent intent = new Intent(this, NoteHandler.class);
                startActivity(intent);
                finish();
            } else { // If password is incorrect.

                /* Create and show new toast(pop up message) saying: Incorrect pin. */
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast_login,
                        (ViewGroup) findViewById(R.id.custom_toast_container));
                TextView text = (TextView) layout.findViewById(R.id.toastIncorrectPin);
                text.setText("Incorrect Pin");
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, -250);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                password.setText(""); // Make password field empty.

                // Bring up keypad
                if (password.requestFocus()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                password.requestFocus();
            }

        });

    }

    private boolean login(String pin) {
        /* If the pin matches the stored pin return true, else false. */
        if (sharedPreferences.getString("password", "").equals(pin)) {
            return true;
        }
        return false;
    }
}