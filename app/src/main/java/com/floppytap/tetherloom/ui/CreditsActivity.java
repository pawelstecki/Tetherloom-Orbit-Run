package com.floppytap.tetherloom.ui;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.floppytap.tetherloom.R;
import com.floppytap.tetherloom.data.PreferencesManager;
import com.floppytap.tetherloom.util.LocaleHelper;
import com.floppytap.tetherloom.util.SystemBarsHelper;

/**
 * Displays FloppyTap studio branding and original-work acknowledgment.
 */
public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesManager preferencesManager = new PreferencesManager(this);
        LocaleHelper.applyLanguage(this, preferencesManager.getLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        SystemBarsHelper.applySystemBarInsets(this);

        Button btnCreditsBack = findViewById(R.id.btnCreditsBack);
        btnCreditsBack.setOnClickListener(v -> finish());
    }
}
