package payment.history;

/**
 * Created by Mikko on 1.8.2016.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mikko.budgetapplication.R;

public class TabFragment extends Fragment {

    private String tabName = "";
    private int numberOfMonths;

    // newInstance constructor for creating fragment with arguments
    public static TabFragment newInstance(String title, int numberOfMonths) {
        TabFragment tabFragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString("someTitle", title);
        args.putInt("someInt", numberOfMonths);
        tabFragment.setArguments(args);
        return tabFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabName = getArguments().getString("someTitle");
        numberOfMonths = getArguments().getInt("someInt");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment, container, false);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.monthly_view_pager);
        final MonthlyPagerAdapter adapter = new MonthlyPagerAdapter
                (getChildFragmentManager(), numberOfMonths);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(numberOfMonths - 1);

        return view;
    }

}