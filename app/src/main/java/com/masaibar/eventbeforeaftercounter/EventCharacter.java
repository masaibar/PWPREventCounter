package com.masaibar.eventbeforeaftercounter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/**
 * Created by masaibar on 2016/02/28.
 */
public class EventCharacter {

    private static final String WIKI_URL_BASE = "http://wiki.famitsu.com/pawapuro/";

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


    public void openWiki(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getWikiUrl()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private  String getWikiUrl() {
        return WIKI_URL_BASE + getName();
    }
}
