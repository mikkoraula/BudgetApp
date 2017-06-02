package com.example.mikko.budgetapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import userprofile.ViewProfileActivity;

/**
 * Created by Mikko on 5.7.2016.
 */
public class MyBaseActivity extends AppCompatActivity {

    int helpStringCode = R.string.help_main;

    public void setHelpString(int helpStringCode) {
        this.helpStringCode = helpStringCode;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
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
            DialogHelper.createHelpDialog(this, getString(R.string.toolbar_help), getString(helpStringCode)).show();
        } else if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
        } else if (id == R.id.action_view_profile) {
            Intent startViewProfileActivity = new Intent(this, ViewProfileActivity.class);
            startActivity(startViewProfileActivity);
        }

        return super.onOptionsItemSelected(item);
    }

}
