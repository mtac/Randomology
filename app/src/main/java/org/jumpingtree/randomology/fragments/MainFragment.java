package org.jumpingtree.randomology.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jumpingtree.randomology.R;
import org.jumpingtree.randomology.RDApplication;
import org.jumpingtree.randomology.entities.ContactItem;
import org.jumpingtree.randomology.events.EnableCallButtonEvent;
import org.jumpingtree.randomology.events.EnableSMSButtonEvent;
import org.jumpingtree.randomology.utils.CommonUtilities;
import org.jumpingtree.randomology.utils.DialogManager;
import org.jumpingtree.randomology.utils.Logger;
import org.jumpingtree.randomology.utils.Logger.LogLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 08/01/2015.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MainFragment";

    private MainOptions mCallback;
    private Button btn_call;
    private Button btn_text;
    public Context mContext;

    /**
     * TODO: Change this callback interface to use EventBus instead
     */
    public interface MainOptions {
        void sendSMS(String phoneNumber, String message);
        void startCall(String phoneNumber);
        void openSettings();
        void openPrivacyPolicy();
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Let this fragment contribute menu items
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        btn_call = (Button) rootView.findViewById(R.id.button_call);
        btn_text = (Button) rootView.findViewById(R.id.button_text);

        btn_call.setOnClickListener(this);
        btn_text.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (MainOptions) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSendMessageListener");
        }

        mContext = context.getApplicationContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(EnableCallButtonEvent event) {
        btn_call.setEnabled(event.isEnabled());
        CommonUtilities.setViewEnabled(btn_call,event.isEnabled());
    }

    @Subscribe
    public void onEvent(EnableSMSButtonEvent event) {
        btn_text.setEnabled(event.isEnabled());
        CommonUtilities.setViewEnabled(btn_text,event.isEnabled());
    }

    @Override
    public void onClick(View v) {
        new SelectRandomContactTask(v.getId()).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mCallback.openSettings();
            return true;
        } else if (id == R.id.action_privacy) {
            mCallback.openPrivacyPolicy();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SelectRandomContactTask extends AsyncTask<Void, Void, ContactItem> {

        private int id;

        public SelectRandomContactTask(int id){
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            DialogManager.showLoadingDialog(getActivity(), null, false);
            super.onPreExecute();
        }

        @Override
        protected ContactItem doInBackground(Void... params) {
            return CommonUtilities.getRandomContact(getActivity().getApplicationContext());
        }

        @Override
        protected void onPostExecute(ContactItem result) {
            DialogManager.dismissLoadingDialog();
            if(result != null) {

                switch (this.id) {
                    case R.id.button_call:
                        btn_call.setEnabled(false);
                        mCallback.startCall(result.getNumber());
                        btn_call.setEnabled(true);
                        break;
                    case R.id.button_text:
                        DialogManager.showMessagePromptAlertDialog(getActivity(), mCallback, result);
                        break;

                    default:
                        break;
                }

            } else {
                //TODO: Show contact not found dialog
            }

            super.onPostExecute(result);
        }
    }

}
