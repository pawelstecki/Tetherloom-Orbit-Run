package com.floppytap.tetherloom.game;

import com.floppytap.tetherloom.entity.Anchor;
import com.floppytap.tetherloom.entity.Lumen;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure Java game engine for Phase 1. Contains no Android or rendering dependencies
 * so it can be exercised directly by unit tests. GameView drives this engine and
 * renders its current state every frame.
 */
public class GameEngine {

    public interface Listener {
        void onGameOver(int finalScore, int bestCombo);
    }

    private final float screenWidth;
    private final float screenHeight;
    private final RouteGenerator routeGenerator;
    private final ScoreSystem scoreSystem = new ScoreSystem();

    private final List<Anchor> anchors = new ArrayList<>();
    private Lumen lumen;
    private float cameraY;
    private boolean gameOver;
    private boolean tapConsumedThisFlight;
    private Listener listener;

    public GameEngine(float screenWidth, float screenHeight, long routeSeed) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.routeGenerator = new RouteGenerator(routeSeed, screenWidth);
        startNewRun();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void startNewRun() {
        anchors.clear();
        float startX = screenWidth / 2f;
        float startY = screenHeight * 0.7f;

        List<Anchor> initial = routeGenerator.generateInitialRoute(startX, startY, GameConfig.VISIBLE_ANCHOR_BUFFER);
        anchors.addAll(initial);

        Anchor firstAnchor = anchors.get(0);
        lumen = new Lumen(firstAnchor.getX(), firstAnchor.getY());
        lumen.attachToAnchor(firstAnchor);

        cameraY = 0f;
        gameOver = false;
        tapConsumedThisFlight = false;
        scoreSystem.reset();
    }

    /**
     * Handles a single tap event. Guards against duplicate actions from a single
     * tap: a tap only has an effect while the Lumen is orbiting, and once a launch
     * has happened the player must reach a new anchor before another meaningful
     * tap-triggered launch can occur.
     */
    public void handleTap() {
        if (gameOver) {
            return;
        }
        if (lumen.getState() == Lumen.STATE_ORBITING) {
            lumen.launchFromOrbit();
            tapConsumedThisFlight = true;
        }
    }

    public void update(float deltaTimeSeconds) {
        if (gameOver) {
            return;
        }

        lumen.update(deltaTimeSeconds);

        if (lumen.getState() == Lumen.STATE_FLYING) {
            for (Anchor anchor : anchors) {
                if (anchor == lumen.getAttachedAnchor()) {
                    continue;
                }
                if (anchor.canCapture(lumen.getX(), lumen.getY())) {
                    lumen.attachToAnchor(anchor);
                    scoreSystem.registerSuccessfulTransfer();
                    tapConsumedThisFlight = false;
                    extendRouteIfNeeded(anchor);
                    break;
                }
            }
        }

        float targetCameraY = lumen.getY() - screenHeight * 0.7f;
        if (targetCameraY < cameraY) {
            cameraY = targetCameraY;
        }

        float fallThreshold = cameraY + screenHeight + GameConfig.FALL_MARGIN_BELOW_CAMERA;
        if (lumen.getY() > fallThreshold) {
            triggerGameOver();
        }
    }

    private void extendRouteIfNeeded(Anchor reachedAnchor) {
        int index = anchors.indexOf(reachedAnchor);
        if (index < 0) {
            return;
        }
        int remaining = anchors.size() - 1 - index;
        if (remaining < 3) {
            Anchor lastAnchor = anchors.get(anchors.size() - 1);
            Anchor newAnchor = routeGenerator.generateNextAnchor(lastAnchor);
            anchors.add(newAnchor);
        }
    }

    private void triggerGameOver() {
        gameOver = true;
        if (listener != null) {
            listener.onGameOver(scoreSystem.getScore(), scoreSystem.getBestCombo());
        }
    }

    public List<Anchor> getAnchors() {
        return anchors;
    }

    public Lumen getLumen() {
        return lumen;
    }

    public float getCameraY() {
        return cameraY;
    }

    public int getScore() {
        return scoreSystem.getScore();
    }

    public int getCombo() {
        return scoreSystem.getCombo();
    }

    public int getComboMultiplierDisplay() {
        return scoreSystem.getComboMultiplierDisplay();
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
