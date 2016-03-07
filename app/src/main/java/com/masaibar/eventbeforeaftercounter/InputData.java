package com.masaibar.eventbeforeaftercounter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masaibar on 2016/03/05.
 */
public class InputData implements Serializable {

    private HighSchool mHighSchool;
    private List<EventCharacter> mEventCharacters;

    public InputData(HighSchool highSchool, List<EventCharacter> eventCharacters) {
        mHighSchool = highSchool;
        mEventCharacters = eventCharacters;
    }

    public HighSchool getHighSchool() {
        return mHighSchool;
    }

    public List<EventCharacter> getEventCharacters() {
        return mEventCharacters;
    }

    public EventCharacter getEvemtCharacter(int index) {
        return mEventCharacters.get(index);
    }
}
