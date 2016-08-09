package addpayment;

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

import data.PaymentType;

/**
 * Created by Mikko on 26.7.2016.
 */
public class AddPaymentTypeActivity extends AppCompatActivity implements BackendlessDataSaverInterface {

    private ArrayList<PaymentType> paymentTypes;
    private GridView paymentTypeColorPickerGridView;

    private EditText newTypeNameEditText;

    private PaymentType newPaymentType;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment_type_creator);

        newTypeNameEditText = (EditText) findViewById(R.id.edit_text_new_payment_type_name);

        paymentTypes = new ArrayList<>();
        addColorToList(R.color.colorCyan);
        addColorToList(R.color.colorPink);
        addColorToList(R.color.colorYellow);
        addColorToList(R.color.colorLime);
        addColorToList(R.color.colorPurple);
        addColorToList(R.color.colorOrange);

        // in the end, add a null in to the list, this is for the add new payment type button
        //paymentTypes.add(null);

        initPaymentTypes();
    }

    private void addColorToList(int colorId) {
        PaymentType tmpType = new PaymentType();
        tmpType.setName("");
        tmpType.setColorId(colorId);
        paymentTypes.add(tmpType);
    }

    private void initPaymentTypes() {
        // init the types
        final PaymentTypePickAdapter paymentTypePickAdapter = new PaymentTypePickAdapter(this, R.layout.payment_type, paymentTypes, paymentTypes.size(), false);
        paymentTypeColorPickerGridView = (GridView) findViewById(R.id.payment_type_color_picker_grid_view);
        paymentTypeColorPickerGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        paymentTypeColorPickerGridView.setAdapter(paymentTypePickAdapter);
    }

    public void cancelNewType(View view) {
        // just exit the activity
        finish();
    }

    public void saveNewType(View view) {
        String newTypeName = newTypeNameEditText.getText().toString();

        System.out.println(newTypeNameEditText.getText());

        if(TextUtils.isEmpty(newTypeNameEditText.getText())) {
            newTypeNameEditText.setError("Payment Type must have a name!");
            System.out.println("asd");
            return;
        } else {
            newPaymentType = new PaymentType();
            newPaymentType.setName(newTypeName);

            // get the chosen color
            int colorId = ((PaymentTypePickAdapter)paymentTypeColorPickerGridView.getAdapter()).getCurrentButton().getPaymentType().getColorId();
            newPaymentType.setColorId(colorId);

            // save the new paymentType
            BackendlessDataSaver.savePaymentType(this, newPaymentType);

            System.out.println("got here");
            // then wait for the response of that
        }

    }

    @Override
    public void saveSuccessful() {
        System.out.println("put a " + newPaymentType + " to send in AddPaymentType");
        // finally exit this activity
        Intent intent = new Intent();
        intent.putExtra(AddPaymentActivity.NEW_PAYMENT_TYPE_SEND_CODE, newPaymentType);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void saveFailed() {
        System.out.println("New Payment type's save unsuccess");
    }
}
