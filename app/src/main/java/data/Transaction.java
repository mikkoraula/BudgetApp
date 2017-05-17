package data;

import java.io.Serializable;

/**
 * A transaction holds the following attributes:
 * - amount             the transaction amount
 * - owner              who created the transaction
 * - isShared           if the transaction is isShared with everyone(true) or private(false)
 * - payment          if the transaction is a payment or an income
 * - dateInMillis       Date when created in milliseconds
 * - transactionType    tells the transaction type (for example groceries)
 *
 * Created by Mikko on 8.8.2016.
 */
public class Transaction implements Serializable {
    private double amount;
    private String owner;
    private boolean isShared;
    private boolean payment;
    private long dateInMilliseconds;
    private TransactionType transactionType;


    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean isShared) {
        this.isShared = isShared;
    }


    public long getDateInMilliseconds() {
        return dateInMilliseconds;
    }

    public void setDateInMilliseconds(long dateInMilliseconds) {
        this.dateInMilliseconds = dateInMilliseconds;
    }

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }
}
