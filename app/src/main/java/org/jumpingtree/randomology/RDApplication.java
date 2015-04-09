package org.jumpingtree.randomology;

import android.app.Application;
import android.os.Build;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EApplication;
import org.jumpingtree.randomology.database.DatabaseAdapter;
import org.jumpingtree.randomology.entities.ContactItem;
import org.jumpingtree.randomology.utils.CommonUtilities;
import org.jumpingtree.randomology.utils.Logger;
import org.jumpingtree.randomology.utils.Logger.LogLevel;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Miguel on 08/01/2015.
 */
//@EApplication
public class RDApplication extends Application {

    public static final String APP_TAG = "Randomology Android App";
    private static final String TAG = "RDApplication";

    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-58565554-2";

    public static int GENERAL_TRACKER = 0;

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public RDApplication() {
        super();
    }

    private static DatabaseAdapter adapter = null;
    private static EventBus eventBus = null;

    private static HashMap<String,ContactItem> blockedContacts;

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.log(LogLevel.INFO, TAG, " ---------------- Application started ----------------");
        Logger.log(LogLevel.INFO, TAG,  "Device fingerprint: " + Build.FINGERPRINT);
        Logger.log(LogLevel.INFO, TAG,  "Device id: " + CommonUtilities.getDeviceId(getApplicationContext()));
        Logger.log(LogLevel.INFO, TAG,  "OS Version: " + CommonUtilities.getOsVersion());
        Logger.log(LogLevel.INFO, TAG,  "Application Version: " + CommonUtilities.getApplicationVersion(getApplicationContext()));

        RDApplication.eventBus = EventBus.getDefault();
        RDApplication.adapter = new DatabaseAdapter(getApplicationContext());

        //loadBlockedContacts();
        RDApplication.blockedContacts = new HashMap<String,ContactItem>();
    }

    /*@Background
    private void loadBlockedContacts() {
        adapter.loadBlockedContacts();
    }*/

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (RDApplication.adapter != null) {
            RDApplication.adapter.close();
            RDApplication.adapter = null;
        }
    }

    public static DatabaseAdapter getAdapter() {
        return RDApplication.adapter;
    }

    public static EventBus getEventBus() {
        return RDApplication.eventBus;
    }

    public static boolean isIdBlocked(String contactId) {
        return (RDApplication.blockedContacts != null && RDApplication.blockedContacts.containsKey(contactId));
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(PROPERTY_ID);
            /*Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(
                    R.xml.global_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);*/
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }
}
