package com.sprd.simple.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.sprd.simple.adapter.AppItemAdapter;
import com.sprd.simple.launcher.Launcher;
import com.sprd.simple.launcher.R;
import com.sprd.simple.model.IconInfo;
import com.sprd.simple.model.LauncherGridView;

import java.util.ArrayList;

/**
 * Created by SPRD on 2016/7/19.
 */
public class ThirdWorkspaceFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "ThirdWorkspaceFragment";
    private final static int ICON_CALLLOG = 0;
    private final static int ICON_CALENDAR = 1;
    private final static int ICON_GALLERY = 2;
    private final static int ICON_FMRADIO = 3;
    private final static int ICON_CLOCK = 4;
    private final static int ICON_SETTINGS = 5;

    private LauncherGridView mGridView;
    private AppItemAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third_pager, container, false);

        // init GridView
        mGridView = (LauncherGridView) rootView.findViewById(R.id.third_grid_view);
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

        IconInfo callLog = new IconInfo();
        callLog.setIconName(getResources().getString(R.string.call_log_name));
        callLog.setIconImage(R.drawable.app_calllog);
        list.add(callLog);

        IconInfo calendar = new IconInfo();
        calendar.setIconName(getResources().getString(R.string.calendar_name));
        calendar.setIconImage(R.drawable.app_calender);
        list.add(calendar);

        IconInfo gallery = new IconInfo();
        gallery.setIconName(getResources().getString(R.string.gallery_name));
        gallery.setIconImage(R.drawable.app_gallery);
        list.add(gallery);

        IconInfo fmRadio = new IconInfo();
        fmRadio.setIconName(getResources().getString(R.string.fm_name));
        fmRadio.setIconImage(R.drawable.app_fm_radio);
        list.add(fmRadio);

        IconInfo clock = new IconInfo();
        clock.setIconName(getResources().getString(R.string.clock_name));
        clock.setIconImage(R.drawable.app_clock);
        list.add(clock);

        IconInfo settings = new IconInfo();
        settings.setIconName(getResources().getString(R.string.settings_name));
        settings.setIconImage(R.drawable.app_settings);
        list.add(settings);

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
        switch (position) {
            case ICON_CALLLOG:
                intent.setClassName(getResources().getString(R.string.call_log_package),
                        getResources().getString(R.string.call_log_activity));
                break;
            case ICON_CALENDAR:
                intent.setClassName(getResources().getString(R.string.calendar_package),
                        getResources().getString(R.string.calendar_activity));
                break;
            case ICON_GALLERY:
                intent.setClassName(getResources().getString(R.string.gallery_package),
                        getResources().getString(R.string.gallery_activity));
                break;
            case ICON_FMRADIO:
                intent.setClassName(getResources().getString(R.string.fm_package),
                        getResources().getString(R.string.fm_activity));
                break;
            case ICON_CLOCK:
                intent.setClassName(getResources().getString(R.string.clock_package),
                        getResources().getString(R.string.clock_activity));
                break;
            case ICON_SETTINGS:
                intent.setClassName(getResources().getString(R.string.settings_package),
                        getResources().getString(R.string.settings_activity));
                break;
            default:
                break;
        }

        try {
            startActivity(intent);
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
}
