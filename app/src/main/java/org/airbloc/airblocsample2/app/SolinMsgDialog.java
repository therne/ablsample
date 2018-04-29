package org.airbloc.airblocsample2.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.airbloc.airblocsample2.R;

import butterknife.Bind;

/**
 *
 */
public class SolinMsgDialog extends SolinDialog {

    public static class Builder {
        private SolinMsgDialog dialog;

        public Builder(Context context) {
            dialog = new SolinMsgDialog(context);
        }

        public Builder setTitle(String title) {
            dialog.title = title;
            return this;
        }

        public Builder setMessage(String msg) {
            dialog.message = msg;
            return this;
        }

        public Builder setPositiveButton(String label, View.OnClickListener listener) {
            dialog.primaryText = label;
            dialog.primaryHandler = listener;
            return this;
        }

        public Builder setNegativeButton(String label, View.OnClickListener listener) {
            dialog.secondaryText = label;
            dialog.secondaryHandler = listener;
            return this;
        }

        public void show() {
            dialog.show();
        }
    }


    public SolinMsgDialog(Context context) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
    }

    String title, message, primaryText, secondaryText;
    View.OnClickListener primaryHandler, secondaryHandler;

    @Bind(R.id.title) TextView titleText;
    @Bind(R.id.message) TextView messageText;
    @Bind(R.id.primary) TextView primary;
    @Bind(R.id.secondary) TextView secondary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_solin);

        if (title != null) titleText.setText(title);
        else titleText.setVisibility(View.GONE);

        if (message != null) messageText.setText(message);

        if (secondaryHandler != null) {
            secondary.setText(secondaryText);
            secondary.setOnClickListener(v -> {
                secondaryHandler.onClick(v);
                dismiss();
            });
        }
        else secondary.setVisibility(View.GONE);

        if (primaryHandler != null) {
            primary.setText(primaryText);
            primary.setOnClickListener(v -> {
                primaryHandler.onClick(v);
                dismiss();
            });
        }
    }
}
