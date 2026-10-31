package com.floppytap.tetherloom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.floppytap.tetherloom.data.PreferencesManager;
import com.floppytap.tetherloom.game.GameView;
import com.floppytap.tetherloom.ui.SettingsActivity;
import com.floppytap.tetherloom.util.LocaleHelper;
import com.floppytap.tetherloom.util.SystemBarsHelper;

/**
 * Hosts the SurfaceView-based gameplay, HUD, pause overlay, and game-over overlay.
 * All gameplay rules live in GameEngine; this Activity only wires UI events and
 * handles Android lifecycle and Back-button behavior.
 */
public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private TextView textScore;
    private TextView textCombo;
    private LinearLayout layoutPauseOverlay;
    private LinearLayout layoutGameOverOverlay;
    private TextView textFinalScore;
    private TextView textBestScore;

    private PreferencesManager preferencesManager;
    private boolean isPaused;

    private final android.os.Handler hudHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final Runnable hudUpdater = new Runnable() {
        @Override
        public void run() {
            updateHud();
            hudHandler.postDelayed(this, 100L);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferencesManager = new PreferencesManager(this);
        LocaleHelper.applyLanguage(this, preferencesManager.getLanguage());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        SystemBarsHelper.applySystemBarInsets(this);

        gameView = findViewById(R.id.gameView);
        textScore = findViewById(R.id.textScore);
        textCombo = findViewById(R.id.textCombo);
        layoutPauseOverlay = findViewById(R.id.layoutPauseOverlay);
        layoutGameOverOverlay = findViewById(R.id.layoutGameOverOverlay);
        textFinalScore = findViewById(R.id.textFinalScore);
        textBestScore = findViewById(R.id.textBestScore);

        Button btnPause = findViewById(R.id.btnPause);
        Button btnResume = findViewById(R.id.btnResume);
        Button btnRestartFromPause = findViewById(R.id.btnRestartFromPause);
        Button btnSettingsFromPause = findViewById(R.id.btnSettingsFromPause);
        Button btnMenuFromPause = findViewById(R.id.btnMenuFromPause);
        Button btnRetry = findViewById(R.id.btnRetry);
        Button btnGameOverMenu = findViewById(R.id.btnGameOverMenu);

        gameView.setGameOverListener((finalScore, bestCombo) -> showGameOver(finalScore, bestCombo));
        gameView.startNewSession();

        btnPause.setOnClickListener(v -> showPause());
        btnResume.setOnClickListener(v -> hidePause());
        btnRestartFromPause.setOnClickListener(v -> restartRun());
        btnSettingsFromPause.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        btnMenuFromPause.setOnClickListener(v -> goToMainMenu());

        btnRetry.setOnClickListener(v -> {
            layoutGameOverOverlay.setVisibility(LinearLayout.GONE);
            restartRun();
        });
        btnGameOverMenu.setOnClickListener(v -> goToMainMenu());

        registerBackHandler();
    }

    private void updateHud() {
        if (gameView.getEngine() == null) {
            return;
        }
        textScore.setText(String.valueOf(gameView.getEngine().getScore()));
        textCombo.setText("x" + gameView.getEngine().getComboMultiplierDisplay());
    }

    private void showPause() {
        isPaused = true;
        gameView.pauseLoop();
        layoutPauseOverlay.setVisibility(LinearLayout.VISIBLE);
    }

    private void hidePause() {
        isPaused = false;
        layoutPauseOverlay.setVisibility(LinearLayout.GONE);
        gameView.resumeLoop();
    }

    private void restartRun() {
        layoutGameOverOverlay.setVisibility(LinearLayout.GONE);
        layoutPauseOverlay.setVisibility(LinearLayout.GONE);
        isPaused = false;
        gameView.startNewSession();
        gameView.resumeLoop();
    }

    private void showGameOver(int finalScore, int bestCombo) {
        preferencesManager.setHighScoreIfBetter(finalScore);
        preferencesManager.setBestComboIfBetter(bestCombo);

        textFinalScore.setText(String.valueOf(finalScore));
        int highScore = preferencesManager.getHighScore();
        if (finalScore >= highScore && finalScore > 0) {
            textBestScore.setText(getString(R.string.gameover_new_best));
        } else {
            textBestScore.setText(getString(R.string.gameover_best_label) + ": " + highScore);
        }
        layoutGameOverOverlay.setVisibility(LinearLayout.VISIBLE);
    }

    private void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * Apps targeting Android 16 use predictive back, where onBackPressed() is no
     * longer called. Back is routed through the OnBackPressedDispatcher instead.
     */
    private void registerBackHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBack();
            }
        });
    }

    private void handleBack() {
        boolean gameOverVisible = layoutGameOverOverlay.getVisibility() == LinearLayout.VISIBLE;
        if (gameOverVisible) {
            goToMainMenu();
            return;
        }
        if (!isPaused) {
            showPause();
        } else {
            goToMainMenu();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hudHandler.post(hudUpdater);
        if (!isPaused && gameView != null) {
            gameView.resumeLoop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hudHandler.removeCallbacks(hudUpdater);
        if (gameView != null) {
            gameView.pauseLoop();
        }
    }
}
