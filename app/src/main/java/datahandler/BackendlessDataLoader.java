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

import data.IncomeType;
import data.PaymentType;

/**
 * Created by Mikko on 29.7.2016.
 */
public class BackendlessDataLoader {

    public static void loadPaymentTypes(Context context) {
        /*
        int PAGESIZE = 20;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setPageSize(PAGESIZE);
        Backendless.Data.of( PaymentType.class ).find( dataQuery, createLoadingCallback(context) );
        */

        // page size is 10 by default
        Backendless.Data.of( PaymentType.class ).find(createPaymentTypeLoadingCallback(context));
    }

    private static LoadingCallback<BackendlessCollection<PaymentType>> createPaymentTypeLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<PaymentType>>(context, context.getString(R.string.loading_empty)) {
            @Override
            public void handleResponse( BackendlessCollection<PaymentType> paymentTypeCollection) {
                super.handleResponse(paymentTypeCollection);
                Toast.makeText(context, "save successful!", Toast.LENGTH_SHORT).show();

                ArrayList<PaymentType> paymentTypes = new ArrayList<>();
                Iterator<PaymentType> iterator = paymentTypeCollection.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    PaymentType paymentType = iterator.next();
                    paymentTypes.add(paymentType);
                }

                // is this the best way to do this?
                ((BackendlessDataLoaderInterface) context).loadSuccessful(paymentTypes);
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataSaverInterface) context).saveFailed();
            }
        };
    }

    public static void loadIncomeTypes(Context context) {
        /*
        int PAGESIZE = 20;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setPageSize(PAGESIZE);
        Backendless.Data.of( PaymentType.class ).find( dataQuery, createLoadingCallback(context) );
        */

        // page size is 10 by default
        Backendless.Data.of( IncomeType.class ).find(createIncomeTypeLoadingCallback(context));
    }

    private static LoadingCallback<BackendlessCollection<IncomeType>> createIncomeTypeLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<IncomeType>>(context, context.getString(R.string.loading_empty)) {
            @Override
            public void handleResponse( BackendlessCollection<IncomeType> incomeTypeCollection) {
                super.handleResponse(incomeTypeCollection);
                Toast.makeText(context, "save successful!", Toast.LENGTH_SHORT).show();

                ArrayList<IncomeType> incomeTypes = new ArrayList<>();
                Iterator<IncomeType> iterator = incomeTypeCollection.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    IncomeType incomeType = iterator.next();
                    incomeTypes.add(incomeType);
                }

                // is this the best way to do this?
                ((BackendlessDataLoaderInterface) context).loadSuccessful(incomeTypes);
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataLoaderInterface) context).loadFailed();
            }
        };
    }




    /*
    public static <T> void loadPayments(Context context) {

        // page size is 10 by default
        Backendless.Data.of( Payment.class ).find(createPaymentLoadingCallback(context));
    }

    private static LoadingCallback<BackendlessCollection<Payment>> createPaymentLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<Payment>>(context, context.getString(R.string.loading_empty)) {
            @Override
            public void handleResponse( BackendlessCollection<Payment> paymentTypeCollection) {
                super.handleResponse(paymentTypeCollection);
                Toast.makeText(context, "save successful!", Toast.LENGTH_SHORT).show();

                ArrayList<Payment> payments = new ArrayList<>();
                Iterator<Payment> iterator = paymentTypeCollection.getCurrentPage().iterator();
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
    */
}
