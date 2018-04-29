package org.airbloc.airblocsample2.models;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.datamgmt.Storable;
import org.airbloc.airblocsample2.rest.results.Result;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.datamgmt.Storable;
import org.airbloc.airblocsample2.rest.results.Result;

import java.util.Date;
import java.util.List;

/**
 *
 */
public class Report extends Result implements Storable {
    public String id;
    public String name, english;
    public Date generatedAt;
    public List<Integer> result; // percentage
    public List<Date> days; // days
    public int total; // 급간
//    public String badgeUrl;
    public Comment totalComment;
    public double average;
    public int count; // 총 시간
    public List<Integer> fail;
    public int failcount;
    public Endurance endurance;

    @Override
    public String toString() {
        return App.getGson().toJson(this);
    }

    public static Report fromJson(String json) {
        return App.getGson().fromJson(json, Report.class);
    }

    @Override
    public String getId() {
        return id;
    }

    public class Comment {
        public String main, sub;
    }

    public class Endurance {
        public String main, sub;
        public int percentage;
    }
}
