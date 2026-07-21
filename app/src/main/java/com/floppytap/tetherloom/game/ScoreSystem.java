package com.floppytap.tetherloom.game;

/**
 * Pure Java scoring and combo logic, fully decoupled from Android and rendering.
 * Phase 1 keeps the combo model simple: each successful anchor transfer increases
 * the combo by one step and grants an increasing score bonus, capped for balance.
 */
public class ScoreSystem {

    private int score;
    private int combo;
    private int bestCombo;

    public void registerSuccessfulTransfer() {
        combo += 1;
        if (combo > bestCombo) {
            bestCombo = combo;
        }
        int comboBonus = Math.min(combo * GameConfig.SCORE_PER_COMBO_STEP, GameConfig.MAX_COMBO_SCORE_BONUS);
        score += GameConfig.SCORE_PER_TRANSFER + comboBonus;
    }

    public void breakCombo() {
        combo = 0;
    }

    public int getScore() {
        return score;
    }

    public int getCombo() {
        return combo;
    }

    public int getBestCombo() {
        return bestCombo;
    }

    public int getComboMultiplierDisplay() {
        return 1 + (combo / 3);
    }

    public void reset() {
        score = 0;
        combo = 0;
        bestCombo = 0;
    }
}
