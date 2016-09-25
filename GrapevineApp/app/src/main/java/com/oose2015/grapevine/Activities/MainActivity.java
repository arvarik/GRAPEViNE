package com.oose2015.grapevine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.oose2015.grapevine.R;

/**
 * @author OOSE Group 9 2015
 */

public class MainActivity extends AppCompatActivity {

    @Override
    /**
     * Opens the main screen of the application.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Gives the option for the user to select Sign in or Create an Account.
     * @param view View that includes buttons for either logging in or creating an account
     */
    public void onClick(View view) {
        Button signInButton = (Button) this.findViewById(R.id.sign_in_button);
        Button createNewAccountButton = (Button) this.findViewById(R.id.create_new_account_button);
        if(view.equals(signInButton)) {
            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);
        }
        if(view.equals(createNewAccountButton)) {
            Intent intent = new Intent(this,CreateNewAccountActivity.class);
            startActivity(intent);
        }
    }
}