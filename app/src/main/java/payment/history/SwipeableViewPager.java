package payment.history;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Mikko on 2.8.2016.
 */
public class SwipeableViewPager extends ViewPager {
    public SwipeableViewPager(Context context) {
        super(context);
    }

    public SwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        System.out.println("got to CHILD's intercepttouchevent");
        return true;
    }

}
