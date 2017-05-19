package payment.history;

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

    private Transaction transaction;

    public TextView transactionAmount;
    //public TextView authorName;


    public TransactionViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        transactionAmount = (TextView) itemView.findViewById(R.id.transaction_item_button);
        //authorName = (TextView) itemView.findViewById(R.id.AuthorName);
    }

    @Override
    public void onClick(View view)
    {
        Toast.makeText(view.getContext(),
                "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT)
                .show();
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
