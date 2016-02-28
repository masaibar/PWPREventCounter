package com.masaibar.eventbeforeaftercounter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by masaibar on 2016/02/28.
 */
public class JSONData {

    @SerializedName("high_schools")
    private List<HighSchool> mHighSchools;

    @SerializedName("charactes")
    private List<EventCharacter> mEventCharacters;

    public JSONData(List<HighSchool> highSchools, List<EventCharacter> eventCharacters) {
        mHighSchools = highSchools;
        mEventCharacters = eventCharacters;
    }

    public List<HighSchool> getHighSchools() {
        return mHighSchools;
    }

    public List<EventCharacter> getEventCharacters() {
        return mEventCharacters;
    }
}
