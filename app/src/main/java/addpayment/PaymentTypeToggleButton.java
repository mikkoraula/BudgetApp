package addpayment;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import data.PaymentType;

public class PaymentTypeToggleButton extends ToggleButton {
    private PaymentType paymentType;

    public PaymentTypeToggleButton(Context context) {
        super(context);
    }

    public PaymentTypeToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaymentTypeToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
