package com.floppytap.tetherloom.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * A small procedurally drawn diagram used on the How to Play screen to illustrate
 * the orbit-and-launch mechanic without relying on any external image asset.
 */
public class OrbitDiagramView extends View {

    private final Paint anchorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint orbitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint lumenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public OrbitDiagramView(Context context) {
        super(context);
        init();
    }

    public OrbitDiagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        anchorPaint.setColor(Color.parseColor("#72E6E1"));
        anchorPaint.setStyle(Paint.Style.STROKE);
        anchorPaint.setStrokeWidth(4f);

        orbitPaint.setColor(Color.parseColor("#40203E52"));
        orbitPaint.setStyle(Paint.Style.STROKE);
        orbitPaint.setStrokeWidth(2f);

        lumenPaint.setColor(Color.parseColor("#FFD36E"));
        lumenPaint.setStyle(Paint.Style.FILL);

        arrowPaint.setColor(Color.parseColor("#A98CFF"));
        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setStrokeWidth(4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float orbitRadius = Math.min(getWidth(), getHeight()) * 0.28f;
        float anchorRadius = orbitRadius * 0.22f;
        float lumenRadius = orbitRadius * 0.12f;

        canvas.drawCircle(centerX, centerY, orbitRadius, orbitPaint);
        canvas.drawCircle(centerX, centerY, anchorRadius, anchorPaint);

        float lumenAngle = (float) Math.toRadians(-40);
        float lumenX = centerX + (float) Math.cos(lumenAngle) * orbitRadius;
        float lumenY = centerY + (float) Math.sin(lumenAngle) * orbitRadius;
        canvas.drawCircle(lumenX, lumenY, lumenRadius, lumenPaint);

        float tangentAngle = lumenAngle + (float) (Math.PI / 2.0);
        float arrowEndX = lumenX + (float) Math.cos(tangentAngle) * orbitRadius * 0.9f;
        float arrowEndY = lumenY + (float) Math.sin(tangentAngle) * orbitRadius * 0.9f;
        canvas.drawLine(lumenX, lumenY, arrowEndX, arrowEndY, arrowPaint);

        float nextAnchorX = centerX + orbitRadius * 1.7f;
        float nextAnchorY = centerY - orbitRadius * 0.6f;
        canvas.drawCircle(nextAnchorX, nextAnchorY, anchorRadius, anchorPaint);
    }
}
