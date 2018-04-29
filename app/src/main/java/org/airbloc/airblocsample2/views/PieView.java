package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.utils.Utils;

import org.airbloc.airblocsample2.utils.Utils;

public class PieView extends View {

    public static class Pie {
        int color;
        String reason;
        public int days;

        public Pie(int color, String reason, int days) {
            this.color = color;
            this.reason = reason;
            this.days = days;
        }

        public int getColor() {
            return color;
        }

        public String getReason() {
            return reason;
        }

        public int getDays() {
            return days;
        }
    }

    private Pie[] data;
    private int total;
    private int border;

    private int w;
    private Paint piePaint;
    private Paint innerPaint;
    private Paint mPaint;
    private Context mContext;
    private Bitmap mBitmap;

    public PieView(Context context) {
        super(context);
        init(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        data = null;
        border = (int) Utils.dp2px(mContext, 6);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GraphView);
            border = a.getDimensionPixelSize(R.styleable.GraphView_border, border);
            a.recycle();
        }

        piePaint = new Paint();
        piePaint.setAntiAlias(true);

        innerPaint = new Paint();
        innerPaint.setColor(Color.WHITE);
        innerPaint.setAntiAlias(true);

        mPaint = new Paint();
    }

    private void createBitmap() {
        if (w == 0 || data == null) return;

        Bitmap bitmap = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float center = w / 2f;
        float radius = w / 2f;
        RectF rectF = new RectF(0, 0, w, w);
        float cumulated = 0;
        for (Pie pie : data) {
            float angle = 360f * pie.days / total;
            piePaint.setColor(pie.color);
            canvas.drawArc(rectF, -90 + cumulated, angle, true, piePaint);
            cumulated += angle;
        }
        canvas.drawCircle(center, center, radius - border, innerPaint);

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

    public void setData(Pie[] data) {
        this.data = data;
        total = 0;
        for (Pie pie : data) {
            total += pie.days;
        }
        createBitmap();
    }

    public Pie[] getData() {
        return data;
    }

    public void setBorder(int border) {
        this.border = border;
        createBitmap();
    }

    public int getBorder() {
        return border;
    }
}
