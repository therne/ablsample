package org.airbloc.airblocsample2.app.intro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.airbloc.airblocsample2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BadgeInfoFragment extends BaseFullScreenFragment {

    public static BadgeInfoFragment newInstance() {
        return new BadgeInfoFragment();
    }

    private View badgeView, badgeContentView;

    public BadgeInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStatusBarColor(R.color.statusBgBadge);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_info_badge, container, false);
        badgeView = root.findViewById(R.id.badgeImage);
        badgeContentView = root.findViewById(R.id.badgeImageContent);
        return root;
    }

    public View getBadgeView() {
        return badgeView;
    }

    public View getBadgeContentView() {
        return badgeContentView;
    }
}
