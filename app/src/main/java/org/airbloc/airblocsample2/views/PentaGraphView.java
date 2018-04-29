package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.utils.Utils;

import org.airbloc.airblocsample2.utils.Utils;

public class PentaGraphView extends View {
    private float[] data;

    int w, h;

    private float radius, interval;
    private Bitmap person;
    private Paint outerLinePaint;
    private Paint innerLinePaint;
    private Paint shapePaint;
    private Paint borderPaint;
    private Paint outerDotPaint;
    private Paint innerDotPaint;
    private Paint mPaint;
    private Context mContext;
    private Bitmap mBitmap;
    private RelativeLayout mLayout;
    private SolinTextView[] mTextViews;

    public PentaGraphView(Context context) {
        super(context);
        init(context, null);
    }

    public PentaGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PentaGraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        data = null;

        radius = Utils.dp2px(context, 132.66888955608747f);
        interval = Utils.dp2px(context, 17.5f);
        person = BitmapFactory.decodeResource(getResources(), R.drawable.graph_center_man);

        outerLinePaint = new Paint();
        outerLinePaint.setColor(Color.WHITE);
        outerLinePaint.setStyle(Paint.Style.STROKE);
        outerLinePaint.setStrokeWidth(Utils.dp2px(context, 2));
        outerLinePaint.setAntiAlias(true);
        innerLinePaint = new Paint();
        innerLinePaint.setColor(Color.argb(166, 255, 255, 255));
        innerLinePaint.setStyle(Paint.Style.STROKE);
        innerLinePaint.setStrokeWidth(Utils.dp2px(context, 1.5f));
        innerLinePaint.setAntiAlias(true);
        shapePaint = new Paint();
        shapePaint.setStyle(Paint.Style.FILL);
        shapePaint.setColor(Color.parseColor("#80FE5250"));
        shapePaint.setStrokeWidth(Utils.dp2px(context, 1.5f));
        shapePaint.setAntiAlias(true);
        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.parseColor("#A6FE5250"));
        borderPaint.setStrokeWidth(Utils.dp2px(context, 1.5f));
        borderPaint.setAntiAlias(true);
        outerDotPaint = new Paint();
        outerDotPaint.setStrokeCap(Paint.Cap.ROUND);
        outerDotPaint.setColor(Color.WHITE);
        outerDotPaint.setStrokeWidth(Utils.dp2px(context, 8));
        outerDotPaint.setAntiAlias(true);
        innerDotPaint = new Paint();
        innerDotPaint.setStrokeCap(Paint.Cap.ROUND);
        innerDotPaint.setColor(Color.parseColor("#FE5250"));
        innerDotPaint.setStrokeWidth(Utils.dp2px(context, 4));
        innerDotPaint.setAntiAlias(true);

        mPaint = new Paint();

        mLayout = new RelativeLayout(mContext);

        mTextViews = new SolinTextView[5];
        String texts[] = {"외향적인", "계획적인", "활발한", "사교적인", "스트레스"};
        for (int i = 0; i < 5; i++) {
            mTextViews[i] = new SolinTextView(mContext);
            mTextViews[i].setLayoutParams(new ViewGroup.LayoutParams((int) Utils.dp2px(context, 50), ViewGroup.LayoutParams.WRAP_CONTENT));
            mTextViews[i].setText(texts[i]);
            mTextViews[i].setTextColor(Color.argb(191, 255, 255, 255));
            mTextViews[i].setTextSize(13.5f);
            mTextViews[i].setGravity(Gravity.CENTER);
            mLayout.addView(mTextViews[i]);
        }
    }

    private void createBitmap() {
        if (w == 0 || h == 0 || data == null) return;

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float cx = w / 2f;
        float cy = h / 2f;
        Path[] paths = new Path[5];
        Path plotPath = new Path();
        PointF[] plots = new PointF[5];
        PointF[] points = new PointF[5];
        for (int i = -1; i < 5; i++) {
            double angle = Math.PI * 2 / 5 * i - Math.PI / 2;
            float x = (float) (radius * Math.cos(angle));
            float y = (float) (radius * Math.sin(angle));
            for (int j = 0; j < 5; j++) {
                float ratio = (radius - interval * j) / radius;
                float rx = x * ratio;
                float ry = y * ratio;
                if (i == -1) {
                    paths[j] = new Path();
                    paths[j].moveTo(cx + rx, cy + ry);
                } else {
                    paths[j].lineTo(cx + rx, cy + ry);
                    if (j == 0) {
                        points[i] = new PointF(rx, ry);
                    }
                }
            }
            float ratio = data[(i + 5) % 5] * (interval * 4 / radius) + 1 - (interval * 4 / radius);
            float rx = x * ratio;
            float ry = y * ratio;
            if (i == -1) {
                plotPath.moveTo(cx + rx, cy + ry);
            } else {
                plotPath.lineTo(cx + rx, cy + ry);
                plots[i] = new PointF(rx, ry);
            }
        }

        for (int j = 0; j < 5; j++) {
            paths[j].close();
            canvas.drawPath(paths[j], j == 0 ? outerLinePaint : innerLinePaint);
        }
        plotPath.close();
        canvas.drawPath(plotPath, shapePaint);
        canvas.drawPath(plotPath, borderPaint);

        for (int i = 0; i < 5; i++) {
            canvas.drawPoint(cx + plots[i].x, cy + plots[i].y, outerDotPaint);
            canvas.drawPoint(cx + plots[i].x, cy + plots[i].y, innerDotPaint);
        }

        int dp30 = (int) Utils.dp2px(getContext(), 30);
        int dp25 = (int) Utils.dp2px(getContext(), 25);
        int dp10 = (int) Utils.dp2px(getContext(), 10);
        for (int i = 0; i < 5; i++) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTextViews[i].getLayoutParams();
            layoutParams.leftMargin = (int) (cx + points[i].x - dp25 + (i == 4 ? -dp10 : i == 1 ? dp10 : 0));
            layoutParams.topMargin = (int) (cy + points[i].y + (i % 4 < 2 ? -dp30 : dp10));
        }

        canvas.drawBitmap(person, cx - person.getWidth() / 2, cy - person.getHeight() / 2, mPaint);

        mLayout.setLayoutParams(new RelativeLayout.LayoutParams(canvas.getWidth(), canvas.getHeight()));
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
        this.data = data;
        createBitmap();
    }

    public float[] getData() {
        return data;
    }
}
