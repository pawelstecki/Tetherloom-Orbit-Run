package com.floppytap.tetherloom.util;

import android.app.Activity;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Apps targeting Android 15+ are drawn edge-to-edge, behind the status bar,
 * navigation bar, and display cutout. This helper pads the Activity content by
 * the system bar insets so HUD buttons and menu text are never hidden.
 * Call it once, right after setContentView().
 */
public final class SystemBarsHelper {

    private SystemBarsHelper() {
    }

    public static void applySystemBarInsets(Activity activity) {
        View content = activity.findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(content, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        ViewCompat.requestApplyInsets(content);
    }
}
