package com.vgaw.nrfconnect.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.vgaw.nrfconnect.util.DensityUtil;

/**
 * Created by dell on 2018/3/4.
 */

public class HorizontalSwipeLayout extends FrameLayout {
    private static final String TAG = "HorizontalSwipeLayout";
    private static final int DEFAULT_DURATION = 300;

    private Scroller mScroller;
    private float startY;
    private float startX;
    private float downX;

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
                    expand();
                } else {
                    fold();
                }
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

    public void expand() {
        HorizontalSwipeLayout.this.scrollTo(-getRightPosition(), 0);
        setExpand(true);
    }

    public void fold() {
        HorizontalSwipeLayout.this.scrollTo(0, 0);
        setExpand(false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (horizontalSwipeDisabled) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                startX = event.getX();

                downX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = event.getY();
                float endX = event.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 水平方向
                if (distanceX > distanceY) {
                    return true;
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
                    scrollBy(moveDX, 0);
                }
                downX = event.getRawX();
                break;
            case MotionEvent.ACTION_UP:
                int dx;
                int dy;
                if (xLeft()) {
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
                break;
        }
        return super.onTouchEvent(event);
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