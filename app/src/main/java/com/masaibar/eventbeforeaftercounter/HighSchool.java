package com.masaibar.eventbeforeaftercounter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by masaibar on 2016/02/28.
 */
public class HighSchool {

    @SerializedName("name")
    private String mName;

    @SerializedName("before_events")
    private int mBeforeEvents;

    @SerializedName("after_events")
    private int mAfterEvents;

    public HighSchool(String name, int beforeEvents, int afterEvents) {
        mName = name;
        mBeforeEvents = beforeEvents;
        mAfterEvents = afterEvents;
    }

    public String getName() {
        return mName;
    }

    public int getBeforeEvents() {
        return mBeforeEvents;
    }

    public int getAfterEvents() {
        return mAfterEvents;
    }

    @Override
    public String toString() {
        return mName;
    }
}
