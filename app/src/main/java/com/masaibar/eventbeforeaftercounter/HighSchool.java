package com.masaibar.eventbeforeaftercounter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by masaibar on 2016/02/28.
 */
public class HighSchool {

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("before_events")
    private int mBeforeEvents;

    @SerializedName("after_events")
    private int mAfterEvents;

    public HighSchool(int id, String name, int beforeEvents, int afterEvents) {
        mId = id;
        mName = name;
        mBeforeEvents = beforeEvents;
        mAfterEvents = afterEvents;
    }

    public int getId() {
        return mId;
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
