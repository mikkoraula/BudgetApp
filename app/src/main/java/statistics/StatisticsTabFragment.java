package statistics;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mikko.budgetapplication.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import data.Transaction;
import data.TransactionData;

/**
 * Created by Mikko on 1.6.2017.
 *
 * Shows stats about a single month
 */

public class StatisticsTabFragment extends Fragment {
    public static final String VIEW_TRANSACTION_CODE = "1";
    public static final int TRANSACTION_REMOVED_CODE = 2;

    private String tabName = "";
    private TextView textView;

    private boolean statsInitiated;

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

        statsInitiated = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_tab, container, false);

        // update the header to correspond the month and year
        textView = (TextView) view.findViewById(R.id.statistics_tab_fragment_header_text_view);
        textView.setText(tabName);

        if (!statsInitiated) {
            initStatistics(view, payments, incomes);
            initDistribution(view, payments, R.id.fragment_statistics_linear_layout_payments_distribution);
            initDistribution(view, incomes, R.id.fragment_statistics_linear_layout_incomes_distribution);

            statsInitiated = true;
        }

        container.setTag(tabName);

        return view;
    }


    /**
     *  Sets text to all the information text views:
     *
     *  info1: incomes total this month
     *  info2: payments total this month
     *
     *  info3: total incomes - total payments       (= did you lose or gain money this month)
     *
     *  info4: who owes who and how much
     *
     */
    private void initStatistics(View view, ArrayList<Transaction> payments, ArrayList<Transaction> incomes) {
        // get the information texts
        TextView info1 = (TextView) view.findViewById(R.id.statistics_tab_fragment_info1);
        TextView info2 = (TextView) view.findViewById(R.id.statistics_tab_fragment_info2);
        TextView info3 = (TextView) view.findViewById(R.id.statistics_tab_fragment_info3);
        TextView info4 = (TextView) view.findViewById(R.id.statistics_tab_fragment_info4);

        /**
         * infos 1-2
         */
        // calculate some helper values
        double totalIncome = sumTransactions(incomes, "", false);
        double totalPayments = sumTransactions(payments, "", false);
        // set the values to the strings
        // bold the values by using the method
        info1.append(getStringBuilderWithBold(" " + String.valueOf(totalIncome)));
        info2.append(getStringBuilderWithBold(" " + String.valueOf(totalPayments)));

        /**
         * info 3
         */
        info3.append(getStringBuilderWithBold(" " + String.valueOf(totalIncome - totalPayments)));

        /**
         * info 4
         */
        // fetch all the transactions that are marked "shared"
        //ArrayList<Transaction> sharedPayments = getTransactionsWithVisibility(payments, true);
        //ArrayList<Transaction> sharedIncomes = getTransactionsWithVisibility(incomes, true);
        //double allShared

    }

    /**
     * inits the linear layout that shows the distribution of this month's transactions sorted by transactiontypes
     * @param view
     * @param transactions
     * @param linearLayoutId
     */
    private void initDistribution(View view, ArrayList<Transaction> transactions, int linearLayoutId) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(linearLayoutId);

        // this list will hold one transaction of each transactiontype
        // each of these transactions will have a summed amount of all the transactions that belong in that transactionType group
        ArrayList<Transaction> distributionList = new ArrayList<>();
        outerLoop:
        for (Transaction transaction : transactions) {
            for (Transaction transactionType : distributionList) {
                // check if there already is a transaction created in the distribution list
                if (transaction.getTransactionType().equals(transactionType.getTransactionType())) {
                    // if the transaction type is already found in the distributionList
                    // then just simply add the amount to that transaction placeholder
                    transactionType.setAmount(transactionType.getAmount() + transaction.getAmount());
                    // and then continue to the next iteration
                    continue outerLoop;
                }
            }
            // if we get this far, it means that the transaction has not yet been created into the distributionList
            distributionList.add(transaction);
        }

        // the distributionList has been initiated

        // sort the list so biggest transaction types are shown first
        Collections.sort(distributionList);

        // now those transactions need to be added to the linearLayout
        for (Transaction transaction : distributionList) {
            linearLayout.addView(createTextView(transaction));

        }
    }

    /**
     *
     * @param transaction
     * @return
     */
    private TextView createTextView(final Transaction transaction) {
        // create button
        TextView view = new TextView(getContext());

        // set the text
        view.setSingleLine(false);
        view.setText(transaction.getTransactionType().getName() + "\n" +
                String.valueOf(transaction.getAmount()));

        // set the background resource from drawable xml file
        // this sets some style to the textview (border)
        view.setBackgroundResource(R.drawable.transaction_item_button);
        // now we need to change the background color of this resource to match the transaction type
        int colorId = transaction.getTransactionType().getColorId();
        GradientDrawable backgroundShape = (GradientDrawable) view.getBackground().getCurrent();
        // this mutate() is required to differentiate all the different transactions to have separate shapes as resources.
        backgroundShape.mutate();
        backgroundShape.setColor(ContextCompat.getColor(getContext(), colorId));
        //transactionItemButton.setBackgroundColor(ContextCompat.getColor(getContext(), colorId));

        // set the size to match the transaction amount
        view.setMinHeight(0);
        view.setMinimumHeight(0);
        view.setHeight((int) transaction.getAmount() * 3);

        return view;
    }

    /**
     * creates a SpannableStringBuilder from the string it gets as a parameter.
     * This builder has a bolded style
     *
     * @param stringToAppend
     * @return A string that has a bold style
     */
    private SpannableStringBuilder getStringBuilderWithBold(String stringToAppend) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(stringToAppend);
        StyleSpan boldStyle = new StyleSpan(android.graphics.Typeface.BOLD);

        stringBuilder.setSpan(boldStyle, 0, stringToAppend.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return stringBuilder;
    }

    /**
     * Simple method that calculates the total value of all transactions in the list it is given.
     *
     * @param transactions a list of transactions
     * @param ownerName    if wants to calculate only transactions of this ownername
     *                     if left empty, calculates every transaction
     * @param onlyShared    if wants to calculate only the transactions that are shared
     * @return sum of the transactions' values
     */
    private double sumTransactions(ArrayList<Transaction> transactions, String ownerName, boolean onlyShared) {
        double sum = 0;
        for (Transaction transaction : transactions) {
            // check if the ownerName is right
            if (ownerName.equals("") || ownerName.equals(transaction.getOwnerName())) {
                // check if wants only shared transactions
                if (onlyShared) {
                    if (transaction.isShared()) {
                        sum += transaction.getAmount();
                    }
                }
                else {
                    sum += transaction.getAmount();
                }
            }
        }
        return sum;
    }

    /**
     * returns a list of transactions that have the same visibility as given in parameter
     *
     * @param transactions
     * @param shared
     * @return
     */
    private ArrayList<Transaction> getTransactionsWithVisibility(ArrayList<Transaction> transactions, boolean shared) {
        ArrayList<Transaction> newList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.isShared() == shared) {
                newList.add(transaction);
            }
        }
        return newList;
    }

}
