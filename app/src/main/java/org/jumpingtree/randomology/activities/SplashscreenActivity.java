package org.jumpingtree.randomology.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import org.jumpingtree.randomology.R;

public class SplashscreenActivity extends Activity {

    private static final int SPLASH_SCREEN_TIME = 1500;

    private Runnable runnable;
    private Handler handlerTimer = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Check if we're running on Android 5.0 or higher
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        } else {
//            // Implement this feature without material design
//        }

        setContentView(R.layout.activity_splashscreen);

        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // Check if we're running on Android 5.0 or higher
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //    getWindow().setExitTransition(new Fade());
                //    startActivity(intent,
                //            ActivityOptions
                //                    .makeSceneTransitionAnimation(SplashscreenActivity.this).toBundle());
                //} else {
                startActivity(intent);
                //}

                finish();
            }
        };

        handlerTimer.postDelayed(runnable, SPLASH_SCREEN_TIME);

    }
}
