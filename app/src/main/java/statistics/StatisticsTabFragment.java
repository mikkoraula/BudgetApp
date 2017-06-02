package statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
