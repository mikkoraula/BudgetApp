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

import data.ProcessedUserGroup;
import data.Transaction;
import data.TransactionType;
import data.User;
import data.UserGroup;

/**
 * Created by Mikko on 13.7.2016.
 *
 * contains static methods to save and load objects in Backendless
 */
public class BackendlessDataSaver {

    public static void saveTransaction(Context context, Transaction transaction) {
        LoadingCallback<Object> callback = createSavingCallback(context);
        callback.showLoading();
        Backendless.Persistence.save( transaction, callback );
    }

    public static void saveTransactionType(Context context, TransactionType transactionType) {
        LoadingCallback<Object> callback = createSavingCallback(context);
        callback.showLoading();
        Backendless.Persistence.save( transactionType, callback );
    }

    public static void saveUserGroup(Context context, ProcessedUserGroup processedUserGroup) {
        LoadingCallback<Object> callback = createSavingCallback(context);
        callback.showLoading();

        // decode the usergroup
        UserGroup userGroup = new UserGroup();
        userGroup.setObjectId(processedUserGroup.getObjectId());
        userGroup.setGroupName(processedUserGroup.getGroupName());
        ArrayList<BackendlessUser> backendlessUsers = new ArrayList<>();
        for (User user : processedUserGroup.getUsers()) {
            BackendlessUser backendlessUser = new BackendlessUser();
            backendlessUser.setEmail(user.getEmail());
            backendlessUser.setProperty("name", user.getName());
            backendlessUser.setProperty("created", user.getCreated());
            backendlessUser.setProperty("objectId", user.getObjectId());
            backendlessUsers.add(backendlessUser);
        }
        userGroup.setUsers(backendlessUsers);

        Backendless.Persistence.save( userGroup, callback );
    }


    private static LoadingCallback<Object> createSavingCallback(final Context context) {
        return new LoadingCallback<Object>(context, "Saving the new User Group...") {
            @Override
            public void handleResponse( Object response ) {
                super.handleResponse(response);
                Toast.makeText(context, "save successful!", Toast.LENGTH_SHORT).show();

                // is this the best way to do this?
                ((BackendlessDataSaverInterface) context).saveSuccessful();
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataSaverInterface) context).saveFailed();
            }
        };
    }
}
