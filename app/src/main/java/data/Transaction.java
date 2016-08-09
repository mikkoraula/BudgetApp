package data;

import java.util.Date;

/**
 * the parent activity for both payment and income
 *
 * Created by Mikko on 8.8.2016.
 */
public class Transaction {
    protected double amount;
    protected String payer;
    protected boolean shared;

    //protected Date date;

    protected long dateInMilliseconds;

    //protected TransactionType transactionType;

    /*
    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    */
    /*
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    */

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }


    public long getDateInMilliseconds() {
        return dateInMilliseconds;
    }

    public void setDateInMilliseconds(long dateInMilliseconds) {
        this.dateInMilliseconds = dateInMilliseconds;
    }
}
