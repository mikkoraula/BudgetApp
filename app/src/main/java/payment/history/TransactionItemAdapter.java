package payment.history;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.Transaction;

/**
 * Created by Mikko on 21.8.2016.
 */
public class TransactionItemAdapter extends ArrayAdapter<Transaction> {

    private final int numberOfTypes;
    private Context context;
    private int resource;
    private ArrayList<Transaction> transactions;

    public TransactionItemAdapter(Context context, int resource, ArrayList<Transaction> sortedTransactions, int numberOfTypes) {
        super(context, resource, sortedTransactions);
        this.context = context;
        this.resource = resource;
        this.transactions = sortedTransactions;
        this.numberOfTypes = numberOfTypes;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_item, parent, false);
        }
        Transaction currentTransaction = transactions.get(position);

        if (currentTransaction != null) {
            final TransactionItemButton button = (TransactionItemButton) convertView.findViewById(R.id.transaction_item_button);
            button.setText(currentTransaction.getAmount() + "");
            button.setTransaction(currentTransaction);

            int colorId = currentTransaction.getTransactionType().getColorId();

            button.setBackgroundColor(ContextCompat.getColor(convertView.getContext(), colorId));

            button.setMinHeight(0);
            button.setMinimumHeight(0);
            button.setHeight((int)currentTransaction.getAmount() *  3);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(button.getTransaction().getAmount() + " clicked......................");
                }
            });
        }

        return  convertView;
    }
}
