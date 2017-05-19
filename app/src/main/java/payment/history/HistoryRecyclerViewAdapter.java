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
    private ArrayList<Transaction> payments, incomes;


    private Context context;
    public HistoryRecyclerViewAdapter(Context context, ArrayList<Transaction> payments, ArrayList<Transaction> incomes) {
        this.payments = payments;
        this.incomes = incomes;
        this.context = context;
    }

    @Override
    public TransactionViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.transaction_item, null);
        TransactionViewHolders rcv = new TransactionViewHolders(layoutView);
        return rcv;
    }


    // will change between payment and income
    // if position dvisible with 2 -> means it's a payment (left column)
    // else it's a income (right column)
    @Override
    public void onBindViewHolder(TransactionViewHolders holder, int position) {
        Transaction currentTransaction;
        if (position % 2 == 0) {
           currentTransaction = payments.get(position / 2);
        } else {
            currentTransaction = incomes.get(position / 2);
        }

        // set the transaction amount
        holder.setTransaction(currentTransaction);
        holder.transactionAmount.setText("" + currentTransaction.getAmount());

        // set the background color to match the transactiontype's color
        int colorId = currentTransaction.getTransactionType().getColorId();
        holder.transactionAmount.setBackgroundColor(ContextCompat.getColor(context, colorId));

        // set the size to match the transaction amount
        holder.transactionAmount.setMinHeight(0);
        holder.transactionAmount.setMinimumHeight(0);
        holder.transactionAmount.setHeight((int)currentTransaction.getAmount() *  3);
    }

    @Override
    public int getItemCount() {
        return payments.size() + incomes.size();
    }

}
