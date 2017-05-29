package payment.history;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.Transaction;
import data.TransactionData;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Mikko on 2.8.2016.
 */
public class MonthlyTabFragment extends Fragment {
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

        recyclerViewInitiated = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_tab3, container, false);

        // update the header
        textView = (TextView) view.findViewById(R.id.monthly_tab_fragment_header_text_view);
        textView.setText(tabName);

        if (!recyclerViewInitiated) {
            //initRecyclerView(view);



            initLinearLayout(view, payments, R.id.monthly_tab_fragment_payments_linear_layout);
            initLinearLayout(view, incomes, R.id.monthly_tab_fragment_incomes_linear_layout);

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
            linearLayout.addView(createTransactionItemButton(transactions.get(i)));
        }
    }

    /**
     * Creates a button based on the transaction given as a parameter
     *
     * Sets the text for it, changes its size based on the transaction amount
     * changes the background color based on its transaction type
     *
     * @param transaction
     * @return
     */
    private Button createTransactionItemButton(final Transaction transaction) {
        // create button
        Button transactionItemButton = new Button(getContext());

        // set the text
        transactionItemButton.setText(String.valueOf(transaction.getAmount()));

        // set the background resource from drawable xml file
        // this sets some style to the buttons (border)
        transactionItemButton.setBackgroundResource(R.drawable.transaction_item_button);
        // now we need to change the background color of this resource to match the transaction type
        int colorId = transaction.getTransactionType().getColorId();
        GradientDrawable backgroundShape = (GradientDrawable) transactionItemButton.getBackground().getCurrent();
        // this mutate() is required to differentiate all the different transactions to have separate shapes as resources.
        backgroundShape.mutate();
        backgroundShape.setColor(ContextCompat.getColor(getContext(), colorId));
        //transactionItemButton.setBackgroundColor(ContextCompat.getColor(getContext(), colorId));

        // set the size to match the transaction amount
        transactionItemButton.setMinHeight(0);
        transactionItemButton.setMinimumHeight(0);
        transactionItemButton.setHeight((int) transaction.getAmount() * 3);


        // finally add an on click listener to the button
        transactionItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewTransaction = new Intent(getContext(), ViewTransactionActivity.class);
                viewTransaction.putExtra(VIEW_TRANSACTION_CODE, transaction);
                // we expect the viewTransaction to respond when finished
                // we catch this response in the ShowHistory class (the context of this Activity)
                // so the Showhistory class's method onActivityResult catches this
                getActivity().startActivityForResult(viewTransaction, TRANSACTION_REMOVED_CODE);
            }
        });

        return transactionItemButton;
    }


}
