package payment.history;

/**
 * Created by Mikko on 1.8.2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;


    public MyPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        // for now just a hard number of months
        int numberOfMonths = 14;

        switch (position) {
            case 0:
                return TabFragment.newInstance("first tab", numberOfMonths);
            case 1:
                return TabFragment.newInstance("second tab", numberOfMonths);
            case 2:
                return TabFragment.newInstance("third tab", numberOfMonths);
            default:
                return TabFragment.newInstance("manyth tab", numberOfMonths);
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}