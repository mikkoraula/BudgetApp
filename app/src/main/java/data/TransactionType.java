package data;

import java.io.Serializable;

/**
 * Created by Mikko on 8.8.2016.
 */
public class TransactionType implements Serializable {
    protected String name;
    protected int colorId;

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
