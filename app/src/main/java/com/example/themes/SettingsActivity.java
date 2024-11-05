package com.example.themes;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.app.ActivityCompat.recreate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTheme();
        setContentView(R.layout.activity_settings);

        themeSwitch = findViewById(R.id.themeSwitch);

        SharedPreferences preferences = getSharedPreferences("TicTacToePrefs", MODE_PRIVATE);
        themeSwitch.setChecked(preferences.getBoolean("darkTheme", false));

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("darkTheme", isChecked);
            editor.apply();
            recreate();
        });
    }

    private void loadTheme() {
        SharedPreferences preferences = getSharedPreferences("TicTacToePrefs", MODE_PRIVATE);
        boolean darkTheme = preferences.getBoolean("darkTheme", false);
        if (darkTheme) {
            setTheme(R.style.Base_Theme_Themes);
        } else {
            setTheme(R.style.Theme_Themes);
        }
    }

}
