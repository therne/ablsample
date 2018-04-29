package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class CustomViewPager extends ViewPager {
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPersonalityViewScrollable(boolean scrollable) {
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof PersonalityTestView) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}