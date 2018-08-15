package com.example.schae.opentodo.data;

import android.net.Uri;

public class ItemInfo {

    private Uri mUri;
    private int mId;
    private String mText;
    private boolean mCheckState;
    private boolean mPrevRemoved;
    private String mNote;

    public void setChecked(boolean checked) {
        mCheckState = checked;
    }

    public void setPrevRemoved(boolean removed) {
        mPrevRemoved = removed;
    }

    public void setText(String text) {mText = text;}
    public void setNote(String note) {mNote = note;}

    public Uri getUri() {return mUri;}
    public int getId() {return mId;}
    public String getText() {return mText;}
    public boolean getChecked() {return mCheckState;}
    public String getNote() {return mNote;}

    public boolean getPrevRemoved() {return mPrevRemoved;}

    public ItemInfo(Uri uri,int id,String text,String note, boolean checked,boolean prevRemoved) {
        mUri = uri;
        mId = id;
        mText = text;
        mNote = note;
        mCheckState = checked;
        mPrevRemoved = prevRemoved;
    }
}
