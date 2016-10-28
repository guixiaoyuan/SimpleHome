package com.sprd.simple.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CallLog;
import android.util.Log;

/**
 * Created by SPRD on 2016/7/22.
 */
public class UnreadInfoUtil {
    private static final String TAG = "UnreadInfoUtil";
    private static final Uri MMS_CONTENT_URI = Uri.parse("content://mms");
    private static final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
    private static final Uri CALLS_CONTENT_URI = CallLog.Calls.CONTENT_URI;
    private static final String MISSED_CALLS_SELECTION =
            CallLog.Calls.TYPE + " = " + CallLog.Calls.MISSED_TYPE + " AND " + CallLog.Calls.NEW + " = 1";


    public static int getUnreadMessageCount(Context context) {
        int unreadSms = 0;
        int unreadMms = 0;
        Cursor cursor = null;
        ContentResolver resolver = context.getContentResolver();

        // get Unread SMS count
        cursor = resolver.query(SMS_CONTENT_URI, new String[]{BaseColumns._ID},
                "type =1 AND read = 0", null, null);
        if (cursor != null) {
            unreadSms = cursor.getCount();
            cursor.close();
            Log.i(TAG, "SMS count = " + unreadSms);
        }

        // get Unread MMS count
        cursor = resolver.query(MMS_CONTENT_URI, new String[]{BaseColumns._ID},
                "msg_box = 1 AND read = 0 AND ( m_type =130 OR m_type = 132 ) AND thread_id > 0",
                null, null);
        if (cursor != null) {
            unreadMms = cursor.getCount();
            cursor.close();
            Log.i(TAG, "MMS count = " + unreadMms);
        }

        return unreadMms + unreadSms;
    }

    public static int getMissedCallCount(Context context) {
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
