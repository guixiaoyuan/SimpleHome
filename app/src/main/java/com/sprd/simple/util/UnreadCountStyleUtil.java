package com.sprd.simple.util;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by SPRD on 7/29/2016.
 */
public class UnreadCountStyleUtil {
    private static final String TAG = "UnreadCountStyleUtil";
    private static final String EXCEED_TEXT = "99+";
    private static final int MAX_UNREAD_COUNT = 99;

    private static float mLargeTextSize = 14;
    private static float mMiddleTextSize = 12;
    private static float mSmallTextSize = 10;

    public static void setReadCountStyle(TextView textView, int count) {
        if (textView != null) {
            String finalText;
            if (count <= MAX_UNREAD_COUNT) {
                finalText = String.valueOf(count);
            } else {
                finalText = EXCEED_TEXT;
            }
            Log.i(TAG, "setReadCount count = " + count);
            switch (finalText.length()) {
                case 1:
                    textView.setTextSize(mLargeTextSize);
                    break;
                case 2:
                    textView.setTextSize(mMiddleTextSize);
                    break;
                default:
                    textView.setTextSize(mSmallTextSize);
                    break;
            }
            textView.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(count));
            textView.setPadding(0, 0, 0, 5);
            Log.i(TAG, "setReadCount textView = " + textView.getText().toString());
            textView.setTextColor(Color.WHITE);
            textView.getPaint().setAntiAlias(true);
        }
    }
}
