package data;

import android.os.Parcelable;

import com.backendless.BackendlessUser;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mikko on 1.6.2017.
 */

public class UserGroup implements Serializable {

    private String groupName;
    private ArrayList<BackendlessUser> users;

    public UserGroup() {}

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<BackendlessUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<BackendlessUser> users) {
        this.users = users;
    }
}
