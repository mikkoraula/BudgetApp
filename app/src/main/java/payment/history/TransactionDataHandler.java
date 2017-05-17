package payment.history;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mikko.budgetapplication.SharedPreferencesHandler;
import com.example.mikko.budgetapplication.SharedPreferencesSettings;
import com.google.gson.Gson;

import java.util.ArrayList;

import data.Transaction;
import data.TransactionData;

/**
 * Created by Mikko on 12.8.2016.
 *
 * Contains static methods to load and save transaction data to phone's internal storage
 */
public class TransactionDataHandler {

    public static ArrayList<Transaction> loadTransactions(Context context, String key) {
        String jsonString = SharedPreferencesHandler.getString(
                context, SharedPreferencesSettings.TRANSACTIONS_KEY, key);

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
                context, SharedPreferencesSettings.TRANSACTIONS_KEY);
        editor.putString(key, jsonString);
        editor.apply();
    }
}
