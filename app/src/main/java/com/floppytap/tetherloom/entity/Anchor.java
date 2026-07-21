package com.floppytap.tetherloom.entity;

/**
 * A standard orbital anchor node. Phase 1 includes only the standard anchor type;
 * additional anchor types are planned for a later phase and are intentionally
 * not implemented yet.
 */
public class Anchor {

    private final float x;
    private final float y;
    private final float radius;
    private final float captureRadius;
    private final float orbitRadius;

    public Anchor(float x, float y, float radius, float captureRadius, float orbitRadius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.captureRadius = captureRadius;
        this.orbitRadius = orbitRadius;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public float getCaptureRadius() {
        return captureRadius;
    }

    public float getOrbitRadius() {
        return orbitRadius;
    }

    /**
     * Returns true if the point at (px, py) lies within this anchor's capture radius,
     * meaning a free-flying Lumen close enough to this anchor should be captured.
     */
    public boolean canCapture(float px, float py) {
        float dx = px - x;
        float dy = py - y;
        float distanceSquared = dx * dx + dy * dy;
        return distanceSquared <= captureRadius * captureRadius;
    }
}
