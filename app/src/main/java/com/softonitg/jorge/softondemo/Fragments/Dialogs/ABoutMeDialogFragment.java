package com.softonitg.jorge.softondemo.Fragments.Dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.softonitg.jorge.softondemo.R;

/**
 * Shows when the corresponding overflow button is pressed
 */
public class AboutMeDialogFragment extends DialogFragment {

    private Button okButton;

    public AboutMeDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_me_dialog, container, false);
        okButton = (Button) view.findViewById(R.id.about_me_ok_button);
        setClickListeners();
        getDialog().setTitle(getActivity().getString(R.string.about_me_title_text));
        return view;
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
