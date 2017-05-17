package datahandler;

import android.content.Context;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Transaction;

/**
 * Created by Mikko on 9.8.2016.
 */
public class BackendlessTransactionLoader {

    private Context context;
    private ArrayList<Transaction> loadedTransactions;


    private int requiredTransactionAmount;

    public BackendlessTransactionLoader(Context context) {
        this.context = context;

        loadedTransactions = new ArrayList<>();

        requiredTransactionAmount = 0;
    }

    // load all the transactions that are newer than the parameters date in milliseconds
    // the app loads all the transactions to the memory and only downloads newest transactions
    // which makes for less transactions queried over internet
    public  void loadTransactions(long lastLoadDateInMillis) {

        String whereClause = "dateInMilliseconds > " + lastLoadDateInMillis;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        // page size is 10 by default

        LoadingCallback<BackendlessCollection<Transaction>> callback = createTransactionLoadingCallback(context);
        callback.showLoading();
        Backendless.Data.of( Transaction.class ).find(dataQuery, callback);
    }

    private LoadingCallback<BackendlessCollection<Transaction>> createTransactionLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<Transaction>>(context, "can you see me") {
            @Override
            public void handleResponse( BackendlessCollection<Transaction> transactionCollection) {
                super.handleResponse(transactionCollection);

                // if no payments are loaded from backendless, just go back
                if (transactionCollection.getTotalObjects() == 0) {
                    ((BackendlessDataLoaderInterface) context).loadSuccessful(loadedTransactions);
                }

                System.out.println("Loaded " + transactionCollection.getCurrentPage().size() + " payment objects");
                System.out.println("Total payments in the Backendless storage - " + transactionCollection.getTotalObjects());
                setRequiredTransactionAmount(transactionCollection.getTotalObjects());

                Iterator<Transaction> iterator = transactionCollection.getCurrentPage().iterator();
                while( iterator.hasNext() ) {
                    Transaction currentTransaction = iterator.next();
                    List<String> relations = new ArrayList<>();
                    relations.add( "transactionType" );
                    Backendless.Data.of( Transaction.class ).loadRelations(
                            currentTransaction, relations, loadTransactionRelationsCallback(context));
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

    private LoadingCallback<Transaction> loadTransactionRelationsCallback(final Context context) {
        return new LoadingCallback<Transaction>(context) {
            @Override
            public void handleResponse( Transaction transaction ) {
                System.out.println("loaded relations of transaction: " + transaction);
                System.out.println("transaction type: " + transaction.getTransactionType());
                addTransaction(transaction);
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault ) {
                super.handleFault(backendlessFault);
                System.out.println("epic transaction fail XD");
            }
        };
    }

    public void addTransaction(Transaction transaction) {
        //System.out.println("addTransaction method transaction: " + payment);
        //System.out.println("loadedTransactionList: " + loadedTransactionList);
        loadedTransactions.add(transaction);

        if (loadedTransactions.size() == requiredTransactionAmount) {
            System.out.println("finally loaded enough transactions!!!!");
            ((BackendlessDataLoaderInterface) context).loadSuccessful(loadedTransactions);
        } else {
            System.out.println("loaded " + loadedTransactions.size() + "/" + requiredTransactionAmount + " transactions so far");
        }
    }


    public void setRequiredTransactionAmount(int requiredTransactionAmount) {
        this.requiredTransactionAmount = requiredTransactionAmount;
    }
}
