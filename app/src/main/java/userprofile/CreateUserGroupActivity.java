package userprofile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mikko.budgetapplication.R;

/**
 * Created by Mikko on 1.6.2017.
 */
public class CreateUserGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_group);



    }


    public void closeActivityClicked(View view) {
        finish();
    }

    public void createGroupClicked(View view) {
        System.out.println("TODO");
    }

}
