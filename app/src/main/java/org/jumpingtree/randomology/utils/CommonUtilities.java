package org.jumpingtree.randomology.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;

import org.jumpingtree.randomology.RDApplication;
import org.jumpingtree.randomology.entities.ContactItem;
import org.jumpingtree.randomology.utils.Logger.LogLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonUtilities {
	
	private final static String TAG = "CommonUtilities";

    public static final int INVALID_ID = -1;
	
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

    public static List<ContactItem> getContactsList(Context context) {
        ArrayList<ContactItem> contacts = new ArrayList<ContactItem>();

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur != null && cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    if(pCur != null) {
                        while (pCur.moveToNext()) {
                            String name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                            String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String photo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));

                            //Remove non numeric or plus chars
                            phone = phone.replaceAll("[^\\d+]", "");

                            contacts.add(new ContactItem(id, name, phone.trim(), photo));
                            //Logger.log(LogLevel.DEBUG, "MainFragment", "Number: " + phone.trim());
                        }
                        if (!pCur.isClosed()) {
                            pCur.close();
                        }
                    }
                }
            }
        }
        if (!cur.isClosed()) {
            cur.close();
        }
        return contacts;
    }

    public static ContactItem getRandomContact(Context context) {
        ContactItem result = null;
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int size = (cur != null ? cur.getCount() : 0);

        if(size > 0) {
            boolean found = false;
            Random rnd = new Random();

            while (!found) {
                int index = rnd.nextInt(size);
                cur.moveToPosition(index);
                String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                found = Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0;
                if (RDApplication.isIdBlocked(contactId)) {
                    found = false;
                }
                if (found) {
                    Cursor phones = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    if (phones != null) {
                        while (phones.moveToNext()) {
                            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String photo = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));

                            //Remove non numeric or plus chars
                            phoneNumber = phoneNumber.replaceAll("[^\\d+]", "");

                            result = new ContactItem(contactId, name, phoneNumber, photo);
                        }
                        if (!phones.isClosed()) {
                            phones.close();
                        }
                    }
                }
            }
            if (!cur.isClosed()) {
                cur.close();
            }
        }
        if(result != null) {
            Logger.log(LogLevel.DEBUG, TAG, "getRandomContact - id: " + result.getId());
            Logger.log(LogLevel.DEBUG, TAG, "getRandomContact - name: " + result.getName());
            Logger.log(LogLevel.DEBUG, TAG, "getRandomContact - number: " + result.getNumber());
            Logger.log(LogLevel.DEBUG, TAG, "getRandomContact - photo: " + result.getPhoto());
        } else {
            Logger.log(LogLevel.DEBUG, TAG, "getRandomContact - result: null");
        }
        return result;
    }
}
