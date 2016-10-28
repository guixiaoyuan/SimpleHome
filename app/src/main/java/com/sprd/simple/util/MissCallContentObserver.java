package com.sprd.simple.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.CallLog;
import android.util.Log;

/**
 * Created by SPRD on 7/28/2016.
 */
public class MissCallContentObserver extends ContentObserver {
    private final static String TAG = "MissCallContentObserver";
    private Context mContext;
    private Handler mHandler;

    public static final String CALL_URI = "content://call_log/calls";
    public static final Uri CALLS_CONTENT_URI = Uri.parse(CALL_URI);
    private static final String MISSED_CALLS_SELECTION =
            CallLog.Calls.TYPE + " = " + CallLog.Calls.MISSED_TYPE + " AND " + CallLog.Calls.NEW + " = 1";

    private int missCallCount = 0;
    public static final int MISS_CALL_CONTENT = 3;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public MissCallContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        try {
            missCallCount = getMissedCallCount(mContext);
            mHandler.obtainMessage(MISS_CALL_CONTENT, missCallCount).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getMissedCallCount(Context context) {
        int missedCalls = 0;
        Cursor cursor = null;
        ContentResolver resolver = context.getContentResolver();

        cursor = resolver.query(CALLS_CONTENT_URI, new String[]{BaseColumns._ID},
                MISSED_CALLS_SELECTION, null, null);
        if (cursor != null) {
            missedCalls = cursor.getCount();
            cursor.close();
            Log.i(TAG, "Missed Call count = " + missedCalls);
        }
        return missedCalls;
    }
}
