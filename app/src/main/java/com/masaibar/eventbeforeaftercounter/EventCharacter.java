package com.masaibar.eventbeforeaftercounter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by masaibar on 2016/02/28.
 */
public class EventCharacter implements Serializable{

    private static final String WIKI_URL_BASE = "http://wiki.famitsu.com/pawapuro/";

    @SerializedName("name")
    private String mName;

    @SerializedName("rarity")
    private String mRarity;

    @SerializedName("before")
    private int mBefore;

    @SerializedName("multi_before")
    private int mMultiBefore;

    @SerializedName("after")
    private int mAfter;

    @SerializedName("multi_after")
    private int mMultiAfter;

    @SerializedName("role")
    private String mRole;

    public EventCharacter(
            String name, String rarity, int before, int multiBefore, int after, int multiAfter, String role) {
        mName = name;
        mRarity = rarity;
        mBefore = before;
        mMultiBefore = multiBefore;
        mAfter = after;
        mMultiAfter = multiAfter;
        mRole = role;
    }

    public static String getWikiUrlBase() {
        return WIKI_URL_BASE;
    }

    public String getName() {
        return mName;
    }

    public String getRarity() {
        return mRarity;
    }

    public int getBefore() {
        return mBefore;
    }

    public int getMultiBefore() {
        return mMultiBefore;
    }

    public int getAfter() {
        return mAfter;
    }

    public int getMultiAfter() {
        return mMultiAfter;
    }

    public String getRole() {
        return mRole;
    }

    public void openWiki(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getWikiUrl()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private  String getWikiUrl() {
        return getWikiUrlBase() + getName();
    }

    @Override
    public String toString() {
        return String.format("name = %s, before = %d, multiBefore = %d, after = %d, multiAfter = %d, role = %s\n",
                getName(), getBefore(), getMultiBefore(), getAfter(), getMultiAfter(), getRole());
    }
}
