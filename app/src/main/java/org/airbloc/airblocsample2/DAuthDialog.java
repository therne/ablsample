package org.airbloc.airblocsample2;

import android.content.Context;
import android.os.Bundle;

import org.airbloc.airblocsample2.app.SolinDialog;

public class DAuthDialog extends SolinDialog {

    public DAuthDialog(Context context, String appName) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_dauth);

        findViewById(R.id.submit).setOnClickListener(view -> dismiss());
    }
}
