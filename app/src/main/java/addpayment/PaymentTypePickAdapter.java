package addpayment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.PaymentType;

/**
 * Created by Mikko on 15.7.2016.
 */
public class PaymentTypePickAdapter extends ArrayAdapter<PaymentType> {

    private Context context;
    private int resource;
    private ArrayList<PaymentType> paymentTypes;

    private int numberOfTypes;
    private PaymentTypeToggleButton currentButton;
    private PaymentType currentType;

    // if the gridview's last button is a button to add more
    private boolean hasAddButton;

    public PaymentTypePickAdapter(Context context, int resource, ArrayList<PaymentType> paymentTypes, int numberOfTypes, boolean hasAddButton) {
        super(context, resource, paymentTypes);
        this.context = context;
        this.resource = resource;
        this.paymentTypes = paymentTypes;
        this.numberOfTypes = numberOfTypes;
        this.hasAddButton = hasAddButton;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // in this case this view is just another payment type
        if (paymentTypes.get(position) != null) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.payment_type, parent, false);
            }

            PaymentType paymentType = paymentTypes.get(position);


            final PaymentTypeToggleButton toggleButton = (PaymentTypeToggleButton) convertView.findViewById(R.id.payment_type_button);

            toggleButton.setText(paymentType.getName());
            toggleButton.setTextOn(paymentType.getName());
            toggleButton.setTextOff(paymentType.getName());
            toggleButton.setPaymentType(paymentType);

            FrameLayout frameLayout = (FrameLayout) convertView.findViewById(R.id.payment_type_picker_frame_layout);
            toggleButton.setBackgroundColor(ContextCompat.getColor(context, paymentType.getColorId()));


            // if no type is yet picked, automatically pick the first one
            if (currentButton == null) {
                currentButton = toggleButton;
                currentButton.setChecked(true);
            }

            toggleButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (currentButton != toggleButton) {
                        currentButton.setChecked(false);
                        currentButton = toggleButton;
                        //currentType =
                    } else {
                        currentButton.setChecked(true);
                    }
                }
            });

        }
        // in this case it's the last view => the add new payment view
        else if (hasAddButton) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_payment_type, parent, false);
            }
            Drawable addPaymentTypeIcon = convertView.getResources().getDrawable(R.drawable.ic_add_payment_type);
            ImageButton addPaymentTypeButton = (ImageButton) convertView.findViewById(R.id.add_payment_type_button);
            addPaymentTypeIcon.setColorFilter(new LightingColorFilter(Color.BLACK, ContextCompat.getColor(context, R.color.colorPrimary)));
            addPaymentTypeButton.setImageDrawable(addPaymentTypeIcon);
        }
            return convertView;
    }

    public PaymentTypeToggleButton getCurrentButton() {
        return currentButton;
    }

    public void setPaymentTypes(ArrayList<PaymentType> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public PaymentType getCurrentType() {
        return currentButton.getPaymentType();
    }



}
