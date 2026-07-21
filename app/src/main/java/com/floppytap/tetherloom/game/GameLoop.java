package com.floppytap.tetherloom.game;

/**
 * A frame-rate-independent game loop thread. Calls update() with a delta time in
 * seconds and render() once per frame, targeting a stable pace without depending
 * on any specific hardware refresh rate.
 */
public class GameLoop extends Thread {

    public interface Callback {
        void onUpdate(float deltaTimeSeconds);
        void onRender();
    }

    private final Callback callback;
    private volatile boolean running;
    private volatile boolean paused;

    private static final long TARGET_FRAME_TIME_MS = 16L; // roughly 60 FPS
    private static final float MAX_DELTA_SECONDS = 0.05f;

    public GameLoop(Callback callback) {
        super("TetherloomGameLoop");
        this.callback = callback;
    }

    public void startLoop() {
        running = true;
        paused = false;
        start();
    }

    public void stopLoop() {
        running = false;
        try {
            join(500);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    @Override
    public void run() {
        long lastTimeNanos = System.nanoTime();
        while (running) {
            long now = System.nanoTime();
            float deltaSeconds = (now - lastTimeNanos) / 1_000_000_000f;
            lastTimeNanos = now;

            if (deltaSeconds > MAX_DELTA_SECONDS) {
                deltaSeconds = MAX_DELTA_SECONDS;
            }

            if (!paused) {
                callback.onUpdate(deltaSeconds);
            }
            callback.onRender();

            long frameDurationMs = (System.nanoTime() - now) / 1_000_000L;
            long sleepTime = TARGET_FRAME_TIME_MS - frameDurationMs;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
