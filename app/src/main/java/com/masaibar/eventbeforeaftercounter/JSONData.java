package com.masaibar.eventbeforeaftercounter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by masaibar on 2016/02/28.
 */
public class JSONData {

    @SerializedName("need_update")
    private boolean mNeedUpdate;

    @SerializedName("high_schools")
    private List<HighSchool> mHighSchools;

    @SerializedName("event_characters")
    private List<EventCharacter> mEventCharacters;

    public JSONData(
            boolean needUpdate,
            List<HighSchool> highSchools,
            List<EventCharacter> eventCharacters) {
        mNeedUpdate = needUpdate;
        mHighSchools = highSchools;
        mEventCharacters = eventCharacters;
    }

    public boolean isNeedUpdate() {
        return mNeedUpdate;
    }

    public List<HighSchool> getHighSchools() {
        return mHighSchools;
    }

    public List<EventCharacter> getEventCharacters() {
        return mEventCharacters;
    }
}
