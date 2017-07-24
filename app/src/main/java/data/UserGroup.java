package data;

import com.backendless.BackendlessUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mikko on 4.7.2017.
 *
 * This is a data class that is used when loading the usergroups from backendless
 * this raw version of the usergroups holds the users as BackendlessUser
 * from this version a usable ProcessedUserGroup is made that is serializable
 */

public class UserGroup implements Serializable {
    private String groupName;
    private String objectId;
    private ArrayList<BackendlessUser> users;

    // test
    private double serialVersionUID;
    private String ownerId;

    public UserGroup() {
        //this.serialVersionUID = 0;
        //this.ownerId = "73B9521F-430B-F31C-FFC6-28936C7AE800";
    }
    /*
    public UserGroup(ProcessedUserGroup processedUserGroup) {
        this.groupName = processedUserGroup.getGroupName();
        this.objectId = processedUserGroup.getObjectId();
        users = new ArrayList<>();
        for (User user : processedUserGroup.getUsers()) {
            BackendlessUser backendlessUser = new BackendlessUser();
            backendlessUser.setEmail(user.getEmail());
            backendlessUser.setProperty("name", user.getName());
            backendlessUser.setProperty("created", user.getCreated());
            backendlessUser.setProperty("objectId", user.getObjectId());
            users.add(backendlessUser);
        }
    }
    */

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<BackendlessUser> getUsers() {
        return users;
    }

    public void setUsers(BackendlessUser user) {
        users = new ArrayList<>();
        users.add(user);
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public double getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(double serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "groupName='" + groupName + '\'' +
                ", objectId='" + objectId + '\'' +
                ", users=" + users +
                ", serialVersionUID=" + serialVersionUID +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}
