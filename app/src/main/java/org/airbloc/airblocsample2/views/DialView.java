package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.models.Goal;
import org.airbloc.airblocsample2.utils.Utils;

public class DialView extends View implements View.OnTouchListener {

    private float percentage = 0f, beforePercentage = 0f;

    private int w;
    private final int defaultColor = Color.parseColor("#dddddd");
    private final int lowColor = Color.parseColor("#9E0D4E");
    private final int mediumColor = Color.parseColor("#486F9B");
    private final int highColor = Color.parseColor("#0DBBAE");
    private float thinStroke;
    private float thickStroke;
    private float dotLength;
    private Paint mDotPaint;
    private Paint mPaint;
    private Context mContext;
    private Goal goal;
    private Bitmap mBitmap;
    private LinearLayout mLayout;
    private SolinTextView mTextView;
    private Vibrator vibrator;
    private AnimationSet m100PercentAnim;

    public DialView(Context context) {
        super(context);
        init(context, null);
    }

    public DialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DialView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
        updateText();
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        w = context.getResources().getDimensionPixelSize(R.dimen.dialview);
        thinStroke = Utils.dp2px(context, 3);
        thickStroke = Utils.dp2px(context, 5f);
        dotLength = Utils.dp2px(context, 3f);

        mDotPaint = new Paint();
        mDotPaint.setStrokeCap(Paint.Cap.ROUND);
        mDotPaint.setAntiAlias(true);

        mPaint = new Paint();

        mLayout = new LinearLayout(mContext);
        mLayout.setGravity(Gravity.CENTER);

        mTextView = new SolinTextView(mContext);
        mTextView.setTextSize(36);
        mTextView.setGravity(Gravity.CENTER);
        mLayout.addView(mTextView);

        // 100%!
        m100PercentAnim = new AnimationSet(false);
        ScaleAnimation start = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        start.setDuration(80);
        start.setInterpolator(new AccelerateInterpolator());
        m100PercentAnim.addAnimation(start);

        ScaleAnimation end = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        end.setDuration(80);
        end.setStartOffset(80);
        end.setInterpolator(new AccelerateInterpolator());
        m100PercentAnim.addAnimation(end);

        setOnTouchListener(this);
    }

    private void updateText() {
        int color = highColor;
        if (percentage < 0.4f) color = lowColor;
        else if (percentage < 0.8f) color = mediumColor;

        mTextView.setTextColor(color);
        if (goal != null) {
            switch (goal.target.getType()) {
                case AMOUNT:
                    // 개수
                    mTextView.setText((int) (goal.target.score * percentage) + "개");
                    break;

                case TIME:
                    int hour = (int)(goal.target.score * percentage);
                    int minute = (int)((goal.target.score * percentage - hour)*60);

                    String text = hour == 0 ? minute + "분" :
                            String.format("%d시간\n%02d분", hour, minute);

                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 29.5f);
                    mTextView.setLineSpacing(0, 0.8f);
                    mTextView.setText(text);
                    break;
            }
        }
        else mTextView.setText(String.valueOf((int) (percentage * 100))); // no goal? percent
    }

    private void createBitmap() {
        if (w == 0) return;

        Bitmap bitmap = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float center = w / 2f;
        float radius = w / 2f;

        int color = highColor;
        if (percentage < 0.4f) color = lowColor;
        else if (percentage < 0.8f) color = mediumColor;

        mDotPaint.setStrokeWidth(thickStroke);
        for (int i = 0; i < 19; i++) {
            mDotPaint.setStrokeWidth((percentage - 1f) > i / 19f ? thickStroke : thinStroke);
            mDotPaint.setColor(percentage > i / 19f ? color : defaultColor);
            double angle = i / 19f * 2 * Math.PI - Math.PI / 2;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            float sx = center + (float) (cos * (radius - thickStroke));
            float sy = center + (float) (sin * (radius - thickStroke));
            float ex = center + (float) (cos * (radius - thickStroke - dotLength));
            float ey = center + (float) (sin * (radius - thickStroke - dotLength));
            canvas.drawLine(sx, sy, ex, ey, mDotPaint);
        }

        mLayout.measure(canvas.getWidth(), canvas.getHeight());
        mLayout.layout(0, 0, canvas.getWidth(), canvas.getHeight());

        updateText();
        mLayout.draw(canvas);

        mBitmap = bitmap;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode()) return;

        if (mBitmap == null) createBitmap();
        else canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }

    private double getAngle(MotionEvent motionEvent) {
        float center = w / 2f;
        return -Math.atan2(motionEvent.getX() - center, motionEvent.getY() - center) + Math.PI;
    }

    double prevAngle;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                prevAngle = getAngle(motionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                double angle = getAngle(motionEvent);
                double diff = angle - prevAngle;
                if (diff < -Math.PI) diff += Math.PI * 2;
                if (diff > Math.PI) diff -= Math.PI * 2;

                beforePercentage = percentage;
                percentage += (float) (diff / (Math.PI * 2) / (percentage > 1 ? 8 : 1));
                if (percentage < 0) percentage = 0;
                if (percentage > 2) percentage = 2;
                if (beforePercentage < 1 && percentage > 1) {
                    startAnimation(m100PercentAnim);
                    vibrator.vibrate(200);
                }

                prevAngle = angle;
                createBitmap();
                break;
        }
        return true;
    }

    public float getPercentage() {
        return percentage;
    }
}