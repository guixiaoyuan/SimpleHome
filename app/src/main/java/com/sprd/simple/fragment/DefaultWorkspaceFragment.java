package com.sprd.simple.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.sprd.simple.adapter.DefaultPageAdapter;
import com.sprd.simple.launcher.Launcher;
import com.sprd.simple.launcher.R;
import com.sprd.simple.model.IconInfo;
import com.sprd.simple.util.MissCallContentObserver;
import com.sprd.simple.model.LauncherGridView;
import com.sprd.simple.util.UnreadMMSContentObserver;
import com.sprd.simple.util.UnreadSMSContentObserver;

import java.util.ArrayList;

/**
 * Created by SPRD on 2016/7/19.
 */
public class DefaultWorkspaceFragment extends Fragment implements AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "DefaultWorkspaceFragment";
    private LauncherGridView myGridView = null;
    private View mRootView = null;
    DefaultPageAdapter mAdapter;

    private int smsCount = 0;
    private int mmsCount = 0;
    private int callCount = 0;

    private UnreadSMSContentObserver mSMSObserver;
    private UnreadMMSContentObserver mMMSObserver;
    private MissCallContentObserver mCallObserver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_default_pager, container, false);

        // init GridView
        myGridView = (LauncherGridView) mRootView.findViewById(R.id.default_grid_view);
        mAdapter = new DefaultPageAdapter(getData(), getActivity(), myGridView);
        myGridView.setAdapter(mAdapter);

        // SOS Long click Listener
        myGridView.setOnItemLongClickListener(this);

        // View click Listener
        myGridView.setOnItemClickListener(this);

        // view has focus Listener
        myGridView.setOnItemSelectedListener(this);

        // Create ContentObservers
        createObserver();

        // Register ContentObservers
        registerContentObservers();

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void createObserver() {
        mSMSObserver = new UnreadSMSContentObserver(getActivity(), mHandler);
        mMMSObserver = new UnreadMMSContentObserver(getActivity(), mHandler);
        mCallObserver = new MissCallContentObserver(getActivity(), mHandler);
    }

    private void registerContentObservers() {
        Uri smsUri = Uri.parse(UnreadSMSContentObserver.SMS_URI);
        getActivity().getContentResolver().registerContentObserver(smsUri, true, mSMSObserver);

        Uri mmsUri = Uri.parse(UnreadMMSContentObserver.MMS_URI);
        getActivity().getContentResolver().registerContentObserver(mmsUri, true, mMMSObserver);

        Uri callUri = Uri.parse(MissCallContentObserver.CALL_URI);
        getActivity().getContentResolver().registerContentObserver(callUri, true, mCallObserver);
    }

    private ArrayList getData() {
        ArrayList<IconInfo> list = new ArrayList<IconInfo>();
        IconInfo contact = new IconInfo();
        contact.setIconName(getResources().getString(R.string.contacts_name));
        contact.setIconImage(R.drawable.app_contacts);
        list.add(contact);

        IconInfo camera = new IconInfo();
        camera.setIconName(getResources().getString(R.string.camera_name));
        camera.setIconImage(R.drawable.app_camera);
        list.add(camera);

        IconInfo phone = new IconInfo();
        phone.setIconName(getResources().getString(R.string.phone_name));
        phone.setIconImage(R.drawable.app_phone);
        list.add(phone);

        IconInfo message = new IconInfo();
        message.setIconName(getResources().getString(R.string.message_name));
        message.setIconImage(R.drawable.app_message);
        list.add(message);

        return list;
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
            case 0:
                intent.setClassName(getResources().getString(R.string.contacts_package),
                        getResources().getString(R.string.contacts_activity));
                break;
            case 1:
                intent.setClassName(getResources().getString(R.string.camera_package),
                        getResources().getString(R.string.camera_activity));
                break;
            case 2:
                intent.setClassName(getResources().getString(R.string.phone_package),
                        getResources().getString(R.string.phone_activity));
                break;
            case 3:
                intent.setClassName(getResources().getString(R.string.message_package),
                        getResources().getString(R.string.message_activity));
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "position = " + position);
        mAdapter.setSelectPosition(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UnreadSMSContentObserver.SMS_UNREAD_CONTENT: {
                    smsCount = (int) msg.obj;
                    Log.i(TAG, "Handler smsCount = " + smsCount);
                }

                case UnreadMMSContentObserver.MMS_UNREAD_CONTENT: {
                    mmsCount = (int) msg.obj;
                    Log.i(TAG, "Handler mmsCount = " + mmsCount);
                }

                case MissCallContentObserver.MISS_CALL_CONTENT: {
                    callCount = (int) msg.obj;
                    Log.i(TAG, "Handler callCount = " + callCount);
                }

                default:
                    mAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };

}
