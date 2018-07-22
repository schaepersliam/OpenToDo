package com.example.schae.opentodo.data;

import android.net.Uri;

import java.util.List;

public class ItemInfo {

    private Uri mUri;
    private int mId;
    private String mText;
    private boolean mCheckState;
    private boolean mRemoved;
    private boolean mPrevRemoved;

    public Uri setUri(Uri uri) {return mUri = uri;}
    public int setId(int id) {return mId = id;}
    public String setText(String text) {return mText = text;}
    public boolean setChecked(boolean checked) {return mCheckState = checked;}
    public boolean setRemoved(boolean removed) {return mRemoved=removed;}
    public boolean setPrevRemoved(boolean removed) {return mPrevRemoved=removed;}

    public Uri getUri() {return mUri;}
    public int getId() {return mId; }
    public String getText() {return mText;}
    public boolean getChecked() {return mCheckState;}
    public boolean getRemoved() {return mRemoved;}
    public boolean getPrevRemoved() {return mPrevRemoved;}

    public ItemInfo(Uri uri,int id,String text,boolean checked,boolean removed,boolean prevRemoved) {
        mUri = uri;
        mId = id;
        mText = text;
        mCheckState = checked;
        mRemoved = removed;
        mPrevRemoved = prevRemoved;
    }
}
