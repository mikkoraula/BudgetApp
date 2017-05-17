package data;

import java.io.Serializable;

/**
 * Created by Mikko on 8.8.2016.
 */
public class TransactionType implements Serializable {
    private String name;
    private int colorId;
    private boolean payment;

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean isPayment) {
        this.payment = isPayment;
    }
}
