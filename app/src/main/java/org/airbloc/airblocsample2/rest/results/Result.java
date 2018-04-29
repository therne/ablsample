package org.airbloc.airblocsample2.rest.results;

import org.airbloc.airblocsample2.App;

/**
 * Base result class.
 */
public class Result {
    public int code;
    public String msg;

    @Override
    public String toString() {
        return App.getGson().toJson(this);
    }
}
