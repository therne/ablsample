package org.airbloc.airblocsample2.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.airbloc.airblocsample2.Logger;
import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.addgoal.GoalAdder;
import org.airbloc.airblocsample2.app.intro.LoginActivity;
import org.airbloc.airblocsample2.app.intro.WelcomeActivity;
import org.airbloc.airblocsample2.app.settings.FeedbackActivity;
import org.airbloc.airblocsample2.app.settings.SettingsActivity;
import org.airbloc.airblocsample2.models.Goal;
import org.airbloc.airblocsample2.rest.Session;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MainActivity extends SolinActivity {

    // views
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView list;

    GoalAdder goalAdder;
    CardViewAdapter listAdapter;
    StaggeredGridLayoutManager staggeredGridManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        // 로그인되지 않았다면 로그인 화면으로
        if (!Session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_new_main);

        // Google Analytics 흐름 추적 시작
        goalAdder = new GoalAdder(this);

//        actionBar.setTitle(null);

        swipeRefreshLayout.setColorSchemeResources(R.color.accentMintDark);
        swipeRefreshLayout.setOnRefreshListener(this::refreshGoals);

        staggeredGridManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        list.setLayoutManager(staggeredGridManager);

        listAdapter = new CardViewAdapter(Arrays.asList(new MainCard()));
        list.setAdapter(listAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!Session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("onDestroy()");
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(Goal goal) {
        // TODO: eventBus
    }

    private void refreshGoals() {

    }

    static class MainCard {
        enum Type {
            STATUS(0),
            ADVISE(1),
            TALKING_BUTTON(2),
            REPORT(3),
            MOTIVE_CONTENT(4),
            NEW_BADGE(5),
            JIPDANGI(6); // 집단기

            private int number;

            Type(int number) {
                this.number = number;
            }
        };

        Type type;
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        // TODO: Card items

        public CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CardViewAdapter extends RecyclerView.Adapter<CardViewHolder> {
        private final List<MainCard> cards;

        public CardViewAdapter(List<MainCard> cards) {
            this.cards = cards;
        }

        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            switch (cards.get(position).type) {
                case STATUS:
                case ADVISE:
                case TALKING_BUTTON:
                case REPORT:
                case MOTIVE_CONTENT:
                case NEW_BADGE:
                case JIPDANGI:
            }
            return new CardViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(CardViewHolder holder, int position) {
            MainCard card = cards.get(position);

            // TODO: Do something with holder.
            switch (card.type) {
                case STATUS:
                case ADVISE:
                case TALKING_BUTTON:
                case REPORT:
                case MOTIVE_CONTENT:
                case NEW_BADGE:
                case JIPDANGI:
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return cards.size();
        }
    }
}
