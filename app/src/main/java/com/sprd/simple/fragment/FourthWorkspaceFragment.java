package com.sprd.simple.fragment;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.sprd.simple.adapter.AppItemAdapter;
import com.sprd.simple.launcher.Launcher;
import com.sprd.simple.launcher.R;
import com.sprd.simple.launcher.ToolsActivity;
import com.sprd.simple.model.IconInfo;
import com.sprd.simple.util.MemoryInfoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SPRD on 2016/7/19.
 */
public class FourthWorkspaceFragment extends Fragment implements AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "FourthWorkspaceFragment";

    private static final int ICON_VIDEO = 0;
    private static final int ICON_MUSIC = 1;
    private static final int ICON_BROWSER = 2;
    private static final int ICON_SPEED = 3;
    private static final int ICON_TOOLS = 4;

    private static final int START_ANIMATION = 5;
    private static final int STOP_ANIMATION = 6;

    private ActivityManager mActivityManager = null;

    private GridView mGridView = null;
    private AppItemAdapter mAdapter;
    private Animation cleanAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fourth_pager, container, false);

        mActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);

        cleanAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.clean);
        LinearInterpolator lin = new LinearInterpolator();
        cleanAnimation.setInterpolator(lin);

        // init GridView
        mGridView = (GridView) rootView.findViewById(R.id.fourth_grid_view);
        mAdapter = new AppItemAdapter(getData(), getActivity(), mGridView);
        mGridView.setAdapter(mAdapter);

        // View click Listener
        mGridView.setOnItemClickListener(this);

        // SOS Long click Listener
        mGridView.setOnItemLongClickListener(this);

        // view has focus Listener
        mGridView.setOnItemSelectedListener(this);

        return rootView;
    }

    private ArrayList getData() {
        ArrayList<IconInfo> list = new ArrayList<IconInfo>();

        IconInfo video = new IconInfo();
        video.setIconName(getResources().getString(R.string.video_name));
        video.setIconImage(R.drawable.app_video);
        list.add(video);

        IconInfo music = new IconInfo();
        music.setIconName(getResources().getString(R.string.music_name));
        music.setIconImage(R.drawable.app_music);
        list.add(music);

        IconInfo browser = new IconInfo();
        browser.setIconName(getResources().getString(R.string.browser_name));
        browser.setIconImage(R.drawable.app_browser);
        list.add(browser);

        IconInfo speed = new IconInfo();
        speed.setIconName(getResources().getString(R.string.speed_name));
        speed.setIconImage(R.drawable.app_speed);
        list.add(speed);

        IconInfo tools = new IconInfo();
        tools.setIconName(getResources().getString(R.string.tools_name));
        tools.setIconImage(R.drawable.app_tool);
        list.add(tools);
        return list;
    }

    /**
     * Each item click and enter different app
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        try {
            switch (position) {
                case ICON_VIDEO:
                    intent.setClassName(getResources().getString(R.string.video_package),
                            getResources().getString(R.string.video_activity));
                    startActivity(intent);
                    break;
                case ICON_MUSIC:
                    intent.setClassName(getResources().getString(R.string.music_package),
                            getResources().getString(R.string.music_activity));
                    startActivity(intent);
                    break;
                case ICON_BROWSER:
                    intent.setClassName(getResources().getString(R.string.browser_package),
                            getResources().getString(R.string.browser_activity));
                    startActivity(intent);
                    break;
                case ICON_SPEED:
                    SecurityThread thread = new SecurityThread();
                    thread.start();
                    break;
                case ICON_TOOLS:
                    intent = new Intent(getActivity(), ToolsActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        } catch (ActivityNotFoundException e) {
            Log.i(TAG, "App not found");
            Toast.makeText(getActivity(), R.string.app_not_found, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Long press any item in this page, it will send a broadcast to SOS
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Launcher.SOS_BROADCAST);
        getActivity().sendBroadcast(intent);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAdapter.setSelectPosition(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class SecurityThread extends Thread {

        @Override
        public void run() {
            handler.sendEmptyMessage(START_ANIMATION);
            Log.i(TAG, "SecurityThread");
            List<ActivityManager.RunningAppProcessInfo> appProcessesList =
                    mActivityManager.getRunningAppProcesses();

            long beforeMem = MemoryInfoUtil.getAvailMemory(getActivity());
            Log.d(TAG, "-----------before memory info : " + beforeMem);

            int count = 0;
            if (appProcessesList != null) {
                for (int i = 0; i < appProcessesList.size(); ++i) {
                    ActivityManager.RunningAppProcessInfo appProcessInfo = appProcessesList.get(i);
                    Log.d(TAG, "process name : " + appProcessInfo.processName);
                    Log.d(TAG, "importance : " + appProcessInfo.importance);

                    if (appProcessInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                        String[] pkgList = appProcessInfo.pkgList;
                        for (int j = 0; j < pkgList.length; j++) {
                            Log.d(TAG, "It will be killed, package name : " + pkgList[j]);
                            mActivityManager.killBackgroundProcesses(pkgList[j]);
                            count++;
                        }
                    }
                }
            }
            long afterMem = MemoryInfoUtil.getAvailMemory(getActivity());
            Log.d(TAG, "----------- after memory info : " + afterMem);
            Log.i(TAG, "----------- count = " + count);
            Message msg = new Message();
            msg.what = STOP_ANIMATION;
            msg.arg1 = count;
            msg.arg2 = (int) afterMem;
            handler.sendMessage(msg);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mGridView != null) {
                View view = mGridView.getChildAt(3);
                ImageView img = (ImageView) view
                        .findViewById(R.id.app_image_icon);
                switch (msg.what) {
                    case START_ANIMATION: {
                        // clear iconAnimation when click speed
                        if (cleanAnimation != null) {
                            view.clearAnimation();
                            img.setImageDrawable(getActivity().getResources()
                                    .getDrawable(
                                            R.drawable.app_speed_clean_anim));
                            img.startAnimation(cleanAnimation);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    }

                    case STOP_ANIMATION: {
                        int processCount = msg.arg1;
                        int availableMemory = msg.arg2;
                        img.clearAnimation();

                        Toast.makeText(
                                getContext(),
                                getContext().getResources().getString(
                                        R.string.clean_toast_prompt,
                                        processCount, availableMemory),
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }

        }
    };
}
