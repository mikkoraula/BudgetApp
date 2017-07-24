package payment.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import data.Transaction;
import datahandler.BackendlessDataLoaderInterface;

import com.example.mikko.budgetapplication.ConstantVariableSettings;
import com.example.mikko.budgetapplication.MyBaseActivity;
import com.example.mikko.budgetapplication.R;
import com.example.mikko.budgetapplication.SharedPreferencesHandler;

import java.util.ArrayList;
import java.util.Date;

import datahandler.TransactionsLoader;
import statistics.ShowStatisticsActivity;

/**
 * Created by Mikko on 6.7.2016.
 */
public class ShowHistoryActivity extends MyBaseActivity implements BackendlessDataLoaderInterface<Transaction> {

    private ArrayList<Transaction> payments;
    private ArrayList<Transaction> incomes;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;


    private Date currentLoadDate;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        setContentView(R.layout.activity_show_history);
        super.onCreate(savedInstanceState);
        setHelpString(R.string.help_show_history);


        // set three tabs from which the user can view payments and incomes:
        // Both shows all the transactions
        // Personal only shows the transactions that are personal
        // Shared shows only shared transactions
        tabLayout = (TabLayout) findViewById(R.id.show_history_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Both"));
        tabLayout.addTab(tabLayout.newTab().setText("Personal"));
        tabLayout.addTab(tabLayout.newTab().setText("Shared"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.show_history_pager);
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

        // if the user came here from the ShowStatistics activity, the activity has already loaded
        // the transactions.
        // => test and see if there are transactions
        payments = (ArrayList<Transaction>) getIntent().getSerializableExtra(ConstantVariableSettings.TRANSACTION_LIST_PAYMENTS);
        incomes = (ArrayList<Transaction>) getIntent().getSerializableExtra(ConstantVariableSettings.TRANSACTION_LIST_INCOMES);

        // if real lists were found, skip the transaction loading and init the page immediately
        if (payments != null && incomes != null) {
            // get the current month tab from internal storage
            // this makes a seamless transition as the tab will be the same as when left in showStatistics
            int currentMonthPosition = SharedPreferencesHandler.getInt(
                this, ConstantVariableSettings.MONTH_TAB_FRAGMENT_KEY, ConstantVariableSettings.MONTH_TAB_FRAGMENT_CURRENT_MONTH_INT);
            initPages(currentMonthPosition);
        }
        // else load the transactions
        else {
            // load transactions from backend and internal storage
            // this will communicate back via the BackendlessDataLoaderInterface (Load successful)
            payments = new ArrayList<>();
            incomes = new ArrayList<>();
            new TransactionsLoader(this).loadTransactions();
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

        initPages(ConstantVariableSettings.NUMBER_OF_MONTHS - 1);
    }

    // this is the method that is called after the transactions are loaded and we are ready to show
    // the user everything in this activity
    // it inits the pagerAdapter and links it to the viewpager, thus creating the monthly pages
    private void initPages(int startingMonth) {
        myPagerAdapter = new MyPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), payments, incomes, this, startingMonth);
        viewPager.setAdapter(myPagerAdapter);
    }

    @Override
    public void loadFailed() {
        System.out.println("loading of Transactions failed");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MonthlyTabFragment.TRANSACTION_REMOVED_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                removeTransaction((Transaction) data.getSerializableExtra(MonthlyTabFragment.VIEW_TRANSACTION_CODE));
            }
        }
    }

    // removes the parameter transaction from list and saves the new list in to the internal storage
    private void removeTransaction(Transaction transactionToRemove) {
        System.out.println("got to removeTransaction");
        if (transactionToRemove.isPayment()) {
            System.out.println("want to remove payment");
            System.out.println(payments.remove(transactionToRemove));
            System.out.println("over and out");
        } else {
            System.out.println("want to remove income");
            System.out.println(incomes.remove(transactionToRemove));
            System.out.println("over and out");

        }
        // combine the lists and save the transactions in internal storage
        payments.addAll(incomes);
        TransactionsLoader.saveTransactions(this, payments, ConstantVariableSettings.TRANSACTIONS_KEY_STRING);

        // then reload the activity so that the removed transaction doesn't show up anymore
        finish();
        startActivity(getIntent());
    }


    public void showStatistics(View view) {
        Intent showStatisticsIntent = new Intent(this, ShowStatisticsActivity.class);

        // send the loaded transactions so the activity doesn't have to load them all over again from everywhere
        showStatisticsIntent.putExtra(ConstantVariableSettings.TRANSACTION_LIST_PAYMENTS, payments);
        showStatisticsIntent.putExtra(ConstantVariableSettings.TRANSACTION_LIST_INCOMES, incomes);

        startActivity(showStatisticsIntent);
        finish();
    }

}
