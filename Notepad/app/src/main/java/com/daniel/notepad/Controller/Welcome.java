package com.daniel.notepad.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.daniel.notepad.R;

public class Welcome extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static int TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedPreferences = this.getSharedPreferences("login", this.MODE_PRIVATE);

        // Display welcome page for set duration.
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          // If no existing password go to registration page, else go to login page.
                                          if (sharedPreferences.getString("password", "").isEmpty()) {
                                              Intent intent = new Intent(Welcome.this, Register.class);
                                              startActivity(intent);
                                              finish();
                                          } else {
                                              Intent intent = new Intent(Welcome.this, Login.class);
                                              startActivity(intent);
                                              finish();
                                          }
                                      }
                                  },
                TIME_OUT);
    }
}