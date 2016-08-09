package addincome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import datahandler.BackendlessDataSaverInterface;
import datahandler.BackendlessDataSaver;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.IncomeType;

/**
 * Created by Mikko on 6.8.2016.
 */
public class AddIncomeTypeActivity extends AppCompatActivity implements BackendlessDataSaverInterface {

    private ArrayList<IncomeType> incomeTypes;
    private GridView incomeTypeColorPickerGridView;

    private EditText newTypeNameEditText;

    private IncomeType newIncomeType;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_income_type_creator);

        newTypeNameEditText = (EditText) findViewById(R.id.edit_text_new_income_type_name);

        incomeTypes = new ArrayList<>();
        addColorToList(R.color.colorCyan);
        addColorToList(R.color.colorPink);
        addColorToList(R.color.colorYellow);
        addColorToList(R.color.colorLime);
        addColorToList(R.color.colorPurple);
        addColorToList(R.color.colorOrange);

        initIncomeTypes();
    }

    private void addColorToList(int colorId) {
        IncomeType tmpType = new IncomeType();
        tmpType.setName("");
        tmpType.setColorId(colorId);
        incomeTypes.add(tmpType);
    }

    private void initIncomeTypes() {
        // init the types
        final IncomeTypePickAdapter incomeTypePickAdapter = new IncomeTypePickAdapter(this, R.layout.income_type, incomeTypes, incomeTypes.size(), false);
        incomeTypeColorPickerGridView = (GridView) findViewById(R.id.income_type_color_picker_grid_view);
        incomeTypeColorPickerGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        incomeTypeColorPickerGridView.setAdapter(incomeTypePickAdapter);
    }

    public void cancelNewType(View view) {
        // just exit the activity
        finish();
    }

    public void saveNewType(View view) {
        String newTypeName = newTypeNameEditText.getText().toString();

        System.out.println(newTypeNameEditText.getText());

        if(TextUtils.isEmpty(newTypeNameEditText.getText())) {
            newTypeNameEditText.setError("Income Type must have a name!");
            return;
        } else {
            newIncomeType = new IncomeType();
            newIncomeType.setName(newTypeName);

            // get the chosen color
            int colorId = ((IncomeTypePickAdapter)incomeTypeColorPickerGridView.getAdapter()).getCurrentButton().getIncomeType().getColorId();
            newIncomeType.setColorId(colorId);

            // save the new incomeType
            BackendlessDataSaver.saveIncomeType(this, newIncomeType);

            System.out.println("got here");
            // then wait for the response of that
        }

    }

    @Override
    public void saveSuccessful() {
        System.out.println("put a " + newIncomeType + " to send in AddIncomeType");
        // finally exit this activity
        Intent intent = new Intent();
        intent.putExtra(AddIncomeActivity.NEW_INCOME_TYPE_SEND_CODE, newIncomeType);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void saveFailed() {
        System.out.println("New Income type's save unsuccess");
    }
}
