package data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * used when loading and saving transactions from internal storage
 *
 * Created by Mikko on 13.8.2016.
 */
public class TransactionData implements Serializable {

    private ArrayList<Transaction> transactionList;


    public ArrayList<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
