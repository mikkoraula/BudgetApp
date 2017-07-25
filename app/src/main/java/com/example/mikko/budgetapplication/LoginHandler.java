package com.example.mikko.budgetapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;

/**
 * Created by Mikko on 29-May-16.
 */
public class LoginHandler {

    private AppCompatActivity activity;

    public LoginHandler(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void loginToBackendless(String email, String password) {
        if( isLoginValuesValid( email, password ) ) {

            CheckBox rememberMeBox = (CheckBox) activity.findViewById(R.id.rememberMeBox);
            // if remembermeBox is null, we are automatically logging in, no need to save credentials again
            if (rememberMeBox != null) {
                if (rememberMeBox.isChecked()) {
                    Toast.makeText(activity, "login credentials about to be saved", Toast.LENGTH_SHORT).show();

                    // if we get here, login credentials are valid and the user wants to remember login next time app opens
                    // => save email and password to app's sharedpreferences
                    SharedPreferences loginPreferences = activity.getSharedPreferences("loginpreferences", activity.MODE_PRIVATE);
                    SharedPreferences.Editor loginPreferencesEditor = loginPreferences.edit();
                    // not sure if needed to clear the prefs before
                    loginPreferencesEditor.clear();

                    loginPreferencesEditor.putBoolean("login_remembered", true);
                    loginPreferencesEditor.putString("email", email);
                    loginPreferencesEditor.putString("password", password);
                    loginPreferencesEditor.apply();
                }
            }

            LoadingCallback<BackendlessUser> loginCallback = createLoginCallback();

            //loginCallback.showLoading();
            loginUser( email, password, loginCallback );
        }
    }

    /**
     * Sends a request to Backendless to log in user by email and password.
     *
     * @param email         user's email
     * @param password      user's password
     * @param loginCallback a callback, containing actions to be executed on request result
     */
    public void loginUser( String email, String password, AsyncCallback<BackendlessUser> loginCallback ) {
        Backendless.UserService.login(email, password, loginCallback, false);
    }

    /**
     * Validates the values, which user entered on login screen.
     * Shows Toast with a warning if something is wrong.
     *
     * @param email    user's email
     * @param password user's password
     * @return true if all values are OK, false if something is wrong
     */
    public boolean isLoginValuesValid( CharSequence email, CharSequence password ) {
        return Validator.isEmailValid( activity, email ) && Validator.isPasswordValid( activity, password );
    }

    /**
     * Creates a callback, containing actions to be executed on login request result.
     * Shows a Toast with BackendlessUser's objectId on success,
     * show a dialog with an error message on failure.
     *
     * @return a callback, containing actions to be executed on login request result
     */
    public LoadingCallback<BackendlessUser> createLoginCallback() {
        return new LoadingCallback<BackendlessUser>( activity, activity.getString(R.string.loading_login) )
        {
            @Override
            public void handleResponse( BackendlessUser loggedInUser )
            {
                super.handleResponse(loggedInUser);
                Toast.makeText( activity, String.format( activity.getString( R.string.info_logged_in ), loggedInUser.getProperties().get("name") ), Toast.LENGTH_SHORT ).show();

                // is this the best way to do this?
                ((LoginHandlerInterface) activity).loginSuccessful();
            }
        };
    }

    public void logoutFromBackendless() {
        SharedPreferences loginPreferences = activity.getSharedPreferences("loginpreferences", activity.MODE_PRIVATE);
        SharedPreferences.Editor loginPreferencesEditor = loginPreferences.edit();
        // not sure if needed to clear the prefs before
        // not sure what this even does :D
        loginPreferencesEditor.clear();

        loginPreferencesEditor.putBoolean("login_remembered", false);
        loginPreferencesEditor.putString("email", "");
        loginPreferencesEditor.putString("password", "");
        loginPreferencesEditor.apply();

        LoadingCallback<Void> logoutCallback = createLogoutCallback();
        Backendless.UserService.logout(logoutCallback);
    }

    public LoadingCallback<Void> createLogoutCallback() {
        return new LoadingCallback<Void>( activity, "Loggingingout" )
        {
            @Override
            public void handleResponse( Void response ) {
                super.handleResponse(response);
                Toast.makeText( activity, "Logout successful?", Toast.LENGTH_SHORT ).show();

                // is this the best way to do this?
                ((LoginHandlerInterface) activity).logoutSuccessful();
            }
        };
    }
}
