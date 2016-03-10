package com.masaibar.eventbeforeaftercounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by masaibar on 2016/03/05.
 */
public class InputData implements Serializable {

    private static final String PREF_KEY_INPUTDATA = "prefKeyInputData";

    private HighSchool mHighSchool;
    private List<EventCharacter> mEventCharacterList;

    public InputData(HighSchool highSchool, List<EventCharacter> eventCharacterList) {
        mHighSchool = highSchool;
        mEventCharacterList = eventCharacterList;
    }

    public HighSchool getHighSchool() {
        return mHighSchool;
    }

    public List<EventCharacter> getEventCharacterList() {
        return mEventCharacterList;
    }

    public EventCharacter getEvemtCharacter(int index) {
        return mEventCharacterList.get(index);
    }

    public boolean save(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putString(PREF_KEY_INPUTDATA, new Gson().toJson(this)).commit();
    }

    public static @Nullable InputData read(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new Gson().fromJson(preferences.getString(PREF_KEY_INPUTDATA, null), InputData.class);
    }

    public static boolean hasData(Context context) {
        return read(context) != null;
    }
}
