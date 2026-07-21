package com.floppytap.tetherloom.game;

/**
 * Central configuration constants for Phase 1 gameplay tuning.
 * Kept as simple static constants so behavior stays predictable and testable.
 */
public final class GameConfig {

    private GameConfig() {
    }

    public static final float ANCHOR_RADIUS = 26f;
    public static final float ANCHOR_CAPTURE_RADIUS = 70f;
    public static final float ANCHOR_ORBIT_RADIUS = 90f;

    public static final float LUMEN_RADIUS = 14f;

    public static final float VERTICAL_ANCHOR_SPACING = 420f;
    public static final float HORIZONTAL_JITTER_RANGE = 220f;

    public static final float FALL_MARGIN_BELOW_CAMERA = 520f;

    public static final int SCORE_PER_TRANSFER = 10;
    public static final int SCORE_PER_COMBO_STEP = 5;
    public static final int MAX_COMBO_SCORE_BONUS = 100;

    public static final int VISIBLE_ANCHOR_BUFFER = 6;
}
