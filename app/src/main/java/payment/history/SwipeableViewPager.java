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

    private float xDown, xUp, yDown, yUp;
    private static final int MIN_DISTANCE = 150;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                xDown = event.getX();
                yDown = event.getY();
                //System.out.println("ACTION_DOWN");
                break;
        }
        if (isSwipeAllowed(event)) {
            //System.out.println("returned false");
            return super.onInterceptTouchEvent(event);
        } else {
            //System.out.println("returned true");
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //System.out.println("ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                //System.out.println("ACTION_UP");
                xUp = event.getX();
                yUp = event.getY();

                float dx = Math.abs(xUp - xDown);
                float dy = Math.abs(yUp - yDown);

                //System.out.println("over min distance:  dx = " + dx + ", MIN_DISTANCE = " + MIN_DISTANCE);
                // if movement exceeds minimum distance for a swipe
                if (dx > MIN_DISTANCE) {
                    // if the movement is larger in x axis than in y axis

                    if (dx > dy) {
                        //System.out.println("SwipeableViewPager: detected horizontal swiping");
                        //return super.onTouchEvent(event);
                    }
                }

                //System.out.println("SwipeableViewPager: detected some other form of touch");

                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isSwipeAllowed(MotionEvent event) {
        return true;
    }


}
