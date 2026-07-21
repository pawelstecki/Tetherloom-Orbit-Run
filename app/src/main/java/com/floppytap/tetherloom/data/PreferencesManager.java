package com.floppytap.tetherloom.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Wraps SharedPreferences access for all persisted settings and simple progress values.
 * All keys and defaults are centralized here so no other class needs to know about
 * the underlying storage mechanism.
 */
public class PreferencesManager {

    private static final String PREFS_NAME = "tetherloom_prefs";

    private static final String KEY_LANGUAGE = "key_language";
    private static final String KEY_MUSIC_ENABLED = "key_music_enabled";
    private static final String KEY_SFX_ENABLED = "key_sfx_enabled";
    private static final String KEY_HAPTICS_ENABLED = "key_haptics_enabled";
    private static final String KEY_SCREEN_SHAKE_ENABLED = "key_screen_shake_enabled";
    private static final String KEY_REDUCED_EFFECTS_ENABLED = "key_reduced_effects_enabled";
    private static final String KEY_HIGH_SCORE = "key_high_score";
    private static final String KEY_BEST_COMBO = "key_best_combo";

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_POLISH = "pl";

    private final SharedPreferences prefs;

    public PreferencesManager(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getLanguage() {
        String stored = prefs.getString(KEY_LANGUAGE, LANGUAGE_ENGLISH);
        if (!LANGUAGE_ENGLISH.equals(stored) && !LANGUAGE_POLISH.equals(stored)) {
            return LANGUAGE_ENGLISH;
        }
        return stored;
    }

    public void setLanguage(String languageCode) {
        String safeValue = LANGUAGE_POLISH.equals(languageCode) ? LANGUAGE_POLISH : LANGUAGE_ENGLISH;
        prefs.edit().putString(KEY_LANGUAGE, safeValue).apply();
    }

    public boolean isMusicEnabled() {
        return prefs.getBoolean(KEY_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_MUSIC_ENABLED, enabled).apply();
    }

    public boolean isSfxEnabled() {
        return prefs.getBoolean(KEY_SFX_ENABLED, true);
    }

    public void setSfxEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SFX_ENABLED, enabled).apply();
    }

    public boolean isHapticsEnabled() {
        return prefs.getBoolean(KEY_HAPTICS_ENABLED, true);
    }

    public void setHapticsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_HAPTICS_ENABLED, enabled).apply();
    }

    public boolean isScreenShakeEnabled() {
        return prefs.getBoolean(KEY_SCREEN_SHAKE_ENABLED, true);
    }

    public void setScreenShakeEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SCREEN_SHAKE_ENABLED, enabled).apply();
    }

    public boolean isReducedEffectsEnabled() {
        return prefs.getBoolean(KEY_REDUCED_EFFECTS_ENABLED, false);
    }

    public void setReducedEffectsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_REDUCED_EFFECTS_ENABLED, enabled).apply();
    }

    public int getHighScore() {
        int value = prefs.getInt(KEY_HIGH_SCORE, 0);
        if (value < 0) {
            return 0;
        }
        return value;
    }

    public void setHighScoreIfBetter(int candidateScore) {
        if (candidateScore > getHighScore()) {
            prefs.edit().putInt(KEY_HIGH_SCORE, candidateScore).apply();
        }
    }

    public int getBestCombo() {
        int value = prefs.getInt(KEY_BEST_COMBO, 0);
        if (value < 0) {
            return 0;
        }
        return value;
    }

    public void setBestComboIfBetter(int candidateCombo) {
        if (candidateCombo > getBestCombo()) {
            prefs.edit().putInt(KEY_BEST_COMBO, candidateCombo).apply();
        }
    }
}
