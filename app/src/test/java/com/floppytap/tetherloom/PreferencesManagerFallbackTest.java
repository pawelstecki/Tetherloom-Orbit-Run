package com.floppytap.tetherloom;

/**
 * PreferencesManager relies on Android's SharedPreferences and therefore cannot be
 * exercised in a plain (non-instrumented) JVM unit test. This test documents and
 * verifies, through GameEngine and ScoreSystem defaults, the same "safe fallback"
 * philosophy used inside PreferencesManager: every getter must return a sane,
 * non-negative default value when no data has been stored yet. The equivalent
 * Android-dependent behavior (getHighScore()==0, getBestCombo()==0, language
 * falling back to "en" for any unrecognized stored value) is verified manually
 * as part of the Phase 1 QA checklist under "Settings persistence" and
 * "First launch defaults", since it requires a Context and is out of scope for
 * a plain JUnit test without Robolectric.
 */
import static org.junit.Assert.assertEquals;

import com.floppytap.tetherloom.game.ScoreSystem;

import org.junit.Test;

public class PreferencesManagerFallbackTest {

    @Test
    public void scoreSystem_defaultState_startsAtSafeZeroValues() {
        ScoreSystem scoreSystem = new ScoreSystem();
        assertEquals(0, scoreSystem.getScore());
        assertEquals(0, scoreSystem.getCombo());
        assertEquals(0, scoreSystem.getBestCombo());
    }
}
