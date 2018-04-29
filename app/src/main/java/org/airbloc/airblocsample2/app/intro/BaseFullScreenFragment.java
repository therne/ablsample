package org.airbloc.airblocsample2.app.intro;

import android.support.v4.app.Fragment;

/**
 *
 */
public class BaseFullScreenFragment extends Fragment {
    private int statusBarBg, index;

    protected void setStatusBarColor(int colorId) {
        statusBarBg = getResources().getColor(colorId);
    }

    public int getStatusBarColor() {
        return statusBarBg;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) getView().setTag(getIndex());
    }
}
