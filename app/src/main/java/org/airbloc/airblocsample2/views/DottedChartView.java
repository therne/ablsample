package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.airbloc.airblocsample2.utils.Utils;

import org.airbloc.airblocsample2.utils.Utils;

public class DottedChartView extends View {

    private float[] data;

    private int w, h;
    private Paint linePaint, baselinePaint;
    private Paint outerDotPaint;
    private Paint innerDotPaint;
    private Paint mPaint;
    private Context mContext;
    private Bitmap mBitmap;
    private int lineColor = Color.parseColor("#DAF2ED");
    private int outerDotColor = Color.WHITE;
    private int innerDotColor = Color.parseColor("#FFC251");
    private float verticalPadding;

    public DottedChartView(Context context) {
        super(context);
        init(context, null);
    }

    public DottedChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DottedChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        verticalPadding = Utils.dp2px(context, 6);

        linePaint = new Paint();
        linePaint.setColor(lineColor);

        mPaint = new Paint();

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(Utils.dp2px(context, 4));
        linePaint.setAntiAlias(true);

        baselinePaint = new Paint();
        baselinePaint.setColor(Color.parseColor("#33FFFFFF"));
        baselinePaint.setStrokeWidth(Utils.dp2px(context, 1));
        baselinePaint.setAntiAlias(true);

        outerDotPaint = new Paint();
        outerDotPaint.setColor(outerDotColor);
        outerDotPaint.setStrokeWidth(Utils.dp2px(context, 12));
        outerDotPaint.setStrokeCap(Paint.Cap.ROUND);
        outerDotPaint.setAntiAlias(true);

        innerDotPaint = new Paint();
        innerDotPaint.setColor(innerDotColor);
        innerDotPaint.setStrokeWidth(Utils.dp2px(context, 6));
        innerDotPaint.setStrokeCap(Paint.Cap.ROUND);
        innerDotPaint.setAntiAlias(true);
    }

    private void createBitmap() {
        if (w == 0 || h == 0 || data == null) return;

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#7AD1BC"));

        // TODO: 면제권 사용시엔 7개 미만이 들어올 수 있음
        float partWidth = w / 7;
        float x = w - (partWidth / 2);

        int maxI = 0, minI = 0;
        for (int i = 1; i < data.length; i++) {
            if (data[maxI] < data[i]) maxI = i;
            if (data[minI] > data[i]) minI = i;
        }
        float maxValue = Math.max(data[maxI], 1);
        float bx = 0, by = 0;

        for (int i = data.length - 1; i >= 0; i--, x -= partWidth) {
            float top = (h - verticalPadding * 2) * (maxValue - data[i]) / maxValue;
            float y = top + verticalPadding;
            if (i < data.length - 1) {
                canvas.drawLine(bx, by, x, y, linePaint);
            }
            if (i == 0 && data.length > 0 && data.length < 7) {
                canvas.drawLine(x, 0, x, h, baselinePaint);
            }

            bx = x;
            by = y;
        }


        x = w - (partWidth / 2);

        for (int i = data.length-1; i >= 0; i--, x -= partWidth) {
            float top = (h - verticalPadding * 2) * (maxValue - data[i]) / maxValue;
            float y = top + verticalPadding;
            canvas.drawPoint(x, y, outerDotPaint);
            canvas.drawPoint(x, y, innerDotPaint);
        }

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
        if (data.length >= 1) {
            this.data = data;
            createBitmap();
        } else {
            throw new Error("length of data have to be 1 at least");
        }
    }
}
