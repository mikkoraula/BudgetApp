package addincome;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import data.IncomeType;

/**
 * Created by Mikko on 6.8.2016.
 */
public class IncomeTypeToggleButton extends ToggleButton{
    private IncomeType incomeType;

    public IncomeTypeToggleButton(Context context) {
        super(context);
    }

    public IncomeTypeToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IncomeTypeToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }
}
