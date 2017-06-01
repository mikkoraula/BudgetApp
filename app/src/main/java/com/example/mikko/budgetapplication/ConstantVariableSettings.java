package com.example.mikko.budgetapplication;

/**
 * Created by Mikko on 13.8.2016.
 */
public class ConstantVariableSettings {

    // Shared Preferences
    public static final String TRANSACTIONS_LAST_LOAD_KEY = "transactions_key";
    public static final String LAST_BACKENDLESS_LOAD_LONG = "last_backendless_load";
    public static final String MONTH_TAB_FRAGMENT_KEY = "month_tab_fragment_key";
    public static final String MONTH_TAB_FRAGMENT_CURRENT_MONTH_INT = "current_month_int";
    public static final String TRANSACTIONS_KEY_STRING = "transactions_key_string";

    // number of months the user can see the history of in the app
    public static final int NUMBER_OF_MONTHS = 14;

    // sending transaction lists across activities
    public static final String TRANSACTION_LIST_PAYMENTS = "transaction_list_payments";
    public static final String TRANSACTION_LIST_INCOMES = "transaction_list_incomes";
}
