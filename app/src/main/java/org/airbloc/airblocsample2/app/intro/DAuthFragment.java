package org.airbloc.airblocsample2.app.intro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.airbloc.airblocsample2.DAuthDialog;
import org.airbloc.airblocsample2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DAuthFragment extends BaseFullScreenFragment {

    public static DAuthFragment newInstance() {
        return new DAuthFragment();
    }

    private View reportView;

    public DAuthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStatusBarColor(R.color.statusBgReport);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_reportinfo, container, false);
        reportView = root.findViewById(R.id.reportImage);

        root.findViewById(R.id.allow).setOnClickListener(view -> {
            DAuthDialog dialog = new DAuthDialog(getContext(), "Airshop");
            dialog.show();
        });
        return root;
    }

    View getReportView() {
        return reportView;
    }
}
