package addtransaction;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mikko.budgetapplication.MyBaseActivity;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.TransactionType;
import data.TransactionTypeData;
import datahandler.BackendlessDataLoader;
import datahandler.BackendlessDataLoaderInterface;
import datahandler.BackendlessDataRemoverInterface;
import datahandler.BackendlessDataSaverInterface;
import payment.history.NonSwipeableViewPager;
import payment.history.ShowHistoryActivity;

/**
 * This class is to add a new transaction to the system.
 *
 */

public class AddTransactionActivity extends MyBaseActivity implements BackendlessDataSaverInterface, BackendlessDataLoaderInterface<TransactionType>, BackendlessDataRemoverInterface {

    private ArrayList<TransactionType> paymentTypes;
    private ArrayList<TransactionType> incomeTypes;
    private int loadCounter;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private NonSwipeableViewPager mViewPager;

    // nothing really interesting happens on Create yet
    // we call the loadTransactionTypes, which initiates the transaction type loading from backendless
    // and then we wait for the loading to be ready to actually start initiating things
    // this is done in the method loadSuccessful()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        setHelpString(R.string.help_add_transaction);



        loadTransactionTypes();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.add_transaction_pager);

        tabLayout = (TabLayout) findViewById(R.id.add_transaction_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Payment"));
        tabLayout.addTab(tabLayout.newTab().setText("Income"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /********************************************************************************
     *
     * Transaction Type stuff
     */

    private void loadTransactionTypes() {
        System.out.println(" ");
        System.out.println(" ");
        paymentTypes = new ArrayList<>();
        incomeTypes = new ArrayList<>();
        loadCounter = 0;

        // load the pre-made transaction types from backendless
        BackendlessDataLoader.loadTransactionTypes(this);
    }

    @Override
    public void loadSuccessful(ArrayList<TransactionType> loadedTransactionTypeList) {
        loadCounter++;
        if (loadedTransactionTypeList.size() == 0) {
            //System.out.println("didn't find any of that transaction type in backendlessload");
        }

        for (TransactionType transactionType : loadedTransactionTypeList) {
            if (transactionType.isPayment()) {
                paymentTypes.add(transactionType);
            } else {
                incomeTypes.add(transactionType);
            }
        }
        System.out.println(" ");
        System.out.println("total paymentTypes: " + paymentTypes.size());
        System.out.println("total incomeTypes: " + incomeTypes.size());
        System.out.println(" ");

        // add a null in to both lists, these are for the add new payment/transactionv type button
        paymentTypes.add(null);
        incomeTypes.add(null);

        // Create the adapter that will return a fragment for both
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                AddTransactionFragment fragment = (AddTransactionFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, tab.getPosition());
                if (fragment != null) {
                    fragment.onResume();
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

    @Override
    public void loadFailed() {
        System.out.println("failed to load paymenttypes :(");
        // probably because no paymentType table in backendless
        // try to create first paymentType:
        //TODOaddPaymentType(null);
    }

    @Override
    public void removeSuccessful() {

    }

    @Override
    public void removeFailed() {

    }

    @Override
    public void saveSuccessful() {
        System.out.println("got back to the activity with successful save");
        // move to history activity
        Intent showHistoryActivityIntent = new Intent(this, ShowHistoryActivity.class);
        startActivity(showHistoryActivityIntent);
        // finish this activity, so that user can skip straight back to main menu
        finish();
    }

    @Override
    public void saveFailed() {
        Log.d("asd", "got back to the activity with FAILED save :(");
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            //return PlaceholderFragment.newInstance(position + 1);


            TransactionTypeData paymentTypeData = new TransactionTypeData();
            TransactionTypeData incomeTypeData = new TransactionTypeData();

            switch (position) {
                case 0:
                    paymentTypeData.setTransactionTypeList(paymentTypes);
                    // create a new fragment(= a new tab)
                    // first parameter tells if the tab is for creating a new Payment (=true)
                    return AddTransactionFragment.newInstance(true, paymentTypeData);
                case 1:
                    incomeTypeData.setTransactionTypeList(incomeTypes);
                    System.out.println("asdfasdfasdf");
                    // isPayment = false
                    return AddTransactionFragment.newInstance(false, incomeTypeData);
                default:
                    return AddTransactionFragment.newInstance(true, paymentTypeData);
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}
