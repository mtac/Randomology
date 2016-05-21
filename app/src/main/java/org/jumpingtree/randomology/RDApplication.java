package org.jumpingtree.randomology;

import android.app.Application;
import android.os.Build;

import org.jumpingtree.randomology.database.DatabaseAdapter;
import org.jumpingtree.randomology.entities.ContactItem;
import org.jumpingtree.randomology.utils.CommonUtilities;
import org.jumpingtree.randomology.utils.Logger;
import org.jumpingtree.randomology.utils.Logger.LogLevel;

import java.util.HashMap;

/**
 * Created by Miguel on 08/01/2015.
 */
//@EApplication
public class RDApplication extends Application {

    public static final String APP_TAG = "Randomology Android App";
    private static final String TAG = "RDApplication";

    public RDApplication() {
        super();
    }

    private static DatabaseAdapter adapter = null;

    private static HashMap<String,ContactItem> blockedContacts;

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.log(LogLevel.INFO, TAG, " ---------------- Application started ----------------");
        Logger.log(LogLevel.INFO, TAG,  "Device fingerprint: " + Build.FINGERPRINT);
        Logger.log(LogLevel.INFO, TAG,  "Device id: " + CommonUtilities.getDeviceId(getApplicationContext()));
        Logger.log(LogLevel.INFO, TAG,  "OS Version: " + CommonUtilities.getOsVersion());
        Logger.log(LogLevel.INFO, TAG,  "Application Version: " + CommonUtilities.getApplicationVersion(getApplicationContext()));

        RDApplication.adapter = new DatabaseAdapter(getApplicationContext());

        //loadBlockedContacts();
        RDApplication.blockedContacts = new HashMap<>();
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

    public static boolean isIdBlocked(String contactId) {
        return (RDApplication.blockedContacts != null && RDApplication.blockedContacts.containsKey(contactId));
    }
}
