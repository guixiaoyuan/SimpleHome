package com.sprd.simple.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sprd.simple.launcher.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SPRD on 2016/7/19.
 */
public class FamilyAdapter extends BaseAdapter {
    public static final String TAG = "FamilyAdapter";
    public static final String DATABASE = "FamilyNumber";
    private ArrayList<HashMap<String, Object>> mMenuList = new ArrayList<HashMap<String, Object>>();
    private Context mContext = null;
    private int mPosition;
    private Animation mIconAnimation;

    public FamilyAdapter(Context context, ArrayList<HashMap<String, Object>> menuList) {
        mContext = context;
        mMenuList = menuList;
    }

    @Override
    public int getCount() {
        return mMenuList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewTag viewTag;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.family_item, null);
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.family_item_image),
                    (TextView) convertView.findViewById(R.id.family_item_name));
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }
        SharedPreferences sp = mContext.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
        if (sp.contains(position + "") == true) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            viewTag.itemName.setLayoutParams(layoutParams);
            viewTag.itemName.setTextSize(44);
        } else {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            viewTag.itemName.setLayoutParams(layoutParams);
            viewTag.itemName.setTextSize(22);
        }
        viewTag.itemImage.setBackgroundResource((Integer) mMenuList.get(position).get("ItemImage"));
        viewTag.itemName.setText(mMenuList.get(position).get("ItemName").toString());
        Log.d(TAG, "Name = " + mMenuList.get(position).get("ItemName").toString() + " position = " + position);

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

    class ItemViewTag {
        protected ImageView itemImage;
        protected TextView itemName;

        public ItemViewTag(ImageView imageView, TextView name) {
            this.itemImage = imageView;
            this.itemName = name;
        }
    }
}
