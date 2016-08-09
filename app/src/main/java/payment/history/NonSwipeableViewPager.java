package payment.history;

/**
 * Created by Mikko on 1.8.2016.
 */
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipeableViewPager extends ViewPager {

    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        System.out.println("got to intercepttouchevent");
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("got to parent's on touchevent!!!!!!!!!!!!!!!!!!!!!!");
        return false;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        System.out.println(" super.canScrollHorizontally(direction: " + super.canScrollHorizontally(direction));
        return false;
    }
}
