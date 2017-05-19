package com.example.mikko.budgetapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.backendless.Backendless;

/**
 * Created by Mikko on 8.6.2016.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_settings);

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
                SharedPreferencesHandler.getEditor(this, SharedPreferencesSettings.TRANSACTIONS_KEY);
        editor.putString(SharedPreferencesSettings.PAYMENTS_KEY_STRING, "");
        editor.putString(SharedPreferencesSettings.INCOMES_KEY_STRING, "");
        editor.putLong(SharedPreferencesSettings.LAST_BACKENDLESS_LOAD_LONG, 0);
        editor.apply();
        System.out.println("Deleted payments and incomes from internal storage and resetted last load time");

    }

    public void loadTransactionsToInternalStorage(View view) {
        System.out.println("unimplemented feature :(");
    }
}
