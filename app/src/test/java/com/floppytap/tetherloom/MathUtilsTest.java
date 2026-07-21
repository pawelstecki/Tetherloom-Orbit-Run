package com.floppytap.tetherloom;

import static org.junit.Assert.assertEquals;

import com.floppytap.tetherloom.util.MathUtils;

import org.junit.Test;

public class MathUtilsTest {

    private static final float EPSILON = 0.001f;

    @Test
    public void tangentialVelocity_clockwise_atAngleZero_pointsDownward() {
        float[] velocity = MathUtils.tangentialVelocity(0.0, 1, 100f);
        assertEquals(0f, velocity[0], EPSILON);
        assertEquals(100f, velocity[1], EPSILON);
    }

    @Test
    public void tangentialVelocity_counterClockwise_atAngleZero_pointsUpward() {
        float[] velocity = MathUtils.tangentialVelocity(0.0, -1, 100f);
        assertEquals(0f, velocity[0], EPSILON);
        assertEquals(-100f, velocity[1], EPSILON);
    }

    @Test
    public void tangentialVelocity_magnitudeMatchesLaunchSpeed() {
        float[] velocity = MathUtils.tangentialVelocity(Math.PI / 3.0, 1, 250f);
        double magnitude = Math.sqrt(velocity[0] * velocity[0] + velocity[1] * velocity[1]);
        assertEquals(250.0, magnitude, 0.01);
    }

    @Test
    public void normalizeAngle_wrapsNegativeAngleIntoPositiveRange() {
        double normalized = MathUtils.normalizeAngle(-Math.PI / 2.0);
        assertEquals(3.0 * Math.PI / 2.0, normalized, 0.001);
    }

    @Test
    public void clamp_returnsValueWithinBounds() {
        assertEquals(5f, MathUtils.clamp(5f, 0f, 10f), EPSILON);
        assertEquals(0f, MathUtils.clamp(-3f, 0f, 10f), EPSILON);
        assertEquals(10f, MathUtils.clamp(99f, 0f, 10f), EPSILON);
    }
}
