package org.airbloc.airblocsample2.app.addgoal;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import org.airbloc.airblocsample2.R;

/**
 *
 */
public class GoalIntroDialog extends Dialog {
    public GoalIntroDialog(Context context) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_remember);
        findViewById(R.id.ok).setOnClickListener(v -> dismiss());
    }
}
