package datahandler;

import android.content.Context;
import android.content.SharedPreferences;

import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.mikko.budgetapplication.ConstantVariableSettings;
import com.example.mikko.budgetapplication.DateHandler;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.SharedPreferencesHandler;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import data.Transaction;
import data.TransactionData;
import data.UserGroup;

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

public class TransactionsHandler {

    private Context context;
    private UserGroup userGroup;
    private ArrayList<Transaction> transactions;

    private Date currentLoadDate;

    public TransactionsHandler(Context context, UserGroup userGroup) {
        this.context = context;
        this.userGroup = userGroup;
    }

    public void loadTransactions() {
        System.out.println(" ");
        System.out.println(" ");
        transactions = new ArrayList<>();

        currentLoadDate = new Date();
        long lastBackendlessLoadInMillis = SharedPreferencesHandler.getLong(
                context, ConstantVariableSettings.TRANSACTIONS_LAST_LOAD_KEY, ConstantVariableSettings.LAST_BACKENDLESS_LOAD_LONG);

        DateHandler.getYear(lastBackendlessLoadInMillis);
        DateHandler.getMonth(lastBackendlessLoadInMillis);



        transactions = loadTransactions(context, ConstantVariableSettings.TRANSACTIONS_KEY_STRING);
        System.out.println("transactions!!!!!!!!!!!! " + transactions.size());
        // load the newest transactions from Backendless
        loadTransactionsFromBackendless(lastBackendlessLoadInMillis);
    }

    /*****
     * LOADING FROM INTERNAL STORAGE
     *
     */

    public static ArrayList<Transaction> loadTransactions(Context context, String key) {
        String jsonString = SharedPreferencesHandler.getString(
                context, ConstantVariableSettings.TRANSACTIONS_LAST_LOAD_KEY, key);

        Gson gson = new Gson();
        TransactionData loadedData = gson.fromJson(jsonString, TransactionData.class);

        System.out.println("loaded following data from preferences: " + loadedData);

        if (loadedData != null) {
            System.out.println("loaded transactions in list: " + loadedData.getTransactionList().size());

            return loadedData.getTransactionList();
        } else {
            System.out.println("didn't find any transactions in internal storage");
            return new ArrayList<>();
        }
    }

    public static void saveTransactions(Context context, ArrayList<Transaction> transactions, String key) {
        TransactionData transactionData = new TransactionData();
        transactionData.setTransactionList(transactions);

        Gson gson = new Gson();
        System.out.println("saving next json string: " + gson.toJson(transactionData));
        String jsonString = gson.toJson(transactionData);

        SharedPreferences.Editor editor = SharedPreferencesHandler.getEditor(
                context, ConstantVariableSettings.TRANSACTIONS_LAST_LOAD_KEY);
        editor.putString(key, jsonString);
        editor.apply();
    }






    /************************
     * Backendless loading
     *
     *
     */

    // load all the transactions that are newer than the parameters date in milliseconds
    // the app loads all the transactions to the memory and only downloads newest transactions
    // which makes for less transactions queried over internet
    // only the transactions that have the userGroup's id are fetched
    private  void loadTransactionsFromBackendless(long lastLoadDateInMillis) {

        String whereClause = "dateInMilliseconds > " + lastLoadDateInMillis + " and userGroupId = '" + userGroup.getObjectId() + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        LoadingCallback<List<Transaction>> callback = createTransactionLoadingCallback(context);
        callback.showLoading();
        Backendless.Data.of( Transaction.class ).find(queryBuilder, callback);
    }

    private LoadingCallback<List<Transaction>> createTransactionLoadingCallback(final Context context) {
        return new LoadingCallback<List<Transaction>>(context, "Loading Transactions") {
            @Override
            public void handleResponse( List<Transaction> transactionCollection) {
                super.handleResponse(transactionCollection);
                ArrayList<Transaction> loadedTransactions = new ArrayList<>(transactionCollection);
                loadSuccessful(loadedTransactions);
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                loadFailed();
            }
        };
    }

    /****************
     * rest of the things
     * @param loadedTransactionList
     */
    private void loadSuccessful(ArrayList<Transaction> loadedTransactionList) {
        if (loadedTransactionList.size() == 0) {
            //System.out.println("didn't find any of that transaction in backendlessload");
        }
        else {
            // add the backendless transactions to the list
            transactions.addAll(loadedTransactionList);

            saveTransactions(context, transactions, ConstantVariableSettings.TRANSACTIONS_KEY_STRING);

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


    private void loadFailed() {
        ((BackendlessDataLoaderInterface) context).loadFailed();
    }
}
