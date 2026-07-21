package com.floppytap.tetherloom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.floppytap.tetherloom.entity.Anchor;

import org.junit.Test;

public class AnchorTest {

    @Test
    public void canCapture_pointInsideCaptureRadius_returnsTrue() {
        Anchor anchor = new Anchor(500f, 500f, 26f, 70f, 90f);
        assertTrue(anchor.canCapture(540f, 500f));
    }

    @Test
    public void canCapture_pointOutsideCaptureRadius_returnsFalse() {
        Anchor anchor = new Anchor(500f, 500f, 26f, 70f, 90f);
        assertFalse(anchor.canCapture(700f, 500f));
    }

    @Test
    public void canCapture_pointExactlyAtCaptureRadius_returnsTrue() {
        Anchor anchor = new Anchor(0f, 0f, 26f, 70f, 90f);
        assertTrue(anchor.canCapture(70f, 0f));
    }
}
