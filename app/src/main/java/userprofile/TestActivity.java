package userprofile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.example.mikko.budgetapplication.ConstantVariableSettings;
import com.example.mikko.budgetapplication.LoadingCallback;
import com.example.mikko.budgetapplication.MyBaseActivity;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.UserGroup;
import datahandler.BackendlessDataLoader;
import datahandler.BackendlessDataLoaderInterface;
import datahandler.BackendlessDataSaver;
import datahandler.BackendlessDataSaverInterface;

/**
 * Created by Mikko on 12.7.2017.
 */

/*
public class TestActivity extends MyBaseActivity implements BackendlessDataLoaderInterface<UserGroup>, BackendlessDataSaverInterface {

    private UserGroup userGroup;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState);
        setContentView(R.layout.activity_test);

        BackendlessUser test = (BackendlessUser) getIntent().getSerializableExtra("lol");
        System.out.println("caught " + test);

        setHelpString(R.string.help_view_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UserGroup userGroup = new UserGroup();
        ArrayList<BackendlessUser> users = new ArrayList<>();
        users.add(Backendless.UserService.CurrentUser());
        userGroup.setUsers(users);
        userGroup.setGroupName("testi ryhmi");

        this.userGroup = userGroup;
        System.out.println("the user service user; " + Backendless.UserService.CurrentUser());
        System.out.println("current users in group before any magic: " + userGroup.getUsers().size());

        new BackendlessDataSaver(this, userGroup, users, "users", UserGroup.class).saveObject();

    }

    public void buttonClicked(View view) {
        ArrayList<BackendlessUser> users = userGroup.getUsers();

        System.out.println("before saving, this many users " + userGroup.getUsers().size());
        //BackendlessDataSaver.removeTestFromGroup(this, userGroup, users.get(0));
    }

    public void button2Clicked(View view) {
        ArrayList<BackendlessUser> users = new ArrayList<>();
        users.add(Backendless.UserService.CurrentUser());
        userGroup.setUsers(users);

        //BackendlessDataSaver.addMembersToGroup(this, userGroup, users);
    }

    @Override
    public void loadSuccessful(ArrayList<UserGroup> objectList) {


        for (UserGroup group : objectList) {
                if (group.getOwnerId().equals(Backendless.UserService.CurrentUser().getObjectId())) {
                    /*
                    System.out.println("unaltered user: ");
                    System.out.println(group.toString());
                    System.out.println(" ");
                    userGroup = new UserGroup();
                    ArrayList<BackendlessUser> users = new ArrayList<>();
                    users.add(user);
                    userGroup.setUsers(users);
                    userGroup.setGroupName(group.getGroupName());
                    userGroup.setObjectId(group.getObjectId());

                    System.out.println("altered user: ");
                    System.out.println(userGroup.toString());

                    userGroup = group;
                }
        }

        if (userGroup != null) {
            System.out.println("when loaded, this many users " + userGroup.getUsers().size());
            TextView textView = (TextView) findViewById(R.id.test_activity_text_view);
            if (userGroup.getUsers().size() > 0)
                textView.setText(userGroup.getUsers().get(0).getEmail());
            else
                textView.setText("");
        }

    }

    /*
    @Override
    public void loadSuccessful(ArrayList<ProcessedUserGroup> objectList) {


        for (ProcessedUserGroup group : objectList) {
            for (User user : group.getUsers()) {
                if (user.getObjectId().equals(Backendless.UserService.CurrentUser().getObjectId())) {
                    userGroup = new UserGroup();
                    ArrayList<BackendlessUser> users = new ArrayList<>();
                    users.add(Backendless.UserService.CurrentUser());
                    userGroup.setUsers(users);
                    userGroup.setGroupName(group.getGroupName());
                    userGroup.setObjectId(group.getObjectId());
                }
            }
        }

        if (userGroup != null) {
            System.out.println("when loaded, this many users " + userGroup.getUsers().size());
            TextView textView = (TextView) findViewById(R.id.test_activity_text_view);
            textView.setText(userGroup.getUsers().get(0).getEmail());
        }

    }


    @Override
    public void loadFailed() {

    }

    @Override
    public void saveSuccessful() {
        System.out.println("asd");

        //BackendlessDataLoader.loadUserGroups(this);
        loadUserGroups(this);
    }

    @Override
    public void saveFailed() {

    }

    public static void loadUserGroups(Context context) {
        LoadingCallback<List<UserGroup>> callback = createUserGroupLoadingCallback(context);
        callback.showLoading();
        Backendless.Data.of( UserGroup.class ).find(callback);
    }

    private static LoadingCallback<List<UserGroup>> createUserGroupLoadingCallback(final Context context) {
        return new LoadingCallback<List<UserGroup>>(context, context.getString(R.string.loading_user_groups)) {
            @Override
            public void handleResponse( List<UserGroup> userCollection) {
                super.handleResponse(userCollection);

                ArrayList<UserGroup> userGroups = new ArrayList<>(userCollection);

                ((BackendlessDataLoaderInterface) context).loadSuccessful(userGroups);
            }

            @Override
            public void handleFault( BackendlessFault fault ) {
                super.handleFault(fault);
                ((BackendlessDataLoaderInterface) context).loadFailed();
            }
        };
    }
}
*/
