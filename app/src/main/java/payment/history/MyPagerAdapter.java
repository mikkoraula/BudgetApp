package payment.history;

/**
 * Created by Mikko on 1.8.2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MyPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return TabFragment.newInstance("first tab");
            case 1:
                return TabFragment.newInstance("second tab");
            case 2:
                return TabFragment.newInstance("third tab");
            default:
                return TabFragment.newInstance("manyth tab");
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}