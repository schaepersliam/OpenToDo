package com.example.schae.opentodo.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class ContentProvider extends android.content.ContentProvider {

    private SQLiteHelper mDbHelper;

    private static final int ALL = 100;
    private static final int SINGLE = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY,Contract.PATH_TODO,ALL);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY,Contract.PATH_TODO + "/#",SINGLE);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new SQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ALL:
                cursor = db.query(Contract.Entry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case SINGLE:
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[] { uri.toString() };
                cursor = db.query(Contract.Entry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALL:
                return Contract.Entry.CONTENT_LIST_TYPE;
            case SINGLE:
                return Contract.Entry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALL:
                return insertPet(uri,values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALL:
                return db.delete(Contract.Entry.TABLE_NAME,selection,selectionArgs);
            case SINGLE:
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[] {uri.toString()};
                return db.delete(Contract.Entry.TABLE_NAME,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if(values.containsKey(Contract.Entry.COLUMN_TODO)) {
            String todo = values.getAsString(Contract.Entry.COLUMN_TODO);
            if (todo == null) {
                throw new IllegalArgumentException("Todo requires a text");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        return db.update(Contract.Entry.TABLE_NAME,values,selection,selectionArgs);
    }

    private Uri insertPet(Uri uri, ContentValues values) {
        String todo = values.getAsString(Contract.Entry.COLUMN_TODO);
        if (todo == null) {
            throw new IllegalArgumentException("Todo requires a text");
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(Contract.Entry.TABLE_NAME,null,values);
        if (id == -1) {
            Log.e("Error: ","Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri,id);
    }
}
