package com.vgaw.nrfconnect.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * @author caojin
 * @date 2018/3/4
 */

public class HorizontalSwipeLayout extends FrameLayout {
    private static final String TAG = "HorizontalSwipeLayout";
    private static final int DEFAULT_DURATION = 300;

    private Scroller mScroller;
    private float downY;
    private float downX;
    private Boolean childAbsorbed;
    private int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    private boolean expand;

    private HorizontalListener listener;

    private boolean horizontalSwipeDisabled;

    public HorizontalSwipeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(getContext());

        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (velocityX > 0) {
                    startScroll(true);
                } else {
                    startScroll(false);
                }
                reset();
                return true;
            }
        };
        final GestureDetector gd = new GestureDetector(getContext(), gestureListener);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });
    }

    public void setHorizontalSwipeEnabled(boolean enabled) {
        this.horizontalSwipeDisabled = !enabled;
    }

    public void setHorizontalSwipeListener(HorizontalListener listener) {
        this.listener = listener;
    }

    public void expand(boolean animate) {
        if (animate) {
            startScroll(true);
        } else {
            HorizontalSwipeLayout.this.scrollTo(-getRightPosition(), 0);
            setExpand(true);
        }
    }

    public void fold(boolean animate) {
        if (animate) {
            startScroll(false);
        } else {
            HorizontalSwipeLayout.this.scrollTo(0, 0);
            setExpand(false);
        }
    }

    public void expand() {
        expand(true);
    }

    public void fold() {
        fold(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (horizontalSwipeDisabled) {
            return false;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getRawX();
                float endY = event.getRawY();
                if (childAbsorbed != null) {
                    if (childAbsorbed) {
                        return false;
                    } else {
                        return true;
                    }
                }
                float distanceX = Math.abs(endX - downX);
                float distanceY = Math.abs(endY - downY);
                // 防止影响点击事件触发
                if (distanceX > mTouchSlop || distanceY > mTouchSlop) {
                    childAbsorbed = distanceX < distanceY;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (childAbsorbed != null) {
                    boolean temp = childAbsorbed;
                    reset();
                    return !temp;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                int moveDX = (int) (downX - event.getRawX());
                boolean toRight = (moveDX < 0);
                boolean reachLimit = (-getScrollX()) > getRightPosition();
                boolean reachRight = (getScrollX() >= 0);
                boolean canMove = ((toRight && !reachLimit) || (!toRight && !reachRight));
                if (canMove) {
                    int finalScrollX = getScrollX() + moveDX;
                    if (finalScrollX > 0) {
                        moveDX = -getScrollX();
                    }
                    scrollBy(moveDX, 0);
                }
                downX = event.getRawX();
                break;
            case MotionEvent.ACTION_UP:
                reset();

                startScroll(!xLeft());
                break;
        }
        return super.onTouchEvent(event);
    }

    private void reset() {
        childAbsorbed = null;
    }

    private void startScroll(boolean expand) {
        int dx;
        int dy;
        if (!expand) {
            dx = -getScrollX();
            dy = -0;

            setExpand(false);
        } else {
            dx = -(getRightPosition() + getScrollX());
            dy = 0;

            setExpand(true);
        }
        mScroller.startScroll(getScrollX(), 0,
                dx, dy, DEFAULT_DURATION);

        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);

            invalidate();
        }
    }

    private void setExpand(boolean expand) {
        if (this.expand != expand) {
            if (this.listener != null) {
                this.listener.onStateChanged(expand);
            }
        }
        this.expand = expand;
    }

    private boolean xLeft() {
        return -getScrollX() * 2 < getWidth();
    }

    private int getRightPosition() {
        return getWidth() * 8 / 10;
    }

    public interface HorizontalListener {
        void onStateChanged(boolean expand);
    }
}