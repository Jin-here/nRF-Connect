package com.vgaw.nrfconnect.view;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * @author caojin
 * @date 2018/3/17
 *
 * 适用于多行文字测不出来准确高度的情况
 */

public class MeasurableTextView extends androidx.appcompat.widget.AppCompatTextView {
    public MeasurableTextView(Context context) {
        super(context);
    }

    public MeasurableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasurableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Layout layout = getLayout();
        if (layout != null) {
            int height = (int) Math.ceil(getMaxLineHeight(this.getText().toString()))
                    + getCompoundPaddingTop() + getCompoundPaddingBottom();
            int width = getMeasuredWidth();
            setMeasuredDimension(width, height);
        }
    }

    /**
     * 如果文本中本来就含有\n则不能准确测出高度
     * @param str
     * @return
     */
    private float getMaxLineHeight(String str) {
        float screenW = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth();
        float paddingLeft = ((ViewGroup) this.getParent()).getPaddingLeft();
        float paddingRight = ((ViewGroup) this.getParent()).getPaddingRight();
        int line = (int) Math.ceil((this.getPaint().measureText(str) / (screenW - paddingLeft - paddingRight)));
        float height = (this.getPaint().getFontMetrics().bottom - this.getPaint().getFontMetrics().top) * line;
        return height;
    }
}
