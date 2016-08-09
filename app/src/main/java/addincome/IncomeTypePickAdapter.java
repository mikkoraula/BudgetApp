package addincome;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.IncomeType;

/**
 * Created by Mikko on 6.8.2016.
 */
public class IncomeTypePickAdapter extends ArrayAdapter<IncomeType> {

    private Context context;
    private int resource;
    private ArrayList<IncomeType> incomeTypes;

    private int numberOfTypes;
    private IncomeTypeToggleButton currentButton;
    private IncomeType currentType;

    // if the gridview's last button is a button to add more
    private boolean hasAddButton;

    public IncomeTypePickAdapter(Context context, int resource, ArrayList<IncomeType> incomeTypes, int numberOfTypes, boolean hasAddButton) {
        super(context, resource, incomeTypes);
        this.context = context;
        this.resource = resource;
        this.incomeTypes = incomeTypes;
        this.numberOfTypes = numberOfTypes;
        this.hasAddButton = hasAddButton;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // in this case this view is just another income type
        if (incomeTypes.get(position) != null) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.income_type, parent, false);
            }

            IncomeType incomeType = incomeTypes.get(position);


            final IncomeTypeToggleButton toggleButton = (IncomeTypeToggleButton) convertView.findViewById(R.id.income_type_button);

            toggleButton.setText(incomeType.getName());
            toggleButton.setTextOn(incomeType.getName());
            toggleButton.setTextOff(incomeType.getName());
            toggleButton.setIncomeType(incomeType);

            FrameLayout frameLayout = (FrameLayout) convertView.findViewById(R.id.income_type_picker_frame_layout);
            toggleButton.setBackgroundColor(ContextCompat.getColor(context, incomeType.getColorId()));


            // if no type is yet picked, automatically pick the first one
            if (currentButton == null) {
                currentButton = toggleButton;
                currentButton.setChecked(true);
            }

            toggleButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (currentButton != toggleButton) {
                        currentButton.setChecked(false);
                        currentButton = toggleButton;
                    } else {
                        currentButton.setChecked(true);
                    }
                }
            });

        }
        // in this case it's the last view => the add new income view
        else if (hasAddButton) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_income_type, parent, false);
            }
            Drawable addIncomeTypeIcon = convertView.getResources().getDrawable(R.drawable.ic_add_payment_type);
            ImageButton addIncomeTypeButton = (ImageButton) convertView.findViewById(R.id.add_income_type_button);
            addIncomeTypeIcon.setColorFilter(new LightingColorFilter(Color.BLACK, ContextCompat.getColor(context, R.color.colorPrimary)));
            addIncomeTypeButton.setImageDrawable(addIncomeTypeIcon);
        }
        return convertView;
    }

    public IncomeTypeToggleButton getCurrentButton() {
        return currentButton;
    }

    public void setIncomeTypes(ArrayList<IncomeType> incomeTypes) {
        this.incomeTypes = incomeTypes;
    }

    public IncomeType getCurrentType() {
        return currentButton.getIncomeType();
    }

}
