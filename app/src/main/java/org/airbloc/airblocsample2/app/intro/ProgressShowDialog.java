package org.airbloc.airblocsample2.app.intro;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import org.airbloc.airblocsample2.R;

/**
 * 프로그래스 바를 보여준다.
 */
public class ProgressShowDialog extends Dialog {
    public ProgressShowDialog(Context context) {
        super(context, R.style.Base_Theme_AppCompat_Dialog);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setDimAmount(0.6f);
    }
}
