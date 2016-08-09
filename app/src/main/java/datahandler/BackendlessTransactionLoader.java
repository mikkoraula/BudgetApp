package datahandler;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Payment;
import payment.history.ShowHistoryActivity;

/**
 * Created by Mikko on 9.8.2016.
 */
public class BackendlessTransactionLoader {

    private Context context;
    private ArrayList<Payment> loadedPaymentList;

    private int requiredPaymentAmount;

    public BackendlessTransactionLoader(Context context) {
        this.context = context;
        loadedPaymentList = new ArrayList<>();
        requiredPaymentAmount = 0;
    }

    public  void loadPayments(long lastLoginDateInMillis) {

        String whereClause = "dateInMilliseconds > " + lastLoginDateInMillis;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        // page size is 10 by default
        Backendless.Data.of( Payment.class ).find(dataQuery, createPaymentLoadingCallback(context));
    }

    private LoadingCallback<BackendlessCollection<Payment>> createPaymentLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<Payment>>(context, context.getString(R.string.loading_empty)) {
            @Override
            public void handleResponse( BackendlessCollection<Payment> paymentCollection) {
                super.handleResponse(paymentCollection);

                System.out.println("Loaded " + paymentCollection.getCurrentPage().size() + " payment objects");
                System.out.println("Total payments in the Backendless storage - " + paymentCollection.getTotalObjects());
                setRequiredPaymentAmount(paymentCollection.getTotalObjects());

                Toast.makeText(context, "load successful!", Toast.LENGTH_SHORT).show();

                Iterator<Payment> iterator = paymentCollection.getCurrentPage().iterator();
                while( iterator.hasNext() ) {
                    Payment currentPayment = iterator.next();
                    List<String> relations = new ArrayList<String>();
                    relations.add( "paymentType" );
                    Backendless.Data.of( Payment.class ).loadRelations(currentPayment, relations, loadRelationsCallback(context));
                    System.out.println("just initiated the nested stiaht");

                }
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataLoaderInterface) context).loadFailed();
            }
        };
    }

    private LoadingCallback<Payment> loadRelationsCallback(final Context context) {
        return new LoadingCallback<Payment>(context) {
            @Override
            public void handleResponse( Payment payment ) {
                //System.out.println( "\nRestaurant name = " + restaurant.getName() );
                System.out.println("loaded relations of payment: " + payment);
                System.out.println("payments type: " + payment.getPaymentType());
                addPayment(payment);
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault ) {
                super.handleFault(backendlessFault);
                System.out.println("epic fail XD");
            }
        };
    }

    public void setRequiredPaymentAmount(int requiredPaymentAmount) {
        this.requiredPaymentAmount = requiredPaymentAmount;
    }

    public void addPayment(Payment payment) {
        System.out.println("addPayment method payment: " + payment);
        System.out.println("loadedPaymentList: " + loadedPaymentList);
        loadedPaymentList.add(payment);

        if (loadedPaymentList.size() == requiredPaymentAmount) {
            System.out.println("finally loaded enough payments!!!!");
            ((BackendlessDataLoaderInterface) context).loadSuccessful(loadedPaymentList);
        } else {
            System.out.println("loaded " + loadedPaymentList.size() + "/" + requiredPaymentAmount + " payments so far");
        }
    }
}
