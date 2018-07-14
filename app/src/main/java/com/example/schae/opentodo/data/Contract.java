package com.example.schae.opentodo.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    private Contract() {}

    public static final String CONTENT_AUTHORITY = "com.example.schae.opentodo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TODO = "todo";

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "entrys";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TODO = "todo";

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TODO;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TODO;
    }
}
