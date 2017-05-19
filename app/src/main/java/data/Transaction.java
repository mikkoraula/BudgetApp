package data;

import java.io.Serializable;

/**
 * A transaction holds the following attributes:
 * - amount             the transaction amount
 * - ownerId              who created the transaction
 * - shared           if the transaction is shared with everyone(true) or private(false)
 * - payment          if the transaction is a payment or an income
 * - dateInMillis       Date when created in milliseconds
 * - transactionType    tells the transaction type (for example groceries)
 *
 * Created by Mikko on 8.8.2016.
 */
public class Transaction implements Serializable {
    private double amount;
    private String ownerId;
    private boolean shared;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }
}
