package com.vgaw.nrfconnect.view;

import android.content.Context;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dell on 2018/3/22.
 */

public class InterceptSlidingPaneLayout extends SlidingPaneLayout {
    private boolean intercept;

    public InterceptSlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (intercept) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean isIntercept() {
        return intercept;
    }

    public void setIntercept(boolean intercept) {
        this.intercept = intercept;
    }
}
