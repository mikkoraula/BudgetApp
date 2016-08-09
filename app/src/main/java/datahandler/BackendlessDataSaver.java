package datahandler;

import android.content.Context;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessFault;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.R;

import data.Income;
import data.IncomeType;
import data.Payment;
import data.PaymentType;

/**
 * Created by Mikko on 13.7.2016.
 *
 * contains static methods to save and load objects in Backendless
 */
public class BackendlessDataSaver {

    public static void savePayment(Context context, Payment payment) {
        Backendless.Persistence.save( payment, createSavingCallback(context) );
    }

    public static void savePaymentType(Context context, PaymentType paymentType) {
        Backendless.Persistence.save( paymentType, createSavingCallback(context) );
    }

    public static void saveIncome(Context context, Income income) {
        Backendless.Persistence.save( income, createSavingCallback(context) );
    }

    public static void saveIncomeType(Context context, IncomeType incomeType) {
        Backendless.Persistence.save( incomeType, createSavingCallback(context));
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
