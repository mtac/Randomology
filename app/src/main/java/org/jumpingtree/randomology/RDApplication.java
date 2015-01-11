package org.jumpingtree.randomology;

import android.app.Application;
import android.os.Build;

import org.jumpingtree.randomology.utils.CommonUtilities;
import org.jumpingtree.randomology.utils.Logger;
import org.jumpingtree.randomology.utils.Logger.LogLevel;

/**
 * Created by Miguel on 08/01/2015.
 */
public class RDApplication extends Application {
    public static final String APP_TAG = "Randomology Android Application";
    private final String TAG = "RDApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.log(LogLevel.INFO, TAG, " ---------------- Application started ----------------");
        Logger.log(LogLevel.INFO, TAG,  "Device fingerprint: " + Build.FINGERPRINT);
        Logger.log(LogLevel.INFO, TAG,  "Device id: " + CommonUtilities.getDeviceId(getApplicationContext()));
        Logger.log(LogLevel.INFO, TAG,  "OS Version: " + CommonUtilities.getOsVersion());
        Logger.log(LogLevel.INFO, TAG,  "Application Version: " + CommonUtilities.getApplicationVersion(getApplicationContext()));
    }

}
