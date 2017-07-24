package userprofile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.mikko.budgetapplication.ConstantVariableSettings;
import com.example.mikko.budgetapplication.DialogHelper;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.UserGroup;
import datahandler.BackendlessDataLoader;
import datahandler.BackendlessDataLoaderInterface;
import datahandler.BackendlessDataSaver;
import datahandler.BackendlessDataSaverInterface;

/**
 * Created by Mikko on 2.6.2017.
 */
public class ViewUserGroupActivity extends AppCompatActivity implements BackendlessDataLoaderInterface<BackendlessUser>, BackendlessDataSaverInterface {

    //private ArrayList<String> memberNames;
    //private String userGroupName;
    private UserGroup userGroup;
    private ArrayList<UserGroup> userGroups;

    // when the user searches for a new user to add to his group
    private BackendlessUser searchedUser;
    private LinearLayout foundUserLayout;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_group);

        userGroups = (ArrayList<UserGroup>) getIntent().getSerializableExtra(ConstantVariableSettings.SEND_USER_GROUPS);
        userGroup = (UserGroup) getIntent().getSerializableExtra(ConstantVariableSettings.SEND_USER_GROUP);

        // for weird Serializable reasons, when a new group was created and the user wants to add new members,
        // we can't pass this new group between activities
        // instead we pass a placeholder group that has no members
        // so here we check if we caught one of these groups (= a group with no members)
        // and if so, add the current user to the group
        if (userGroup.getUsers().size() == 0) {
            userGroup.setUsers(Backendless.UserService.CurrentUser());
        }

        // the layout in which searched users are shown
        foundUserLayout = (LinearLayout) findViewById(R.id.view_user_group_linear_layout_search_result);

        // set the group name
        TextView groupNameTextView = (TextView) findViewById(R.id.view_user_group_group_name);
        groupNameTextView.setText("Group name: " + userGroup.getGroupName());

        initMembersList();
    }

    /**
     * make a list of the members show in this activity
     * the items are added to the linearlayout
     */
    private void initMembersList() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.view_user_group_linear_layout_members);
        // first clear the layout of former members
        linearLayout.removeAllViews();

        for (BackendlessUser user : userGroup.getUsers()) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setText("- " + user.getProperty("name"));

            linearLayout.addView(textView);
        }
    }

    /**
     * This method is called when the user wants to search for a user to join his group
     * The user should have entered his searchword (the searched user's email) before calling this method
     * @param view
     */
    public void searchMembersClicked(View view) {
        // get the search key word and then query for users named that query
        EditText searchWordEditText = (EditText) findViewById(R.id.view_user_group_edit_text_search_bar);
        BackendlessDataLoader.searchUser(this, searchWordEditText.getText().toString());

        // then close the keyboard out of the way
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * adds the searchedUser as a new member of the user's group
     * but first it checks if the user already belongs to another group
     * @param view
     */
    public void addMemberClicked(View view) {
        // first check if the user is already in a group
        if (isUserInGroup(searchedUser, userGroups)) {
            DialogHelper.createErrorDialog(this, "User already in group", "This user is already in a group. Ask him/her to leave the current group and then try again").show();
            System.out.println("rip");
        } else {
            // getting here means the searched user is eligible to join this group

            // so add the user in the current group
            ArrayList<BackendlessUser> currentUsers = userGroup.getUsers();
            currentUsers.add(searchedUser);
            userGroup.setUsers(currentUsers);

            // and save the changes to backendless and wait for the response
            new BackendlessDataSaver(this, userGroup, searchedUser, "users", UserGroup.class).saveObject();
        }
    }

    public boolean isUserInGroup(BackendlessUser user, ArrayList<UserGroup> userGroups) {
        for (UserGroup userGroup : userGroups) {
            for (BackendlessUser iterUser : userGroup.getUsers()) {
                if (iterUser.getEmail().equals(user.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void closeActivityClicked(View view) {
        finish();
    }

    /**
     * prompt the user if he is sure he wants to leave the group
     * @param view
     */
    public void leaveGroupClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Remove")
                .setMessage("Are you sure you want to leave this group? You can get back to the group if other members invite you.")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        leaveGroup();
                    }
                }).create().show();
    }

    /**
     * this method is called when the user has confirmed that he wants to leave the group
     *
     * - the actual removing happens in ViewProfileActivity
     * - here we just set the flag (RESULT_OK) for the parent activity so that it understands
     *   that the user wants to leave the group
     * - and then finish this activity and return to the parent
     */
    private void leaveGroup() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * this is called when the query for a user with the certain email has been completed
     * @param foundUsers has a list of the user's that had matching emails to the query word
     *                   this should always be a list with a size of 1 since you shouldn't
     *                   be able to register multiple users with the same email address
     */
    @Override
    public void loadSuccessful(ArrayList<BackendlessUser> foundUsers) {
        if (foundUsers.size() == 0) {
            DialogHelper.createErrorDialog(this, "no user found", "No user found with that email. Try again.");
        } else {
            // we can assume the right user is the first item of the list and ignore rest if there ever would be more
            searchedUser = foundUsers.get(0);

            // get the textview inside this layout and set its text to the found user's name
            TextView foundUserTextView = (TextView) findViewById(R.id.view_user_group_text_view_search_result);
            foundUserTextView.setText(searchedUser.getProperties().get("name").toString());

            // make the search result visible
            foundUserLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadFailed() {

    }

    @Override
    public void saveSuccessful() {
        // when the new member is successfully added to the group, reset the memberlist and search results
        foundUserLayout.setVisibility(View.INVISIBLE);
        initMembersList();
    }

    @Override
    public void saveFailed() {

    }
}
