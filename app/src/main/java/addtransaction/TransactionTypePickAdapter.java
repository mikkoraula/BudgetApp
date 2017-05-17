package addtransaction;

import data.TransactionType;

/**
 * Created by Mikko on 10.5.2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

/**
 * Created by Mikko on 15.7.2016.
 */
public class TransactionTypePickAdapter extends ArrayAdapter<TransactionType> {

    private Context context;
    private View.OnClickListener onClickListener;
    private int resource;
    private ArrayList<TransactionType> transactionTypes;

    private int numberOfTypes;
    private TransactionTypeToggleButton currentButton;
    private TransactionType currentType;

    // if the gridview's last button is a button to add more
    private boolean hasAddButton;

    public TransactionTypePickAdapter(Context context, View.OnClickListener onClickListener, int resource, ArrayList<TransactionType> transactionTypes, int numberOfTypes, boolean hasAddButton) {
        super(context, resource, transactionTypes);
        this.context = context;
        this.onClickListener = onClickListener;
        this.resource = resource;
        this.transactionTypes = transactionTypes;
        this.numberOfTypes = numberOfTypes;
        this.hasAddButton = hasAddButton;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // in this case this view is just another transaction type
        if (transactionTypes.get(position) != null) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_type, parent, false);
            }

            TransactionType transactionType = transactionTypes.get(position);


            final TransactionTypeToggleButton toggleButton = (TransactionTypeToggleButton) convertView.findViewById(R.id.transaction_type_button);

            toggleButton.setText(transactionType.getName());
            toggleButton.setTextOn(transactionType.getName());
            toggleButton.setTextOff(transactionType.getName());
            toggleButton.setTransactionType(transactionType);


            //FrameLayout frameLayout = (FrameLayout) convertView.findViewById(R.id.payment_type_picker_frame_layout);
            toggleButton.setBackgroundColor(ContextCompat.getColor(context, transactionType.getColorId()));
            //System.out.println("colorId: " + paymentType.getColorId());
            //System.out.println("color: " + ContextCompat.getColor(context, paymentType.getColorId()));


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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_transaction_type, parent, false);
            }
            Drawable addPaymentTypeIcon = convertView.getResources().getDrawable(R.drawable.ic_add_transaction_type);
            ImageButton addPaymentTypeButton = (ImageButton) convertView.findViewById(R.id.add_transaction_type_button);
            addPaymentTypeIcon.setColorFilter(new LightingColorFilter(Color.BLACK, ContextCompat.getColor(context, R.color.colorPrimary)));
            addPaymentTypeButton.setImageDrawable(addPaymentTypeIcon);
            addPaymentTypeButton.setOnClickListener(onClickListener);
        }
            return convertView;
    }

    public TransactionTypeToggleButton getCurrentButton() {
        return currentButton;
    }

    public void setTransactionTypes(ArrayList<TransactionType> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    public TransactionType getCurrentType() {
        return currentButton.getTransactionType();
    }




}
