package datahandler;

import android.content.Context;
import android.content.SharedPreferences;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.mikko.budgetapplication.ConstantVariableSettings;
import com.example.mikko.budgetapplication.DateHandler;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.SharedPreferencesHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import data.Transaction;
import payment.history.TransactionDataHandler;

/**
 * Created by Mikko on 31.5.2017.
 *
 * Loads all the transactions from Backendless AND internal storage.
 *
 * Uses the Dataloaderinterface to communicate the loaded transactions back
 * Sends a back a list of transactions, with both payments and incomes in the same list
 *
 * Uses BackendlessTransactionLoader class to load new transactions from Backendless
 */

public class TransactionsLoader {

    private Context context;
    //private ArrayList<Transaction> payments, incomes;
    private ArrayList<Transaction> transactions, loadedTransactions;
    private int requiredTransactionAmount;

    private Date currentLoadDate;

    public TransactionsLoader(Context context) {
        this.context = context;
        loadedTransactions = new ArrayList<>();

        requiredTransactionAmount = 0;
    }

    public void loadTransactionsFromBackendless() {
        System.out.println(" ");
        System.out.println(" ");
        transactions = new ArrayList<>();

        currentLoadDate = new Date();
        long lastBackendlessLoadInMillis = SharedPreferencesHandler.getLong(
                context, ConstantVariableSettings.TRANSACTIONS_LAST_LOAD_KEY, ConstantVariableSettings.LAST_BACKENDLESS_LOAD_LONG);

        DateHandler.getYear(lastBackendlessLoadInMillis);
        DateHandler.getMonth(lastBackendlessLoadInMillis);


        /*
        // load the old payments from internal storage
        transactions = TransactionDataHandler.loadTransactionsFromBackendless(context, ConstantVariableSettings.PAYMENTS_KEY_STRING);
        // load the old incomes from internal storage
        transactions.addAll(TransactionDataHandler.loadTransactionsFromBackendless(context, ConstantVariableSettings.INCOMES_KEY_STRING));
        */
        transactions = TransactionDataHandler.loadTransactions(context, ConstantVariableSettings.TRANSACTIONS_KEY_STRING);
        System.out.println("transactions!!!!!!!!!!!! " + transactions.size());
        // load the newest transactions from Backendless
        loadTransactionsFromBackendless(lastBackendlessLoadInMillis);
    }


    public void loadSuccessful(ArrayList<Transaction> loadedTransactionList) {
        if (loadedTransactionList.size() == 0) {
            //System.out.println("didn't find any of that transaction in backendlessload");
        }
        else {
            // add the backendless transactions to the list
            transactions.addAll(loadedTransactionList);

            TransactionDataHandler.saveTransactions(context, transactions, ConstantVariableSettings.TRANSACTIONS_KEY_STRING);

            // update the latest backend transactions load time
            SharedPreferences.Editor editor = SharedPreferencesHandler.getEditor(
                    context, ConstantVariableSettings.TRANSACTIONS_LAST_LOAD_KEY);
            editor.putLong(ConstantVariableSettings.LAST_BACKENDLESS_LOAD_LONG, currentLoadDate.getTime());
            editor.apply();
        }
        System.out.println(" ");
        System.out.println("Loaded a total of " + transactions.size() + " transactions.");
        System.out.println(" ");

        ((BackendlessDataLoaderInterface) context).loadSuccessful(transactions);
    }


    public void loadFailed() {
        ((BackendlessDataLoaderInterface) context).loadFailed();
    }

    /************************
     * Backendless loading
     *
     *
     */

    // load all the transactions that are newer than the parameters date in milliseconds
    // the app loads all the transactions to the memory and only downloads newest transactions
    // which makes for less transactions queried over internet
    public  void loadTransactionsFromBackendless(long lastLoadDateInMillis) {

        String whereClause = "dateInMilliseconds > " + lastLoadDateInMillis;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        // page size is 10 by default

        LoadingCallback<BackendlessCollection<Transaction>> callback = createTransactionLoadingCallback(context);
        callback.showLoading();
        Backendless.Data.of( Transaction.class ).find(dataQuery, callback);
    }

    private LoadingCallback<BackendlessCollection<Transaction>> createTransactionLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<Transaction>>(context, "Loading Transactions") {
            @Override
            public void handleResponse( BackendlessCollection<Transaction> transactionCollection) {
                super.handleResponse(transactionCollection);

                // if no transactions are loaded from backendless, just go back
                if (transactionCollection.getTotalObjects() == 0) {
                    loadSuccessful(loadedTransactions);
                }

                System.out.println("Loaded " + transactionCollection.getCurrentPage().size() + " transaction objects");
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
                loadFailed();
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
        System.out.println("addTransaction method transaction: " + transaction);
        System.out.println("loadedTransactionList: " + loadedTransactions);
        loadedTransactions.add(transaction);

        if (loadedTransactions.size() == requiredTransactionAmount) {
            System.out.println("finally loaded enough transactions!!!!");
            loadSuccessful(loadedTransactions);
        } else {
            System.out.println("loaded " + loadedTransactions.size() + "/" + requiredTransactionAmount + " transactions so far");
        }
    }


    public void setRequiredTransactionAmount(int requiredTransactionAmount) {
        this.requiredTransactionAmount = requiredTransactionAmount;
    }
}
