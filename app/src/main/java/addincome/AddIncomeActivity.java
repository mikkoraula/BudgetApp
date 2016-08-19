package addincome;

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

import data.Income;
import data.IncomeType;
import payment.history.ShowHistoryActivity;

/**
 * Created by Mikko on 6.8.2016.
 */
public class AddIncomeActivity extends MyBaseActivity
        implements BackendlessDataSaverInterface, BackendlessDataLoaderInterface<IncomeType>, BackendlessDataRemoverInterface {

    private static final int NEW_INCOME_TYPE_CODE = 3;
    public static final String NEW_INCOME_TYPE_SEND_CODE = "4";
    private EditText incomeText;

    private ToggleButton buttonPersonal;
    private ToggleButton buttonShared;

    private GridView incomeTypeGridView;
    private IncomeTypePickAdapter incomeTypePickAdapter;

    private ArrayList<IncomeType> incomeTypes;

    private IncomeType contextMenuIncomeType;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        incomeText = (EditText) findViewById(R.id.add_income_expense_field);

        buttonPersonal = (ToggleButton) findViewById(R.id.add_income_toggle_personal);
        buttonShared = (ToggleButton) findViewById(R.id.add_income_toggle_shared);
        // sets the income visibility to shared as default
        setVisibility("shared");

        loadIncomeTypes();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            DialogHelper.createHelpDialog(this, getString(R.string.toolbar_help), getString(R.string.help_add_income)).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /*******************************************************************************************
     *
     * Saving the new Income
     */

    // checks if all the required fields are filled and no illegal arguments are passed
    public boolean isFormValid() {
        if (Validator.isExpenseValid(this, incomeText.getText())) {
            return true;
        } else {
            return false;
        }
    }


    public void saveIncome(View view) {
        System.out.println(incomeText.getText());
        if (isFormValid()) {

            // create new income object
            Income newIncome = new Income();

            // set the payment amount
            newIncome.setAmount(Double.parseDouble(incomeText.getText().toString()));

            // set the paymentType
            newIncome.setIncomeType(incomeTypePickAdapter.getCurrentType());

            // set the date of payment
            newIncome.setDateInMilliseconds(new Date().getTime());

            // save the new income in to the backendless
            BackendlessDataSaver.saveIncome(this, newIncome);
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
            Log.d("debug", "bug in set visibility method in AddIncomeActivity");
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
     * Income Type stuff
     */

    public void loadIncomeTypes() {
        incomeTypes = new ArrayList<IncomeType>();

        // load the pre-made income types from backendless
        BackendlessDataLoader.loadIncomeTypes(this);

        // then wait for response
    }

    @Override
    public void loadFailed() {
        System.out.println("failed to load incometypes :(");
        // probably means the app is  opened first time and there is no income table in backendless
        // so try to make a temp income
        /*
        IncomeType newIncomeType = new IncomeType();
        newIncomeType.setName("Example Income");
        newIncomeType.setColorId(R.color.colorPink);
        */
        addIncomeType(null);
    }

    @Override
    public void loadSuccessful(ArrayList<IncomeType> loadedTypes) {
        incomeTypes = loadedTypes;

        // add a null in to the list, this is for the add new income type button
        incomeTypes.add(null);

        // init the types
        incomeTypeGridView = (GridView) findViewById(R.id.add_income_type_picker_grid_layout);
        incomeTypeGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        // init the adapter
        refreshIncomeTypes();

        registerForContextMenu(incomeTypeGridView);
    }

    // used if a new income type is added
    public void refreshIncomeTypes() {
        incomeTypePickAdapter = new IncomeTypePickAdapter(
                this, R.layout.income_type, incomeTypes, incomeTypes.size() - 1, true
        );
        incomeTypeGridView.setAdapter(incomeTypePickAdapter);
    }


    public void addIncomeType(View view) {

        Intent addIncomeTypeIntent = new Intent(this, AddIncomeTypeActivity.class);
        startActivityForResult(addIncomeTypeIntent, NEW_INCOME_TYPE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == NEW_INCOME_TYPE_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                recreate();
                /*
                IncomeType newType = (IncomeType) data.getSerializableExtra(NEW_INCOME_TYPE_SEND_CODE);
                System.out.println("caught a " + newType + " back in AddIncome");
                if (incomeTypes.size() > 0)
                    incomeTypes.remove(incomeTypes.size() - 1);
                incomeTypes.add(newType);
                incomeTypes.add(null);

                // if we get here, means we need to update the incometypelist, since a new incometype has been added
                refreshIncomeTypes();
                */
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        //if (view.getId() == R.id.add_income_type_picker_grid_layout) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        //menu.setHeaderTitle("options");
        FrameLayout frameLayout = (FrameLayout) info.targetView;
        contextMenuIncomeType = ((IncomeTypeToggleButton) frameLayout.getChildAt(0)).getIncomeType();
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

        BackendlessDataRemover.deleteObject(this, IncomeType.class, contextMenuIncomeType);

        System.out.println("Option " + menuItemName + " was clicked");
        return true;
    }

    @Override
    public void removeSuccessful() {
        System.out.println("remove successful!!!");
        // remove it from the list
        incomeTypes.remove(contextMenuIncomeType);

        refreshIncomeTypes();
    }

    @Override
    public void removeFailed() {
        System.out.println("removing of a income type failed :(");
    }
}
