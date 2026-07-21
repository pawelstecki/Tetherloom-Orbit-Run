package com.floppytap.tetherloom.game;

import com.floppytap.tetherloom.entity.Anchor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Phase 1 route generation: a simple procedurally jittered vertical column of
 * standard anchors. Multiple anchor types, hazards, and formal reachability
 * proofing are planned for a later phase and are intentionally not implemented yet.
 */
public class RouteGenerator {

    private final Random random;
    private final float screenWidth;

    public RouteGenerator(long seed, float screenWidth) {
        this.random = new Random(seed);
        this.screenWidth = screenWidth;
    }

    public Anchor generateFirstAnchor(float startX, float startY) {
        return new Anchor(startX, startY, GameConfig.ANCHOR_RADIUS,
                GameConfig.ANCHOR_CAPTURE_RADIUS, GameConfig.ANCHOR_ORBIT_RADIUS);
    }

    public Anchor generateNextAnchor(Anchor previousAnchor) {
        float minX = Math.max(120f, screenWidth * 0.2f);
        float maxX = Math.min(screenWidth - 120f, screenWidth * 0.8f);
        float rangeWidth = Math.max(1f, maxX - minX);

        float jitter = (random.nextFloat() * 2f - 1f) * GameConfig.HORIZONTAL_JITTER_RANGE;
        float candidateX = previousAnchor.getX() + jitter;
        float clampedX = Math.max(minX, Math.min(maxX, candidateX));

        float nextY = previousAnchor.getY() - GameConfig.VERTICAL_ANCHOR_SPACING;

        return new Anchor(clampedX, nextY, GameConfig.ANCHOR_RADIUS,
                GameConfig.ANCHOR_CAPTURE_RADIUS, GameConfig.ANCHOR_ORBIT_RADIUS);
    }

    public List<Anchor> generateInitialRoute(float startX, float startY, int anchorCount) {
        List<Anchor> anchors = new ArrayList<>();
        Anchor first = generateFirstAnchor(startX, startY);
        anchors.add(first);
        Anchor previous = first;
        for (int i = 1; i < anchorCount; i++) {
            Anchor next = generateNextAnchor(previous);
            anchors.add(next);
            previous = next;
        }
        return anchors;
    }
}
