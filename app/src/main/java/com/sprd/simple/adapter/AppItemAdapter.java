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
import com.sprd.simple.util.MemoryInfoUtil;

import java.util.ArrayList;

/**
 * Created by SPRD on 2016/7/19.
 */
public class AppItemAdapter extends BaseAdapter {

    private static final String TAG = "AppItemAdapter";
    private ArrayList<IconInfo> mArrayList = new ArrayList<IconInfo>();
    private Context mContext = null;
    private GridView mGridView = null;
    private int mPosition;
    private Animation mIconAnimation;

    public AppItemAdapter(ArrayList<IconInfo> arrayList, Context context, GridView gridView) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.app_item_appliction, null);
            iconHolder.iconName = (TextView) convertView.findViewById(R.id.app_name_icon);
            iconHolder.iconImage = (ImageView) convertView.findViewById(R.id.app_image_icon);
            iconHolder.memoryValue = (TextView) convertView.findViewById(R.id.memory_info);
            convertView.setTag(iconHolder);
        } else {
            iconHolder = (IconHolder) convertView.getTag();
        }
        iconHolder.iconName.setText(mArrayList.get(position).getIconName());
        iconHolder.iconImage.setImageResource(mArrayList.get(position).getIconImage());

        // Show available memory
        if (iconHolder.iconName.getText().equals(mContext.getResources().getString(R.string.speed_name))) {
            iconHolder.memoryValue.setText(MemoryInfoUtil.getUsedPercentValue(mContext));
            iconHolder.memoryValue.setVisibility(View.VISIBLE);
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

        return convertView;
    }

    public void setSelectPosition(int position) {
        mPosition = position;
    }

    public class IconHolder {
        private TextView iconName;
        private ImageView iconImage;
        private TextView memoryValue;
    }
}
