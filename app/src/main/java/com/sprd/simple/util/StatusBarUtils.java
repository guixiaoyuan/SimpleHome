package com.sprd.simple.util;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.sprd.simple.launcher.R;

/**
 * Created by SPRD on 8/1/2016.
 */
public class StatusBarUtils {
    public static void setWindowStatusBarColor(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(R.color.tools_status_bar_color));
        }
    }
}
