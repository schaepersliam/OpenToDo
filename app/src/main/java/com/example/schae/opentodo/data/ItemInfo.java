package com.example.schae.opentodo.data;

import android.net.Uri;

public class ItemInfo {

    private Uri mUri;
    private int mId;
    private String mText;
    private boolean mCheckState;
    private boolean mPrevRemoved;
    private String mNote;
    private int mPriorityState; //0 -> none; 1 -> low; 2 -> mid; 3 -> high

    public void setChecked(boolean checked) {
        mCheckState = checked;
    }

    public void setPrevRemoved(boolean removed) {
        mPrevRemoved = removed;
    }

    public void setText(String text) {mText = text;}
    public void setNote(String note) {mNote = note;}
    public void setPriorityState(int state) {mPriorityState = state;}

    public Uri getUri() {return mUri;}
    public int getId() {return mId;}
    public String getText() {return mText;}
    public boolean getChecked() {return mCheckState;}
    public String getNote() {return mNote;}
    public int getPriorityState() {return mPriorityState;}

    public boolean getPrevRemoved() {return mPrevRemoved;}

    public ItemInfo(Uri uri,int id,String text,String note, int PriorityState, boolean checked,boolean prevRemoved) {
        mUri = uri;
        mId = id;
        mText = text;
        mNote = note;
        mPriorityState = PriorityState;
        mCheckState = checked;
        mPrevRemoved = prevRemoved;
    }
}
