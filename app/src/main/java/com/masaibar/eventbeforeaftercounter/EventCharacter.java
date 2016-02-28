package com.masaibar.eventbeforeaftercounter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by masaibar on 2016/02/28.
 */
public class EventCharacter {
    @SerializedName("name")
    private String mName;

    @SerializedName("rarity")
    private String mRarity;

    @SerializedName("before_events")
    private int mBeforeEvents;

    @SerializedName("after_events")
    private int mAfterEvents;

    public EventCharacter(String name, String rarity, int beforeEvents, int afterEvents) {
        mName = name;
        mRarity = rarity;
        mBeforeEvents = beforeEvents;
        mAfterEvents = afterEvents;
    }

    public String getName() {
        return mName;
    }

    public String getRarity() {
        return mRarity;
    }

    public int getBeforeEvents() {
        return mBeforeEvents;
    }

    public int getAfterEvents() {
        return mAfterEvents;
    }
}
