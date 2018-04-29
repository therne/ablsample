package org.airbloc.airblocsample2.models;

import org.airbloc.airblocsample2.datamgmt.Storable;
import org.airbloc.airblocsample2.rest.results.Result;

import org.airbloc.airblocsample2.datamgmt.Storable;
import org.airbloc.airblocsample2.rest.results.Result;

import java.util.Date;

/**
 *
 */
public class Diary extends Result implements Storable {
    public String id, text;
    public Date date;

    public Diary(String text) {
        date = new Date();
        this.text = text;
    }

    @Override
    public String getId() {
        return id;
    }
}
