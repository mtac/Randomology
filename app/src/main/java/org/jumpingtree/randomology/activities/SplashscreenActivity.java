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
            ArrayList<String> numbers = new ArrayList<String>();

            ContentResolver cr = getApplicationContext().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            //Remove non numeric or plus chars
                            phone = phone.replaceAll("[^\\d+]", "");

                            numbers.add(phone.trim());
                            //Logger.log(LogLevel.DEBUG, "MainFragment", "Number: " + phone.trim());
                        }
                        pCur.close();
                    }
                }
            }
            if (!cur.isClosed()) {
                cur.close();
            }
            return numbers;
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
