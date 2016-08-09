package data;

import java.util.Date;

/**
 * Created by Mikko on 1.7.2016.
 *
 * the basic payment object, these are saved in backendless
 */
public class Payment extends Transaction {
    private String location;
    private PaymentType paymentType;

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
