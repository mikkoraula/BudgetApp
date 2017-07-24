package datahandler;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import data.TransactionType;
import data.UserGroup;

/**
 * Created by Mikko on 29.7.2016.
 */
public class BackendlessDataLoader {

    // let's try to load the transactionTypes in one run
    public static void loadTransactionTypes(Context context) {
        LoadingCallback<List<TransactionType>> callback = createTransactionTypeLoadingCallback(context);
        // show the loading window while loading transaction types
        callback.showLoading();
        Backendless.Persistence.of( TransactionType.class ).find(callback);
    }
    private static LoadingCallback<List<TransactionType>> createTransactionTypeLoadingCallback(final Context context) {
        return new LoadingCallback<List<TransactionType>>(context, context.getString(R.string.loading_empty)) {
            @Override
            public void handleResponse( List<TransactionType> transactionTypeCollection) {
                super.handleResponse(transactionTypeCollection);
                Toast.makeText(context, "Transaction types loaded!", Toast.LENGTH_SHORT).show();

                ArrayList<TransactionType> transactionTypes = new ArrayList<TransactionType>(transactionTypeCollection);

                ((BackendlessDataLoaderInterface) context).loadSuccessful(transactionTypes);
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataSaverInterface) context).saveFailed();
            }
        };
    }


    public static void loadUserGroups(Context context) {
        LoadingCallback<List<UserGroup>> callback = createUserGroupLoadingCallback(context);
        callback.showLoading();
        Backendless.Persistence.of( UserGroup.class ).find(callback);
    }

    private static LoadingCallback<List<UserGroup>> createUserGroupLoadingCallback(final Context context) {
        return new LoadingCallback<List<UserGroup>>(context, context.getString(R.string.loading_user_groups)) {
            @Override
            public void handleResponse( List<UserGroup> userCollection) {
                super.handleResponse(userCollection);
                ArrayList<UserGroup> userGroups = new ArrayList<UserGroup>(userCollection);

            ((BackendlessDataLoaderInterface) context).loadSuccessful(userGroups);

            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataLoaderInterface) context).loadFailed();
            }
        };
    }

    /**
     * Queries for a user that has the same email as the keyword given in as parameter
     * @param context
     * @param keyword
     */
    public static void searchUser(Context context, String keyword) {
        LoadingCallback<List<BackendlessUser>> searchingCallback = createUserSearchingCallback(context);
        searchingCallback.showLoading();

        String whereClause = "email = '" + keyword + "'";
        System.out.println("searching with the keyword: " + whereClause);

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        Backendless.Data.of( BackendlessUser.class ).find( queryBuilder, searchingCallback );
    }

    private static LoadingCallback<List<BackendlessUser>> createUserSearchingCallback(final Context context) {
        return new LoadingCallback<List<BackendlessUser>>(context, context.getString(R.string.searching)) {
            @Override
            public void handleResponse( List<BackendlessUser> userCollection) {
                super.handleResponse(userCollection);
                ArrayList<BackendlessUser> foundUsers = new ArrayList<BackendlessUser>(userCollection);

                ((BackendlessDataLoaderInterface) context).loadSuccessful(foundUsers);
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataLoaderInterface) context).loadFailed();
            }
        };
    }
}
