package org.jumpingtree.randomology.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import org.jumpingtree.randomology.R;

public class SplashscreenActivity extends Activity {

    private static final int SPLASH_SCREEN_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splashscreen);

        delayedActivityChange(SPLASH_SCREEN_TIME);
    }

    Handler mChangeActivityHandler = new Handler();
    Runnable mChangeActivityRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    };

    /**
     * Schedules the activity change in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedActivityChange(int delayMillis) {
        mChangeActivityHandler.removeCallbacks(mChangeActivityRunnable);
        mChangeActivityHandler.postDelayed(mChangeActivityRunnable, delayMillis);
    }
}
