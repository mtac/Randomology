package org.jumpingtree.randomology;

import android.app.Application;
import android.os.Build;

import org.jumpingtree.randomology.utils.CommonUtilities;
import org.jumpingtree.randomology.utils.Logger;
import org.jumpingtree.randomology.utils.Logger.LogLevel;

import java.util.List;

/**
 * Created by Miguel on 08/01/2015.
 */
public class RDApplication extends Application {

    public static final String APP_TAG = "Randomology Android Application";
    private static final String TAG = "RDApplication";

    private static List<String> contacts;

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.log(LogLevel.INFO, TAG, " ---------------- Application started ----------------");
        Logger.log(LogLevel.INFO, TAG,  "Device fingerprint: " + Build.FINGERPRINT);
        Logger.log(LogLevel.INFO, TAG,  "Device id: " + CommonUtilities.getDeviceId(getApplicationContext()));
        Logger.log(LogLevel.INFO, TAG,  "OS Version: " + CommonUtilities.getOsVersion());
        Logger.log(LogLevel.INFO, TAG,  "Application Version: " + CommonUtilities.getApplicationVersion(getApplicationContext()));
    }

    public static List<String> getContacts() {
        return contacts;
    }

    public static void setContacts(List<String> contacts) {
        RDApplication.contacts = contacts;
    }
}
