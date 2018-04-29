package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;

import org.airbloc.airblocsample2.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PersonalityTestView extends TextureView
        implements TextureView.SurfaceTextureListener, View.OnTouchListener {

    private static final int COLOR_BACKGROUND = Color.parseColor("#FF5250"),
            COLOR_CIRCLE_BG = Color.parseColor("#40FFFFFF"),
            COLOR_TEXT = Color.parseColor("#BBFFFFFF"),
            COLOR_CIRCLE_BG_PRESSED = Color.parseColor("#20FFFFFF");

    private RenderThread renderThread;

    private List<PointF> points;
    private List<String> keywords;
    private OnClickListener onClickListener;
    private List<Tag> tags;
    private TrigonalPoint POINTS[];

    private float RADIUS;
    private int width, height;

    private float cx, cy; // 비트맵 기준 비트맵의 중앙 좌표
    private float mx, my; // 움직인 양

    private Rect showingArea; // 비트맵 기준 실제 보여지는 영역
    private Rect surfaceArea; // 실제 캔버스 크기

    private Paint circlePaint, textPaint;
    private Context mContext;
    private Bitmap bitmap;
    private Paint bitmapPaint;
    private int pressedIndex = -1;
    private float BITMAP_SIZE, MARGIN_TOP, TEXT_HEIGHT;

    public PersonalityTestView(Context context) {
        super(context);
        init(context, null);
    }

    public PersonalityTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PersonalityTestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        // calculate point
        float dp260 = Utils.dp2px(mContext, 260);
        float dp_200= Utils.dp2px(mContext, 200);
        float dp_172 = Utils.dp2px(mContext, 172);
        float dp_120  = Utils.dp2px(mContext, 120);
        float dp_52 = Utils.dp2px(mContext, 52);

        POINTS = new TrigonalPoint[]{
                // 1겹
                new TrigonalPoint(22.5, dp_52),
                new TrigonalPoint(22.5 + 90, dp_52),
                new TrigonalPoint(22.5 + 90*2, dp_52),
                new TrigonalPoint(22.5 + 270, dp_52),

                // 2겹
                new TrigonalPoint(0, dp_120),
                new TrigonalPoint(45, dp_120),
                new TrigonalPoint(45 * 2, dp_120),
                new TrigonalPoint(45 * 3, dp_120),
                new TrigonalPoint(45 * 4, dp_120),
                new TrigonalPoint(45 * 5, dp_120),
                new TrigonalPoint(45 * 6, dp_120),
                new TrigonalPoint(45 * 7, dp_120),

                // 3겹
                new TrigonalPoint(22.5, dp_172),
                new TrigonalPoint(22.5+ 45, dp_172),
                new TrigonalPoint(22.5+ 45 * 2, dp_172),
                new TrigonalPoint(22.5+ 45 * 3, dp_172),
                new TrigonalPoint(22.5+ 45 * 4, dp_172),
                new TrigonalPoint(22.5+ 45 * 5, dp_172),
                new TrigonalPoint(22.5+ 45 * 6, dp_172),
                new TrigonalPoint(22.5+ 45 * 7, dp_172),

                // 4겹
                new TrigonalPoint(0, dp_200),
                new TrigonalPoint(45, dp_200),
                new TrigonalPoint(45 * 2, dp_200),
                new TrigonalPoint(45 * 3, dp_200),
                new TrigonalPoint(45 * 4, dp_200),
                new TrigonalPoint(45 * 5, dp_200),
                new TrigonalPoint(45 * 6, dp_200),
                new TrigonalPoint(45 * 7, dp_200),

                // 5겹
                new TrigonalPoint(67.5, dp260),
                new TrigonalPoint(180 + 22.5, dp260),
                new TrigonalPoint(360 - 22.5, dp260)
        };

        // circle
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);

        // text
        TEXT_HEIGHT = Utils.dp2px(mContext, 13.5f);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(FontCache.get("NotoSans-DemiLight.otf"));
        textPaint.setColor(COLOR_TEXT);
        textPaint.setTextSize(TEXT_HEIGHT);
//        textPaint.setTextAlign(Paint.Align.CENTER);

        RADIUS = Utils.dp2px(mContext, 36); // 36dp circle
        MARGIN_TOP = Utils.dp2px(mContext, 24); // 24dp margin
        BITMAP_SIZE = Utils.dp2px(mContext, 552) + MARGIN_TOP * 2; // bitmap size 276dp + margin

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        showingArea = new Rect();

        my = Utils.dp2px(context, 18);

        setOnTouchListener(this);
        setSurfaceTextureListener(this);
    }

    private void resize(int w, int h) {
        this.width = w;
        this.height = h;
        surfaceArea = new Rect();
        surfaceArea.set(0, 0, w, h);
    }

    private void moveView() {
        showingArea.set((int)(cx - (width/2) - mx),
                (int)(cy - (height/2) - my),
                (int)(cx + (width/2) - mx),
                (int)(cy + (height/2) - my));
    }

    class RenderThread extends Thread {
        private volatile boolean isRunning = true;

        @Override
        public void run() {
            if (bitmap == null) {
                initBitmap();
                moveView();
            }

            try {
                while (isRunning && !Thread.interrupted()) {
                    Canvas canvas = lockCanvas();
                    canvas.drawBitmap(bitmap, showingArea, surfaceArea, bitmapPaint);
                    unlockCanvasAndPost(canvas);

                    // to ensure 60 FPS
                    Thread.sleep(16);
                }
            } catch (Exception e) {
                // rendering stopped. good.
            }
        }

        public void stopRendering() {
            interrupt();
            isRunning = false;
            if (!bitmap.isRecycled()) bitmap.recycle();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        resize(width, height);
        renderThread = new RenderThread();
        renderThread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        resize(width, height);
        moveView();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (renderThread != null) renderThread.stopRendering();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    /**
     * 초기에 비트맵을 생성한다.
     */
    private void initBitmap() {
        if (width == 0 || height == 0 || keywords == null) return;
        bitmap = Bitmap.createBitmap((int) BITMAP_SIZE, (int) BITMAP_SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(COLOR_BACKGROUND);

        cx = cy = BITMAP_SIZE / 2;

        // 자간 줄이기 위해서ㅎㅎ
        float tracking = -1.5f * TEXT_HEIGHT / Utils.dp2px(mContext, 15);
        SolinTextView.LetterSpacingSpan span = new SolinTextView.LetterSpacingSpan(tracking);

        // create bitmap
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            TrigonalPoint point = POINTS[i];

            circlePaint.setColor(pressedIndex == i ? COLOR_CIRCLE_BG_PRESSED : COLOR_CIRCLE_BG);
            canvas.drawCircle(point.getXBy(cx), point.getYBy(cy), RADIUS, circlePaint);

            int textWidth = (int)(textPaint.measureText(tag.keyword, 0, tag.keyword.length())
                    - tracking * tag.keyword.length()
                    - Utils.dp2px(mContext, 2));

            span.draw(canvas, tag.keyword, 0, tag.keyword.length(),
                    point.getXBy(cx) - (textWidth/2), 0,
                    (int) (point.getYBy(cy) + (TEXT_HEIGHT / 2)), 0,
                    textPaint);
        }
    }

    public void setData(List<String> keywords, OnClickListener onClickListener) {
        this.keywords = keywords;
        this.onClickListener = onClickListener;

        // randomize list
        Collections.shuffle(keywords, new Random(System.nanoTime()));

        tags = new ArrayList<>();
        int len = keywords.size();
        for (int i=0; i<len; i++) tags.add(new Tag(POINTS[i], keywords.get(i)));
    }

    private class Tag {
        private String keyword;
        private TrigonalPoint point;

        Tag(TrigonalPoint point, String keyword) {
            this.point = point;
            this.keyword = keyword;
        }
    }

    public interface OnClickListener {
        void onClick(int index);
    }

    /**
     * 눌림 판정
     * @param x 뷰 기준 X
     * @param y 뷰 기준 Y
     * @return index (-1 if not pressed)
     */
    public int getPressed(float x, float y) {
        if (tags == null) return -1;

        // 비트맵 기준 좌표계로 변환
        x += showingArea.left;
        y += showingArea.top;

        for (int i = 0; i < tags.size(); i++) {
            TrigonalPoint p = tags.get(i).point;
            if (Math.sqrt(Math.pow(p.getXBy(cx) - x, 2) + Math.pow(p.getYBy(cy) - y, 2)) < RADIUS) {
                return i;
            }
        }
        return -1;
    }

    private float totalMove;
    private float prevX, prevY;
    private float pressLimit = Utils.dp2px(getContext(), 4);

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressedIndex = getPressed(x, y);
                totalMove = 0;
                prevX = x;
                prevY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                totalMove += Math.sqrt((x - prevX) * (x - prevX) + (y - prevY) * (y - prevY));

                mx += x - prevX;
                my += y - prevY;

                float maxX = BITMAP_SIZE / 2 - width / 2;
                float maxY = BITMAP_SIZE / 2 - height / 2;
                if (mx < -maxX) mx = -maxX;
                if (mx > maxX) mx = maxX;
                if (my < -maxY) my = -maxY;
                if (my > maxY) my = maxY;

                prevX = x;
                prevY = y;
                moveView();
                break;
            case MotionEvent.ACTION_UP:
                pressedIndex = -1;
                if (totalMove < pressLimit) {
                    int index = getPressed(x, y);
                    if (index != -1) {
                        if (onClickListener != null) onClickListener.onClick(index);
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 삼각비를 이용한 좌표계
     */
    static class TrigonalPoint {
        private float hypotenuse;
        private double sin, cos;

        private static final double SIN_22_5 = 0.3826834323650898;
        private static final double SIN_45 = 0.7071067811865475;
        private static final double SIN_67_5 = 0.9238795325112867;


        public TrigonalPoint(double degress, float hypotenuse) {
            sin = Math.sin(Math.toRadians(degress));
            cos = Math.cos(Math.toRadians(degress));
            this.hypotenuse = hypotenuse;
        }

        public float getXBy(float baseX) {
            return (float)(baseX + hypotenuse * sin);
        }

        public float getYBy(float baseY) {
            return (float)(baseY + hypotenuse * cos);
        }
    }
}
