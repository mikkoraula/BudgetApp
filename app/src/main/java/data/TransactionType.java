package data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mikko on 8.8.2016.
 */
public class TransactionType implements Serializable {
    private String objectId;
    private String userGroupId;
    private String name;
    private int colorId;
    private boolean payment;

    public TransactionType() {
    }

    public TransactionType(Map map) {
        setObjectId(map.get("objectId").toString());
        setName(map.get("name").toString());
        setColorId((int) map.get("colorId"));
        setPayment((Boolean) map.get("payment"));
    }

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

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean isPayment) {
        this.payment = isPayment;
    }



    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null)
            if (o instanceof TransactionType)
                if (objectId != null)
                    if (((TransactionType) o).getObjectId().equals(objectId))
                        return true;
        return false;
    }
}
