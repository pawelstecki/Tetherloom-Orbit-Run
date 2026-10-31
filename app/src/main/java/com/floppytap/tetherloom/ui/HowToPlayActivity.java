package com.floppytap.tetherloom.ui;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.floppytap.tetherloom.R;
import com.floppytap.tetherloom.data.PreferencesManager;
import com.floppytap.tetherloom.util.LocaleHelper;
import com.floppytap.tetherloom.util.SystemBarsHelper;

/**
 * Explains the orbit-and-launch control scheme using a small code-drawn diagram
 * plus short localized text.
 */
public class HowToPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesManager preferencesManager = new PreferencesManager(this);
        LocaleHelper.applyLanguage(this, preferencesManager.getLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        SystemBarsHelper.applySystemBarInsets(this);

        Button btnHtpBack = findViewById(R.id.btnHtpBack);
        btnHtpBack.setOnClickListener(v -> finish());
    }
}
