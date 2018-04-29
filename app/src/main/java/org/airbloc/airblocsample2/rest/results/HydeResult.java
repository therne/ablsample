package org.airbloc.airblocsample2.rest.results;

import java.util.List;

public class HydeResult extends Result {
    public List<String> words;

    public HydeResult(List<String> words) {
        this.words = words;
    }
}
