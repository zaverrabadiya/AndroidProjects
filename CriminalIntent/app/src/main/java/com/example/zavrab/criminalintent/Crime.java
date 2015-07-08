package com.example.zavrab.criminalintent;

import java.util.UUID;

/**
 * Created by zavrab on 7/7/15.
 */
public class Crime {

    private UUID mId;
    private String mTitle;

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Crime() {
        //Generate unique identifier
        mId = UUID.randomUUID();
    }
}
