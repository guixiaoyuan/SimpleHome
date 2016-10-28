package com.sprd.simple.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sprd.simple.launcher.R;
import com.sprd.simple.model.IconInfo;
import com.sprd.simple.util.UnreadCountStyleUtil;
import com.sprd.simple.util.UnreadInfoUtil;

import java.util.ArrayList;

/**
 * Created by SPRD on 2016/7/19.
 */
public class DefaultPageAdapter extends BaseAdapter {
    private static final String TAG = "DefaultPageAdapter";

    private static final int CALL_POSITION = 2;
    private static final int MSG_POSITION = 3;

    private ArrayList<IconInfo> mArrayList = new ArrayList<>();
    private Context mContext = null;
    private GridView mGridView = null;
    private int mPosition = -1;
    private Animation mIconAnimation;

    public DefaultPageAdapter(ArrayList<IconInfo> arrayList, Context context, GridView gridView) {
        mArrayList = arrayList;
        mContext = context;
        mGridView = gridView;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IconHolder iconHolder = null;
        if (convertView == null) {
            iconHolder = new IconHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.default_item, null);
            iconHolder.iconName = (TextView) convertView.findViewById(R.id.icon_item_name);
            iconHolder.iconImage = (ImageView) convertView.findViewById(R.id.icon_item_image);
            iconHolder.unreadInfo = (TextView) convertView.findViewById(R.id.unread_info);
            convertView.setTag(iconHolder);
        } else {
            iconHolder = (IconHolder) convertView.getTag();
        }

        // set miss call count
        if (position == CALL_POSITION) {
            int callCount = UnreadInfoUtil.getMissedCallCount(mContext);
            Log.d(TAG, "callCount = " + callCount);
            if (callCount > 0) {
                UnreadCountStyleUtil.setReadCountStyle(iconHolder.unreadInfo, callCount);
                Log.d(TAG, "unreadInfo_call = " + iconHolder.unreadInfo.getText());
            } else {
                iconHolder.unreadInfo.setVisibility(View.INVISIBLE);
            }
        }

        // add the animation for icon
        if (mPosition == position) {
            mIconAnimation = AnimationUtils.loadAnimation(mContext, R.anim.icon_anim);
            mIconAnimation.setRepeatMode(Animation.REVERSE);
            convertView.setAnimation(mIconAnimation);
            mIconAnimation.startNow();
            Log.d(TAG, "startAnimation");
        } else {
            convertView.clearAnimation();
        }

        // set unread msg count
        if (position == MSG_POSITION) {
            int mmsCount = UnreadInfoUtil.getUnreadMessageCount(mContext);
            Log.d(TAG, "mmsCount = " + mmsCount);
            if (mmsCount > 0) {
                UnreadCountStyleUtil.setReadCountStyle(iconHolder.unreadInfo, mmsCount);
                Log.d(TAG, "unreadInfo_mms = " + iconHolder.unreadInfo.getText());
            } else {
                iconHolder.unreadInfo.setVisibility(View.INVISIBLE);
            }
        }
        iconHolder.iconName.setText(mArrayList.get(position).getIconName());
        iconHolder.iconImage.setImageResource(mArrayList.get(position).getIconImage());

        return convertView;
    }

    public void setSelectPosition(int position) {
        mPosition = position;
    }

    public class IconHolder {
        private TextView iconName;
        private ImageView iconImage;
        private TextView unreadInfo;
    }
}
