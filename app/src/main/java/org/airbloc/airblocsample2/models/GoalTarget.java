package org.airbloc.airblocsample2.models;

import org.parceler.Parcel;

@Parcel
public class GoalTarget {
    public float score;
    public GoalUnit type;

    public GoalTarget() {}

    public GoalTarget(float score, GoalUnit type) {
        this.score = score;
        this.type = type;
    }

    public GoalUnit getType() {
        return type;
    }

    public void setType(GoalUnit type) {
        this.type = type;
    }
}
