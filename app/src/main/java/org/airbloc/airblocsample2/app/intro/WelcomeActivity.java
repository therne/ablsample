package org.airbloc.airblocsample2.app.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.MainActivity;
import org.airbloc.airblocsample2.app.SolinActivity;
import org.airbloc.airblocsample2.views.CustomViewPager;
import org.airbloc.airblocsample2.views.IndicatorView;

public class WelcomeActivity extends SolinActivity implements WelcomeFinishFragment.OnFinishListener {

    private CustomViewPager pager;
    private LinearLayout indicatorLayout;
    private int selected;
    private boolean isFirst;

    private BaseFullScreenFragment fragments[];
    private WelcomeFragment welcomeFragment;
    private DAuthFragment reportInfoFragment;
    private BadgeInfoFragment badgeInfoFragment;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        isFirst = !getIntent().getBooleanExtra("showingAgain", false);

        // TO SET status bar background color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        pager = findViewById(R.id.viewPager);
        indicatorLayout = findViewById(R.id.indicatorLayout);

        welcomeFragment = WelcomeFragment.newInstance();
        reportInfoFragment = DAuthFragment.newInstance();
        badgeInfoFragment = BadgeInfoFragment.newInstance();
        fragments = new BaseFullScreenFragment[]{
                welcomeFragment,
                HydeInfoFragment.newInstance(),
                PersonalityTestFragment.newInstance(),
                reportInfoFragment,
                badgeInfoFragment,
                WelcomeFinishFragment.newInstance()
        };

        // add indicators
        for (int i = 0; i < fragments.length; i++) {
            final int index = i;
            IndicatorView indicatorView = new IndicatorView(this);
            indicatorView.setOnClickListener(v ->
                            pager.setCurrentItem(index)
            );
            indicatorLayout.addView(indicatorView);

            // set tag
            fragments[i].setIndex(i);
        }
        selected = 0;
        indicatorLayout.getChildAt(selected).setSelected(true);

        pager.setOffscreenPageLimit(fragments.length);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                indicatorLayout.getChildAt(selected).setSelected(false);
                indicatorLayout.getChildAt(position).setSelected(true);
                selected = position;
            }

            @Override
            public void onPageScrolled(int pos, float offset, int px) {
                pager.getParent().requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // letter popping up
        pager.setPageTransformer(false, (page, position) -> {
            if (position <= -1 || position > 1) return;

            int pageWidth = page.getWidth(), index = (int) page.getTag();

            if (selected == 0 || selected == 1) {
                if (index == 1) {
                    welcomeFragment.getLetterView().setTranslationY((pageWidth / 2) * (1 - position));
                }
            } else if (selected >= 2 && selected <= 4 && index == 3) {
                View report = reportInfoFragment.getReportView();
                report.setTranslationY(report.getHeight() * 0.65f * Math.abs(position));

            } else if (selected >= 3 && selected <= 5 && index == 4) {
                View badge = badgeInfoFragment.getBadgeView(),
                        badgeContent = badgeInfoFragment.getBadgeContentView();

                // reveal with zooming in
                float scale = 1 - 0.7f /* speed */ * Math.abs(position);
                badge.setScaleX(scale);
                badge.setScaleY(scale);
                badgeContent.setScaleX(scale);
                badgeContent.setScaleY(scale);

                // reveal with rotating badge background
                badge.setRotation(360 * 0.5f * position);

                // alpha
                badge.setAlpha(scale);
                badgeContent.setAlpha(scale);
            }
        });

        adapter = new PagerAdapter(getSupportFragmentManager(), isFirst);
        pager.setAdapter(adapter);

        pager.setOnTouchListener((v, event) -> {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
        );
    }

    public void hydeSelected() {
        adapter.count = fragments.length;
        adapter.notifyDataSetChanged();
    }

    public void goNext() {
        int next = pager.getCurrentItem() + 1;
        if (next < fragments.length)
            pager.setCurrentItem(next, true);
    }


    @Override
    public void onBackPressed() {
        if (isFirst) moveTaskToBack(true);
        super.onBackPressed();
    }

    @Override
    public void onFinish() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        int count = fragments.length;
        public PagerAdapter(FragmentManager fm, boolean preventHydeScroll) {
            super(fm);
            if (preventHydeScroll) count = 3;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}