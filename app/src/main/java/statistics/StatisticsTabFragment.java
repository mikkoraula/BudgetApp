package statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.Transaction;
import data.TransactionData;

/**
 * Created by Mikko on 1.6.2017.
 */

public class StatisticsTabFragment extends Fragment {
    public static final String VIEW_TRANSACTION_CODE = "1";
    public static final int TRANSACTION_REMOVED_CODE = 2;

    private String tabName = "";
    private TextView textView;

    private boolean recyclerViewInitiated;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    /*
    private ListView paymentsListView;
    private ListView incomesListView;
    private TransactionItemAdapter paymentItemAdapter;
    private TransactionItemAdapter incomeItemAdapter;
    */

    private ArrayList<Transaction> payments, incomes;

    // newInstance constructor for creating fragment with arguments
    public static StatisticsTabFragment newInstance(String monthTitle, TransactionData monthPaymentData, TransactionData monthIncomeData) {
        StatisticsTabFragment statisticsTabFragment = new StatisticsTabFragment();
        Bundle monthArgs = new Bundle();
        monthArgs.putString("someMonthTitle", monthTitle);
        monthArgs.putSerializable("monthPaymentData", monthPaymentData);
        monthArgs.putSerializable("monthIncomeData", monthIncomeData);
        statisticsTabFragment.setArguments(monthArgs);
        return statisticsTabFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabName = getArguments().getString("someMonthTitle");
        payments = ((TransactionData) getArguments().getSerializable("monthPaymentData")).getTransactionList();
        incomes = ((TransactionData) getArguments().getSerializable("monthIncomeData")).getTransactionList();

        recyclerViewInitiated = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_tab, container, false);

        // update the header
        textView = (TextView) view.findViewById(R.id.statistics_tab_fragment_header_text_view);
        textView.setText(tabName);

        if (!recyclerViewInitiated) {
            //initRecyclerView(view);
            ((TextView) view.findViewById(R.id.statistics_tab_fragment_text_view)).setText("lelelel");


            //initLinearLayout(view, payments, R.id.monthly_tab_fragment_payments_linear_layout);
            //initLinearLayout(view, incomes, R.id.monthly_tab_fragment_incomes_linear_layout);

            recyclerViewInitiated = true;
        }

        container.setTag(tabName);

        return view;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTransactions(ArrayList<Transaction> payments, ArrayList<Transaction> incomes) {
        this.payments = payments;
        this.incomes = incomes;

        //System.out.println("got " + payments.size() + " payments in MonthlyFragment: " + (getArguments().getString("someMonthTitle")));
    }




    /**
     * inits a transaction linear layout
     * takes in as parameter the transactionlist (payments or incomes) and the linearlayout's id
     *
     * Adds a TransactionItemButton for each transaction
     */
    private void initLinearLayout(View view, ArrayList<Transaction> transactions, int layoutId) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(layoutId);

        for (int i = 0; i < transactions.size(); i++) {
            //linearLayout.addView(createTransactionItemButton(transactions.get(i)));
        }
    }

}
