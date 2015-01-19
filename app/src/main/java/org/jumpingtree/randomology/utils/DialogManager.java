package org.jumpingtree.randomology.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jumpingtree.randomology.R;
import org.jumpingtree.randomology.fragments.MainFragment;

/**
 * Created by Miguel on 18/01/2015.
 */
public class DialogManager {

    private static final String TAG = "DialogManager";

    private static AlertDialog myAlertDialog = null;
    private static AlertDialog messagePromptDialog = null;

    /**
     * Shows simple alerts a optional title, content message and dismiss button
     */
    public static void showAlertDialog(Activity activity, String title, String content, String buttonPositiveLabel)
    {
        showAlertDialog(activity, title, content, buttonPositiveLabel, null, null, null, null, null, CommonUtilities.INVALID_ID);
    }

    /**
     * Shows simple alerts a optional title, content message and dismiss button
     */
    public static void showAlertDialog(Activity activity, String title, String content, String buttonPositiveLabel, DialogInterface.OnClickListener buttonPositiveListener)
    {
        showAlertDialog(activity, title, content, buttonPositiveLabel, null, buttonPositiveListener, null, null, null, CommonUtilities.INVALID_ID);
    }

    /**
     * * Shows simple alerts a optional title, content message and dismiss button
     */
    public static void showAlertDialog(Activity activity, String title, String content, String buttonPositiveLabel, String buttonNegativeLabel, DialogInterface.OnClickListener buttonPositiveListener, DialogInterface.OnClickListener buttonNegativeListener)
    {
        showAlertDialog(activity, title, content, buttonPositiveLabel, buttonNegativeLabel, buttonPositiveListener, buttonNegativeListener, null, null, CommonUtilities.INVALID_ID);
    }

    /**
     * Shows complex alerts a optional title, content message and a positive or positive and negative buttons
     *
     * When sending both positive and negative buttons make sure both listeners are defined, as default listener will be override
     */
    public static void showAlertDialog(final Activity activity, String title, String content, String buttonPositiveLabel, String buttonNegativeLabel, DialogInterface.OnClickListener buttonPositiveListener, DialogInterface.OnClickListener buttonNegativeListener, String buttonNeutralLabel, DialogInterface.OnClickListener buttonNeutralListener, int viewId)
    {
        // To avoid multiple dialogs.
        closeAlertDialog();

        AlertDialog.Builder myAlertDialogBuilder = new AlertDialog.Builder(activity);

        if(viewId > 0){
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialoglayout = inflater.inflate(viewId, null);
            myAlertDialogBuilder.setView(dialoglayout);

            TextView titleView = (TextView) dialoglayout.findViewById(R.id.title);
            titleView.setText(title);
            TextView messageView = (TextView) dialoglayout.findViewById(R.id.message);
            messageView.setText(content);
        }
        else {
            myAlertDialogBuilder.setTitle(title);
            myAlertDialogBuilder.setMessage(content);
        }
        if (buttonPositiveLabel != null || (buttonPositiveLabel == null && buttonNegativeLabel == null))
        {
            if (buttonPositiveListener == null)
            {
                //Set generic listener that only closes the dialog
                buttonPositiveListener = new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DialogManager.closeAlertDialog();
                    }
                };
            }

            if (buttonPositiveLabel == null)
            {
                //Set default label
                buttonPositiveLabel = activity.getString(android.R.string.ok);
            }

            myAlertDialogBuilder.setPositiveButton(buttonPositiveLabel, buttonPositiveListener);
        }

        if (buttonNegativeLabel != null)
        {

            if (buttonNegativeListener == null)
            {
                //Set generic listener that only closes the dialog
                buttonNegativeListener = new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DialogManager.closeAlertDialog();
                    }
                };
            }

            myAlertDialogBuilder.setNegativeButton(buttonNegativeLabel, buttonNegativeListener);
        }

        if (buttonNeutralLabel != null)
        {
            if (buttonNeutralListener == null)
            {
                //Set generic listener that only closes the dialog
                buttonNeutralListener = new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DialogManager.closeAlertDialog();
                    }
                };
            }

            myAlertDialogBuilder.setNeutralButton(buttonNeutralLabel, buttonNeutralListener);
        }

        myAlertDialogBuilder.setCancelable(false);

        myAlertDialog = myAlertDialogBuilder.create();
        myAlertDialog.show();
    }

    public static void closeAlertDialog()
    {
        try
        {
            if (myAlertDialog != null && myAlertDialog.isShowing())
            {
                myAlertDialog.dismiss();
            }
        }catch (Exception e)
        {
            //Ignore
        }
        myAlertDialog = null;
    }

    public static void showMessagePromptAlertDialog(final Activity activity, final MainFragment.MainOptions mCallback, final String selected_contact)
    {
        // To avoid multiple dialogs.
        closeMessagePromptAlertDialog();

        AlertDialog.Builder messagePromptDialogBuilder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_message_prompt, null);

        final EditText text = (EditText) dialoglayout.findViewById(R.id.dialog_msg_prompt_input);
        final Button fl = (Button) dialoglayout.findViewById(R.id.dialog_msg_prompt_feeling_lucky);
        final Button send = (Button) dialoglayout.findViewById(R.id.dialog_msg_prompt_send);

        CommonUtilities.setViewEnabled(send, false);
        text.setText("");

        text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    if (!send.isEnabled()) {
                        CommonUtilities.setViewEnabled(send, true);
                    }
                } else {
                    CommonUtilities.setViewEnabled(send, false);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = text.getText().toString().trim();
                sendMessage(activity,mCallback,selected_contact,message);
            }
        });

        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int msgPos = CommonUtilities.getRandomIntInRange(1,5);
                String message = "";
                switch (msgPos) {
                    case 1:
                        message = activity.getString(R.string.msg1);
                        break;
                    case 2:
                        message = activity.getString(R.string.msg2);
                        break;
                    case 3:
                        message = activity.getString(R.string.msg3);
                        break;
                    case 4:
                        message = activity.getString(R.string.msg4);
                        break;
                    case 5:
                        message = activity.getString(R.string.msg5);
                        break;
                    default:
                        message = activity.getString(R.string.msg0);
                        break;
                }
                sendMessage(activity,mCallback,selected_contact,message);
            }
        });

        messagePromptDialogBuilder.setView(dialoglayout);

        messagePromptDialogBuilder.setCancelable(true);

        messagePromptDialog = messagePromptDialogBuilder.create();

        messagePromptDialog.show();
    }

    public static void closeMessagePromptAlertDialog()
    {
        try
        {
            if (messagePromptDialog != null && messagePromptDialog.isShowing())
            {
                messagePromptDialog.dismiss();
            }
        }catch (Exception e)
        {
            //Ignore
        }
        messagePromptDialog = null;
    }

    private static void sendMessage(final Activity activity, MainFragment.MainOptions mCallback, String selected_contact, String msg) {
        Logger.log(Logger.LogLevel.DEBUG, TAG, "Number: " + selected_contact);
        Logger.log(Logger.LogLevel.DEBUG, TAG, "Message: " + msg);
        mCallback.sendSMS("916427929", msg);//Miguel
        //mCallback.sendSMS("914314824", message);//Morais
        closeMessagePromptAlertDialog();
    }
}
