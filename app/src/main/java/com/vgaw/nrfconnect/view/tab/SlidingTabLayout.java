/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vgaw.nrfconnect.view.tab;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * To be used with ViewPager to provide a tab indicator component which give constant feedback as to
 * the user's scroll progress.
 * <p>
 * To use the component, simply add it to your view hierarchy. Then in your
 * {@link android.app.Activity} or {@link androidx.fragment.app.Fragment} call
 * {@link #setViewPager(ViewPager)} providing it the ViewPager this layout is being used for.
 * <p>
 * The colors can be customized in two ways. The first and simplest is to provide an array of colors
 * via {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)}. The
 * alternative is via the {@link TabColorizer} interface which provides you complete control over
 * which color is used for any individual position.
 * <p>
 * The views used as tabs can be customized by calling {@link #setCustomTabView(int, int)},
 * providing the layout ID of your custom layout.
 */

/**
 * 分析
 * 1. 组成
 *    1. tab
 *    2. tab对应的具体内容，可能是fragment或者其他任何
 * 2. tab
 *    1. tab自己构成，可能是创建自己的adapter
 *    2. tab变化（切换/增加/删除）时，需要通知tab对应的具体内容
 *    3. tab具体内容变化（切换/增加/删除）时，需要被通知到
 * 3. tab具体内容
 *    1. 自己管理，可能需要自己的adapter
 *    2. tab变化时，需要被通知到
 *    3. 自身变化时，需要通知tab
 * 4. 约束
 *    1. tab的个数和tab具体内容的个数需要一致（可选）
 *
 * 实现
 * 1. tab变化
 *    1. 切换interface： CustomTabAdapter
 *    2. 增加/删除interface： ModifiableTabInterface extends CustomTabAdapter
 * 2. tab基础功能
 *    1. 获取tab个数：getItemCount()
 *    2. 获取tab内容：getView()
 *    3. 获取tab数据：getItem();
 *    4. 获取tab分类：getItemType();
 */
public class SlidingTabLayout extends HorizontalScrollView implements FixedTabLayout.ScrollListener {
    private static final int TITLE_OFFSET_DIPS = 24;

    private int mTitleOffset;
    private final FixedTabLayout mTabStrip;

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new FixedTabLayout(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        mTabStrip.setTabStyle(FixedTabLayout.STYLE_LIBERTY);
        mTabStrip.setScrollListener(this);
    }

    /**
     * Set the custom {@link FixedTabLayout.TabColorizer} to be used.
     *
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)} to achieve
     * similar effects.
     */
    public void setCustomTabColorizer(FixedTabLayout.TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
     * Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setDividerColors(int... colors) {
        mTabStrip.setDividerColors(colors);
    }

    public void setViewPager(InteractiveInterface viewPager) {
        mTabStrip.setViewPager(viewPager);
    }

    public void setTabAdapter(TabAdapter adapter) {
        mTabStrip.setTabAdapter(adapter);
    }

    @Override
    public void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }
}