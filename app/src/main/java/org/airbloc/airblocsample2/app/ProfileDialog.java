package org.airbloc.airblocsample2.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.models.User;
import org.airbloc.airblocsample2.rest.Session;

import org.airbloc.airblocsample2.rest.Session;

/**
 * 프로필 다이얼로그
 */
public class ProfileDialog extends SolinDialog {
    private User user;
    private boolean isMe = false;

    public ProfileDialog(Context context, User user) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
        this.user = user;
    }

    public ProfileDialog(Context context) {
        this(context, Session.getMe());
        isMe = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_profile);

        // Profile contents
        TextView name = (TextView) findViewById(R.id.profileName),
                status = (TextView) findViewById(R.id.profileStatus);

        Glide.with(getContext())
                .load(user.profileImage)
                .into((ImageView) findViewById(R.id.profileImage));

        name.setText(user.name);
//        status.setText(user.statusMessage == null ? "상태 메세지를 입력하세요" : user.statusMessage);

        if (isMe) findViewById(R.id.friendAddButton).setVisibility(View.GONE);

    }
}
