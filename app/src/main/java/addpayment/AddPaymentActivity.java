package addpayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.ToggleButton;

import datahandler.BackendlessDataLoader;
import datahandler.BackendlessDataLoaderInterface;
import datahandler.BackendlessDataRemover;
import datahandler.BackendlessDataRemoverInterface;
import datahandler.BackendlessDataSaverInterface;
import com.example.mikko.budgetapplication.DialogHelper;
import datahandler.BackendlessDataSaver;
import com.example.mikko.budgetapplication.MyBaseActivity;
import com.example.mikko.budgetapplication.R;
import com.example.mikko.budgetapplication.Validator;

import java.util.ArrayList;
import java.util.Date;

import data.Payment;
import data.PaymentType;
import payment.history.ShowHistoryActivity;

/**
 * Created by Mikko on 4.7.2016.
 */
public class AddPaymentActivity extends MyBaseActivity implements BackendlessDataSaverInterface, BackendlessDataLoaderInterface<PaymentType>, BackendlessDataRemoverInterface {

    private static final int NEW_PAYMENT_TYPE_CODE = 1;
    public static final String NEW_PAYMENT_TYPE_SEND_CODE = "2";
    private EditText expenseText;
    private EditText locationText;

    private ToggleButton buttonPersonal;
    private ToggleButton buttonShared;

    private GridView paymentTypeGridView;
    private PaymentTypePickAdapter paymentTypePickAdapter;

    private ArrayList<PaymentType> paymentTypes;

    private PaymentType contextMenuPaymentType;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expenseText = (EditText) findViewById(R.id.add_payment_expense_field);
        locationText = (EditText) findViewById(R.id.add_payment_location_field);

        buttonPersonal = (ToggleButton) findViewById(R.id.add_payment_toggle_personal);
        buttonShared = (ToggleButton) findViewById(R.id.add_payment_toggle_shared);
        // sets the payment visibility to shared as default
        setVisibility("shared");

        loadPaymentTypes();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            DialogHelper.createHelpDialog(this, getString(R.string.toolbar_help), getString(R.string.help_add_payment)).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /*******************************************************************************************
     *
     * Saving the new Payment
     */

    // checks if all the required fields are filled and no illegal arguments are passed
    public boolean isFormValid() {
        if (Validator.isExpenseValid(this, expenseText.getText())) {
            return true;
        } else {
            return false;
        }
    }


    public void savePayment(View view) {
        System.out.println(expenseText.getText());
        if (isFormValid()) {

            // create new payment object
            Payment newPayment = new Payment();

            // set the payment amount
            newPayment.setAmount(Double.parseDouble(expenseText.getText().toString()));

            // set the paymentType
            newPayment.setPaymentType(paymentTypePickAdapter.getCurrentType());

            // set the date of payment
            newPayment.setDateInMilliseconds(new Date().getTime());

            // save the new payment in to the backendless
            BackendlessDataSaver.savePayment(this, newPayment);
            System.out.println("checked item positions: " + paymentTypeGridView.getCheckedItemPosition());
            //System.out.println("focusables: " + paymentTypeGridView.getFocusables(0));
            System.out.println("focused child: " + paymentTypeGridView.getFocusedChild());
            System.out.println("selector states: " + paymentTypeGridView.getSelector().getState());
            //paymentTypeGridView.


            // then just wait for the response
        }
    }

    @Override
    public void saveSuccessful() {
        Log.d("asd", "got back to the activity with successful save");
        // move to history activity
        Intent showHistoryActivityIntent = new Intent(this, ShowHistoryActivity.class);
        startActivity(showHistoryActivityIntent);
        // finish this activity, so that user can skip straight back to main menu
        finish();
    }

    @Override
    public void saveFailed() {
        Log.d("asd", "got back to the activity with FAILED save :(");
    }

    /*****************************************************************************
     *
     * personal/shared stuff
     */

    public void setVisibility(String visibility) {
        if (visibility.equals("shared")) {
            buttonPersonal.setChecked(false);
            buttonPersonal.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            buttonPersonal.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            buttonShared.setChecked(true);
            buttonShared.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            buttonShared.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

        } else if (visibility.equals("personal")){
            buttonPersonal.setChecked(true);
            buttonPersonal.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            buttonPersonal.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            buttonShared.setChecked(false);
            buttonShared.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            buttonShared.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));

        } else {
            Log.d("debug", "bug in set visibility method in AddPaymentActivity");
        }
    }

    public void setPersonal(View view) {
        setVisibility("personal");
    }
    public void setShared(View view) {
        setVisibility("shared");
    }

    /********************************************************************************
     *
     * Payment Type stuff
     */

    public void loadPaymentTypes() {
        paymentTypes = new ArrayList<PaymentType>();

        // load the pre-made payment types from backendless
        BackendlessDataLoader.loadPaymentTypes(this);

        // then wait for response
    }

    @Override
    public void loadFailed() {
        System.out.println("failed to load paymenttypes :(");
        // probably because no paymentType table in backendless
        // try to create first paymentType:
        addPaymentType(null);
    }

    @Override
    public void loadSuccessful(ArrayList<PaymentType> loadedTypes) {
        paymentTypes = loadedTypes;

        // add a null in to the list, this is for the add new payment type button
        paymentTypes.add(null);

        // init the types
        paymentTypeGridView = (GridView) findViewById(R.id.add_payment_type_picker_grid_layout);
        paymentTypeGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        // init the adapter
        refreshPaymentTypes();

        registerForContextMenu(paymentTypeGridView);
        System.out.println("asdasdasdasd");
    }

    // used if a new payment type is added
    public void refreshPaymentTypes() {
        paymentTypePickAdapter = new PaymentTypePickAdapter(
                this, R.layout.payment_type, paymentTypes, paymentTypes.size() - 1, true
        );
        paymentTypeGridView.setAdapter(paymentTypePickAdapter);
    }


    public void addPaymentType(View view) {

        Intent addPaymentTypeIntent = new Intent(this, AddPaymentTypeActivity.class);
        startActivityForResult(addPaymentTypeIntent, NEW_PAYMENT_TYPE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == NEW_PAYMENT_TYPE_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                recreate();
                /*
                System.out.println("paymentypes.size before: " + paymentTypes.size());
                PaymentType newType = (PaymentType) data.getSerializableExtra(NEW_PAYMENT_TYPE_SEND_CODE);
                System.out.println("caught a " + newType + " back in AddPayment");
                if (paymentTypes.size() > 0)
                    paymentTypes.remove(paymentTypes.size() - 1);
                paymentTypes.add(newType);
                paymentTypes.add(null);

                System.out.println("will this be the last log before error? " + newType.getName());
                // if we get here, means we need to update the paymenttypelist, since a new paymenttype has been added
                //loadPaymentTypes();
                refreshPaymentTypes();
                */
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        //if (view.getId() == R.id.add_payment_type_picker_grid_layout) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //menu.setHeaderTitle("options");
        FrameLayout frameLayout = (FrameLayout) info.targetView;
        contextMenuPaymentType = ((PaymentTypeToggleButton) frameLayout.getChildAt(0)).getPaymentType();
            String[] menuItems = {"delete"};
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        //}
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = {"delete"};
        String menuItemName = menuItems[menuItemIndex];

        BackendlessDataRemover.deleteObject(this, PaymentType.class, contextMenuPaymentType);

        System.out.println("Option " + menuItemName + " was clicked");
        return true;
    }

    @Override
    public void removeSuccessful() {
        System.out.println("remove successful!!!");
        // remove it from the list
        paymentTypes.remove(contextMenuPaymentType);

        refreshPaymentTypes();
    }

    @Override
    public void removeFailed() {
        System.out.println("removing of a payment type failed :(");
    }
}
