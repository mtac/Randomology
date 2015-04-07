package org.jumpingtree.randomology.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.UiThread;
import org.jumpingtree.randomology.R;
import org.jumpingtree.randomology.RDApplication;
import org.jumpingtree.randomology.bus.models.DatabaseResult;
import org.jumpingtree.randomology.database.DatabaseAdapter;
import org.jumpingtree.randomology.database.DatabaseHelper;
import org.jumpingtree.randomology.fragments.BlackListFragment;
import org.jumpingtree.randomology.fragments.ContactsListFragment;
import org.jumpingtree.randomology.fragments.MainFragment;
import org.jumpingtree.randomology.utils.Logger;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BlackListActivity extends ActionBarActivity implements
        BlackListFragment.BlackListOptions {

    private static final String TAG = "BlackListActivity";

    private boolean isContactListOpen;

    private DatabaseAdapter database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the BUS
        RDApplication.getEventBus().register(this);

        setupActionBar();

        setContentView(R.layout.activity_black_list);

        switchContent(new BlackListFragment(), true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the BUS
        RDApplication.getEventBus().unregister(this);
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setCustomView(R.layout.black_list_header);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void openContactsList() {
        switchContent(new ContactsListFragment(),false);
    }

    public void switchContent(Fragment newContent, boolean addToBackStack) {
        switchContent(newContent, addToBackStack, android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void switchContent(Fragment newContent, boolean addToBackStack, int enterAnimation, int exitAnimation) {
        if (newContent != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(enterAnimation, exitAnimation);
            ft.replace(R.id.content_frame, newContent);
            if (addToBackStack) {
                Logger.log(Logger.LogLevel.DEBUG, TAG, "Adding content to back stack: id=" + newContent.getId());
                ft.addToBackStack("" + newContent.getId());
            }
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Logger.log(Logger.LogLevel.DEBUG,TAG,"Settings: " + isContactListOpen);
        if(isContactListOpen) {
            isContactListOpen = false;
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
