package org.airbloc.airblocsample2.rest.results;

import org.airbloc.airblocsample2.models.GoalAdvise;
import org.airbloc.airblocsample2.models.GoalResult;

import java.util.List;

/**
 * 모아보기 화면의 API 호출 결과
 */
public class GoalOverviewData extends Result {
    /**
     * 현재 목표수행 데이터 (그래프)
     */
    public List<GoalResult> currentList;

    /**
     * 조언
     */
    public List<GoalAdvise> advice;

    /**
     * 현재 상태 진단
     */
    public Comment comment;

    public class Comment {
        public String type, title, subtitle;
    }
}
