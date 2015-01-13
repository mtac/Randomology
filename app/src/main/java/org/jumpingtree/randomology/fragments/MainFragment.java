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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jumpingtree.randomology.R;
import org.jumpingtree.randomology.utils.CommonUtilities;
import org.jumpingtree.randomology.utils.Logger;
import org.jumpingtree.randomology.utils.Logger.LogLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 08/01/2015.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

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

        contacts = new ArrayList<String>();

        getContactList();

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
                mCallback.startCall("916427929");
                btn_call.setEnabled(true);
                break;
            case R.id.button_text:
                if (!contacts.isEmpty()){
                    int pos = CommonUtilities.getRandomIntInRange(0,contacts.size() - 1);
                    String selected_contact = contacts.get(pos);
                    // mCallback.sendSMS("916427929", "Olha isto a bombear um SMS ao carregar num botão! :P");
                }
                break;

            default:
                break;
        }
    }

    private void getContactList(){
        new GetContactsTask().execute();
    }

    private class GetContactsTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            if(btn_call != null) {
                btn_call.setEnabled(false);
            }
            if(btn_text != null) {
                btn_text.setEnabled(false);
            }
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            ArrayList<String> numbers = new ArrayList<String>();

            ContentResolver cr = mContext.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            //Remove non numeric or plus chars
                            phone = phone.replaceAll("[^\\d+]", "");

                            numbers.add(phone.trim());
                            Logger.log(LogLevel.DEBUG, "MainFragment", "Number: " + phone.trim());
                        }
                        pCur.close();
                    }
                }
            }
            if (!cur.isClosed()) {
                cur.close();
            }
            return numbers;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (result != null && !result.isEmpty()) {
                contacts = result;
            }
            if(btn_call != null) {
                btn_call.setEnabled(true);
            }
            if(btn_text != null) {
                btn_text.setEnabled(true);
            }
            super.onPostExecute(result);
        }
    }
}
