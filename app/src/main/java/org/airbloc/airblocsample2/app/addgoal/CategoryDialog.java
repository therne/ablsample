package org.airbloc.airblocsample2.app.addgoal;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.SolinDialog;

/**
 * 카테고리 선택창 다이얼로그
 */
public class CategoryDialog extends SolinDialog implements View.OnClickListener {
    public interface OnResultCallback {
        void result(String category);
    }

    private OnResultCallback callback;
    private boolean first;

    private final int ANIM_DURATION = 250;

    public CategoryDialog(Context context, OnResultCallback callback) {
        super(context, R.style.BaseTheme_Translucent);
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        first = true;
        setContentView(R.layout.dialog_category);

        findViewById(R.id.root).setOnClickListener((v) -> animateClose(null));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (first && hasFocus) {
            first = false;

            animateOpen();
        }
    }

    private void animateOpen() {
        AlphaAnimation aanim = new AlphaAnimation(0, 1);
        aanim.setDuration(ANIM_DURATION);
        findViewById(R.id.root).startAnimation(aanim);

        int[] categories = {R.id.category_etc, R.id.category_sport, R.id.category_habit_breaker, R.id.category_learn, R.id.category_challenge, R.id.category_habit_maker};
        View center = findViewById(R.id.category_etc);
        float cx = center.getX();
        float cy = center.getY();
        for (int category : categories) {
            View view = findViewById(category);
            View image = ((ViewGroup) view).getChildAt(0);
            View text = ((ViewGroup) view).getChildAt(1);
            image.setOnClickListener((v) -> {
                CategoryDialog.this.onClick(view);
            });

            TranslateAnimation tanim = new TranslateAnimation(cx - view.getX(), 0, cy - view.getY(), 0);
            tanim.setDuration(ANIM_DURATION);
            tanim.setInterpolator(new AccelerateInterpolator());
            view.startAnimation(tanim);

            ScaleAnimation sanim = new ScaleAnimation(0.3f, 1, 0.3f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            sanim.setDuration(ANIM_DURATION);
            sanim.setInterpolator(new AccelerateInterpolator());
            image.startAnimation(sanim);

            AlphaAnimation aanim2 = new AlphaAnimation(0, 1);
            aanim2.setDuration(ANIM_DURATION);
            text.startAnimation(aanim2);
        }
    }

    private void animateClose(String category_code) {
        AlphaAnimation aanim = new AlphaAnimation(1, 0);
        aanim.setDuration(ANIM_DURATION);
        aanim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (category_code != null) callback.result(category_code);
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        findViewById(R.id.root).startAnimation(aanim);

        int[] categories = {R.id.category_etc, R.id.category_sport, R.id.category_habit_breaker, R.id.category_learn, R.id.category_challenge, R.id.category_habit_maker};
        View center = findViewById(R.id.category_etc);
        float cx = center.getX();
        float cy = center.getY();
        for (int category : categories) {
            View view = findViewById(category);
            View image = ((ViewGroup) view).getChildAt(0);
            View text = ((ViewGroup) view).getChildAt(1);
            image.setOnClickListener((v) -> {
                CategoryDialog.this.onClick(view);
            });

            TranslateAnimation tanim = new TranslateAnimation(0, cx - view.getX(), 0, cy - view.getY());
            tanim.setDuration(ANIM_DURATION);
            tanim.setInterpolator(new AccelerateInterpolator());
            view.startAnimation(tanim);

            ScaleAnimation sanim = new ScaleAnimation(1, 0.3f, 1, 0.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            sanim.setDuration(ANIM_DURATION);
            sanim.setInterpolator(new AccelerateInterpolator());
            image.startAnimation(sanim);

            AlphaAnimation aanim2 = new AlphaAnimation(1, 0);
            aanim2.setDuration(ANIM_DURATION);
            text.startAnimation(aanim2);
        }
    }

    @Override
    public void onClick(View v) {
        String category = "";

        switch (v.getId()) {
            case R.id.category_etc:
                category = "etc";
                break;

            case R.id.category_sport:
                category = "sport";
                break;

            case R.id.category_habit_breaker:
                category = "habit_breaker";
                break;

            case R.id.category_learn:
                category = "learn";
                break;

            case R.id.category_challenge:
                category = "challenge";
                break;

            case R.id.category_habit_maker:
                category = "habit_maker";
                break;
        }

        animateClose(category);
    }

    @Override
    public void onBackPressed() {
        animateClose(null);
    }
}
