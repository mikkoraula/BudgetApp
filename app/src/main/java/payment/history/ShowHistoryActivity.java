package payment.history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import data.Income;
import data.Transaction;
import datahandler.BackendlessDataLoaderInterface;

import com.example.mikko.budgetapplication.DialogHelper;
import com.example.mikko.budgetapplication.MyBaseActivity;
import com.example.mikko.budgetapplication.R;
import com.example.mikko.budgetapplication.SharedPreferencesHandler;
import com.example.mikko.budgetapplication.SharedPreferencesSettings;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;

import data.Payment;
import datahandler.BackendlessTransactionLoader;

/**
 * Created by Mikko on 6.7.2016.
 */
public class ShowHistoryActivity extends MyBaseActivity implements BackendlessDataLoaderInterface<Transaction> {

    private ArrayList<Transaction> paymentList;
    private ArrayList<Transaction> incomeList;

    private int loadCounter;

    private Date currentLoadDate;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadTransactions();


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Both"));
        tabLayout.addTab(tabLayout.newTab().setText("Personal"));
        tabLayout.addTab(tabLayout.newTab().setText("Shared"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               viewPager.requestDisallowInterceptTouchEvent(true);
            }
        });


        //this line may be useless
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            DialogHelper.createHelpDialog(this, getString(R.string.toolbar_help), getString(R.string.help_show_history)).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /*********************************************************
     *
     *  LOADING  PAYMENTS AND INCOMES
     *
     */

    private void loadTransactions() {
        System.out.println(" ");
        System.out.println(" ");
        paymentList = new ArrayList<>();
        incomeList = new ArrayList<>();
        loadCounter = 0;

        currentLoadDate = new Date();
        long lastBackendlessLoadInMillis = SharedPreferencesHandler.getLong(
                this, SharedPreferencesSettings.TRANSACTIONS_KEY, SharedPreferencesSettings.LAST_BACKENDLESS_LOAD_LONG);

        // load the old payments from internal storage
        paymentList = TransactionDataHandler.loadTransactions(this, SharedPreferencesSettings.PAYMENTS_KEY_STRING);
        // load the newest payments from Backendless
        System.out.println("last payments load in millis: " + lastBackendlessLoadInMillis);
        BackendlessTransactionLoader backendlessPaymentsLoader = new BackendlessTransactionLoader(this);
        backendlessPaymentsLoader.loadPayments(lastBackendlessLoadInMillis);

        // do the same with incomes
        incomeList = TransactionDataHandler.loadTransactions(this, SharedPreferencesSettings.INCOMES_KEY_STRING);
        System.out.println("last incomes load in millis: " + lastBackendlessLoadInMillis);
        BackendlessTransactionLoader backendlessIncomesLoader = new BackendlessTransactionLoader(this);
        backendlessIncomesLoader.loadIncomes(lastBackendlessLoadInMillis);
    }

    @Override
    public void loadSuccessful(ArrayList<Transaction> loadedTransactionList) {
        loadCounter++;
        if (loadedTransactionList.size() == 0) {
            System.out.println("didn't find any of that transaction in backendlessload");
        }
        else {
            if (loadedTransactionList.get(0) instanceof Payment) {
                // at this point we have loaded all the
                System.out.println("found payments!!!!");
                for (Transaction transaction : loadedTransactionList) {
                    System.out.println("Transaction: " + transaction);
                    paymentList.add(transaction);
                    System.out.println("amount: " + transaction.getAmount());
                    System.out.println("paymentType name: " + ((Payment) transaction).getPaymentType().getName());
                }
                // immediately save the payments to internal
                TransactionDataHandler.saveTransactions(
                        this, paymentList, SharedPreferencesSettings.PAYMENTS_KEY_STRING);

            } else {
                // at this point we have loaded all the
                System.out.println("found incomes!!!!");
                for (Transaction transaction : loadedTransactionList) {
                    incomeList.add(transaction);
                    System.out.println("amount: " + transaction.getAmount());
                    System.out.println("incomeType name: " + ((Income) transaction).getIncomeType().getName());
                }
                // immediately save the incomes to internal
                TransactionDataHandler.saveTransactions(
                        this, incomeList, SharedPreferencesSettings.INCOMES_KEY_STRING);
            }

            SharedPreferences.Editor editor = SharedPreferencesHandler.getEditor(
                    this, SharedPreferencesSettings.TRANSACTIONS_KEY);
            editor.putLong(SharedPreferencesSettings.LAST_BACKENDLESS_LOAD_LONG, currentLoadDate.getTime());
            editor.apply();
            System.out.println("saved the new loaded date");
        }

        // this means we have loaded both payments and incomes
        if (loadCounter == 2) {
            Toast.makeText(this, "Payments and Incomes loaded successfully!", Toast.LENGTH_SHORT).show();
        }

        System.out.println("after alll the loading, payments size: " + paymentList.size());
    }

    @Override
    public void loadFailed() {
        System.out.println("loading of Transactions failed");
    }
}
