package userprofile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.ProcessedUserGroup;
import data.User;
import datahandler.BackendlessDataSaver;
import datahandler.BackendlessDataSaverInterface;

/**
 * Created by Mikko on 1.6.2017.
 */
public class CreateUserGroupActivity extends AppCompatActivity implements BackendlessDataSaverInterface {

    private EditText groupNameEditText;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_group);

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
            ProcessedUserGroup processedUserGroup = new ProcessedUserGroup();
            processedUserGroup.setGroupName(groupNameEditText.getText().toString());

            ArrayList<User> users = new ArrayList<>();
            // add the current user to the group
            users.add(new User(Backendless.UserService.CurrentUser()));
            processedUserGroup.setUsers(users);
            BackendlessDataSaver.saveUserGroup(this, processedUserGroup);
        }
    }

    @Override
    public void saveSuccessful() {
        System.out.println("save successful");
        finish();
    }

    @Override
    public void saveFailed() {

    }
}
