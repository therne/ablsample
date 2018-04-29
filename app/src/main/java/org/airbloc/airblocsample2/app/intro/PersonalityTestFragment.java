package org.airbloc.airblocsample2.app.intro;

import android.os.Bundle;
import android.support.v4.os.ConfigurationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.rest.apis.HydeService;
import org.airbloc.airblocsample2.rest.results.HydeResult;
import org.airbloc.airblocsample2.views.PentaGraphView;
import org.airbloc.airblocsample2.rest.apis.UserService;
import org.airbloc.airblocsample2.views.PersonalityTestView;

import org.airbloc.airblocsample2.rest.apis.HydeService;
import org.airbloc.airblocsample2.rest.apis.UserService;
import org.airbloc.airblocsample2.rest.results.HydeResult;
import org.airbloc.airblocsample2.views.PersonalityTestView;
import org.apmem.tools.layouts.FlowLayout;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;


public class PersonalityTestFragment extends BaseFullScreenFragment {

    public static PersonalityTestFragment newInstance() {
        return new PersonalityTestFragment();
    }

//    private static final String keywords[] = {"Keyboard", "대립하는", "우울한", "억누르는", "충동적인",
//            "피곤해지는", "따뜻한", "사교적인", "밀어붙이는", "활동적인", "들뜨는", "긍정적인", "비현실적인",
//            "아름다운", "감각적인", "모험하는", "창의적인", "의미있는", "신뢰하는", "똑바른", "돕는", "따르는",
//            "겸손한", "부드러운", "능력있는", "규칙적인", "도덕적인", "노력하는", "성장하는", "조심하는"};

    private static final String KEYWORDS_KR[] = {"결혼", "주방용품", "장난감", "가전제품", "TV",
            "컴퓨터", "휴대폰", "옷", "Court", "Lorem", "Ipsum", "캠핑", "하이킹",
            "서핑", "책", "음악", "화장품", "자동차", "Vehicles"};

    private static final String KEYWORDS_EN[] = {"Wedding", "Kitchen", "Toy", "Electronics", "TV",
            "Computer", "Phone", "Clothes", "Court", "Lorem", "Ipsum", "Camping", "Hiking",
            "Surfing", "Books", "Musics", "Cosmetics", "Car", "Vehicles"};

    private LinkedList<String> selectedKeywords = new LinkedList<>();
    private PersonalityTestView personalityTestView;
    private View personalityResult;
    private PentaGraphView pentaGraphView;
    private FlowLayout tagsContainer;
    private TextView personTitle, personDetails;
    private HydeResult result;
    private String[] keywords;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // load data
        UserService.create().hydeAnalyzeResult(result -> {
            if (result.words != null && !result.words.isEmpty()) {
                for (String str : result.words) {
                    int index = indexOfKeyword(str);
                    if (index != -1) addTag(index, false);
                }
            }
        });

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_personalitytest, container, false);

        tagsContainer = rootView.findViewById(R.id.tagsLayout);

        String language = ConfigurationCompat.getLocales(getResources().getConfiguration()).get(0).getLanguage();
        keywords = language.equals("ko") ? KEYWORDS_KR : KEYWORDS_EN;

        personalityTestView = rootView.findViewById(R.id.sphereView);
        personalityTestView.setData(Arrays.asList(keywords), this::addTag);

        personalityResult = rootView.findViewById(R.id.personalityResult);
        pentaGraphView = rootView.findViewById(R.id.pentaGraphView);

        personDetails = rootView.findViewById(R.id.personalityDetails);
        personTitle = rootView.findViewById(R.id.personalityType);

        personalityResult.setVisibility(View.GONE);
        rootView.findViewById(R.id.next).setOnClickListener(view -> ((WelcomeActivity) getActivity()).goNext());

        return rootView;
    }

    private void addTag(int index, boolean touched) {
        if (selectedKeywords.size() == 6
                || selectedKeywords.indexOf(keywords[index]) != -1) return;

        View tag = getActivity().getLayoutInflater().inflate(R.layout.tag, null);
        ((TextView) tag.findViewById(R.id.tagName)).setText(keywords[index]);
        tag.setOnClickListener(v -> {
            tagsContainer.removeView(tag);
            selectedKeywords.remove(keywords[index]);
        });
        tagsContainer.addView(tag);
        selectedKeywords.push(keywords[index]);

        if (touched && selectedKeywords.size() == 6) {
            String result = "";
            for (String keyword : selectedKeywords) result += keyword;
//            HydeService.create().analyzePersonality(result, analyzeResult -> {
//                float data[] = new float[5];
//                for (int i = 0; i < 5; i++) data[i] = (analyzeResult.result.charAt(i) - '0') * 0.2f;
//                pentaGraphView.setData(data);
//
//                personalityResult.setVisibility(View.VISIBLE);
//                personDetails.setText("");
//                personTitle.setText(analyzeResult.type);
//
//                // animation
//                AlphaAnimation anim = new AlphaAnimation(0f, 1f);
//                anim.setFillAfter(true);
//                anim.setInterpolator(new AccelerateInterpolator());
//                anim.setDuration(600);
//                personalityResult.startAnimation(anim);

                // TODO: Airbloc: 성향분석 결과 서버로 전송
//                UserService.create().hydeAnalyze(new HydeResult(selectedKeywords), res -> {
                    ((WelcomeActivity) getActivity()).hydeSelected();
//                });
//            });
        }
    }

    private void addTag(int index) {
        addTag(index, true);
    }

    private int indexOfKeyword(String str) {
        for (int i=0; i<keywords.length; i++) if (keywords[i].equals(str)) return i;
        return -1;
    }
}
