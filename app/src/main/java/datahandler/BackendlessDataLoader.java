package datahandler;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.exceptions.BackendlessFault;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;
import java.util.Iterator;

import data.TransactionType;

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

}
