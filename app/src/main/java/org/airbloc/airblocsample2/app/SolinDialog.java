package org.airbloc.airblocsample2.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;

import org.airbloc.airblocsample2.App;

import butterknife.ButterKnife;

/**
 *
 */
public class SolinDialog extends Dialog {
    public SolinDialog(Context context) {
        super(context);
    }

    public SolinDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    /**
     * Same as Activity#findViewById, but this method doesn't need a typecasting
     *
     * @return View
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
