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
public class UnreadMMSContentObserver extends ContentObserver {
    private final static String TAG = "UnreadMMSContentObserver";
    private Context mContext;
    private Handler mHandler;

    public static final String MMS_URI = "content://mms";
    public static final Uri MMS_CONTENT_URI = Uri.parse(MMS_URI);

    private int unReadMMSCount = 0;
    public static final int MMS_UNREAD_CONTENT = 2;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public UnreadMMSContentObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        unReadMMSCount = getUnreadMessageCount(mContext);
        mHandler.obtainMessage(MMS_UNREAD_CONTENT, unReadMMSCount).sendToTarget();
    }

    private int getUnreadMessageCount(Context context) {
        int unreadMms = 0;
        Cursor cursor = null;
        ContentResolver resolver = context.getContentResolver();

        // get Unread MMS count
        cursor = resolver.query(MMS_CONTENT_URI, new String[]{BaseColumns._ID},
                "msg_box = 1 AND read = 0 AND ( m_type =130 OR m_type = 132 ) AND thread_id > 0",
                null, null);
        if (cursor != null) {
            unreadMms = cursor.getCount();
            cursor.close();
            Log.i(TAG, "MMS count = " + unreadMms);
        }

        return unreadMms;
    }
}
