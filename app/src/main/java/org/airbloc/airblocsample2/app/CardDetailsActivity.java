package org.airbloc.airblocsample2.app;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.models.Card;
import org.airbloc.airblocsample2.rest.results.GoalContentResult;
import org.airbloc.airblocsample2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 *
 */
public class CardDetailsActivity extends SolinActivity {

    @Bind(R.id.viewPager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        if (getIntent() == null || !getIntent().hasExtra("cards")) {
            finish();
            return;
        }

        setUpToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        setTitle(""); // no title

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        GoalContentResult result = App.getGson().fromJson(getIntent().getStringExtra("cards"), GoalContentResult.class);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), result.list));
        viewPager.setCurrentItem(getIntent().getIntExtra("index", 0));
        viewPager.setPageMargin((int) Utils.dp2px(this, 16));
    }

    class PageAdapter extends FragmentStatePagerAdapter {
        private List<Card> cards;
        private List<CardFragment> fragments;

        public PageAdapter(FragmentManager fm, List<Card> cards) {
            super(fm);
            this.cards = cards;
            fragments = new ArrayList<>(cards.size());
            for (Card card : cards) fragments.add(CardFragment.newInstance(card));
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return cards.size();
        }
    }

    public static class CardFragment extends Fragment {
        private Card card;
        private PhotoViewAttacher attacher;

        public CardFragment() {
            // Required empty constructor
        }

        public static CardFragment newInstance(Card card) {
            CardFragment fragment = new CardFragment();
            Bundle args = new Bundle();
            args.putString("card", App.getGson().toJson(card));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() == null || !getArguments().containsKey("card")) return;
            card = App.getGson().fromJson(getArguments().getString("card"), Card.class);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_card_details, container, false);
            PhotoView image = (PhotoView) root.findViewById(R.id.cardImage);
            TextView content = (TextView) root.findViewById(R.id.cardContent);

            content.setText(card.content);
            Glide.with(this).load(card.image).into(image);
            return root;
        }
    }
}
