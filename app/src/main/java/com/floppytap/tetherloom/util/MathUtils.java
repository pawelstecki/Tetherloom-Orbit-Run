package com.floppytap.tetherloom.util;

/**
 * Small collection of math helpers used throughout the game logic.
 * Kept free of any Android dependency so it can be unit tested as plain Java.
 */
public final class MathUtils {

    private MathUtils() {
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Computes the tangential launch velocity for a body orbiting at angleRadians
     * with the given orbit direction (1 clockwise, -1 counter-clockwise) and linear
     * launch speed. The tangent direction is perpendicular to the radius vector.
     */
    public static float[] tangentialVelocity(double angleRadians, int orbitDirection, float launchSpeed) {
        double tangentAngle = angleRadians + (orbitDirection >= 0 ? (Math.PI / 2.0) : -(Math.PI / 2.0));
        float vx = (float) (Math.cos(tangentAngle) * launchSpeed);
        float vy = (float) (Math.sin(tangentAngle) * launchSpeed);
        return new float[]{vx, vy};
    }

    public static float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }

    public static double normalizeAngle(double angleRadians) {
        double twoPi = Math.PI * 2.0;
        double result = angleRadians % twoPi;
        if (result < 0) {
            result += twoPi;
        }
        return result;
    }
}
