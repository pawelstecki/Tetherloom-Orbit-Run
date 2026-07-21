package com.floppytap.tetherloom.entity;

import com.floppytap.tetherloom.util.MathUtils;

/**
 * The player-controlled energy core. While orbiting, its position is fully
 * determined by the anchor it is attached to, its orbit angle, and orbit radius.
 * While free-flying, its position advances by simple linear velocity.
 */
public class Lumen {

    public static final int STATE_ORBITING = 0;
    public static final int STATE_FLYING = 1;

    private static final float DEFAULT_ORBIT_ANGULAR_SPEED = 2.4f; // radians per second
    private static final float DEFAULT_LAUNCH_SPEED = 620f; // pixels per second

    private float x;
    private float y;
    private float velocityX;
    private float velocityY;

    private int state;
    private Anchor attachedAnchor;
    private double orbitAngle;
    private int orbitDirection; // 1 = clockwise, -1 = counter-clockwise
    private final float orbitAngularSpeed;
    private final float launchSpeed;

    public Lumen(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.state = STATE_FLYING;
        this.orbitDirection = 1;
        this.orbitAngularSpeed = DEFAULT_ORBIT_ANGULAR_SPEED;
        this.launchSpeed = DEFAULT_LAUNCH_SPEED;
    }

    public void attachToAnchor(Anchor anchor) {
        this.attachedAnchor = anchor;
        this.state = STATE_ORBITING;
        double dx = this.x - anchor.getX();
        double dy = this.y - anchor.getY();
        this.orbitAngle = MathUtils.normalizeAngle(Math.atan2(dy, dx));
        this.velocityX = 0f;
        this.velocityY = 0f;
    }

    public void launchFromOrbit() {
        if (state != STATE_ORBITING || attachedAnchor == null) {
            return;
        }
        float[] tangential = MathUtils.tangentialVelocity(orbitAngle, orbitDirection, launchSpeed);
        this.velocityX = tangential[0];
        this.velocityY = tangential[1];
        this.state = STATE_FLYING;
        this.attachedAnchor = null;
    }

    public void update(float deltaTimeSeconds) {
        if (state == STATE_ORBITING && attachedAnchor != null) {
            orbitAngle += orbitDirection * orbitAngularSpeed * deltaTimeSeconds;
            orbitAngle = MathUtils.normalizeAngle(orbitAngle);
            float orbitRadius = attachedAnchor.getOrbitRadius();
            this.x = (float) (attachedAnchor.getX() + Math.cos(orbitAngle) * orbitRadius);
            this.y = (float) (attachedAnchor.getY() + Math.sin(orbitAngle) * orbitRadius);
        } else {
            this.x += velocityX * deltaTimeSeconds;
            this.y += velocityY * deltaTimeSeconds;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getState() {
        return state;
    }

    public double getOrbitAngle() {
        return orbitAngle;
    }

    public int getOrbitDirection() {
        return orbitDirection;
    }

    public void setOrbitDirection(int orbitDirection) {
        this.orbitDirection = orbitDirection >= 0 ? 1 : -1;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Anchor getAttachedAnchor() {
        return attachedAnchor;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public float getLaunchSpeed() {
        return launchSpeed;
    }
}
