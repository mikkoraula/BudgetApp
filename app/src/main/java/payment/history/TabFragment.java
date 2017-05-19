package payment.history;

/**
 * Created by Mikko on 1.8.2016.
 */
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mikko.budgetapplication.R;
import com.example.mikko.budgetapplication.SharedPreferencesHandler;
import com.example.mikko.budgetapplication.SharedPreferencesSettings;

import java.util.ArrayList;

import data.Transaction;
import data.TransactionData;

public class TabFragment extends Fragment {

    private String tabName = "";
    private int numberOfMonths;
    private int currentPosition, previousPosition;

    private ArrayList<Transaction> payments;
    private ArrayList<Transaction> incomes;

    private ViewPager monthlyViewPager;
    private MonthlyPagerAdapter monthlyPagerAdapter;


    // newInstance constructor for creating fragment with arguments
    public static TabFragment newInstance(String title, int numberOfMonths, TransactionData paymentData, TransactionData incomeData) {
        TabFragment tabFragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString("someTitle", title);
        args.putInt("someInt", numberOfMonths);
        args.putSerializable("paymentData", paymentData);
        args.putSerializable("incomeData", incomeData);
        tabFragment.setArguments(args);
        return tabFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabName = getArguments().getString("someTitle");
        numberOfMonths = getArguments().getInt("someInt");
        currentPosition = numberOfMonths - 1;

        // delete the month pointer from last use and set it to the current month
        SharedPreferences.Editor editor = SharedPreferencesHandler.getEditor(getContext(), SharedPreferencesSettings.MONTH_TAB_FRAGMENT_KEY);
        editor.putInt(SharedPreferencesSettings.MONTH_TAB_FRAGMENT_CURRENT_MONTH_INT, numberOfMonths - 1);
        editor.apply();

        payments = ((TransactionData)getArguments().getSerializable("paymentData")).getTransactionList();
        incomes = ((TransactionData)getArguments().getSerializable("incomeData")).getTransactionList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment, container, false);

        //System.out.println("payments in TabFragment: " + payments);

        if (payments != null) {
            initMonths(view);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("RESUMED TABFRAGMENT");
        initMonths(null);
    }

    public void initMonths(View view) {
        if (view == null) {
            view = getView();
        }
        monthlyViewPager = (ViewPager) view.findViewById(R.id.monthly_view_pager);
        monthlyPagerAdapter = new MonthlyPagerAdapter
                (getChildFragmentManager(), numberOfMonths, payments, incomes);
        monthlyViewPager.setAdapter(monthlyPagerAdapter);

        System.out.println(" ");
        System.out.println(" ");

        // get the month position from shared preferences
        // this is to make sure that when the user changes from tabs (shared, personal both)
        // the month stays the same and not resetting
        int currentMonthPosition = SharedPreferencesHandler.getInt(
                getContext(), SharedPreferencesSettings.MONTH_TAB_FRAGMENT_KEY, SharedPreferencesSettings.MONTH_TAB_FRAGMENT_CURRENT_MONTH_INT);
        System.out.println("set currentPosition for monthlyViewPager: "+  currentMonthPosition);
        monthlyViewPager.setCurrentItem(currentMonthPosition);
        System.out.println("currentItem after setting it: " + monthlyViewPager.getCurrentItem());

        monthlyViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            // every time the user changes the month, save the monthPosition in shared preferences
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != currentPosition) {
                    currentPosition = position;
                    SharedPreferences.Editor editor = SharedPreferencesHandler.getEditor(getContext(), SharedPreferencesSettings.MONTH_TAB_FRAGMENT_KEY);
                    editor.putInt(SharedPreferencesSettings.MONTH_TAB_FRAGMENT_CURRENT_MONTH_INT, currentPosition);
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
}