package com.masaibar.eventbeforeaftercounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

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

    /**
     * 前イベの総数を返す
     */
    public int getBeforeEvents() {
        int result = 0;

        for (EventCharacter eventCharacter : mEventCharacterList) {
            result += eventCharacter.getBefore();
        }

        return result;
    }

    /**
     * 後イベの総数を返す
     */
    public int getAfterEvents() {
        int result = 0;
        for (EventCharacter eventCharacter : mEventCharacterList) {
            result += eventCharacter.getAfter();
        }

        return result;
    }

    /**
     * 前複イベの総数を返す
     */
    public int getMultiBeforeEvents() {
        int result = 0;

        for (EventCharacter eventCharacter : mEventCharacterList) {
            result += eventCharacter.getMultiBefore();
        }

        return result;
    }

    /**
     * 後副イベの総数を返す
     */
    public int getMultiAfterEvents() {
        int result = 0;
        for (EventCharacter eventCharacter : mEventCharacterList) {
            result += eventCharacter.getMultiAfter();
        }

        return result;
    }

    public boolean save(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putString(PREF_KEY_INPUTDATA, new Gson().toJson(this)).commit();
    }

    public static
    @Nullable
    InputData read(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new Gson().fromJson(preferences.getString(PREF_KEY_INPUTDATA, null), InputData.class);
    }

    /**
     * 保存された結果があるかどうか返す
     */
    public static boolean hasData(Context context) {
        SharedPreferences preferencesre = PreferenceManager.getDefaultSharedPreferences(context);
        return preferencesre.contains(PREF_KEY_INPUTDATA);
    }
}
