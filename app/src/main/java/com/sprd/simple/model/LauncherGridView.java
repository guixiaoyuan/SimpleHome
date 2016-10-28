package com.sprd.simple.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by SPDR on 16-7-26.
 */
public class LauncherGridView extends GridView {
    public LauncherGridView(Context context) {
        super(context);
    }

    public LauncherGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LauncherGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // override dispatchTouchEvent method forbid GridView slide
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
