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
import java.util.Date;
import java.util.Iterator;

import data.TransactionType;
import data.User;
import data.ProcessedUserGroup;
import data.UserGroup;
import userprofile.ViewUserGroupActivity;

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
        //callback.showLoading();
        Backendless.Data.of( UserGroup.class ).find(callback);
    }

    private static LoadingCallback<BackendlessCollection<UserGroup>> createUserGroupLoadingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<UserGroup>>(context, context.getString(R.string.loading_user_groups)) {
            @Override
            public void handleResponse( BackendlessCollection<UserGroup> userCollection) {
                super.handleResponse(userCollection);

                ArrayList<ProcessedUserGroup> serializableGroups = new ArrayList<>();
                Iterator<UserGroup> iterator = userCollection.getCurrentPage().iterator();
                while( iterator.hasNext() ) {

                    // when all the usergroups have been loaded, manually create them into serializable versions
                    // by recreating the user objects
                    UserGroup rawUserGroup = iterator.next();
                    ProcessedUserGroup serializableGroup = new ProcessedUserGroup();
                    ArrayList<User> serializableUsers = new ArrayList<>();
                    for (BackendlessUser backendlessUser : rawUserGroup.getUsers()) {
                        User serializableUser = new User();
                        serializableUser.setEmail(backendlessUser.getEmail());
                        serializableUser.setName(backendlessUser.getProperties().get("name").toString());
                        serializableUser.setObjectId(backendlessUser.getObjectId());
                        serializableUser.setCreated((Date) backendlessUser.getProperties().get("created"));
                        serializableUsers.add(serializableUser);
                    }
                    serializableGroup.setGroupName(rawUserGroup.getGroupName());
                    serializableGroup.setObjectId(rawUserGroup.getObjectId());
                    serializableGroup.setUsers(serializableUsers);
                    serializableGroups.add(serializableGroup);
                }

                ((BackendlessDataLoaderInterface) context).loadSuccessful(serializableGroups);
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
        LoadingCallback<BackendlessCollection<BackendlessUser>> searchingCallback = createUserSearchingCallback(context);
        searchingCallback.showLoading();

        //keyword = "%" + keyword + "%";
        String whereClause = "email = '" + keyword + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        System.out.println("searching with the keyword: " + whereClause);
        dataQuery.setWhereClause(whereClause);

        Backendless.Persistence.of( BackendlessUser.class ).find( dataQuery, searchingCallback );
    }

    private static LoadingCallback<BackendlessCollection<BackendlessUser>> createUserSearchingCallback(final Context context) {
        return new LoadingCallback<BackendlessCollection<BackendlessUser>>(context, context.getString(R.string.searching)) {
            @Override
            public void handleResponse( BackendlessCollection<BackendlessUser> userCollection) {
                super.handleResponse(userCollection);

                ArrayList<BackendlessUser> users = new ArrayList<>();
                Iterator<BackendlessUser> iterator = userCollection.getCurrentPage().iterator();
                while( iterator.hasNext() ) {
                    BackendlessUser backendlessUser = iterator.next();

                    users.add(backendlessUser);
                }

                ((BackendlessDataLoaderInterface) context).loadSuccessful(users);
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataLoaderInterface) context).loadFailed();
            }
        };
    }
}
