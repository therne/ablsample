package org.airbloc.airblocsample2.models;

/**
 *
 */
public enum GoalUnit {
    TIME("time"),
    AMOUNT("amount");

    String content;
    GoalUnit(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
