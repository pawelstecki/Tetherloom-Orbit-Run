package com.floppytap.tetherloom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.floppytap.tetherloom.data.PreferencesManager;
import com.floppytap.tetherloom.ui.CreditsActivity;
import com.floppytap.tetherloom.ui.HowToPlayActivity;
import com.floppytap.tetherloom.ui.ProgressActivity;
import com.floppytap.tetherloom.ui.SettingsActivity;
import com.floppytap.tetherloom.util.LocaleHelper;

/**
 * Main menu screen. Provides navigation to every Phase 1 destination.
 * Daily Pattern and Practice are declared in the specification as full modes
 * arriving with Phase 2 systems (procedural variety, seeded determinism, and a
 * dedicated practice tuning); Phase 1 shows a clear, honest "coming soon" message
 * rather than a partially implemented mode.
 */
public class MainActivity extends AppCompatActivity {

    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferencesManager = new PreferencesManager(this);
        LocaleHelper.applyLanguage(this, preferencesManager.getLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textHighScoreValue = findViewById(R.id.textHighScoreValue);
        textHighScoreValue.setText(String.valueOf(preferencesManager.getHighScore()));

        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnDaily = findViewById(R.id.btnDaily);
        Button btnPractice = findViewById(R.id.btnPractice);
        Button btnProgress = findViewById(R.id.btnProgress);
        Button btnHowToPlay = findViewById(R.id.btnHowToPlay);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnCredits = findViewById(R.id.btnCredits);

        btnPlay.setOnClickListener(v -> startActivity(new Intent(this, GameActivity.class)));

        btnDaily.setOnClickListener(v ->
                Toast.makeText(this, R.string.menu_coming_soon, Toast.LENGTH_SHORT).show());

        btnPractice.setOnClickListener(v ->
                Toast.makeText(this, R.string.menu_coming_soon, Toast.LENGTH_SHORT).show());

        btnProgress.setOnClickListener(v -> startActivity(new Intent(this, ProgressActivity.class)));

        btnHowToPlay.setOnClickListener(v -> startActivity(new Intent(this, HowToPlayActivity.class)));

        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        btnCredits.setOnClickListener(v -> startActivity(new Intent(this, CreditsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textHighScoreValue = findViewById(R.id.textHighScoreValue);
        if (textHighScoreValue != null) {
            textHighScoreValue.setText(String.valueOf(preferencesManager.getHighScore()));
        }
    }
}
