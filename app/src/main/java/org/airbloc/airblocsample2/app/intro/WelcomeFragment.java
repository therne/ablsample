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
public class WelcomeFragment extends BaseFullScreenFragment {

    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;
    }

    private View letterView;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    public View getLetterView() {
        return letterView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStatusBarColor(R.color.statusBgWelcome);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_welcome, container, false);

        letterView = root.findViewById(R.id.letterContent);

        return root;
    }


}
