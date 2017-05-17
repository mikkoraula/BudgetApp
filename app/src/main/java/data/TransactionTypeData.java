package data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mikko on 8.5.2017.
 */

public class TransactionTypeData implements Serializable {
    private ArrayList<TransactionType> transactionTypeList;


    public ArrayList<TransactionType> getTransactionTypeList() {
        return transactionTypeList;
    }

    public void setTransactionTypeList(ArrayList<TransactionType> transactionTypeList) {
        this.transactionTypeList = transactionTypeList;
    }
}
