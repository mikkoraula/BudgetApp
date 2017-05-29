package datahandler;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessFault;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import data.Transaction;

/**
 * Created by Mikko on 1.8.2016.
 */
public class BackendlessDataRemover {

    public static <T> void deleteObject(Context context, Class<T> typeClass,  T object) {
        LoadingCallback<Long> callback = createRemoveCallback(context);
        callback.showLoading();
        Backendless.Persistence.of(typeClass).remove( object, callback);
    }

    private static LoadingCallback<Long> createRemoveCallback(final Context context) {
        return new LoadingCallback<Long>(context, context.getString(R.string.loading_empty)) {
            @Override
            public void handleResponse( Long response ) {
                super.handleResponse(response);
                Toast.makeText(context, "Remove successful!", Toast.LENGTH_SHORT).show();

                // is this the best way to do this?
                ((BackendlessDataRemoverInterface) context).removeSuccessful();
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataRemoverInterface) context).removeFailed();
            }
        };
    }

    public static void deleteTransaction(Context context,  Transaction transaction) {
        LoadingCallback<Long> callback = createRemoveTransactionCallback(context);
        callback.showLoading();
        System.out.println("objectid: " + transaction.getObjectId());
        Backendless.Persistence.of(Transaction.class).remove( transaction, callback);
    }

    private static LoadingCallback<Long> createRemoveTransactionCallback(final Context context) {
        return new LoadingCallback<Long>(context, context.getString(R.string.loading_remove)) {
            @Override
            public void handleResponse( Long response ) {
                super.handleResponse(response);
                Toast.makeText(context, "Remove successful!", Toast.LENGTH_SHORT).show();

                // is this the best way to do this?
                ((BackendlessDataRemoverInterface) context).removeSuccessful();
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataRemoverInterface) context).removeFailed();
            }
        };
    }

}
