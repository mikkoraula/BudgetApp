package payment.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.mikko.budgetapplication.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.Transaction;
import datahandler.BackendlessDataLoader;
import datahandler.BackendlessDataLoaderInterface;
import datahandler.BackendlessDataRemover;
import datahandler.BackendlessDataRemoverInterface;

/**
 * Created by Mikko on 19.5.2017.
 */
//public class ViewTransactionActivity extends AppCompatActivity implements BackendlessDataLoaderInterface<BackendlessUser>, BackendlessDataRemoverInterface {
public class ViewTransactionActivity extends AppCompatActivity implements BackendlessDataRemoverInterface {

    private Transaction transaction;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);

        // get the transaction
        transaction = (Transaction) getIntent().getSerializableExtra(MonthlyTabFragment.VIEW_TRANSACTION_CODE);

        // set the amount
        ((TextView) findViewById(R.id.view_transaction_amount)).setText("Amount " + transaction.getAmount());

        // set the date
        long dateinMillis = transaction.getDateInMilliseconds();
        Date date = new Date(dateinMillis);
        Format formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        ((TextView) findViewById(R.id.view_transaction_date)).setText("Date " + formatter.format(date));

        // set the owner
        //String ownerName2 = Backendless.UserService.findById(transaction.getOwnerId()).getProperties().get("name").toString();
        String ownerName = transaction.getOwnerName();
        ((TextView) findViewById(R.id.view_transaction_owner)).setText("Owner " + ownerName);


        // set additional info
        ((TextView) findViewById(R.id.view_transaction_additional_information)).setText("Addditional information: " + transaction.getAdditionalInfo());

    }


    public void closeActivityClicked(View view) {
        finish();
    }

    public void removeTransactionClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Remove")
                .setMessage("Are you sure you want to remove this " + (transaction.isPayment() ? "payment" : "income") + "?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                       removeTransaction();
                    }
                }).create().show();
    }

    private void removeTransaction() {
        BackendlessDataRemover.deleteTransaction(this, transaction);
    }

    @Override
    public void removeSuccessful() {
        // when the remove is successfully completed, finish this activity and return to show history
        Intent intent = new Intent();
        intent.putExtra(MonthlyTabFragment.VIEW_TRANSACTION_CODE, transaction);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void removeFailed() {

    }
}
