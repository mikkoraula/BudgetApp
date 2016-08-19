package datahandler;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Income;
import data.Payment;
import data.Transaction;

/**
 * Created by Mikko on 9.8.2016.
 */
public class BackendlessTransactionLoader {

    private Context context;
    private ArrayList<Transaction> loadedTransactionList;

    private int requiredTransactionAmount;

    public BackendlessTransactionLoader(Context context) {
        this.context = context;
        loadedTransactionList = new ArrayList<>();
        requiredTransactionAmount = 0;
    }

    /***************************************************************
     *
     *                     PAYMENTS
     *
     */

    public  void loadPayments(long lastLoadDateInMillis) {

        String whereClause = "dateInMilliseconds > " + lastLoadDateInMillis;
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

                // if no payments are loaded from backendless, just go back
                if (paymentCollection.getTotalObjects() == 0) {
                    ((BackendlessDataLoaderInterface) context).loadSuccessful(loadedTransactionList);
                }

                System.out.println("Loaded " + paymentCollection.getCurrentPage().size() + " payment objects");
                System.out.println("Total payments in the Backendless storage - " + paymentCollection.getTotalObjects());
                setRequiredTransactionAmount(paymentCollection.getTotalObjects());

                Iterator<Payment> iterator = paymentCollection.getCurrentPage().iterator();
                while( iterator.hasNext() ) {
                    Payment currentPayment = iterator.next();
                    List<String> relations = new ArrayList<>();
                    relations.add( "paymentType" );
                    Backendless.Data.of( Payment.class ).loadRelations(
                            currentPayment, relations, loadPaymentRelationsCallback(context));
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

    private LoadingCallback<Payment> loadPaymentRelationsCallback(final Context context) {
        return new LoadingCallback<Payment>(context) {
            @Override
            public void handleResponse( Payment payment ) {
                //System.out.println( "\nRestaurant name = " + restaurant.getName() );
                System.out.println("loaded relations of payment: " + payment);
                System.out.println("payments type: " + payment.getPaymentType());
                addTransaction(payment);
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault ) {
                super.handleFault(backendlessFault);
                System.out.println("epic payment fail XD");
            }
        };
    }


    /***************************************************************
     *
     *                     INCOMES
     *
     */

    public  void loadIncomes(long lastLoadDateInMillis) {

        String whereClause = "dateInMilliseconds > " + lastLoadDateInMillis;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        // page size is 10 by default
        Backendless.Data.of( Income.class ).find(dataQuery, createIncomeLoadingCallback(context));
    }

    private LoadingCallback<BackendlessCollection<Income>> createIncomeLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<Income>>(context, context.getString(R.string.loading_empty)) {
            @Override
            public void handleResponse( BackendlessCollection<Income> incomeCollection) {
                super.handleResponse(incomeCollection);

                // if no incomes are loaded from backendless, just go back
                if (incomeCollection.getTotalObjects() == 0) {
                    ((BackendlessDataLoaderInterface) context).loadSuccessful(loadedTransactionList);
                }

                System.out.println("Loaded " + incomeCollection.getCurrentPage().size() + " income objects");
                System.out.println("Total incomes in the Backendless storage - " + incomeCollection.getTotalObjects());
                setRequiredTransactionAmount(incomeCollection.getTotalObjects());

                Iterator<Income> iterator = incomeCollection.getCurrentPage().iterator();
                while( iterator.hasNext() ) {
                    Income currentIncome = iterator.next();
                    List<String> relations = new ArrayList<>();
                    relations.add( "incomeType" );
                    Backendless.Data.of( Income.class ).loadRelations(
                            currentIncome, relations, loadIncomeRelationsCallback(context));
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

    private LoadingCallback<Income> loadIncomeRelationsCallback(final Context context) {
        return new LoadingCallback<Income>(context) {
            @Override
            public void handleResponse( Income income ) {
                //System.out.println( "\nRestaurant name = " + restaurant.getName() );
                System.out.println("loaded relations of income: " + income);
                System.out.println("income type: " + income.getIncomeType());
                addTransaction(income);
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault ) {
                super.handleFault(backendlessFault);
                System.out.println("epic income fail XD");
            }
        };
    }




    /***************************************************************
     *
     *                     BOTH
     *
     */



    public void setRequiredTransactionAmount(int requiredTransactionAmount) {
        this.requiredTransactionAmount = requiredTransactionAmount;
    }

    public void addTransaction(Transaction transaction) {
        System.out.println("addTransaction method transaction: " + transaction);
        System.out.println("loadedTransactionList: " + loadedTransactionList);
        loadedTransactionList.add(transaction);

        if (loadedTransactionList.size() == requiredTransactionAmount) {
            System.out.println("finally loaded enough payments!!!!");
            ((BackendlessDataLoaderInterface) context).loadSuccessful(loadedTransactionList);
        } else {
            System.out.println("loaded " + loadedTransactionList.size() + "/" + requiredTransactionAmount + " payments so far");
        }
    }
}
