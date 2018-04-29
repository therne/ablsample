package org.airbloc.airblocsample2.app;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.airbloc.airblocsample2.R;

import butterknife.ButterKnife;

/**
 * Base activity for all activities in Solin app.
 */
public class SolinActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    /**
     * Set up a toolbar.
     *
     * @param drawer DrawerLayout
     */
    @SuppressWarnings("ConstantConditions")
    protected void setUpToolbar(DrawerLayout drawer) {
        toolbar = findView(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (drawer != null) {
            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name);
            drawer.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
        }
    }

    protected void setUpToolbar() {
        setUpToolbar(null);
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
}
