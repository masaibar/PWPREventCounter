package com.masaibar.eventbeforeaftercounter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by masaibar on 2016/03/05.
 */
public class InputData implements Serializable {

    private HighSchool mHighSchool;
    private ArrayList<EventCharacter> mEventCharacters;

    public InputData(HighSchool highSchool, ArrayList<EventCharacter> eventCharacters) {
        mHighSchool = highSchool;
        mEventCharacters = eventCharacters;
    }

    public HighSchool getHighSchool() {
        return mHighSchool;
    }

    public ArrayList<EventCharacter> getEventCharacters() {
        return mEventCharacters;
    }

    public EventCharacter getEvemtCharacter(int index) {
        return mEventCharacters.get(index);
    }
}
