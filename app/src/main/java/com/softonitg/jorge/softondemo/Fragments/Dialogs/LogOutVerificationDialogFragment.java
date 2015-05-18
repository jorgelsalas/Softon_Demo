package com.softonitg.jorge.softondemo.Fragments.Dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.softonitg.jorge.softondemo.Interfaces.LogOutListener;
import com.softonitg.jorge.softondemo.R;

/**
 * Shows when user presses back key inside the main screen
 */
public class LogOutVerificationDialogFragment extends DialogFragment {

    private LogOutListener logOutListener;
    private Button yesButton, cancelButton;

    public LogOutVerificationDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_log_out_verification_dialog, container, false);
        yesButton = (Button) v.findViewById(R.id.log_out_fragment_yes_button);
        cancelButton = (Button) v.findViewById(R.id.log_out_fragment_cancel_button);
        setClickListeners();
        getDialog().setTitle(getString(R.string.log_out_fragment_title_text));
        return v;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            logOutListener = (LogOutListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + LogOutListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logOutListener = null;
    }

    private void setClickListeners(){
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutListener.onLogOutConfirmed();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
    }

    private void dismissDialog(){
        getDialog().dismiss();
    }

}
