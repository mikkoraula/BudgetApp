package datahandler;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.exceptions.BackendlessFault;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;
import java.util.Iterator;

import data.Payment;

/**
 * Created by Mikko on 6.8.2016.
 */
public class BackendlessPaymentLoader {


    public <T> void loadPayments(Context context, Class<T> typeClass) {

        // page size is 10 by default
        //Backendless.Data.of( typeClass ).find(createPaymentLoadingCallback(context));
    }

    private static LoadingCallback<BackendlessCollection<Payment>> createPaymentLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<Payment>>(context, context.getString(R.string.loading_empty)) {
            @Override
            public void handleResponse( BackendlessCollection<Payment> paymentCollection) {
                super.handleResponse(paymentCollection);

                System.out.println( "Loaded " + paymentCollection.getCurrentPage().size() + " restaurant objects" );
                System.out.println("Total restaurants in the Backendless storage - " + paymentCollection.getTotalObjects());

                Toast.makeText(context, "save successful!", Toast.LENGTH_SHORT).show();

                ArrayList<Payment> payments = new ArrayList<>();
                Iterator<Payment> iterator = paymentCollection.getCurrentPage().iterator();
                while( iterator.hasNext() ) {
                    Payment currentPayment = iterator.next();
                    payments.add(currentPayment);

                }

                // is this the best way to do this?
                ((BackendlessDataLoaderInterface) context).loadSuccessful(payments);
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataSaverInterface) context).saveFailed();
            }
        };
    }


}
