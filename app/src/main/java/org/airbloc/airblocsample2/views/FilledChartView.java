package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import org.airbloc.airblocsample2.utils.Utils;

public class FilledChartView extends View {

    private float[] data;

    private int w, h;
    private Paint graphPaint;
    private Paint mPaint;
    private Context mContext;
    private Bitmap mBitmap;
    private LinearLayout mLayout;
    private int primaryColor = Color.parseColor("#FFC800");
    private int secondaryColor = Color.parseColor("#9B9B9B");
    private float paddingTop;

    public FilledChartView(Context context) {
        super(context);
        init(context, null);
    }

    public FilledChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FilledChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        paddingTop = Utils.dp2px(context, 24);

        graphPaint = new Paint();
        graphPaint.setColor(primaryColor);
        graphPaint.setAntiAlias(true);

        mPaint = new Paint();

        mLayout = new LinearLayout(mContext);
    }

    private void createBitmap() {
        if (w == 0 || h == 0 || data == null) return;

        mLayout.removeAllViews();
        for (int i = 0; i < data.length - 2; i++) {
            SolinTextView textView = new SolinTextView(mContext);
            textView.setGravity(Gravity.CENTER);
            mLayout.addView(textView);
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float partWidth = w / (data.length - 2);
        float sx = -partWidth / 2;
        float ex = w + partWidth / 2;
        Path path = new Path();
        path.moveTo(sx, h);
        float x = sx;
        int maxI = 1, minI = 1;
        for (int i = 2; i < data.length - 1; i++) {
            if (data[maxI] < data[i]) maxI = i;
            if (data[minI] > data[i]) minI = i;
        }
        float maxValue = Math.max(data[maxI], 1);
        for (int i = 0; i < data.length; i++, x += partWidth) {
            float top = (h - paddingTop) * (maxValue - data[i]) / maxValue;
            if (i >= 1 && i < data.length - 1) {
                boolean focus = i == maxI || i == minI;
                SolinTextView textView = (SolinTextView) mLayout.getChildAt(i - 1);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
                layoutParams.width = (int) partWidth;
                layoutParams.topMargin = (int) top;
                textView.setLayoutParams(layoutParams);
                textView.setTextColor(focus ? primaryColor : secondaryColor);
                textView.setTextSize(focus ? 11 : 9);
                textView.setText(String.valueOf((int) (data[i] * 100)));
            }
            path.lineTo(x, top + paddingTop);
        }
        path.lineTo(ex, h);
        path.close();

        canvas.drawPath(path, graphPaint);

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
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setData(float[] data) {
        if (data.length >= 3) {
            this.data = data;
            createBitmap();
        } else {
            throw new Error("length of data have to be 3 at least");
        }
    }
}
