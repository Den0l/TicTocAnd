package com.example.themes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTheme();
        setContentView(R.layout.activity_main);

        Button twoPlayersButton = findViewById(R.id.twoPlayersButton);
        Button playWithBotButton = findViewById(R.id.playWithBotButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        twoPlayersButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("isBot", false);
            startActivity(intent);
        });

        playWithBotButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("isBot", true);
            startActivity(intent);
        });

        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
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