package payment.history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import data.Income;
import data.Transaction;
import datahandler.BackendlessDataLoader;
import datahandler.BackendlessDataLoaderInterface;

import com.backendless.Backendless;
import com.example.mikko.budgetapplication.DialogHelper;
import com.example.mikko.budgetapplication.MainActivity;
import com.example.mikko.budgetapplication.MyBaseActivity;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.Payment;
import datahandler.BackendlessTransactionLoader;

/**
 * Created by Mikko on 6.7.2016.
 */
public class ShowHistoryActivity extends MyBaseActivity implements BackendlessDataLoaderInterface<Transaction> {

    private ArrayList<Payment> paymentList;
    private ArrayList<Income> incomeList;

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

    private void loadTransactions() {
        SharedPreferences lastLoginPreferences = getSharedPreferences(MainActivity.PREFERENCE_KEY_LAST_LOGIN, MODE_PRIVATE);
        long lastLoginInMillis = lastLoginPreferences.getLong(Backendless.UserService.CurrentUser().getUserId(), 0);
        System.out.println("last login in millis: " + lastLoginInMillis);


        BackendlessTransactionLoader backendlessTransactionLoader = new BackendlessTransactionLoader(this);
        backendlessTransactionLoader.loadPayments(lastLoginInMillis);

        paymentList = new ArrayList<>();
        incomeList = new ArrayList<>();
    }

    @Override
    public void loadSuccessful(ArrayList<Transaction> loadedTransactionList) {
        if (loadedTransactionList.size() == 0) {
            System.out.println("didn't find any of that transaction in backendlessload");
        }
        else if (loadedTransactionList.get(0) instanceof Payment) {
            // at this point we have loaded all the
            System.out.println("found payments!!!!");
            for (Transaction transaction : loadedTransactionList) {
                System.out.println("Transaction: " + transaction);
                paymentList.add((Payment)transaction);
                System.out.println("amount: " + transaction.getAmount());
                System.out.println("paymentType name: " + ((Payment) transaction).getPaymentType().getName());
            }
        }
        else {
            // at this point we have loaded all the
            System.out.println("found incomes!!!!");
            for (Transaction transaction : loadedTransactionList) {
                incomeList.add((Income)transaction);
                System.out.println("amount: " + transaction.getAmount());
                System.out.println("incomeType name: " + ((Income) transaction).getIncomeType().getName());
            }
        }
    }

    @Override
    public void loadFailed() {

    }
}
