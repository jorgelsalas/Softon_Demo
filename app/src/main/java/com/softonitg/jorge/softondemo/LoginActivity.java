package com.softonitg.jorge.softondemo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softonitg.jorge.softondemo.Database.DBManager;
import com.softonitg.jorge.softondemo.Database.DBManagerException;
import com.softonitg.jorge.softondemo.Fragments.Dialogs.LoginFailedDialogFragment;

import java.security.NoSuchAlgorithmException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends ActionBarActivity {

    private DBManager dbManager;
    private TextView title, developer;
    private EditText username, password;
    private Button loginButton;
    private final String TAG = "LOGIN TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        title = (TextView) findViewById(R.id.login_title);
        developer = (TextView) findViewById(R.id.login_dev_name);

        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);

        loginButton = (Button) findViewById(R.id.login_button);
        setClickListeners();

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_to_left);
        title.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right);
        developer.startAnimation(animation);

        dbManager = new DBManager(this);

        //TODO: REMOVE THESE STATEMENTS
        username.setText("maka");
        password.setText("5555");
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void setClickListeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    private void login() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        boolean valid;

        try {
            valid = dbManager.verifyUserCredentials(user, pass);
            if (valid){
                startMainActivity();
            }
            else{
                showFailedLoginDialog();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (DBManagerException dbme) {
            dbme.printStackTrace();
            Log.e(TAG, dbme.getDescription());
            showFailedLoginDialog();
        }
    }

    private void startMainActivity(){
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }

    private void showFailedLoginDialog(){
        FragmentManager fm = getSupportFragmentManager();
        LoginFailedDialogFragment dialog = new LoginFailedDialogFragment();
        dialog.show(fm, getString(R.string.failed_login_fragment_id));
    }



}
