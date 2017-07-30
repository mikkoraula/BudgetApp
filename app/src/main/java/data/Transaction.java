package data;

import java.io.Serializable;
import java.util.Map;

/**
 * A transaction holds the following attributes:
 * - amount             the transaction amount
 * - objectId
 * - ownerId              who created the transaction
 * - userGroupId        to what group the transaction belongs
 * - ownerName          used to simplify transaction information showing
 * - additionalInfo             additional information about the transaction
 *
 * - repetition         if the transaction is repeated ('none', monthly', 'yearly'
 * - shared             if the transaction is shared with everyone(true) or private(false)
 * - payment            if the transaction is a payment or an income
 * - dateInMillis       Date when created in milliseconds
 * - transactionType    tells the transaction type (for example groceries)
 *
 * Created by Mikko on 8.8.2016.
 */
public class Transaction implements Serializable, Comparable<Transaction> {
    private double amount;
    // this has to be in so that you can remove transactions from backendless
    private String objectId;
    private String ownerId;
    private String userGroupId;
    private String ownerName;
    // additional information
    private String additionalInfo;

    private String repetition;
    private boolean shared;
    private boolean payment;
    private long dateInMilliseconds;
    private TransactionType transactionType;

    public Transaction() {

    }

    public Transaction(Map map) {
        setAmount((double) map.get("amount"));
        setObjectId(map.get("objectId").toString());
        setOwnerId(map.get("ownerId").toString());
        setOwnerName(map.get("ownerName").toString());
        setAdditionalInfo(map.get("additionalInfo").toString());
        setShared((Boolean) map.get("shared"));
        setPayment((Boolean) map.get("payment"));
        setDateInMilliseconds((long) map.get("dateInMilliseconds"));
        setTransactionType(new TransactionType((Map) map.get("transactionType")));
    }

    // override the equals method so we can remove transactions from an arraylsit more easily
    // basically just check if the transactions have same objectId
    // the objectId is automatically uniquely determined by Backendless
    @Override
    public boolean equals(Object o) {
        if (o != null)
            if (o instanceof Transaction)
                if (objectId != null)
                    if (((Transaction) o).getObjectId() != null)
                        if (((Transaction) o).getObjectId().equals(objectId))
                            return true;
        return false;
    }

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

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }


    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    @Override
    public int compareTo(Transaction another) {
        if (amount > another.getAmount())
            return 1;
        else if (amount == another.getAmount())
            return 0;
        else
            return -1;
    }

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }
}
