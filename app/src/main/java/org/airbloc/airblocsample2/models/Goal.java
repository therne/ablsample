package org.airbloc.airblocsample2.models;

import android.os.Parcelable;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.datamgmt.Storable;
import org.airbloc.airblocsample2.rest.results.Result;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.datamgmt.Storable;
import org.airbloc.airblocsample2.rest.results.Result;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Parcel
public class Goal extends Result implements Storable {

    public String id;
    public transient long createdAt;

    public String name;
    public Date startDate, endDate;
    public int percent;
    public String category;
    public boolean threeLegged;
    public GoalTarget target;
    public List<GoalResult> result;

    public boolean isNaggingEnabled, isAchievementsEnabled;

    public static Goal fromJson(String json) {
        return App.getGson().fromJson(json, Goal.class);
    }

    public static Goal fromParcel(Parcelable parcel) {
        return Parcels.unwrap(parcel);
    }

    public Goal() {
        result = new ArrayList<>();
        createdAt = new Date().getTime();
    }

    public Goal(String name, String category, Date endDate) {
        this();
        this.name = name;
        this.category = category;
        this.startDate = new Date();
        this.endDate = endDate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getCreatedAt() {
        return startDate != null ? startDate.getTime() : createdAt;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addResult(GoalResult goal) {
        result.add(goal);
    }

    public List<GoalResult> getResult() {
        return result;
    }

    public void setResult(List<GoalResult> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return App.getGson().toJson(this);
    }

    public Parcelable toParcel() {
        return Parcels.wrap(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Goal) return equals((Goal) o);
        return super.equals(o);
    }

    public boolean equals(Goal g) {
        return id != null && id.equals(g.id);
    }
}
