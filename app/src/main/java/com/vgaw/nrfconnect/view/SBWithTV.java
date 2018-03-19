package com.vgaw.nrfconnect.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vgaw.nrfconnect.R;

/**
 * @author caojin
 * @date 2017/1/12
 */

public class SBWithTV extends GridLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private TextView tv_value;
    private TextView tv_label;
    private SeekBar sb;

    private float maxValue;
    private float minValue;
    private float interval;

    private float currentValue;

    public SBWithTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setColumnCount(2);

        tv_label = new TextView(getContext());
        addView(tv_label);
        tv_value = new TextView(getContext());
        addView(tv_value, new LayoutParams(GridLayout.spec(UNDEFINED), GridLayout.spec(UNDEFINED, RIGHT)));

        LinearLayout container = new LinearLayout(getContext());
        container.setGravity(Gravity.CENTER_VERTICAL);
        container.setOrientation(LinearLayout.HORIZONTAL);
        ImageButton btnMinus = new AppCompatImageButton(getContext());
        btnMinus.setTag(0);
        btnMinus.setOnClickListener(this);
        btnMinus.setBackgroundColor(getResources().getColor(R.color.icon_light_2));
        btnMinus.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnMinus.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
        //btnMinus.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.icon_tint)));
        container.addView(btnMinus, new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.space_24), getResources().getDimensionPixelSize(R.dimen.space_24)));

        sb = (SeekBar) View.inflate(getContext(), R.layout.view_seekbar, null);
        container.addView(sb, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        ImageButton btnAdd = new AppCompatImageButton(getContext());
        btnAdd.setTag(1);
        btnAdd.setOnClickListener(this);
        btnAdd.setBackgroundColor(getResources().getColor(R.color.icon_light_2));
        btnAdd.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnAdd.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
        //btnAdd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.icon_tint)));
        container.addView(btnAdd, new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.space_24), getResources().getDimensionPixelSize(R.dimen.space_24)));

        addView(container, new LayoutParams(GridLayout.spec(UNDEFINED), GridLayout.spec(UNDEFINED, 2, FILL)));
    }

    /**
     * 设置初始值
     */
    public void setProgress(float value){
        currentValue = value;
        sb.setProgress((int) ((value - minValue) / interval));
    }

    public void init(String label, float min, float max, float interval){
        this.minValue = min;
        this.maxValue = max;
        this.interval = interval;
        configure();
        tv_label.setText(label);
        this.currentValue = minValue;
        showValue();
    }

    /**
     * 获取值
     * @return
     */
    public float getProgress(){
        return this.currentValue;
    }

    private void configure(){
        sb.setMax((int) ((maxValue - minValue) / interval) + 1 - 1);
        sb.setOnSeekBarChangeListener(this);
    }

    private void showValue(){
        if (listener != null){
            tv_value.setText(listener.onShowValue(this, currentValue));
        }else {
            tv_value.setText(String.valueOf(currentValue));
        }
    }

    @Override
    public void onClick(View v) {
        if (((Integer)v.getTag()) == 0) {
            if (currentValue - interval < minValue) {
                return;
            }
            currentValue -= interval;
        } else {
            if (currentValue + interval > maxValue) {
                return;
            }
            currentValue += interval;
        }
        setProgress(currentValue);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        currentValue = minValue + (float) progress * interval;
        showValue();
        listener.onProgressChanged(SBWithTV.this, currentValue, fromUser);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface ShowValueListener{
        String onShowValue(View view, float raw);

        void onProgressChanged(View view, float currentValue, boolean fromUser);
    }

    private ShowValueListener listener;

    public void setShowValueListener(ShowValueListener listener){
        this.listener = listener;
    }
}
