package userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

import data.UserGroup;
import datahandler.BackendlessDataLoader;
import datahandler.BackendlessDataLoaderInterface;

/**
 * Created by Mikko on 1.6.2017.
 */

public class ViewProfileActivity extends MyBaseActivity implements BackendlessDataLoaderInterface<UserGroup> {

    private BackendlessUser currentUser;

    private UserGroup userGroup;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        setHelpString(R.string.help_view_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BackendlessDataLoader.loadUserGroups(this);

        currentUser = Backendless.UserService.CurrentUser();

        initProfile();


        // do rest when the usergroups have finished loading
    }

    // override the Options menu creation to change to a version of the menu where there is no view profile
    // so the user can't move from view profile to view profile
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_profile, menu);
        return true;
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
    public void initGroup(ArrayList<UserGroup> userGroups) {
        // set the header to correspond the current user's name
        ((TextView) findViewById(R.id.view_profile_text_view_groups_header)).setText(
                String.format(getString(R.string.view_profile_groups_header), currentUser.getProperties().get("name"))
        );

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
    }

    public void createOrViewGroup(View view) {
        // if the user doesn't belong to any group, this button works as a new group creator
        if (userGroup == null) {
            Intent groupCreatorIntent = new Intent(this, CreateUserGroupActivity.class);
            startActivity(groupCreatorIntent);
        }
        // if the user belongs to a group, this button works as a way to show the group's information
        else {
            Intent groupViewerIntent = new Intent(this, ViewUserGroupActivity.class);
            // send the user group's name
            groupViewerIntent.putExtra(ConstantVariableSettings.SEND_USER_GROUP_NAME, userGroup.getGroupName());
            // we want to send the activity information about the group
            // since Backendlessuser doesn't implement serializable, just send a string array of the names in the group
            ArrayList<String> usernames = new ArrayList<>();
            for (BackendlessUser backendlessUser : userGroup.getUsers()) {
                usernames.add(String.valueOf(backendlessUser.getProperties().get("name")));
            }
            groupViewerIntent.putExtra(ConstantVariableSettings.SEND_USER_GROUP_NAMES, usernames);
            startActivityForResult(groupViewerIntent, ConstantVariableSettings.VIEW_USER_GROUP_RESULT);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ConstantVariableSettings.VIEW_USER_GROUP_RESULT) {
            // If the result is ok, it means that the user wants to leave the group
            if (resultCode == RESULT_OK) {
                System.out.println("User wants to leave group.");
            }
        }
    }

    @Override
    public void loadSuccessful(ArrayList<UserGroup> userGroups) {
        System.out.println("wohoo");
        System.out.println("number of usergroups: " + userGroups);
        System.out.println("usergroup's number of users: " + userGroups.get(0).getUsers().size());

        // now that usergroups are loaded, init the group part of the profile
        initGroup(userGroups);
    }

    @Override
    public void loadFailed() {
        System.out.println("Loading user groups failed.");
    }
}
