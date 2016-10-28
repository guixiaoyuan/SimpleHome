package com.sprd.simple.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by SPRD on 7/28/2016.
 */
public class UnreadSMSContentObserver extends ContentObserver {
    private final static String TAG = "UnreadSMSContentObserver";
    private Context mContext;
    private Handler mHandler;

    public static final String SMS_URI = "content://sms";
    public static final Uri SMS_CONTENT_URI = Uri.parse(SMS_URI);

    private int unReadSMSCount = 0;
    public static final int SMS_UNREAD_CONTENT = 1;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public UnreadSMSContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        unReadSMSCount = getUnreadMessageCount(mContext);
        mHandler.obtainMessage(SMS_UNREAD_CONTENT, unReadSMSCount).sendToTarget();
    }

    private int getUnreadMessageCount(Context context) {
        int unreadSms = 0;
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

        return unreadSms;
    }
}
