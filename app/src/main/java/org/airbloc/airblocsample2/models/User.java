package org.airbloc.airblocsample2.models;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.rest.results.Result;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.rest.results.Result;

import java.util.List;

/**
 * User
 */
public class User extends Result {
    public String userId, name; // profiles
    public String profileImage, statusMessage;
    public List<Integer> inventory;
    public int boardingCount;

    public static User fromJson(String json) {
        return App.getGson().fromJson(json, User.class);
    }

    public User(String name) {
        this.name = name;
    }
}
