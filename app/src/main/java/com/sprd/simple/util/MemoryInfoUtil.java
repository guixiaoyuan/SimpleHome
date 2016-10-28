package com.sprd.simple.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

/**
 * Created by SPRD on 8/10/2016.
 */
public class MemoryInfoUtil {
    private static final String TAG = "MemoryInfoUtil";

    /**
     * Get Total Memory
     *
     * @param context
     * @return
     */
    public static long getTotalMemory(Context context) {
        final ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(info);
        return info.totalMem / (1024 * 1024);
    }

    /**
     * Get Available Memory
     *
     * @param context
     * @return
     */
    public static long getAvailMemory(Context context) {
        // get Current available memory
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memInfo);
        return memInfo.availMem / (1024 * 1024);
    }

    /**
     * Calculate the used Memory
     *
     * @return
     */
    public static String getUsedPercentValue(Context context) {
        long totalMemorySize = getTotalMemory(context);
        long availableSize = getAvailMemory(context);
        Log.i(TAG, "totalMemorySize = " + totalMemorySize + "    availableSize = " + availableSize);
        int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
        Log.i(TAG, "percent = " + percent);
        return percent + "%";
    }
}
