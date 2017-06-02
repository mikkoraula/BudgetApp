package com.example.mikko.budgetapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Mikko on 8.6.2016.
 */
public class SettingsActivity extends MyBaseActivity {

    private int helpStringCode;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_settings);

        helpStringCode = R.string.help_settings;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSettingSwitches();

    }

    private void initSettingSwitches() {

    }

    // override the Options menu creation to change to a version of the menu where there is no settings
    // so the user can't move from settings to settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public void deleteInternalStorageTransactions(View view) {
        SharedPreferences.Editor editor =
                SharedPreferencesHandler.getEditor(this, ConstantVariableSettings.TRANSACTIONS_LAST_LOAD_KEY);
        //editor.putString(ConstantVariableSettings.PAYMENTS_KEY_STRING, "");
        //editor.putString(ConstantVariableSettings.INCOMES_KEY_STRING, "");
        editor.putString(ConstantVariableSettings.TRANSACTIONS_KEY_STRING, "");
        editor.putLong(ConstantVariableSettings.LAST_BACKENDLESS_LOAD_LONG, 0);
        editor.apply();
        System.out.println("Deleted payments and incomes from internal storage and resetted last load time");

    }

    public void loadTransactionsToInternalStorage(View view) {
        System.out.println("unimplemented feature :(");
    }
}
