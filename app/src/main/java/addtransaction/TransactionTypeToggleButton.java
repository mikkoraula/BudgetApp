package addtransaction;

/**
 * Created by Mikko on 10.5.2017.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import data.TransactionType;

public class TransactionTypeToggleButton extends ToggleButton {
    private TransactionType transactionType;

    public TransactionTypeToggleButton(Context context) {
        super(context);
    }

    public TransactionTypeToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TransactionTypeToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}