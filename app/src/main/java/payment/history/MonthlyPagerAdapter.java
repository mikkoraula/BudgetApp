package payment.history;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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

        switch (position) {
            case 0:
                return MonthlyTabFragment.newInstance("first month");
            case 1:
                return MonthlyTabFragment.newInstance("second month");
            case 2:
                return MonthlyTabFragment.newInstance("third month");
            default:
                return MonthlyTabFragment.newInstance("X month");
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
