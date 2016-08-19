package payment.history;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mikko.budgetapplication.DateHandler;

/**
 * Created by Mikko on 2.8.2016.
 */
public class MonthlyPagerAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;



    public MonthlyPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        for (int i = numberOfTabs; i >= 0; i--) {
            if (position == i) {
                String title;
                title = DateHandler.getMonthFromCurrentMonth(i);
                title += " ";
                title += DateHandler.getYearFromCurrentMonth(i);
                return MonthlyTabFragment.newInstance(title);
            }
        }
        // just in case math breaks
        return MonthlyTabFragment.newInstance("month X");
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
