package com.floppytap.tetherloom.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.floppytap.tetherloom.entity.Anchor;
import com.floppytap.tetherloom.entity.Lumen;

import java.util.List;

/**
 * SurfaceView-based Canvas renderer and input surface for the game. Contains no
 * gameplay rules directly; all rules live in GameEngine. This view only draws the
 * current engine state and forwards taps to the engine.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public interface GameOverListener {
        void onGameOver(int finalScore, int bestCombo);
    }

    private GameEngine engine;
    private GameLoop gameLoop;
    private GameOverListener gameOverListener;

    private final Paint backgroundPaint = new Paint();
    private final Paint anchorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint anchorOrbitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint lumenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint lumenGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint trailPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private boolean surfaceReady;
    private boolean pendingGameOverNotified;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getHolder().addCallback(this);

        backgroundPaint.setColor(Color.parseColor("#10131F"));

        anchorPaint.setColor(Color.parseColor("#72E6E1"));
        anchorPaint.setStyle(Paint.Style.STROKE);
        anchorPaint.setStrokeWidth(4f);

        anchorOrbitPaint.setColor(Color.parseColor("#33203E52"));
        anchorOrbitPaint.setStyle(Paint.Style.STROKE);
        anchorOrbitPaint.setStrokeWidth(2f);

        lumenPaint.setColor(Color.parseColor("#FFD36E"));
        lumenPaint.setStyle(Paint.Style.FILL);

        lumenGlowPaint.setColor(Color.parseColor("#40FFD36E"));
        lumenGlowPaint.setStyle(Paint.Style.FILL);

        trailPaint.setColor(Color.parseColor("#8072E6E1"));
        trailPaint.setStyle(Paint.Style.STROKE);
        trailPaint.setStrokeWidth(3f);

        setFocusable(true);
    }

    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    public GameEngine getEngine() {
        return engine;
    }

    public void startNewSession() {
        pendingGameOverNotified = false;
        if (engine == null) {
            createEngine();
        } else {
            engine.startNewRun();
        }
    }

    private void createEngine() {
        int width = getWidth() > 0 ? getWidth() : 1080;
        int height = getHeight() > 0 ? getHeight() : 1920;
        long seed = System.currentTimeMillis();
        engine = new GameEngine(width, height, seed);
        engine.setListener((finalScore, bestCombo) -> {
            if (!pendingGameOverNotified) {
                pendingGameOverNotified = true;
                post(() -> {
                    if (gameOverListener != null) {
                        gameOverListener.onGameOver(finalScore, bestCombo);
                    }
                });
            }
        });
    }

    public void pauseLoop() {
        if (gameLoop != null) {
            gameLoop.setPaused(true);
        }
    }

    public void resumeLoop() {
        if (gameLoop != null) {
            gameLoop.setPaused(false);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceReady = true;
        if (engine == null) {
            createEngine();
        }
        if (gameLoop == null || !gameLoop.isAlive()) {
            gameLoop = new GameLoop(new GameLoop.Callback() {
                @Override
                public void onUpdate(float deltaTimeSeconds) {
                    if (engine != null) {
                        engine.update(deltaTimeSeconds);
                    }
                }

                @Override
                public void onRender() {
                    drawFrame();
                }
            });
            gameLoop.startLoop();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Phase 1 does not need special handling for size changes after creation.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceReady = false;
        if (gameLoop != null) {
            gameLoop.stopLoop();
            gameLoop = null;
        }
    }

    private void drawFrame() {
        if (!surfaceReady || engine == null) {
            return;
        }
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            if (canvas == null) {
                return;
            }
            renderEngineState(canvas);
        } finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void renderEngineState(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

        float cameraY = engine.getCameraY();

        List<Anchor> anchors = engine.getAnchors();
        for (Anchor anchor : anchors) {
            float drawX = anchor.getX();
            float drawY = anchor.getY() - cameraY;
            canvas.drawCircle(drawX, drawY, anchor.getOrbitRadius(), anchorOrbitPaint);
            canvas.drawCircle(drawX, drawY, anchor.getRadius(), anchorPaint);
        }

        Lumen lumen = engine.getLumen();
        if (lumen != null) {
            float lumenX = lumen.getX();
            float lumenY = lumen.getY() - cameraY;
            canvas.drawCircle(lumenX, lumenY, GameConfig.LUMEN_RADIUS * 1.8f, lumenGlowPaint);
            canvas.drawCircle(lumenX, lumenY, GameConfig.LUMEN_RADIUS, lumenPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (engine != null) {
                engine.handleTap();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}
