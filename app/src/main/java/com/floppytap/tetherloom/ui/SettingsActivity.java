package com.floppytap.tetherloom.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

import com.floppytap.tetherloom.R;
import com.floppytap.tetherloom.data.PreferencesManager;
import com.floppytap.tetherloom.util.LocaleHelper;

/**
 * Settings screen with working toggles for audio, haptics, screen shake, reduced
 * effects, and language. All values are persisted immediately via PreferencesManager.
 */
public class SettingsActivity extends AppCompatActivity {

    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferencesManager = new PreferencesManager(this);
        LocaleHelper.applyLanguage(this, preferencesManager.getLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RadioGroup radioGroupLanguage = findViewById(R.id.radioGroupLanguage);
        RadioButton radioEn = findViewById(R.id.radioEn);
        RadioButton radioPl = findViewById(R.id.radioPl);
        Switch switchMusic = findViewById(R.id.switchMusic);
        Switch switchSfx = findViewById(R.id.switchSfx);
        Switch switchHaptics = findViewById(R.id.switchHaptics);
        Switch switchScreenShake = findViewById(R.id.switchScreenShake);
        Switch switchReducedEffects = findViewById(R.id.switchReducedEffects);
        Button btnSettingsBack = findViewById(R.id.btnSettingsBack);

        if (PreferencesManager.LANGUAGE_POLISH.equals(preferencesManager.getLanguage())) {
            radioPl.setChecked(true);
        } else {
            radioEn.setChecked(true);
        }

        switchMusic.setChecked(preferencesManager.isMusicEnabled());
        switchSfx.setChecked(preferencesManager.isSfxEnabled());
        switchHaptics.setChecked(preferencesManager.isHapticsEnabled());
        switchScreenShake.setChecked(preferencesManager.isScreenShakeEnabled());
        switchReducedEffects.setChecked(preferencesManager.isReducedEffectsEnabled());

        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            String newLanguage = checkedId == R.id.radioPl
                    ? PreferencesManager.LANGUAGE_POLISH
                    : PreferencesManager.LANGUAGE_ENGLISH;
            preferencesManager.setLanguage(newLanguage);
            LocaleHelper.applyLanguage(this, newLanguage);
            recreate();
        });

        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferencesManager.setMusicEnabled(isChecked));

        switchSfx.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferencesManager.setSfxEnabled(isChecked));

        switchHaptics.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferencesManager.setHapticsEnabled(isChecked));

        switchScreenShake.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferencesManager.setScreenShakeEnabled(isChecked));

        switchReducedEffects.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferencesManager.setReducedEffectsEnabled(isChecked));

        btnSettingsBack.setOnClickListener(v -> finish());
    }
}
