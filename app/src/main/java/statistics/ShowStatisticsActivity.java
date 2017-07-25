package statistics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.mikko.budgetapplication.ConstantVariableSettings;
import com.example.mikko.budgetapplication.MyBaseActivity;
import com.example.mikko.budgetapplication.R;
import com.example.mikko.budgetapplication.SharedPreferencesHandler;

import java.util.ArrayList;

import data.Transaction;
import data.UserGroup;
import datahandler.BackendlessDataLoaderInterface;
import datahandler.TransactionsHandler;
import payment.history.ShowHistoryActivity;

/**
 * Created by Mikko on 31.5.2017.
 */

public class ShowStatisticsActivity extends MyBaseActivity implements BackendlessDataLoaderInterface<Transaction> {

    private ViewPager viewPager;
    private ShowStatisticsPagerAdapter pagerAdapter;
    private int currentPosition;

    private UserGroup userGroup;
    private ArrayList<Transaction> payments, incomes;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        setContentView(R.layout.activity_show_statistics);
        super.onCreate(savedInstanceState);
        setHelpString(R.string.help_show_history);

        userGroup = (UserGroup) getIntent().getSerializableExtra(ConstantVariableSettings.SEND_USER_GROUP);

        viewPager = (ViewPager) findViewById(R.id.show_statistics_view_pager);
        viewPager.setOffscreenPageLimit(0);

        // if the user came here from the Showhistory activity, the activity has already loaded
        // the transactions.
        // => test and see if there are transactions
        payments = (ArrayList<Transaction>) getIntent().getSerializableExtra(ConstantVariableSettings.TRANSACTION_LIST_PAYMENTS);
        incomes = (ArrayList<Transaction>) getIntent().getSerializableExtra(ConstantVariableSettings.TRANSACTION_LIST_INCOMES);

        // if real lists were found, skip the transaction loading and init the page immediately
        if (payments != null && incomes != null) {
            // get the current month tab from internal storage
            // this makes a seamless transition as the tab will be the same as when left in showStatistics
            currentPosition = SharedPreferencesHandler.getInt(
                this, ConstantVariableSettings.MONTH_TAB_FRAGMENT_KEY, ConstantVariableSettings.MONTH_TAB_FRAGMENT_CURRENT_MONTH_INT);

            initPages();
        }
        // else load transactions first
        else {
            // load transactions from backend and internal storage
            // this will communicate back via the BackendlessDataLoaderInterface (Load successful)
            payments = new ArrayList<>();
            incomes = new ArrayList<>();
            new TransactionsHandler(this, userGroup).loadTransactions();
        }
    }

    @Override
    public void loadSuccessful(ArrayList<Transaction> loadedTransactionList) {

        // sort out the transactions to payments and incomes
        for (Transaction transaction : loadedTransactionList) {
            if (transaction.isPayment()) {
                payments.add(transaction);
            } else {
                incomes.add(transaction);
            }
        }

        System.out.println(" ");
        System.out.println("All loading done:");
        System.out.println("total payments: " + payments.size());
        System.out.println("total incomes: " + incomes.size());
        System.out.println(" ");

        // because we had to load the transactions, it means the user has started on this activity
        // we want to start at current (latest) month at default
        // so we also need to reset the pointer from internal storage to point to the current month
        currentPosition = ConstantVariableSettings.NUMBER_OF_MONTHS - 1;
        SharedPreferences.Editor editor = SharedPreferencesHandler.getEditor(this, ConstantVariableSettings.MONTH_TAB_FRAGMENT_KEY);
        editor.putInt(ConstantVariableSettings.MONTH_TAB_FRAGMENT_CURRENT_MONTH_INT, currentPosition);
        editor.apply();
        initPages();
    }

    // this is the method that is called after the transactions are loaded and we are ready to show
    // the user everything in this activity
    // it inits the pagerAdapter and links it to the viewpager, thus creating the monthly pages
    private void initPages() {
        pagerAdapter = new ShowStatisticsPagerAdapter(
                getSupportFragmentManager(), ConstantVariableSettings.NUMBER_OF_MONTHS, payments, incomes);
        viewPager.setAdapter(pagerAdapter);

        // set the current month
        viewPager.setCurrentItem(currentPosition);

        // set the listener for the viewpager so that we can keep track of the current tab
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            // every time the user changes the month, save the monthPosition in shared preferences
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != currentPosition) {
                    currentPosition = position;
                    SharedPreferences.Editor editor = SharedPreferencesHandler.getEditor(ShowStatisticsActivity.this, ConstantVariableSettings.MONTH_TAB_FRAGMENT_KEY);
                    editor.putInt(ConstantVariableSettings.MONTH_TAB_FRAGMENT_CURRENT_MONTH_INT, currentPosition);
                    editor.apply();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void loadFailed() {
        System.out.println("loading of Transactions failed");
    }

    public void showHistory(View view) {
        Intent showHistoryIntent = new Intent(this, ShowHistoryActivity.class);

        // send the loaded transactions
        showHistoryIntent.putExtra(ConstantVariableSettings.TRANSACTION_LIST_PAYMENTS, payments);
        showHistoryIntent.putExtra(ConstantVariableSettings.TRANSACTION_LIST_INCOMES, incomes);

        startActivity(showHistoryIntent);
        finish();
    }
}
