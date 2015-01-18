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

    private OnSendSMSListener mCallback;
    private Button btn_call, btn_text;
    private List<String> contacts;
    public Context mContext;

    // Container Activity must implement this interface
    public interface OnSendSMSListener {
        public void sendSMS(String phoneNumber, String message);
        public void startCall(String phoneNumber);
    }

    public MainFragment() {
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
            mCallback = (OnSendSMSListener) activity;
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

    private View.OnClickListener buttonSendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener buttonFeelingLuckyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
