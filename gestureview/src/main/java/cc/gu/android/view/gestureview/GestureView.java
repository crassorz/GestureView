package cc.gu.android.view.gestureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.gesture.GestureOverlayView;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cc on 2016/12/25.
 */

public class GestureView extends GestureOverlayView {
    public GestureView(Context context) {
        super(context);
    }

    public GestureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GestureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private List<GestureListener> listeners = new LinkedList<>();
    private GestureListener listener;
    final private Comparator<GestureListener> sort = new Comparator<GestureListener>() {
        @Override
        public int compare(GestureListener o1, GestureListener o2) {
            return o1.sort(GestureView.this) - o2.sort(GestureView.this);
        }
    };

    public boolean addGestureListener(GestureListener listener) {
        if (listener == null) {
            return false;
        }
        removeGestureListener(listener);
        boolean ret = listeners.add(listener);
        Collections.sort(listeners, sort);
        return ret;
    }

    private boolean removeGestureListener(GestureListener listener) {
        if (this.listener == listener) {
            this.listener = null;
        }
        return listeners.remove(listener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            return super.dispatchTouchEvent(event);
        } finally {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                listener = null;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        for (GestureListener l:listeners) {
            if (l.onInterceptTouchEvent(this, ev)) {
                listener = l;
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (listener != null) {
            return listener.onTouch(this, event);
        }
        return super.onTouchEvent(event);
    }
}
