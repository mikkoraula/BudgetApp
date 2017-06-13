package userprofile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mikko.budgetapplication.ConstantVariableSettings;
import com.example.mikko.budgetapplication.R;

import java.util.ArrayList;

/**
 * Created by Mikko on 2.6.2017.
 */
public class ViewUserGroupActivity extends AppCompatActivity {

    private ArrayList<String> memberNames;
    private String userGroupName;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_group);

        userGroupName = getIntent().getStringExtra(ConstantVariableSettings.SEND_USER_GROUP_NAME);
        memberNames = (ArrayList<String>) getIntent().getSerializableExtra(ConstantVariableSettings.SEND_USER_GROUP_NAMES);

        System.out.println("caught usergroup " + memberNames);

        // set the group name
        TextView groupNameTextView = (TextView) findViewById(R.id.view_user_group_group_name);
        groupNameTextView.setText("Group name: " + userGroupName);

        initMembers(memberNames);
    }

    /**
     * make a list of the members show in this activity
     * the items are added to the linearlayout
     * @param memberNames
     */
    private void initMembers(ArrayList<String> memberNames) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.view_user_group_linear_layout_members);
        for (String memberName : memberNames) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setText("- " + memberName);

            linearLayout.addView(textView);
        }
    }


    public void closeActivityClicked(View view) {
        finish();
    }

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

    private void leaveGroup() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
