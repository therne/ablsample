package org.airbloc.airblocsample2.datamgmt;

/**
 * DataStore에 저장하려면 이 인터페이스를 구현해야 한다.
 */
public interface Storable {
    /**
     * @return Entity's unique id
     */
    String getId();

    default long getCreatedAt() {
        return System.currentTimeMillis();
    }
}
