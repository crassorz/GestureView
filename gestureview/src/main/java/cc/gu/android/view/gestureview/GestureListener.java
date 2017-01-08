package cc.gu.android.view.gestureview;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cc on 2017/1/8.
 */

public interface GestureListener extends View.OnTouchListener {
    int sort(View v);
    boolean onInterceptTouchEvent(View v, MotionEvent ev);
}
