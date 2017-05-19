package payment.history;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikko.budgetapplication.R;

import data.Transaction;

/**
 * Created by Mikko on 18.5.2017.
 */

public class TransactionViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static final String VIEW_TRANSACTION_CODE = "1";

    private Transaction transaction;

    public TransactionItemButton transactionAmount;
    private Context context;


    public TransactionViewHolders(View itemView, Context context) {
        super(itemView);
        this.context = context;
        transactionAmount = (TransactionItemButton) itemView.findViewById(R.id.transaction_item_button);
        //authorName = (TextView) itemView.findViewById(R.id.AuthorName);

        transactionAmount.setOnClickListener(this);
    }

    // if the user clicks the transaction, open a new activity
    // that gives user more indepth information about that transaction
    @Override
    public void onClick(View view) {
        Intent viewTransaction = new Intent(context, ViewTransactionActivity.class);
        viewTransaction.putExtra(VIEW_TRANSACTION_CODE, transaction);
        context.startActivity(viewTransaction);

        /*
        Toast.makeText(view.getContext(),
                "Clicked Position = " + getPosition() + " with the amount " + transactionAmount.getText().toString(), Toast.LENGTH_SHORT)
                .show();
                */

    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
