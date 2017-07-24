package userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.mikko.budgetapplication.ConstantVariableSettings;
import com.example.mikko.budgetapplication.MyBaseActivity;
import com.example.mikko.budgetapplication.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.Transaction;
import data.UserGroup;
import datahandler.BackendlessDataSaver;
import datahandler.BackendlessDataSaverInterface;

/**
 * Created by Mikko on 1.6.2017.
 *
 * Shows information about the user's profile:
 * - general information, such as username, email etc
 * - user's group info, if he is in a group
 */

public class ViewProfileActivity extends MyBaseActivity implements BackendlessDataSaverInterface {

    private BackendlessUser currentUser;

    private ArrayList<UserGroup> userGroups;
    // the user's current group
    private UserGroup userGroup;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        setHelpString(R.string.help_view_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get the usergroups from the mainactivity's intent
        userGroups = (ArrayList<UserGroup>) getIntent().getSerializableExtra(ConstantVariableSettings.SEND_USER_GROUPS);
        System.out.println("usergroups: " + userGroups);

        currentUser = Backendless.UserService.CurrentUser();

        // check if the user belongs to a group
        for (UserGroup userGroup : userGroups) {
            System.out.println("number of users: " + userGroup.getUsers().size());
            for (BackendlessUser user : userGroup.getUsers()) {
                System.out.println("comparison: " + user.getObjectId() + " vs. current user id " + currentUser.getObjectId());
                if (user.getObjectId().equals(currentUser.getObjectId())) {
                    this.userGroup = userGroup;
                }
            }
        }

        initProfile();

        initGroup();
    }

    /**
     * init and write all the textviews etc in the profile
     */
    public void initProfile() {
        // set the header to correspond the current user's name
        ((TextView) findViewById(R.id.view_profile_text_view_header)).setText(
                String.format(getString(R.string.view_profile_header), currentUser.getProperties().get("name"))
        );

        // show the user's email
        ((TextView) findViewById(R.id.view_profile_text_view_email)).setText(
                String.format(getString(R.string.view_profile_email), currentUser.getEmail())
        );

        // show when the user has registered the account to this app
        Date created = (Date) currentUser.getProperties().get("created");
        Format formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        ((TextView) findViewById(R.id.view_profile_text_view_created)).setText(
                String.format(getString(R.string.view_profile_created), formatter.format(created))
        );
    }

    /**
     * init the user's groups in the linearlayout and the header
     */
    public void initGroup() {
        if (userGroup != null) {
            // set the header to correspond the current user's name
            ((TextView) findViewById(R.id.view_profile_text_view_groups_header)).setText(
                    String.format(getString(R.string.view_profile_groups_header), currentUser.getProperties().get("name"))
            );
            ((Button) findViewById(R.id.view_profile_button_view_or_create_group)).setText(userGroup.getGroupName());
        } else {
            ((Button) findViewById(R.id.view_profile_button_view_or_create_group)).setText("Create Group");
        }

        /*
        // go through all the user groups
        for (UserGroup userGroup : userGroups) {
            // and their members
            for (BackendlessUser user : userGroup.getUsers()) {
                // check if the current user is found in any of the existing groups

                System.out.println("just about to check if users the same");
                if (user.getObjectId().equals(currentUser.getObjectId())) {
                    this.userGroup = userGroup;
                    // if he is found, change the create group's button's text into the group's name
                    ((Button) findViewById(R.id.view_profile_button_view_or_create_group)).setText(userGroup.getGroupName());
                }
            }
        }
        */
    }

    /**
     * depending on if the user already belongs to a group or not, this button does different things
     *
     * if the user is not in a group, this method opens a group creator activity
     *
     * if the user is in a group, this method opens an overview activity for the current group
     * @param view
     */
    public void createOrViewGroup(View view) {
        // if the user doesn't belong to any group
        if (userGroup == null) {
            Intent groupCreatorIntent = new Intent(this, CreateUserGroupActivity.class);
            groupCreatorIntent.putExtra(ConstantVariableSettings.SEND_USER_GROUP, currentUser);
            startActivityForResult(groupCreatorIntent, ConstantVariableSettings.CREATE_USER_GROUP_RESULT);
        }
        // if the user belongs to a group
        else {
            Intent groupViewerIntent = new Intent(this, ViewUserGroupActivity.class);
            // we want to send the activity information about the group
            groupViewerIntent.putExtra(ConstantVariableSettings.SEND_USER_GROUP, userGroup);
            groupViewerIntent.putExtra(ConstantVariableSettings.SEND_USER_GROUPS, userGroups);
            startActivityForResult(groupViewerIntent, ConstantVariableSettings.VIEW_USER_GROUP_RESULT);
        }
    }

    /**
     * this method is used to catch requests from the two child activities:
     *
     * - to change UI when a new group has been created
     * - to leave the group when a user comes back from the ViewUserGroupActivity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ConstantVariableSettings.CREATE_USER_GROUP_RESULT) {
            // if result is OK from user creation activity, it means the user created a new group
            // we need to catch that group from intent and update the UI
            if (resultCode == RESULT_OK) {
                UserGroup newGroup = (UserGroup) data.getSerializableExtra(ConstantVariableSettings.SEND_USER_GROUP);
                System.out.println("caught new group: " + newGroup);
                userGroup = newGroup;
                System.out.println("ples");
                initGroup();
            }
        }
        else if (requestCode == ConstantVariableSettings.VIEW_USER_GROUP_RESULT) {
            // If the result is ok, it means that the user wants to leave the group
            if (resultCode == RESULT_OK) {
                leaveGroup();
            }
        }
    }

    public void leaveGroup() {

        ArrayList<BackendlessUser> updatedList = userGroup.getUsers();
        int currentUserIndex = -1;

        // get the index of the current user in the group
        for (int i = 0; i < updatedList.size(); i++) {
            if (updatedList.get(i).getEmail().equals(currentUser.getEmail())) {
                currentUserIndex = i;
            }
        }


        //ArrayList<User> updatedList = new ArrayList<>();
        // if for some reason the user is not found in the group
        if (currentUserIndex != -1) {
            updatedList.remove(currentUserIndex);
            userGroup.setUsers(updatedList);

            System.out.println("following members still in the group");
            for (BackendlessUser user : userGroup.getUsers()) {
                System.out.println(user.getEmail());
            }


            // remove the relation from the backendless and then wait for response
            new BackendlessDataSaver(this, userGroup, Backendless.UserService.CurrentUser(), "users", UserGroup.class).removeRelation();
        }
    }

    @Override
    public void saveSuccessful() {
        System.out.println("leaving successful");

        userGroup = null;
        initGroup();
    }

    @Override
    public void saveFailed() {
        System.out.println("leaving the group failed");
    }

}
