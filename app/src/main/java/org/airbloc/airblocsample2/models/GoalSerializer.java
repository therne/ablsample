package org.airbloc.airblocsample2.models;

import android.os.Parcelable;

import org.parceler.Parcels;

/**
 * TODO: Need to be replaced with {@link Goal#toParcel()} and {@link Goal#fromJson(String)}
 */
public class GoalSerializer {
    public static Parcelable marshall(Goal goal) {
        return Parcels.wrap(goal);
    }

    public static Goal unmarshall(Parcelable parcel) {
        return Parcels.unwrap(parcel);
    }
}
