package datahandler;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;
import java.util.Iterator;

import data.TransactionType;
import data.UserGroup;

/**
 * Created by Mikko on 29.7.2016.
 */
public class BackendlessDataLoader {

    // let's try to load the transactionTypes in one run
    public static void loadTransactionTypes(Context context) {
        LoadingCallback<BackendlessCollection<TransactionType>> callback = createTransactionTypeLoadingCallback(context);
        // show the loading window while loading transaction types
        callback.showLoading();
        Backendless.Data.of( TransactionType.class ).find(callback);
    }
    private static LoadingCallback<BackendlessCollection<TransactionType>> createTransactionTypeLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<TransactionType>>(context, context.getString(R.string.loading_empty)) {
            @Override
            public void handleResponse( BackendlessCollection<TransactionType> transactionTypeCollection) {
                super.handleResponse(transactionTypeCollection);
                Toast.makeText(context, "Transaction types loaded!", Toast.LENGTH_SHORT).show();

                ArrayList<TransactionType> transactionTypes = new ArrayList<>();
                Iterator<TransactionType> iterator = transactionTypeCollection.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    TransactionType transactionType = iterator.next();
                    transactionTypes.add(transactionType);
                }

                // is this the best way to do this?
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
        LoadingCallback<BackendlessCollection<UserGroup>> callback = createUserGroupLoadingCallback(context);
        callback.showLoading();
        Backendless.Data.of( UserGroup.class ).find(callback);
    }

    private static LoadingCallback<BackendlessCollection<UserGroup>> createUserGroupLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<UserGroup>>(context, context.getString(R.string.loading_user_groups)) {
            @Override
            public void handleResponse( BackendlessCollection<UserGroup> userCollection) {
                super.handleResponse(userCollection);

                ArrayList<UserGroup> userGroups = new ArrayList<>();
                Iterator<UserGroup> iterator = userCollection.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    UserGroup userGroup = iterator.next();
                    userGroups.add(userGroup);
                }

                ((BackendlessDataLoaderInterface) context).loadSuccessful(userGroups);
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataSaverInterface) context).saveFailed();
            }
        };
    }


    /*********
     * legacy
     *
     */
    public static void loadUsers(Context context) {
        LoadingCallback<BackendlessCollection<BackendlessUser>> callback = createUserLoadingCallback(context);
        callback.showLoading();
        Backendless.Data.of( BackendlessUser.class ).find(callback);
    }

    private static LoadingCallback<BackendlessCollection<BackendlessUser>> createUserLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<BackendlessUser>>(context, context.getString(R.string.loading_empty)) {
            @Override
            public void handleResponse( BackendlessCollection<BackendlessUser> userCollection) {
                super.handleResponse(userCollection);

                ArrayList<BackendlessUser> users = new ArrayList<>();
                Iterator<BackendlessUser> iterator = userCollection.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    BackendlessUser user = iterator.next();
                    users.add(user);
                }

                ((BackendlessDataLoaderInterface) context).loadSuccessful(users);
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataSaverInterface) context).saveFailed();
            }
        };
    }

}
