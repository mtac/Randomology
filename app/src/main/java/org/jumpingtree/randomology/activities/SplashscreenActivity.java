package org.jumpingtree.randomology.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;

import org.jumpingtree.randomology.R;
import org.jumpingtree.randomology.RDApplication;
import org.jumpingtree.randomology.utils.CommonUtilities;

import java.util.ArrayList;
import java.util.List;

public class SplashscreenActivity extends Activity {

    private static final int SPLASH_SCREEN_TIME = 1500;

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

        new GetContactsTask().execute();
    }

    private class GetContactsTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            return CommonUtilities.getContactNumbersList(getApplicationContext());
        }

        @Override
        protected void onPostExecute(List<String> result) {

            RDApplication.setContacts(result);

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

            super.onPostExecute(result);
            finish();
        }
    }
}
