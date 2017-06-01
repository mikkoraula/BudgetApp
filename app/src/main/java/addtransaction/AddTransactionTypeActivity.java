package addtransaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import data.TransactionType;
import datahandler.BackendlessDataSaverInterface;
import datahandler.BackendlessDataSaver;

import com.example.mikko.budgetapplication.MyBaseActivity;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;


/**
 * Created by Mikko on 11.5.2017.
 */
public class AddTransactionTypeActivity extends AppCompatActivity implements BackendlessDataSaverInterface, View.OnClickListener {

    // this is a cheaty way to do this:
    // we want to have a similar grid of options to pick from, just like in addTransaction activity
    // but we want to pick from different colors instead of transaction types
    // so we use the already implemented grid system from addTransaction, and mask the colors
    // into transactiontypes
    private ArrayList<TransactionType> colorList;
    private GridView transactionTypeColorPickerGridView;

    private EditText newTypeNameEditText;
    private boolean isPayment;


    private TransactionType newTransactionType;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction_type_creator);


        isPayment = getIntent().getBooleanExtra(AddTransactionFragment.TRANSACTION_TYPE_CHECKER, true);

        newTypeNameEditText = (EditText) findViewById(R.id.edit_text_new_transaction_type_name);

        // change the strings according to which transaction type we are about to create
        TextView title = (TextView) findViewById(R.id.transaction_type_creator_title);
        title.setText("Create new " + getTransactionString() + " type");

        // initiate the colorList
        initColors();

        initColorGrid();
    }

    // returns a string of either "Payment" or "Income" based on the boolean isPayment
    private String getTransactionString() {
        if (isPayment) {
            return "Payment";
        } else {
            return "Income";
        }
    }

    private void initColors() {
        colorList = new ArrayList<>();
        addColorToList(R.color.colorBlue);
        addColorToList(R.color.colorPink);
        addColorToList(R.color.colorYellow);
        addColorToList(R.color.colorGreen);
        addColorToList(R.color.colorPurple);
        addColorToList(R.color.colorBrown);
    }

    // masks the color into a transaction type
    private void addColorToList(int colorId) {
        TransactionType tmpType = new TransactionType();
        tmpType.setName("");
        tmpType.setColorId(colorId);
        colorList.add(tmpType);
    }

    private void initColorGrid() {
        // init the types
        final TransactionTypePickAdapter colorPickAdapter = new TransactionTypePickAdapter(this, this, R.layout.transaction_type, colorList, colorList.size(), false);
        transactionTypeColorPickerGridView = (GridView) findViewById(R.id.transaction_type_color_picker_grid_view);
        transactionTypeColorPickerGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        transactionTypeColorPickerGridView.setAdapter(colorPickAdapter);
    }

    // if the user just cancels creating the new type
    public void cancelNewType(View view) {
        // just exit the activity
        finish();
    }

    // save a new Transaction type to the Backendless
    public void saveNewType(View view) {
        String newTypeName = newTypeNameEditText.getText().toString();

        System.out.println(newTypeNameEditText.getText());

        if(TextUtils.isEmpty(newTypeNameEditText.getText())) {
            newTypeNameEditText.requestFocus();
            newTypeNameEditText.setError("The type must have a name!");
            return;
        } else {
            // create a new Transaction type
            newTransactionType = new TransactionType();

            // set the name
            newTransactionType.setName(newTypeName);

            // isPayment
            newTransactionType.setPayment(isPayment);
            System.out.println("isPayment= " + newTransactionType.isPayment());

            // get the chosen color
            int colorId = ((TransactionTypePickAdapter) transactionTypeColorPickerGridView.getAdapter()).getCurrentButton().getTransactionType().getColorId();
            newTransactionType.setColorId(colorId);
            System.out.println("colorID= " + colorId);

            // save the new incomeType
            BackendlessDataSaver.saveTransactionType(this, newTransactionType);

            System.out.println("got here");
            // then wait for the response of that
        }

    }

    @Override
    public void saveSuccessful() {
        System.out.println("put a " + newTransactionType + " to send in AddTransactionType");
        // finally exit this activity
        Intent intent = new Intent();
        intent.putExtra(AddTransactionFragment.NEW_TRANSACTION_TYPE_SEND_CODE, newTransactionType);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void saveFailed() {
        System.out.println("New Transaction type's save unsuccess");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_transaction_type_button:
                // if we want to have the user be able to add own colors, add some action here
                break;
        }
    }
}
