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
 * Created by dell on 2018/3/4.
 */

public class HorizontalSwipeLayout extends FrameLayout {
    private static final String TAG = "SwipeLayout";
    private Scroller mScroller;

    private float downX;
    private boolean expand;
    private SwipeListener listener;

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

    public void setSwipeListener(SwipeListener listener) {
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

    private float startY;
    private float startX;

    private final int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                startX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                if (distanceX > mTouchSlop) {
                    // 水平方向
                    if (distanceX > distanceY) {
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveDX = (int) (downX - event.getRawX());
                boolean toRight = (moveDX < 0);
                boolean reachLimit = event.getRawX() > getRightPosition();
                boolean reachRight = (getScrollX() == 0);
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
                        dx, dy);

                invalidate();
                break;
            default:
                return super.onTouchEvent(event);
        }
        return true;
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

    public interface SwipeListener {
        void onStateChanged(boolean expand);
    }
}