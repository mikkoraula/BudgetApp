package data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mikko on 6.8.2016.
 */
public class Income extends Transaction implements Serializable {
    private IncomeType incomeType;

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }
}
