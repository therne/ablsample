package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ReplacementSpan;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import org.airbloc.airblocsample2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 자간조절 가능하고 폰트 변경가능한 텍스트뷰.
 */
public class SolinTextView extends android.support.v7.widget.AppCompatTextView {

    private static float BASELINE = 0;

    /**
     * Predefine the frequently using letter spacings in dp.
     */
    static class LetterSpacing {
        public final static float NORMAL = -1.4f;
        public final static float HEADER = 6.0f;
        public final static float TITLE = 7.25f;
        public final static float REGULAR = -1.4f;
        public final static float BOLD = -1.4f;
        public final static float ABOUT = -1.0f;
        public final static float NONE = 0;
        public final static float RALEWAY = -1f;
    }

    private static final float LETTER_SPACINGS[] = {
            LetterSpacing.NORMAL,
            LetterSpacing.HEADER,
            LetterSpacing.TITLE,
            LetterSpacing.REGULAR,
            LetterSpacing.BOLD,
            LetterSpacing.ABOUT,
            LetterSpacing.NONE,
            LetterSpacing.RALEWAY
    };

    private static final String FONT_PATHS[] = {
            "NotoSans-DemiLight.otf",
            "Raleway-Regular.ttf",
            "Raleway-Regular.ttf",
            "NotoSans-Regular.otf",
            "NotoSans-Medium.otf",
            "Raleway-Regular.ttf",
            "NotoSans-Regular.otf",
            "Raleway-Regular.ttf",
    };

    // preloaded typefaces
    private static List<Typeface> fonts = new ArrayList<>();

    private float letterSpacing = 0;
    private boolean applied = false, initiated = false;

    public SolinTextView(Context context) {
        super(context);
        init(context, null);
    }

    public SolinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SolinTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) return;

        int type = 0; // Default: LetterSpacing.NORMAL
        float lineSpacingMult = 0.9f;

        // Load font
        if (fonts.isEmpty()) {
            for (String path : FONT_PATHS) fonts.add(FontCache.get(path));
        }
        if (BASELINE == 0) BASELINE = dipToPixels(15);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SolinTextView);
            type = a.getInt(R.styleable.SolinTextView_type, 0);
            lineSpacingMult = a.getFloat(R.styleable.SolinTextView_lineSpacingMult, 0.9f);
            a.recycle();
        }

        letterSpacing = dipToPixels(LETTER_SPACINGS[type]);
        setTypeface(fonts.get(type));

        initiated = true;
        applyLetterSpacing();

        setLineSpacing(0, lineSpacingMult);
    }

    public void setType(int typeIndex) {
        letterSpacing = dipToPixels(LETTER_SPACINGS[typeIndex]);
        setTypeface(fonts.get(typeIndex));
        applyLetterSpacing();
    }

    /**
     * Get current letter spacing value
     *
     * @return Letter spacing in px (float)
     */
    public float getLetterSpacing() {
        return letterSpacing;
    }

    /**
     * Set text's letter spacing.
     * @param letterSpacing a space bettwen letter (in px, negative allowed)
     */
    public void setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
        applyLetterSpacing();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (!initiated) return;

        if (applied) {
            // to prevent infinite recursion
            applied = false;
            return;
        }
        else applied = true;
        applyLetterSpacing();
    }

    /**
     * DIP to px
     * @param dipValue Dip
     * @return px
     */
    public float dipToPixels(float dipValue) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    private void applyLetterSpacing() {
        if (isInEditMode() || getText().length() == 0 || letterSpacing == 0) return;

        String original = getText().toString() + "\u200b";
        int base = 0, index=0, len = original.length() - 1;
        float tracking = letterSpacing * getTextSize() / BASELINE;

        SpannableStringBuilder builder = new SpannableStringBuilder(original);
        while (true) {

            // find space.
            while (index < len && original.charAt(index) != '\n' && original.charAt(index) != ' ') index++;
            if (index >= len) break;

            if (base == index) {
                base = ++index;
                continue;
            }
            builder.setSpan(new LetterSpacingSpan(tracking), base, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ScaleXSpan(0.25f), index, index+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            base = ++index;
        }

        if (base != len) builder.setSpan(new LetterSpacingSpan(tracking), base, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        super.setText(builder);
    }

    public static class LetterSpacingSpan extends ReplacementSpan {
        private float mTrackingPx;

        public LetterSpacingSpan(float tracking) {
            mTrackingPx = tracking;
        }

        @Override
        public int getSize(Paint paint, CharSequence text,
                           int start, int end, Paint.FontMetricsInt fm) {

            return (int) (paint.measureText(text, start, end)
                    + mTrackingPx * (end - start  - (end == text.length() - 1 ? 1.5 : 1)));
        }

        @Override
        public void draw(Canvas canvas, CharSequence text,
                         int start, int end, float x, int top, int y,
                         int bottom, Paint paint) {
            float dx = x;
            for (int i = start; i < end; i++) {
                canvas.drawText(text, i, i + 1, dx, y, paint);
                dx += paint.measureText(text, i, i + 1) + mTrackingPx;
            }
        }
    }
}
