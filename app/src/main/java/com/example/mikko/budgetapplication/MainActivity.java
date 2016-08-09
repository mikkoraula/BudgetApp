package com.example.mikko.budgetapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.backendless.Backendless;

import java.util.Date;

import addincome.AddIncomeActivity;
import addpayment.AddPaymentActivity;
import payment.history.ShowHistoryActivity;

public class MainActivity extends MyBaseActivity implements LoginHandlerInterface {

    public static final String PREFERENCE_KEY_LOGIN_CREDENTIALS = "loginpreferences";
    public static final String PREFERENCE_KEY_LAST_LOGIN = "lastlogin";
    public static final String PREFERENCE_KEY_CURRENT_LOGIN = "currentlogin";

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init Backendless
        Backendless.initApp(this, BackendSettings.APPLICATION_ID, BackendSettings.ANDROID_SECRET_KEY, BackendSettings.VERSION);

        // for difficult loop reasons, we need to check if user is already logged in at this point
        if (Backendless.UserService.isValidLogin()) {

            loginSuccessful();

        } else {
            Log.d("asd", "USER NOT LOGGED IN YET");

            // next check if user has saved account creds in a previous run and therefore log user automatically in
            SharedPreferences loginPreferences = getSharedPreferences(PREFERENCE_KEY_LOGIN_CREDENTIALS, MODE_PRIVATE);

            if (isLoginCredsSaved(loginPreferences)) {
                // login with the saved credentials
                LoginHandler loginHandler = new LoginHandler(this);
                Toast.makeText(this, "login creds saved, logging in automatically", Toast.LENGTH_SHORT).show();
                loginHandler.loginToBackendless(loginPreferences.getString("email", ""), loginPreferences.getString("password", ""));

            }
            // else just move to login activity
            else {
                goToLoginActivity();
            }
        }
    }

    public boolean isLoginCredsSaved(SharedPreferences loginPreferences) {
        // if this kind of preference is not found (=first using app)
        // then return false
        return loginPreferences.getBoolean("login_remembered", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // first create base menu
        super.onCreateOptionsMenu(menu);
        // then add main menu things
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // automatically handle clicks on the Home/Up button, so long
        // Handle action bar item clicks here. The action bar will
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            Log.d("asd", "signout pressed");
            new LoginHandler(this).logoutFromBackendless();
            return true;
        } else if (id == R.id.action_help) {
            DialogHelper.createHelpDialog(this, getString(R.string.toolbar_help), getString(R.string.help_main)).show();
        } else if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToLoginActivity() {
        Intent loginIntent = new Intent( this, LoginActivity.class );
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void loginSuccessful() {
        userId = Backendless.UserService.CurrentUser().getUserId();
        Log.i("Login", "User " + userId + " logged in.");

        // update the preferences on last login dates
        SharedPreferences lastLoginPreferences = getSharedPreferences(PREFERENCE_KEY_LAST_LOGIN, MODE_PRIVATE);
        SharedPreferences currentLoginPreferences = getSharedPreferences(PREFERENCE_KEY_CURRENT_LOGIN, MODE_PRIVATE);

        System.out.println(" ");
        System.out.println("Your second latest login was on : " + new Date(lastLoginPreferences.getLong(userId, 0)));

        // update lastLogin date
        SharedPreferences.Editor lastLoginPreferencesEditor = lastLoginPreferences.edit();
        lastLoginPreferencesEditor.putLong(userId, currentLoginPreferences.getLong(userId, new Date().getTime()));
        lastLoginPreferencesEditor.apply();

        // update currentLogin date
        SharedPreferences.Editor currentLoginPreferencesEditor = currentLoginPreferences.edit();
        currentLoginPreferencesEditor.putLong(userId, new Date().getTime());
        currentLoginPreferencesEditor.apply();

        System.out.println("Your last login was on : " + new Date(lastLoginPreferences.getLong(userId, 0)));
        System.out.println("Your current login is on : " + new Date(currentLoginPreferences.getLong(userId, 0)));
        System.out.println(" ");

    }

    @Override
    public void logoutSuccessful() {
        goToLoginActivity();
    }

    public void addPayment(View view) {
        Intent addPaymentIntent = new Intent(this, AddPaymentActivity.class);
        System.out.println();
        startActivity(addPaymentIntent);
    }

    public void addIncome(View view) {
        Intent addIncomeIntent = new Intent(this, AddIncomeActivity.class);
        startActivity(addIncomeIntent);
    }

    public void seeHistory(View view) {
        Intent showHistoryIntent = new Intent(this, ShowHistoryActivity.class);
        startActivity(showHistoryIntent);
    }

    /***************************************
     *
     * Closing the application
     */

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        closeApplication();
                    }
                }).create().show();
    }

    public void closeApplication() {
        new LoginHandler(this).logoutFromBackendless();
        finish();
        System.exit(0);
    }
}

