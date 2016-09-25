package com.oose2015.grapevine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.Requester;
import com.oose2015.grapevine.VolleySingleton;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mOldPasswordText;
    private EditText mNewPasswordText;
    private EditText mConfirmPasswordText;
    private RequestQueue requestQueue;

    @Override
    /**
     * Called user first wants to change password
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mToolbar =  (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    /**
     * Called when user wants to save passowrd
     * @param view the View that was clicked
     */
    public void onClick(View view) {
        mOldPasswordText   = (EditText)findViewById(R.id.current_pass_edit_text);
        mNewPasswordText   = (EditText)findViewById(R.id.password_edit_text);
        mConfirmPasswordText   = (EditText)findViewById(R.id.confirm_password_edit_text);

        String oldPasswordString = mOldPasswordText.getText().toString();
        String newPasswordString = mNewPasswordText.getText().toString();
        String confirmPasswordString = mConfirmPasswordText.getText().toString();

        if(oldPasswordString.equals("")) {
            mOldPasswordText.setError("Current password cannot be blank");
        } else if(newPasswordString.equals("")) {
            mNewPasswordText.setError("New password cannot be blank");
        } else if(confirmPasswordString.equals("")) {
            mConfirmPasswordText.setError("Confirm password cannot be blank");
        } else if(!newPasswordString.equals(confirmPasswordString)) {
            mConfirmPasswordText.setError("Passwords must match");
        } else {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Keys.EndpointUser.KEY_USERNAME, Grapevine.getMyUsername());
            params.put(Keys.EndpointUser.KEY_OLD_PASSWORD, oldPasswordString);
            params.put(Keys.EndpointUser.KEY_PASSWORD, newPasswordString);
            Requester.changePassword(requestQueue, ChangePasswordActivity.this,
                    SettingsActivity.class, params);
        }
    }
}
