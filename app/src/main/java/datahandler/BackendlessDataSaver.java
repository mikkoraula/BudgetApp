package datahandler;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import data.Transaction;
import data.TransactionType;
import data.UserGroup;

/**
 * Created by Mikko on 13.7.2016.
 *
 * contains static methods to save and load objects in Backendless
 */
public class BackendlessDataSaver<T, U> {

    private Context context;
    private boolean saveChildren;
    private ArrayList<U> children;
    private String childrenColumnName;
    private T parent;

    private Class dataClass;

    public BackendlessDataSaver(Context context, T parent, ArrayList<U> children, String childrenColumnName, Class parentClass) {
        this.context = context;
        this.dataClass = parentClass;
        this.parent = parent;
        this.children = children;
        this.childrenColumnName = childrenColumnName;
    }

    public BackendlessDataSaver(Context context, T parent, Class parentClass) {
        this.context = context;
        this.dataClass = parentClass;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.childrenColumnName = "";
    }

    public BackendlessDataSaver(Context context, T parent, U child, String childrenColumnName, Class parentClass) {
        this.context = context;
        this.dataClass = parentClass;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.children.add(child);
        this.childrenColumnName = childrenColumnName;
    }

    /**
     * This is a generic saving method for backendless data classes
     *
     * T object is the parent object that the user wants to save
     * U children are possible children relations to the parent that need to be added separately
     *
     */
    public void saveObject() {
        if (children.size() > 0) {
            saveChildren = true;
        }
        LoadingCallback<T> callback = createSavingCallback(context, "Saving...");
        callback.showLoading();

        System.out.println("parent" + parent);
        Backendless.Data.of( dataClass ).save( parent, callback);
    }


    public void removeRelation() {
        saveChildren = false;

        LoadingCallback<T> callback = createSavingCallback(context, "Updating...");
        callback.showLoading();

        Backendless.Persistence.of( dataClass ).deleteRelation(parent, childrenColumnName, children, callback);
    }


    private <V> LoadingCallback<V> createSavingCallback(final Context context, String info) {
        return new LoadingCallback<V>(context, info) {
            @Override
            public void handleResponse( V response ) {
                super.handleResponse(response);
                saveSuccessful();
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataSaverInterface) context).saveFailed();
            }
        };
    }

    /**
     * this is kind of a checkpoint in the saving action:
     * here we check what to do every time again
     * we either init a new saving of children, or finish the action by calling the saveSuccessful via tha interface
     */
    private void saveSuccessful() {
        // if we still have to save relations
        if (saveChildren) {
            LoadingCallback<Integer> callback = createSavingCallback(context, "asd");
            Backendless.Persistence.of( dataClass ).addRelation(parent, childrenColumnName, children, callback);
            saveChildren = false;
        } else {
            ((BackendlessDataSaverInterface) context).saveSuccessful();
        }
    }







/*
    public static void saveTransaction(Context context, Transaction transaction) {
        LoadingCallback<Transaction> callback = createCallback(context, "Saving...");
        callback.showLoading();
        Backendless.Data.of( Transaction.class ).save( transaction, callback );
    }

    public static void saveTransactionType(Context context, TransactionType transactionType) {
        LoadingCallback<TransactionType> callback = createCallback(context, "Saving...");
        callback.showLoading();
        Backendless.Data.of( TransactionType.class ).save( transactionType, callback );
    }

    public static void saveUserGroup(Context context, ProcessedUserGroup processedUserGroup) {
        LoadingCallback<UserGroup> callback = createCallback(context, "Saving...");
        callback.showLoading();

        // decode the usergroup
        UserGroup userGroup = new UserGroup();
        userGroup.setObjectId(processedUserGroup.getObjectId());
        userGroup.setGroupName(processedUserGroup.getGroupName());
        ArrayList<BackendlessUser> backendlessUsers = new ArrayList<>();
        System.out.println("emails that stay");
        for (User user : processedUserGroup.getUsers()) {
            BackendlessUser backendlessUser = new BackendlessUser();
            backendlessUser.setEmail(user.getEmail());
            backendlessUser.setProperty("name", user.getName());
            backendlessUser.setProperty("created", user.getCreated());
            backendlessUser.setProperty("objectId", user.getObjectId());
            backendlessUsers.add(backendlessUser);
            System.out.println(backendlessUser.getEmail());
        }
        userGroup.setUsers(backendlessUsers);

        Backendless.Data.of( UserGroup.class ).save( userGroup, callback );
    }

    public static void removeUserFromGroup(Context context, ProcessedUserGroup processedUserGroup, BackendlessUser removeUser) {
        LoadingCallback<Integer> callback = createCallback(context, "Removing...");
        callback.showLoading();

        // decode the usergroup
        UserGroup userGroup = new UserGroup();
        userGroup.setObjectId(processedUserGroup.getObjectId());
        userGroup.setGroupName(processedUserGroup.getGroupName());
        ArrayList<BackendlessUser> backendlessUsers = new ArrayList<>();
        System.out.println("emails that stay");
        for (User user : processedUserGroup.getUsers()) {
            BackendlessUser backendlessUser = new BackendlessUser();
            backendlessUser.setEmail(user.getEmail());
            backendlessUser.setProperty("name", user.getName());
            backendlessUser.setProperty("created", user.getCreated());
            backendlessUser.setProperty("objectId", user.getObjectId());
            backendlessUsers.add(backendlessUser);
            System.out.println(backendlessUser.getEmail());
        }
        userGroup.setUsers(backendlessUsers);
        ArrayList<BackendlessUser> removeUsers = new ArrayList<>();
        removeUsers.add(removeUser);

        Backendless.Data.of( UserGroup.class ).deleteRelation( userGroup, "users", removeUsers,  callback );
    }

    public static void removeTestFromGroup(Context context, UserGroup userGroup, BackendlessUser user) {
        LoadingCallback<Integer> callback = createCallback(context, "lelelell");
        callback.showLoading();

        ArrayList<BackendlessUser> removeUsers = new ArrayList<>();
        removeUsers.add(user);

        Backendless.Data.of( UserGroup.class ).deleteRelation( userGroup, "users", removeUsers, callback );
    }

    public static void addMembersToGroup(Context context, UserGroup userGroup, ArrayList<BackendlessUser> users) {
        LoadingCallback<Integer> callback = createCallback(context, "woopy doo");
        callback.showLoading();

        Backendless.Persistence.of( UserGroup.class ).addRelation(userGroup, "users", users, callback);
    }

    public static void saveTestGroup(Context context, UserGroup userGroup) {
        LoadingCallback<UserGroup> callback = createCallback(context, "hehehehehahahahah");
        callback.showLoading();



        Backendless.Data.of( UserGroup.class ).save(userGroup, callback);
    }



    private static <A> LoadingCallback<A> createCallback(final Context context, String info) {
        return new LoadingCallback<A>(context, info) {
            @Override
            public void handleResponse( A response ) {
                super.handleResponse(response);

                //System.out.println("removed " + response + " users from group");
                ((BackendlessDataSaverInterface) context).saveSuccessful();
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataSaverInterface) context).saveFailed();
            }
        };
    }
*/

}
