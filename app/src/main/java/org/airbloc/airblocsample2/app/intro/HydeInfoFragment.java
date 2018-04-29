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
public class HydeInfoFragment extends BaseFullScreenFragment {

    public static HydeInfoFragment newInstance() {
        return new HydeInfoFragment();
    }

    public HydeInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStatusBarColor(R.color.statusBgHydeInfo);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hydeinfo, container, false);
    }
}
