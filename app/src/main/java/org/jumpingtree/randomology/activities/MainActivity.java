package org.jumpingtree.randomology.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.jumpingtree.randomology.R;
import org.jumpingtree.randomology.fragments.MainFragment;
import org.jumpingtree.randomology.utils.Logger;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActionBarActivity implements
        MainFragment.OnSendSMSListener  {

    private static final String TAG = "MainActivity";

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(smsSent, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(smsDelivered, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    @Override
    public void startCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
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
