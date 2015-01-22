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

import org.jumpingtree.randomology.R;
import org.jumpingtree.randomology.RDApplication;
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
    private Button btn_call, btn_text;
    private List<String> contacts;
    public Context mContext;

    public interface MainOptions {
        public void sendSMS(String phoneNumber, String message);
        public void startCall(String phoneNumber);
        public void openSettings();
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Let this fragment contribute menu items
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        btn_call        = (Button)      rootView.findViewById(R.id.button_call);
        btn_text        = (Button)      rootView.findViewById(R.id.button_text);

        btn_call.setOnClickListener(this);
        btn_text.setOnClickListener(this);

        contacts = RDApplication.getContacts();
        if(contacts == null){
            contacts = new ArrayList<String>();
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (MainOptions) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSendMessageListener");
        }

        mContext = activity.getApplicationContext();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_call:
                btn_call.setEnabled(false);
                //mCallback.startCall("916427929");//Miguel
                mCallback.startCall("914314824");//Morais
                btn_call.setEnabled(true);
                break;
            case R.id.button_text:
                if (!contacts.isEmpty()){
                    int pos = CommonUtilities.getRandomIntInRange(0,contacts.size() - 1);
                    String selected_contact = contacts.get(pos);
                    DialogManager.showMessagePromptAlertDialog(getActivity(), mCallback, selected_contact);
                }
                break;

            default:
                break;
        }
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
        }

        return super.onOptionsItemSelected(item);
    }

}
