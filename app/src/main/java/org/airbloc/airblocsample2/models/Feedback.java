package org.airbloc.airblocsample2.models;

import org.airbloc.airblocsample2.datamgmt.Storable;

import org.airbloc.airblocsample2.datamgmt.Storable;

import java.util.Date;

/**
 * 사용자 피드백 (질문 / 답변)
 */
public class Feedback implements Storable {
    public String id, type, userId, question, answer;
    public Date date;

    public Feedback(String id, String type, String question) {
        this.id = id;
        this.type = type;
        this.question = question;
        this.date = new Date();
    }

    @Override
    public String getId() {
        return id;
    }
}
