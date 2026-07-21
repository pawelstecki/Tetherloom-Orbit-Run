package com.floppytap.tetherloom;

import static org.junit.Assert.assertEquals;

import com.floppytap.tetherloom.game.ScoreSystem;

import org.junit.Test;

public class ScoreSystemTest {

    @Test
    public void registerSuccessfulTransfer_increasesComboAndScore() {
        ScoreSystem scoreSystem = new ScoreSystem();
        scoreSystem.registerSuccessfulTransfer();
        assertEquals(1, scoreSystem.getCombo());
        assertEquals(15, scoreSystem.getScore());
    }

    @Test
    public void registerSuccessfulTransfer_multipleTimes_accumulatesScore() {
        ScoreSystem scoreSystem = new ScoreSystem();
        scoreSystem.registerSuccessfulTransfer();
        scoreSystem.registerSuccessfulTransfer();
        scoreSystem.registerSuccessfulTransfer();
        assertEquals(3, scoreSystem.getCombo());
        assertEquals(3, scoreSystem.getBestCombo());
        assertEquals(15 + 20 + 25, scoreSystem.getScore());
    }

    @Test
    public void breakCombo_resetsComboButKeepsScoreAndBestCombo() {
        ScoreSystem scoreSystem = new ScoreSystem();
        scoreSystem.registerSuccessfulTransfer();
        scoreSystem.registerSuccessfulTransfer();
        int scoreBeforeBreak = scoreSystem.getScore();
        scoreSystem.breakCombo();
        assertEquals(0, scoreSystem.getCombo());
        assertEquals(2, scoreSystem.getBestCombo());
        assertEquals(scoreBeforeBreak, scoreSystem.getScore());
    }

    @Test
    public void reset_clearsScoreComboAndBestCombo() {
        ScoreSystem scoreSystem = new ScoreSystem();
        scoreSystem.registerSuccessfulTransfer();
        scoreSystem.reset();
        assertEquals(0, scoreSystem.getScore());
        assertEquals(0, scoreSystem.getCombo());
        assertEquals(0, scoreSystem.getBestCombo());
    }

    @Test
    public void comboMultiplierDisplay_increasesEveryThreeCombo() {
        ScoreSystem scoreSystem = new ScoreSystem();
        assertEquals(1, scoreSystem.getComboMultiplierDisplay());
        scoreSystem.registerSuccessfulTransfer();
        scoreSystem.registerSuccessfulTransfer();
        scoreSystem.registerSuccessfulTransfer();
        assertEquals(2, scoreSystem.getComboMultiplierDisplay());
    }
}
