package com.vgaw.nrfconnect.view.expansion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.vgaw.nrfconnect.R;

public class ExpansionHeader extends RelativeLayout implements ExpansionLayout.IndicatorListener {
    private static final int HEADER_ROTATION_EXPANDED = 180;
    private static final int HEADER_ROTATION_COLLAPSED = 0;

    private int headerIndicatorId;
    private int expansionLayoutId;
    @Nullable
    private View headerIndicator;
    @Nullable
    private ExpansionLayout expansionLayout;
    @Nullable
    private Animator indicatorAnimator;

    private boolean expansionLayoutInitialised = false;

    public ExpansionHeader(@NonNull Context context) {
        this(context, null);
    }

    public ExpansionHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpansionHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpansionHeader);
            if (a != null) {
                setHeaderIndicatorId(a.getResourceId(R.styleable.ExpansionHeader_headerIndicatorId, headerIndicatorId));
                setExpansionLayoutId(a.getResourceId(R.styleable.ExpansionHeader_expansionLayoutId, expansionLayoutId));
                a.recycle();
            }
        }
    }

    public void setHeaderIndicatorId(int headerIndicatorId){
        this.headerIndicatorId = headerIndicatorId;
        if (headerIndicatorId != 0) {
            headerIndicator = findViewById(headerIndicatorId);
            setExpansionHeaderIndicator(headerIndicator);
        }
    }

    public void setExpansionHeaderIndicator(@Nullable View headerIndicator) {
        this.headerIndicator = headerIndicator;

        //if not, the view will clip when rotate
        if (headerIndicator != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            headerIndicator.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        setup();
    }

    public void setExpansionLayout(@Nullable  ExpansionLayout expansionLayout) {
        this.expansionLayout = expansionLayout;
        setup();
    }

    public void setExpansionLayoutId(int expansionLayoutId) {
        this.expansionLayoutId = expansionLayoutId;

        if (expansionLayoutId != 0) {
            final ViewParent parent = getParent();
            if (parent instanceof ViewGroup) {
                final View view = ((ViewGroup) parent).findViewById(expansionLayoutId);
                if(view instanceof ExpansionLayout){
                    setExpansionLayout(((ExpansionLayout) view));
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //setup or update
        setHeaderIndicatorId(this.headerIndicatorId);
        //setup or update
        setExpansionLayoutId(this.expansionLayoutId);
        setup();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (expansionLayout != null) {
            expansionLayout.removeIndicatorListener(this);
        }
    }

    private void setup() {
        if (expansionLayout != null && !expansionLayoutInitialised) {
            expansionLayout.addIndicatorListener(this);

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    expansionLayout.toggle(true);
                }
            });

            initialiseView(expansionLayout.expanded());
            expansionLayoutInitialised = true;
        }
    }

    protected void initialiseView(boolean isExpanded) {
        if (headerIndicator != null) {
            headerIndicator.setRotation(isExpanded ? HEADER_ROTATION_EXPANDED : HEADER_ROTATION_COLLAPSED);
        }
    }

    protected void onExpansionModifyView(boolean willExpand) {
        setSelected(willExpand);
        if (headerIndicator != null) {
            if (indicatorAnimator != null) {
                indicatorAnimator.cancel();
            }
            if (willExpand) {
                indicatorAnimator = ObjectAnimator.ofFloat(headerIndicator, View.ROTATION, HEADER_ROTATION_EXPANDED);
            } else {
                indicatorAnimator = ObjectAnimator.ofFloat(headerIndicator, View.ROTATION, HEADER_ROTATION_COLLAPSED);
            }

            indicatorAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    indicatorAnimator = null;
                }
            });

            if (indicatorAnimator != null) {
                indicatorAnimator.setDuration(ExpansionLayout.ANIMATOR_DURING);
                indicatorAnimator.start();
            }
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle savedInstance = new Bundle();
        savedInstance.putParcelable("super", super.onSaveInstanceState());

        savedInstance.putInt("headerIndicatorId", headerIndicatorId);
        savedInstance.putInt("expansionLayoutId", expansionLayoutId);

        return savedInstance;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle savedInstance = (Bundle)state;

            headerIndicatorId = savedInstance.getInt("headerIndicatorId");
            expansionLayoutId = savedInstance.getInt("expansionLayoutId");
            //setup(); will wait to onAttachToWindow

            expansionLayoutInitialised = false;

            super.onRestoreInstanceState(savedInstance.getParcelable("super"));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Nullable
    public View getHeaderIndicator() {
        return headerIndicator;
    }

    @Override
    public void onStartedExpand(ExpansionLayout expansionLayout, boolean willExpand) {
        onExpansionModifyView(willExpand);
    }
}