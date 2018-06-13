package com.example.schae.opentodo;

public class Entry {

    private String mText;
    private boolean mIsChecked = false;
    private boolean mPrevChecked = false;

    public String getText() {return mText;}
    public String setText(String text) {return mText = text;}

    public boolean getIsChecked() {return mIsChecked; }
    public boolean setIsChecked(boolean yon) {return mIsChecked = yon;}

    public Entry(String text) {
        mText = text;
    }
}

