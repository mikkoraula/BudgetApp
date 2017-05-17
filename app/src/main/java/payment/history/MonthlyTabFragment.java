package payment.history;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.Transaction;
import data.TransactionData;

/**
 * Created by Mikko on 2.8.2016.
 */
public class MonthlyTabFragment extends Fragment {
    private String tabName = "";
    private TextView textView;

    private boolean listViewsInitiated;

    private ListView paymentsListView;
    private ListView incomesListView;
    private TransactionItemAdapter paymentItemAdapter;
    private TransactionItemAdapter incomeItemAdapter;

    private ArrayList<Transaction> payments, incomes;

    // newInstance constructor for creating fragment with arguments
    public static MonthlyTabFragment newInstance(String monthTitle, TransactionData monthPaymentData, TransactionData monthIncomeData) {
        MonthlyTabFragment monthlyTabFragment = new MonthlyTabFragment();
        Bundle monthArgs = new Bundle();
        monthArgs.putString("someMonthTitle", monthTitle);
        monthArgs.putSerializable("monthPaymentData", monthPaymentData);
        monthArgs.putSerializable("monthIncomeData", monthIncomeData);
        monthlyTabFragment.setArguments(monthArgs);
        return monthlyTabFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabName = getArguments().getString("someMonthTitle");
        payments = ((TransactionData) getArguments().getSerializable("monthPaymentData")).getTransactionList();
        incomes = ((TransactionData) getArguments().getSerializable("monthIncomeData")).getTransactionList();

        listViewsInitiated = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_tab, container, false);
        textView = (TextView) view.findViewById(R.id.monthly_tab_fragment_header_text_view);
        textView.setText(tabName);

        if (!listViewsInitiated) {
            initListViews(view);
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


    private void initListViews(View view) {
        // payments
        paymentsListView = (ListView) view.findViewById(R.id.monthly_tab_fragment_payments_list_view);
        paymentItemAdapter = new TransactionItemAdapter(
                getActivity(), R.layout.transaction_item, payments, payments.size() - 1
        );
        paymentsListView.setAdapter(paymentItemAdapter);

        // incomes
        incomesListView = (ListView) view.findViewById(R.id.monthly_tab_fragment_incomes_list_view);
        incomeItemAdapter = new TransactionItemAdapter(
                getActivity(), R.layout.transaction_item, incomes, incomes.size() - 1
        );
        incomesListView.setAdapter(incomeItemAdapter);

        listViewsInitiated = true;
    }
}
