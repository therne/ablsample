package org.airbloc.airblocsample2.app.intro;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.addgoal.GoalAdder;

import org.airbloc.airblocsample2.app.addgoal.GoalAdder;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFinishFragment extends BaseFullScreenFragment {
    public OnFinishListener listener;
    private GoalAdder goalAdder;

    public static WelcomeFinishFragment newInstance() {
        return new WelcomeFinishFragment();
    }

    public WelcomeFinishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof OnFinishListener)) {
            throw new IllegalStateException("Your activity must inherit OnFinishListener");
        }
        listener = (OnFinishListener) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStatusBarColor(R.color.primaryDarkTranslucent);
        goalAdder = new GoalAdder(getActivity());

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_intro_finish, container, false);

        root.findViewById(R.id.startButton).setOnClickListener(v -> showAddGoalDialog());
        return root;
    }


    private void showAddGoalDialog() {
        Toast.makeText(getContext(), "Not Implemented Yet!!", Toast.LENGTH_SHORT).show();
        listener.onFinish();
        //        goalAdder.showAddGoalDialog(goal -> listener.onFinish());
    }

    public interface OnFinishListener {
        void onFinish();
    }
}
