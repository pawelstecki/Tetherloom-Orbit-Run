package com.floppytap.tetherloom.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Applies the persisted language selection to the application's resources.
 * Must be called at the start of onCreate for every entry-point Activity so the
 * correct localized strings are resolved before layouts inflate.
 */
public final class LocaleHelper {

    private LocaleHelper() {
    }

    public static void applyLanguage(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
