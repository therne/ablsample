package org.airbloc.airblocsample2.views;

import android.content.Context;
import android.util.AttributeSet;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.rest.Session;

/**
 * 사용자라는 텍스트를 모두 실명으로 바꿔버린다.
 */
public class SayongjaTextView extends SolinTextView {


    public SayongjaTextView(Context context) {
        super(context);
    }

    public SayongjaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SayongjaTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (Session.isLoggedIn() && text.toString().contains("사용자")) {
            text = text.toString().replace("사용자", Session.getMe().name);
        }
        super.setText(text, type);
    }
}
