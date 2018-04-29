package org.airbloc.airblocsample2.models;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class GoalResult {
    public Date date;
    public int percent;

    public GoalResult() {

    }

    public GoalResult(int percent) {
        this.date = new Date(); // today...
        this.percent = percent;
    }

    public GoalResult(Date date, int percent) {
        this.date = date;
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }
}