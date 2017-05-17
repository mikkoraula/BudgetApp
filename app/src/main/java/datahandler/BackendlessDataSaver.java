package datahandler;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessFault;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import data.Transaction;
import data.TransactionType;

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

    private static LoadingCallback<Object> createSavingCallback(final Context context) {
        return new LoadingCallback<Object>(context, context.getString(R.string.loading_empty)) {
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
