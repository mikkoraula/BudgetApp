package payment.history;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.Transaction;

/**
 * Created by Mikko on 18.5.2017.
 */

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<TransactionViewHolders> {
    private ArrayList<Transaction> combinedList;


    private Context context;
    public HistoryRecyclerViewAdapter(Context context, ArrayList<Transaction> payments, ArrayList<Transaction> incomes) {
        combineTransactions(payments, incomes);
        this.context = context;
    }

    @Override
    public TransactionViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.transaction_item, null);
        TransactionViewHolders rcv = new TransactionViewHolders(layoutView, context);
        return rcv;
    }


    // this method combines the payments and incomes into one big list
    // that alternates items with payment and income
    // this makes it easier to feed the list to the Recycler View
    // because there most likely will not be the same amount of payments and incomes
    // we need to create "dummy" transactions to even it out
    private void combineTransactions(ArrayList<Transaction> payments, ArrayList<Transaction> incomes) {
        // calculate how many items there will be in the final hybridlist
        int iterationCount;
        if (payments.size() > incomes.size()) {
            iterationCount = payments.size();
        } else {
            iterationCount = incomes.size();
        }

        // init the new list
        combinedList = new ArrayList<>();

        for (int i = 0; i < iterationCount; i++) {
            // alternate between payment and income, start with payment
            Transaction currentPayment = null;

            if (i < payments.size()) currentPayment = payments.get(i);
           combinedList.add(currentPayment);

            Transaction currentIncome = null;
            if (i < incomes.size()) currentIncome = incomes.get(i);
            combinedList.add(currentIncome);
        }
    }

    private Transaction createDummyTransaction() {
        return null;
    }


    // will change between payment and income
    // if position divisible with 2 -> means it's a payment (left column)
    // else it's a income (right column)
    @Override
    public void onBindViewHolder(TransactionViewHolders holder, int position) {
        Transaction currentTransaction = combinedList.get(position);
        if (currentTransaction != null) {


            // set the transaction amount
            holder.setTransaction(currentTransaction);
            holder.transactionAmount.setText("" + currentTransaction.getAmount());


            // check if this transaction is just a placeholder null (= there are no payments or incomes)
            if (currentTransaction.getTransactionType() != null) {
                // set the background color to match the transactiontype's color
                int colorId = currentTransaction.getTransactionType().getColorId();
                holder.transactionAmount.setBackgroundColor(ContextCompat.getColor(context, colorId));
            }

            // set the size to match the transaction amount
            holder.transactionAmount.setMinHeight(0);
            holder.transactionAmount.setMinimumHeight(0);
            holder.transactionAmount.setHeight((int) currentTransaction.getAmount() * 3);
        }
    }

    // this defines how many items there will be in the recycler view
    // so we check whether there are more payments or incomes
    // and based on that define the recycler view's size to twice the size of the bigger list
    @Override
    public int getItemCount() {
        /*
        if (payments.size() > incomes.size()) {
            return payments.size() * 2;
        } else {
            return incomes.size() * 2;
        }
        */
        return combinedList.size();
    }

}
