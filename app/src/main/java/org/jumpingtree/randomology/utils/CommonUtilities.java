package org.jumpingtree.randomology.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;

import org.jumpingtree.randomology.utils.Logger.LogLevel;

import java.util.Random;

public class CommonUtilities {
	
	private final static String TAG = "CommonUtilities";

	/**
	 * This method converts dp unit to equivalent pixels, depending on device density. 
	 * 
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}
	
	public static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	    	Logger.log(LogLevel.DEBUG, TAG, "EMAIL VALIDATION - Email is null...");
	        return false;
	    } else {
	    	long time = System.currentTimeMillis();
	    	boolean result = android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    	android.util.Patterns.EMAIL_ADDRESS.matcher(target).reset();
	    	Logger.log(LogLevel.DEBUG, TAG, "EMAIL VALIDATION - Email is " + (result ? "valid!":"invalid!") + " ["+(System.currentTimeMillis() - time)+"ms]");
	        return result;
	    }
	}
	
	public static String getDeviceId(Context context) {
		
		String deviceId = null;
		try {
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = telephonyManager.getDeviceId();
			telephonyManager = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceId;
	}
	
	public static String getOsVersion() {
		
		String androidOS = null;
		
		try{
			androidOS = Build.VERSION.RELEASE;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return androidOS;
	}
	
	public static String getApplicationVersion(Context context) {
		
		String appVersion = null;
		try {
			
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			
			appVersion = pInfo.versionName;
			
			pInfo = null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return appVersion;
		
	}

	public static void setViewEnabled(View v, boolean enabled){ 
		v.setEnabled(enabled); 
		v.setClickable(enabled); 
		if(enabled){ 
			setItemAlpha(v, 0.5f, 1.0f); 
		} else{ 
			setItemAlpha(v, 1.0f, 0.5f); 
		} 
	} 


	public static void setItemAlpha(View v,float initialAlpha, float finalAlpha) 
	{ 
		AlphaAnimation alpha = new AlphaAnimation (initialAlpha, finalAlpha); 
		alpha.setDuration(50); 
		alpha.setFillAfter(true); 
		v.startAnimation(alpha); 
	}

    public static int getRandomIntInRange(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }
}
