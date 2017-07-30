package com.example.mikko.budgetapplication;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

import data.Transaction;
import data.UserGroup;
import datahandler.BackendlessDataLoader;
import datahandler.BackendlessDataLoaderInterface;
import datahandler.TransactionsHandler;

/**
 * Created by Mikko on 26.7.2017.
 */
public class ManageRepetitiveTransactionsActivity extends MyBaseActivity implements BackendlessDataLoaderInterface<Transaction> {

    private UserGroup userGroup;
    private ArrayList<Transaction> repetitivePayments, repetitiveIncomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_manage_repetitive_transactions);
        super.onCreate(savedInstanceState);
        setHelpString(R.string.help_manage_repetitive_transactions);

        userGroup = (UserGroup) getIntent().getSerializableExtra(ConstantVariableSettings.SEND_USER_GROUP);

        new TransactionsHandler(this, userGroup).loadTransactions();

    }

    @Override
    public void loadSuccessful(ArrayList<Transaction> transactions) {
        // have to filter out the transactions that are repetitive
        repetitiveIncomes = new ArrayList<>();
        repetitivePayments = new ArrayList<>();

        for (Transaction transaction : transactions) {
            // if the transaction is a repetitive
            if (!transaction.getRepetition().equals("none")) {
                // now we just have to see which list the transaction is added
                if (transaction.isPayment()) {
                    repetitivePayments.add(transaction);
                } else {
                    repetitiveIncomes.add(transaction);
                }
            }
        }

        // then draw these transactions to lists in the activity
        //drawTransactions()
    }

    @Override
    public void loadFailed() {

    }

    private void drawTransction(Transaction transaction, LinearLayout linearLayout) {

    }
}
