package userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.mikko.budgetapplication.ConstantVariableSettings;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.Transaction;
import data.UserGroup;
import datahandler.BackendlessDataSaver;
import datahandler.BackendlessDataSaverInterface;

/**
 * Created by Mikko on 1.6.2017.
 */
public class CreateUserGroupActivity extends AppCompatActivity implements BackendlessDataSaverInterface {

    private EditText groupNameEditText;
    private UserGroup newGroup;
    private BackendlessUser currentUser;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_group);

        //currentUser = (BackendlessUser) getIntent().getSerializableExtra(ConstantVariableSettings.SEND_USER_GROUP);
        currentUser = Backendless.UserService.CurrentUser();
        System.out.println("user " + currentUser);
        groupNameEditText = (EditText) findViewById(R.id.create_user_group_edit_text_name);

    }


    public void closeActivityClicked(View view) {
        finish();
    }

    public void createGroupClicked(View view) {
        // check if the user hasn't entered any name for the new group
        if (TextUtils.isEmpty(groupNameEditText.getText())) {
            groupNameEditText.requestFocus();
            groupNameEditText.setError("The Group must have a name!");
            System.out.println("rip");
        } else {
            newGroup = new UserGroup();
            newGroup.setGroupName(groupNameEditText.getText().toString());

            ArrayList<BackendlessUser> users = new ArrayList<>();
            // add the current user to the group
            users.add(currentUser);
            newGroup.setUsers(users);
            new BackendlessDataSaver(this, newGroup, users, "users", UserGroup.class).saveObject();
        }
    }

    @Override
    public void saveSuccessful() {
        System.out.println("save successful");

        Intent intent = new Intent();
        //intent.putExtra("lol", currentUser);

        UserGroup userGroup = new UserGroup();
        userGroup.setGroupName(newGroup.getGroupName());
        userGroup.setObjectId(newGroup.getObjectId());
        userGroup.setUsers(new ArrayList<BackendlessUser>());
        intent.putExtra(ConstantVariableSettings.SEND_USER_GROUP, userGroup);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void saveFailed() {
        System.out.println("failed to save");
    }
}
