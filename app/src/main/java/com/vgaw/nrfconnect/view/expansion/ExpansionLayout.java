package com.vgaw.nrfconnect.view.expansion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.vgaw.nrfconnect.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by caojin on 2018/3/7.
 */

public class ExpansionLayout extends FrameLayout implements View.OnClickListener {
    private static final int BACKGROUND_COLLAPSE = Color.TRANSPARENT;
    private static final int BACKGROUND_EXPANDED = Color.LTGRAY;

    private View backgroundView;
    private View contentLayout;
    private boolean expanded;

    private ValueAnimator animator;
    private int maxHeight;

    private List<IndicatorListener> mIndicatorListenerList = new ArrayList<>();

    public ExpansionLayout(@NonNull Context context) {
        this(context, null);
    }

    public ExpansionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpansionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void addIndicatorListener(IndicatorListener listener) {
        mIndicatorListenerList.add(listener);
    }

    public void removeIndicatorListener(IndicatorListener listener) {
        mIndicatorListenerList.remove(listener);
    }

    public void toggle(boolean animate) {
        if (expanded) {
            collapse(animate);
        } else {
            expand(animate);
        }
    }

    public boolean expanded() {
        return this.expanded;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentLayout = getChildAt(1);
        proNoAnimation(false, expanded);
    }

    private void expand(boolean animate) {
        if (animate) {
            proAnimation(true);
        } else {
            proNoAnimation(true, true);
        }
    }

    private void collapse(boolean animate) {
        if (animate) {
            proAnimation(false);
        } else {
            proNoAnimation(true, false);
        }
    }

    private void proNoAnimation(boolean callListener, boolean expand) {
        if (callListener) {
            callListener(expand);
        }

        if (expand) {
            updateContentLayoutHeight(maxHeight);
            backgroundView.setBackgroundColor(BACKGROUND_EXPANDED);
            setVisibility(VISIBLE);

            expanded = true;
        } else {
            updateContentLayoutHeight(0);
            backgroundView.setBackgroundColor(BACKGROUND_COLLAPSE);
            setVisibility(GONE);

            expanded = false;
        }
    }

    private void proAnimation(final boolean expand) {
        if (animator != null) {
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animator.getAnimatedValue();
                if (!expand) {
                    animatedValue = 1 - animatedValue;
                }
                backgroundView.setBackgroundColor(blendColors(BACKGROUND_COLLAPSE, BACKGROUND_EXPANDED, animatedValue));
                updateContentLayoutHeight((int) (maxHeight * animatedValue));
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (expand) {
                    expanded = true;
                } else {
                    ExpansionLayout.this.setVisibility(GONE);
                    expanded = false;
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (expand) {
                    ExpansionLayout.this.setVisibility(VISIBLE);
                }

                callListener(expand);
            }
        });
        animator.start();
    }

    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float a = (Color.alpha(color2) * ratio) + (Color.alpha(color1) * inverseRation);
        float r = (Color.red(color2) * ratio) + (Color.red(color1) * inverseRation);
        float g = (Color.green(color2) * ratio) + (Color.green(color1) * inverseRation);
        float b = (Color.blue(color2) * ratio) + (Color.blue(color1) * inverseRation);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

    private void updateContentLayoutHeight(int height) {
        ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
        layoutParams.height = height;
        contentLayout.setLayoutParams(layoutParams);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        maxHeight = getResources().getDimensionPixelOffset(R.dimen.space_200);
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpansionHeader);
            if (a != null) {
                setExpanded(a.getBoolean(R.styleable.ExpansionLayout_expanded, false));
                a.recycle();
            }
        }
        backgroundView = new View(getContext());
        addView(backgroundView, new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        backgroundView.setOnClickListener(this);
    }

    private void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private void callListener(boolean willExpand) {
        Iterator<IndicatorListener> iterator = mIndicatorListenerList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onStartedExpand(this, willExpand);
        }
    }

    @Override
    public void onClick(View v) {
        toggle(true);
    }

    public interface IndicatorListener {
        void onStartedExpand(ExpansionLayout expansionLayout, boolean willExpand);
    }
}