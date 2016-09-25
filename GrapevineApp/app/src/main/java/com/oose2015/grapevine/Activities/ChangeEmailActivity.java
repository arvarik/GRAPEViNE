package com.oose2015.grapevine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.Requester;
import com.oose2015.grapevine.VolleySingleton;

import java.util.HashMap;


public class ChangeEmailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mEmailText;
    private EditText mConfirmEmailText;
    private RequestQueue requestQueue;

    @Override
    /**
     * Opens to screen to change email
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        mToolbar =  (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    /**
     * Starts intent to change the email.
     * @param view view that was clicked
     */
    public void onClick(View view) {
        mEmailText   = (EditText)findViewById(R.id.email_edit_text);
        mConfirmEmailText   = (EditText)findViewById(R.id.confirm_email_edit_text);

        String emailString = mEmailText.getText().toString();
        String confirmEmailString = mConfirmEmailText.getText().toString();

        if(emailString.equals("")) {
            mEmailText.setError("Email cannot be blank");
        } else if(confirmEmailString.equals("")) {
            mConfirmEmailText.setError("Confirm email cannot be blank");
        } else if(!emailString.equals(confirmEmailString)) {
            mConfirmEmailText.setError("Emails must match");
        } else {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Keys.EndpointUser.KEY_EMAIL, emailString);
            Requester.changeEmail(requestQueue, ChangeEmailActivity.this,
                    SettingsActivity.class, params);
        }
    }
}
