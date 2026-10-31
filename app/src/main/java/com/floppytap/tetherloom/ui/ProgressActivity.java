package com.floppytap.tetherloom.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.floppytap.tetherloom.R;
import com.floppytap.tetherloom.data.PreferencesManager;
import com.floppytap.tetherloom.util.LocaleHelper;
import com.floppytap.tetherloom.util.SystemBarsHelper;

/**
 * Displays locally persisted progress: high score and best combo.
 * Missions, achievements, and cosmetics are planned for a later phase.
 */
public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesManager preferencesManager = new PreferencesManager(this);
        LocaleHelper.applyLanguage(this, preferencesManager.getLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        SystemBarsHelper.applySystemBarInsets(this);

        TextView textProgressHighScore = findViewById(R.id.textProgressHighScore);
        TextView textProgressBestCombo = findViewById(R.id.textProgressBestCombo);
        Button btnProgressBack = findViewById(R.id.btnProgressBack);

        textProgressHighScore.setText(String.valueOf(preferencesManager.getHighScore()));
        textProgressBestCombo.setText(String.valueOf(preferencesManager.getBestCombo()));

        btnProgressBack.setOnClickListener(v -> finish());
    }
}
