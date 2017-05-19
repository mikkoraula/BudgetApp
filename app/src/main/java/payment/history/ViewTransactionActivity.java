package payment.history;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.backendless.Backendless;
import com.example.mikko.budgetapplication.R;

import data.Transaction;

/**
 * Created by Mikko on 19.5.2017.
 */
public class ViewTransactionActivity extends AppCompatActivity {

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
        ((TextView) findViewById(R.id.view_transaction_date)).setText("Date " + transaction.getDateInMilliseconds());

        // set the owner
        //String ownerName = Backendless.UserService.findById(transaction.getOwnerId()).getProperties().get("name").toString();
        String ownerName = transaction.getOwnerId();
        ((TextView) findViewById(R.id.view_transaction_owner)).setText("Owner " + ownerName);



        /*
        newTypeNameEditText = (EditText) findViewById(R.id.edit_text_new_transaction_type_name);

        // change the strings according to which transaction type we are about to create
        TextView title = (TextView) findViewById(R.id.transaction_type_creator_title);
        title.setText("Create new " + getTransactionString() + " type");

        // initiate the colorList
        initColors();

        initColorGrid();
        */
    }


    public void closeActivity(View view) {
        finish();
    }

}
