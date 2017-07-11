package data;

import com.backendless.BackendlessUser;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mikko on 4.7.2017.
 *
 * This is a data class that tries to replicate the BackendlessUser dataclass and make it serializable
 */

public class User implements Serializable {
    private String email;
    private String name;
    private String objectId;
    private Date created;

    public User(BackendlessUser user) {
        this.email = user.getEmail();
        this.name = user.getProperties().get("name").toString();
        this.objectId = user.getObjectId();
        this.created = (Date) user.getProperties().get("created");
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
