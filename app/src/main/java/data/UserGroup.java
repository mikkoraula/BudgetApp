package data;

import com.backendless.BackendlessUser;

import java.util.ArrayList;

/**
 * Created by Mikko on 4.7.2017.
 *
 * This is a data class that is used when loading the usergroups from backendless
 * this raw version of the usergroups holds the users as BackendlessUser
 * from this version a usable ProcessedUserGroup is made that is serializable
 */

public class UserGroup {
    private String groupName;
    private String objectId;
    private ArrayList<BackendlessUser> users;

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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
