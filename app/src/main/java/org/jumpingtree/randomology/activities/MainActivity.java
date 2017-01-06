package org.jumpingtree.randomology.activities;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.jumpingtree.randomology.R;
import org.jumpingtree.randomology.events.EnableCallButtonEvent;
import org.jumpingtree.randomology.events.EnableSMSButtonEvent;
import org.jumpingtree.randomology.fragments.MainFragment;
import org.jumpingtree.randomology.fragments.PrivacyPolicyDialog;
import org.jumpingtree.randomology.utils.DialogManager;
import org.jumpingtree.randomology.utils.Logger;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements
        MainFragment.MainOptions {

    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1001;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1002;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1003;

    private boolean mIsLargeLayout;
    private String tempPhoneNumber;
    private String tempMsg;
    private boolean requestedContactsPermission = false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Champagne&Limousines.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setupActionBar();

        setContentView(R.layout.activity_main);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!requestedContactsPermission && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestedContactsPermission = true;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setCustomView(R.layout.application_header);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            tempPhoneNumber = phoneNumber;
            tempMsg = message;

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
            return;
        }

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(smsSent, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(smsDelivered, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        logFirebaseSMSAction();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    @Override
    public void startCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            tempPhoneNumber = phoneNumber;

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }
        logFirebaseCallAction();
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    EventBus.getDefault().post(new EnableCallButtonEvent(true));
                    startCall(tempPhoneNumber);
                    tempPhoneNumber = "";
                    tempMsg = "";
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    EventBus.getDefault().post(new EnableCallButtonEvent(false));
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    EventBus.getDefault().post(new EnableSMSButtonEvent(true));
                    sendSMS(tempPhoneNumber,tempMsg);
                    tempPhoneNumber = "";
                    tempMsg = "";
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    EventBus.getDefault().post(new EnableSMSButtonEvent(false));
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    EventBus.getDefault().post(new EnableCallButtonEvent(true));
                    EventBus.getDefault().post(new EnableSMSButtonEvent(true));
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    DialogManager.showAlertDialog(this, getString(R.string.permissions), getString(R.string.contacts_permissions), getString(R.string.exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            quitApp();
                        }
                    });
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void openSettings() {
        //Intent blackListIntent = new Intent(getApplicationContext(), BlackListActivity.class);
        //startActivity(blackListIntent);
        //overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }

    @Override
    public void openPrivacyPolicy() {
        DialogManager.showPrivacyPolicyDialog(this,mIsLargeLayout);
    }

    private void quitApp() {
        this.finish();
    }

    private void logFirebaseCallAction() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "start_call");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.random_call));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "action");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void logFirebaseSMSAction() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "sent_sms");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.msg_sent));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "action");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void logFirebaseSMSIntentAction() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "send_sms_intent");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.random_text));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private BroadcastReceiver smsSent = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode())
            {
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), getString(R.string.msg_sent), Toast.LENGTH_SHORT).show();
                    Logger.log(Logger.LogLevel.INFO, TAG, "SMS sent");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    //Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                    Logger.log(Logger.LogLevel.DEBUG, TAG, "Generic failure");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    //Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                    Logger.log(Logger.LogLevel.DEBUG, TAG, "No service");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    //Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                    Logger.log(Logger.LogLevel.DEBUG, TAG, "Null PDU");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    //Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                    Logger.log(Logger.LogLevel.DEBUG, TAG, "Radio off");
                    break;
            }
            //---clean up---
            unregisterReceiver(smsSent);
        }
    };

    private BroadcastReceiver smsDelivered = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode())
            {
                case Activity.RESULT_OK:
                    //Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                    Logger.log(Logger.LogLevel.INFO, TAG, "SMS delivered");
                    break;
                case Activity.RESULT_CANCELED:
                    //Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                    Logger.log(Logger.LogLevel.INFO, TAG, "SMS not delivered");
                    break;
            }
            //---clean up---
            unregisterReceiver(smsDelivered);
        }
    };
}
