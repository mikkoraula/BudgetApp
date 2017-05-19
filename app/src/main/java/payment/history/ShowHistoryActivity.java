package payment.history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import data.Transaction;
import datahandler.BackendlessDataLoaderInterface;

import com.example.mikko.budgetapplication.DateHandler;
import com.example.mikko.budgetapplication.DialogHelper;
import com.example.mikko.budgetapplication.MyBaseActivity;
import com.example.mikko.budgetapplication.R;
import com.example.mikko.budgetapplication.SharedPreferencesHandler;
import com.example.mikko.budgetapplication.SharedPreferencesSettings;

import java.util.ArrayList;
import java.util.Date;

import datahandler.BackendlessTransactionLoader;

/**
 * Created by Mikko on 6.7.2016.
 */
public class ShowHistoryActivity extends MyBaseActivity implements BackendlessDataLoaderInterface<Transaction> {

    private ArrayList<Transaction> payments;
    private ArrayList<Transaction> incomes;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;

    private int loadCounter;
    private int oldMonthPosition;

    private Date currentLoadDate;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        setContentView(R.layout.activity_show_history);
        super.onCreate(savedInstanceState);
        setHelpString(R.string.help_show_history);

        // load transactions from backend and internal storage
        loadTransactions();

        // set three tabs from which the user can view payments and incomes:
        // Both shows all the transactions
        // Personal only shows the transactions that are personal
        // Shared shows only shared transactions
        tabLayout = (TabLayout) findViewById(R.id.show_history_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Both"));
        tabLayout.addTab(tabLayout.newTab().setText("Personal"));
        tabLayout.addTab(tabLayout.newTab().setText("Shared"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(0);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                TabFragment tabFragment = (TabFragment) myPagerAdapter.instantiateItem(viewPager, tab.getPosition());
                if (tabFragment != null) {
                    tabFragment.onResume();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    /*********************************************************
     *
     *  LOADING  PAYMENTS AND INCOMES
     *
     */

    private void loadTransactions() {
        System.out.println(" ");
        System.out.println(" ");
        payments = new ArrayList<>();
        incomes = new ArrayList<>();
        loadCounter = 0;

        currentLoadDate = new Date();
        long lastBackendlessLoadInMillis = SharedPreferencesHandler.getLong(
                this, SharedPreferencesSettings.TRANSACTIONS_KEY, SharedPreferencesSettings.LAST_BACKENDLESS_LOAD_LONG);

        DateHandler.getYear(lastBackendlessLoadInMillis);
        DateHandler.getMonth(lastBackendlessLoadInMillis);



        // load the old payments from internal storage
        payments = TransactionDataHandler.loadTransactions(this, SharedPreferencesSettings.PAYMENTS_KEY_STRING);
        // load the old incomes from internal storage
        incomes = TransactionDataHandler.loadTransactions(this, SharedPreferencesSettings.INCOMES_KEY_STRING);
        // load the newest transactions from Backendless
        BackendlessTransactionLoader backendlessPaymentsLoader = new BackendlessTransactionLoader(this);
        backendlessPaymentsLoader.loadTransactions(lastBackendlessLoadInMillis);

    }

    @Override
    public void loadSuccessful(ArrayList<Transaction> loadedTransactionList) {
        loadCounter++;
        if (loadedTransactionList.size() == 0) {
            //System.out.println("didn't find any of that transaction in backendlessload");
        }
        else {
            // go through all the transactions
            // and sort out payments to their own list and incomes to their own list
            for (Transaction transaction : loadedTransactionList) {
                if (transaction.isPayment()) {
                    payments.add(transaction);
                } else {
                    incomes.add(transaction);
                }
            }
            // immediately save the new payments to internal
            TransactionDataHandler.saveTransactions(
                        this, payments, SharedPreferencesSettings.PAYMENTS_KEY_STRING);
            // immediately save the new incomes to internal
            TransactionDataHandler.saveTransactions(
                        this, incomes, SharedPreferencesSettings.INCOMES_KEY_STRING);

            // update the latest backend transactions load time
            SharedPreferences.Editor editor = SharedPreferencesHandler.getEditor(
                    this, SharedPreferencesSettings.TRANSACTIONS_KEY);
            editor.putLong(SharedPreferencesSettings.LAST_BACKENDLESS_LOAD_LONG, currentLoadDate.getTime());
            editor.apply();
        }
        System.out.println(" ");
        System.out.println("All loading done:");
        System.out.println("total payments: " + payments.size());
        System.out.println("total incomes: " + incomes.size());
        System.out.println(" ");

        // initiate the pageradapter to get things rolling now that the transactions have been loaded
        myPagerAdapter = new MyPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), payments, incomes, this);
        viewPager.setAdapter(myPagerAdapter);
    }

    @Override
    public void loadFailed() {
        System.out.println("loading of Transactions failed");
    }

}
