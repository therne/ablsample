package org.airbloc.airblocsample2.app.settings;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.airbloc.airblocsample2.R;

/**
 * 오류신고/건의사항 다이얼로그
 */
public class FeedbackDialog extends Dialog {
    String type = "error";

    public interface OnResultCallback {
        void result(String type, String feedback);
    }

    private OnResultCallback callback;

    public FeedbackDialog(Context context, OnResultCallback callback) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_feedback);

        toggle();

        findViewById(R.id.titleLayout).setOnClickListener(view -> {
            type = type.equals("error") ? "proposal" : "error";
            toggle();
        });

        findViewById(R.id.submit).setOnClickListener(v -> {
            EditText feedback = (EditText) findViewById(R.id.feedback);
            callback.result(type, feedback.getText().toString());
            dismiss();
        });

        findViewById(R.id.cancel).setOnClickListener(v -> {
            dismiss();
        });
    }

    private void toggle() {
        ((TextView) findViewById(R.id.title)).setText(type.equals("error") ? "오류신고" : "건의사항");
        ((EditText) findViewById(R.id.feedback)).setHint(type.equals("error") ? "겪으신 문제상황에 대해서 적어주세요." : "SOLIN에게 부탁하고 싶은것들을 적어주세요.");
    }
}
