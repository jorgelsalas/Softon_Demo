package com.softonitg.jorge.softondemo.Fragments.Dialogs;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.softonitg.jorge.softondemo.R;

/**
 * Shown when user fails to login.
 */
public class LoginFailedDialogFragment extends DialogFragment {

    private Button okButton;

    public LoginFailedDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_failed_dialog, container, false);
        okButton = (Button) v.findViewById(R.id.failed_login_ok_button);
        setClickListeners();
        getDialog().setTitle(getActivity().getString(R.string.login_failed_fragment_title));
        return v;
    }

    private void setClickListeners(){
        okButton.setOnClickListener(new View.OnClickListener() {
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
