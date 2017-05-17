package payment.history;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import data.Transaction;

/**
 * Created by Mikko on 21.8.2016.
 */
public class TransactionItemButton extends Button {
        private Transaction transaction;

        public TransactionItemButton(Context context) {
            super(context);
        }

        public TransactionItemButton(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public TransactionItemButton(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public Transaction getTransaction() {
            return transaction;
        }

        public void setTransaction(Transaction transaction) {
            this.transaction = transaction;
        }
}
