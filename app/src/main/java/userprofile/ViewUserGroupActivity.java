package userprofile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mikko.budgetapplication.ConstantVariableSettings;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

import data.UserGroup;

/**
 * Created by Mikko on 2.6.2017.
 */
public class ViewUserGroupActivity extends AppCompatActivity {

    private ArrayList<String> memberNames;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_group);

        memberNames = (ArrayList<String>) getIntent().getSerializableExtra(ConstantVariableSettings.SEND_USER_GROUP_NAMES);

        System.out.println("caught usergroup " + memberNames);

    }


    public void closeActivityClicked(View view) {
        finish();
    }

    public void leaveGroupClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Remove")
                .setMessage("Are you sure you want to leave this group?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        leaveGroup();
                    }
                }).create().show();
    }

    private void leaveGroup() {
        //todo
    }
}
