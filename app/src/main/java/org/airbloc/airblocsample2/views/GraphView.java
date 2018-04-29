package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.utils.Utils;

import org.airbloc.airblocsample2.utils.Utils;

public class GraphView extends View {

    private int primaryColor;
    private int secondaryColor;
    private float percentage;
    private int border;

    private int w;
    private Paint primaryPaint;
    private Paint secondaryPaint;
    private Paint innerPaint;
    private Paint mPaint;
    private Context mContext;
    private Bitmap mBitmap;
    private LinearLayout mLayout;
    private SolinTextView mTextView;

    public GraphView(Context context) {
        super(context);
        init(context, null);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        primaryColor = Color.GRAY;
        secondaryColor = Color.RED;
        percentage = 0.5f;
        border = (int) Utils.dp2px(mContext, 3);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GraphView);
            primaryColor = a.getColor(R.styleable.GraphView_primaryColor, primaryColor);
            secondaryColor = a.getColor(R.styleable.GraphView_secondaryColor, secondaryColor);
            percentage = a.getFloat(R.styleable.GraphView_percentage, percentage);
            border = a.getDimensionPixelSize(R.styleable.GraphView_border, border);
            a.recycle();
        }

        primaryPaint = new Paint();
        primaryPaint.setColor(primaryColor);
        primaryPaint.setAntiAlias(true);

        secondaryPaint = new Paint();
        secondaryPaint.setColor(secondaryColor);
        secondaryPaint.setAntiAlias(true);

        innerPaint = new Paint();
        innerPaint.setColor(Color.WHITE);
        innerPaint.setAntiAlias(true);

        mPaint = new Paint();

        mLayout = new LinearLayout(mContext);
        mLayout.setGravity(Gravity.CENTER);

        mTextView = new SolinTextView(mContext);
        mTextView.setTextColor(primaryColor);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.5f);
        mTextView.setType(7);
        mLayout.addView(mTextView);
    }

    private void createBitmap() {
        if (w == 0) return;

        Bitmap bitmap = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float center = w / 2f;
        float radius = w / 2f;
        float angle = percentage * 360;
        RectF rectF = new RectF(0, 0, w, w);
        canvas.drawArc(rectF, -90, angle, true, primaryPaint);
        canvas.drawArc(rectF, angle - 90, 360 - angle, true, secondaryPaint);
        canvas.drawCircle(center, center, radius - border, innerPaint);

        mTextView.setText(String.valueOf((int) (percentage * 100)));

        mLayout.measure(canvas.getWidth(), canvas.getHeight());
        mLayout.layout(0, 0, canvas.getWidth(), canvas.getHeight());

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
        createBitmap();
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
        createBitmap();
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
        createBitmap();
    }

    public float getPercentage() {
        return percentage;
    }

    public void setBorder(int border) {
        this.border = border;
        createBitmap();
    }

    public int getBorder() {
        return border;
    }
}
