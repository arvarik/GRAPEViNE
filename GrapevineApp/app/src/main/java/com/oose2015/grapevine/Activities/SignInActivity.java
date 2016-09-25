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

public class SignInActivity extends AppCompatActivity {

    private EditText mUsernameText;
    private EditText mPasswordText;
    private RequestQueue requestQueue;

    @Override
    /**
     * Opens up sign in page for user, allows user to enter in login and password.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mUsernameText = (EditText)findViewById(R.id.username_edit_text);
        mPasswordText   = (EditText)findViewById(R.id.password_edit_text);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    /**
     * Opens to the main screen once user clicks button to log in.
     * @param view View for this page
     */
    public void onClick(View view) {
        String usernameString = mUsernameText.getText().toString();
        String passwordString = mPasswordText.getText().toString();

        usernameString = usernameString.trim();
        passwordString = passwordString.trim();

        if(usernameString.equals("")) {
            mUsernameText.setError("Username cannot be blank");
        }else if(passwordString.equals("")) {
            mPasswordText.setError("Password cannot be blank");
        } else {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Keys.EndpointUser.KEY_USERNAME, usernameString);
            params.put(Keys.EndpointUser.KEY_PASSWORD, passwordString);
            Requester.login(requestQueue, SignInActivity.this,
                    MainScreen.class, params);
        }
    }
}