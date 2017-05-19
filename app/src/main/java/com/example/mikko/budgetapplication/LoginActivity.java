package com.example.mikko.budgetapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles user login:
 * by email and password
 */
public class LoginActivity extends MyBaseActivity implements LoginHandlerInterface {
    private static final int REGISTER_REQUEST_CODE = 1;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setHelpString(R.string.help_login);


        Button loginButton = (Button) findViewById( R.id.loginButton );
        loginButton.setOnClickListener( createLoginButtonListener() );

        makeRegistrationLink();
    }

    /**
     * Makes registration link clickable and assigns it a click listener.
     */
    public void makeRegistrationLink()
    {
        SpannableString registrationPrompt = new SpannableString( getString( R.string.register_prompt ) );

        ClickableSpan clickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick( View widget )
            {
                startRegistrationActivity();
            }
        };

        String linkText = getString( R.string.register_link );
        int linkStartIndex = registrationPrompt.toString().indexOf( linkText );
        int linkEndIndex = linkStartIndex + linkText.length();
        registrationPrompt.setSpan(clickableSpan, linkStartIndex, linkEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView registerPromptView = (TextView) findViewById( R.id.registerPromptText );
        registerPromptView.setText(registrationPrompt);
        registerPromptView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Sends a request for registration to RegistrationActivity,
     * expects for result in onActivityResult.
     */
    public void startRegistrationActivity() {
        Intent registrationIntent = new Intent( this, RegistrationActivity.class );
        startActivityForResult(registrationIntent, REGISTER_REQUEST_CODE);
    }

    /**
     * Creates a listener, which proceeds with login by email and password on button click.
     *
     * @return a listener, handling login button click
     */
    public View.OnClickListener createLoginButtonListener()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                EditText emailField = (EditText) findViewById( R.id.emailField );
                EditText passwordField = (EditText) findViewById( R.id.passwordField );

                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                LoginHandler loginHandler = new LoginHandler(LoginActivity.this);
                loginHandler.loginToBackendless(email, password);
            }
        };
    }



    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if( resultCode == RESULT_OK )
        {
            switch( requestCode )
            {
                case REGISTER_REQUEST_CODE:
                    String email = data.getStringExtra( BackendlessUser.EMAIL_KEY );
                    EditText emailField = (EditText) findViewById( R.id.emailField );
                    emailField.setText( email );

                    EditText passwordField = (EditText) findViewById( R.id.passwordField );
                    passwordField.requestFocus();

                    Toast.makeText( this, getString( R.string.info_registered_success ), Toast.LENGTH_SHORT ).show();
            }
        }
    }

    @Override
    public void loginSuccessful() {
        Log.d("asd", "Login pls");
        Intent openMainMenu = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(openMainMenu);
        finish();
    }

    @Override
    public void logoutSuccessful() {
        Log.d("asd", "this message should never come up (logoutSuccessful() in LoginActivity)");
    }
}