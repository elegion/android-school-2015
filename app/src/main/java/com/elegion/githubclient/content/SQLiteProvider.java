package com.elegion.githubclient.content;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;

public class SQLiteProvider extends ContentProvider {

    private SQLiteHelper mHelper;

    private static boolean isRowiUri(Uri uri) {
        return uri.getPathSegments().size() == 2
                && TextUtils.isDigitsOnly(uri.getLastPathSegment());
    }

    @Override
    public boolean onCreate() {
        mHelper = new SQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] columns, String where, String[] whereArgs,
                        String orderBy) {
        final String table = uri.getPathSegments().get(0);
        final SQLiteDatabase db = mHelper.getReadableDatabase();
        final Cursor cursor;
        if(isRowiUri(uri)) {
            cursor = db.query(table, columns, BaseColumns._ID + " = ?",
                    new String[]{uri.getLastPathSegment()},
                    null, null, orderBy);
        } else {
            cursor = db.query(table, columns, where, whereArgs, null, null, orderBy);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if(isRowiUri(uri)) {
            return "vnd.android.cursor.item/" + uri.getPathSegments().get(0);
        } else {
            return "vnd.android.cursor.dir/" + uri.getPathSegments().get(0);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(isRowiUri(uri)) {
            throw new SQLiteException("Insert by unique element uri not supported yet =P");
        }
        final String table = uri.getPathSegments().get(0);
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final long rowId = db.insert(table, BaseColumns._ID, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, rowId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] bulkValues) {
        final String table = uri.getPathSegments().get(0);
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues values : bulkValues) {
                db.insert(table, BaseColumns._ID, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return bulkValues.length;
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        return super.applyBatch(operations);
    }

    private static class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context) {
            super(context, null, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS repo(" +
                    "_id INTEGER PRIMARY KEY ON CONFLICT REPLACE, " +
                    "name TEXT, " +
                    "description TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

}
