package com.example.schae.opentodo;

public class Entry {

    private String mText;
    private boolean mIsChecked = false;
    private boolean mPrevDel = false;

    public String getText() {return mText;}

    public boolean getIsChecked() {return mIsChecked; }
    public void setIsChecked(boolean yon) {
        mIsChecked = yon;
    }

    public boolean getPrevDel() { return mPrevDel; }
    public void setPrevDel(boolean yon) {
        mPrevDel = yon;
    }

    Entry(String text) {
        mText = text;
    }
}

