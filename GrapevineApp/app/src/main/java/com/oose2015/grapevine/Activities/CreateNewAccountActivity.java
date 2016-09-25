package com.oose2015.grapevine.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.Requester;
import com.oose2015.grapevine.VolleySingleton;

import java.util.HashMap;

public class CreateNewAccountActivity extends AppCompatActivity {

    private EditText mEmailText;
    private EditText mUsernameText;
    private EditText mPasswordText;
    private EditText mConfirmPasswordText;
    private RequestQueue requestQueue;

    @Override
    /**
     * Opens up to screen that allows user to create an account
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    /**
     * When the user selects to create an account, will take in user information and ensure they
     * enter all information
     * @param view View with user information
     */
    public void onClick(View view) {
        mEmailText   = (EditText)findViewById(R.id.email_edit_text);
        mUsernameText   = (EditText)findViewById(R.id.username_edit_text);
        mPasswordText   = (EditText)findViewById(R.id.password_edit_text);
        mConfirmPasswordText   = (EditText)findViewById(R.id.confirm_password_edit_text);

        String emailString = mEmailText.getText().toString();
        String usernameString = mUsernameText.getText().toString();
        String passwordString = mPasswordText.getText().toString();
        String confirmPasswordString = mConfirmPasswordText.getText().toString();

        emailString = emailString.trim();
        usernameString = usernameString.trim();
        passwordString = passwordString.trim();
        confirmPasswordString = confirmPasswordString.trim();

        if(emailString.equals("")) {
            mEmailText.setError("Email cannot be blank");
        } else if(usernameString.equals("")) {
            mUsernameText.setError("Username cannot be blank");
        } else if(passwordString.equals("")) {
            mPasswordText.setError("Password cannot be blank");
        } else if(confirmPasswordString.equals("")) {
            mConfirmPasswordText.setError("Password cannot be blank");
        } else if(!passwordString.equals(confirmPasswordString)) {
            mConfirmPasswordText.setError("Passwords must match");
        } else {
            // send user credentials to backend and validate
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Keys.EndpointUser.KEY_USERNAME, usernameString);
            params.put(Keys.EndpointUser.KEY_EMAIL, emailString);
            params.put(Keys.EndpointUser.KEY_PASSWORD, passwordString);
            Requester.createNewAccount(requestQueue, CreateNewAccountActivity.this,
                    MainScreen.class, params);
        }
    }
}
