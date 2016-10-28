package com.sprd.simple.launcher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sprd.simple.adapter.ToolsItemAdapter;
import com.sprd.simple.model.IconInfo;
import com.sprd.simple.util.StatusBarUtils;

import java.util.ArrayList;

/**
 * Created by SPDR on 2016/7/21.
 */
public class ToolsActivity extends Activity implements AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener {
    private static final String TAG = "ToolsActivity";
    private ListView mToolsListView;
    private Button mChoose;
    private Button mBack;
    private static final int ICON_SOS = 0;
    private static final int ICON_FIREWALL = 1;
    private static final int ICON_RECORDER = 2;
    private static final int ICON_NOTEBOOK = 3;
    private static final int ICON_BACKUP = 4;
    private static final int ICON_FILE = 5;
    private static final int ICON_CALCULATOR = 6;
    private ToolsItemAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        StatusBarUtils.setWindowStatusBarColor(this); // change status bar color
        setContentView(R.layout.activity_tools);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.tools_title_bar);
        mChoose = (Button) findViewById(R.id.btn_choose);
        mBack = (Button) findViewById(R.id.btn_back);
        mToolsListView = (ListView) findViewById(R.id.list_second_item);
        mAdapter = new ToolsItemAdapter(getData(), this);
        mAdapter.setPosition(0);
        mToolsListView.setAdapter(mAdapter);
        mToolsListView.setOnItemLongClickListener(this);
        mToolsListView.setOnItemClickListener(this);
    }

    private ArrayList getData() {
        ArrayList<IconInfo> arrayList = new ArrayList<>();
        IconInfo sos = new IconInfo();
        sos.setIconName(getResources().getString(R.string.sos_name));
        sos.setIconImage(R.drawable.app_sos);
        arrayList.add(sos);

        IconInfo fireWaller = new IconInfo();
        fireWaller.setIconName(getResources().getString(R.string.call_fire_wall_name));
        fireWaller.setIconImage(R.drawable.app_firewall);
        arrayList.add(fireWaller);

        IconInfo recorder = new IconInfo();
        recorder.setIconName(getResources().getString(R.string.sound_recorder_name));
        recorder.setIconImage(R.drawable.app_recorder);
        arrayList.add(recorder);

        IconInfo notebook = new IconInfo();
        notebook.setIconImage(R.drawable.app_notebook);
        notebook.setIconName(getResources().getString(R.string.note_name));
        arrayList.add(notebook);

        IconInfo backup = new IconInfo();
        backup.setIconName(getResources().getString(R.string.backup_name));
        backup.setIconImage(R.drawable.app_backup);
        arrayList.add(backup);

        IconInfo fileManager = new IconInfo();
        fileManager.setIconName(getResources().getString(R.string.file_manager_name));
        fileManager.setIconImage(R.drawable.app_filemanamger);
        arrayList.add(fileManager);

        IconInfo calculator = new IconInfo();
        calculator.setIconName(getResources().getString(R.string.calculator_name));
        calculator.setIconImage(R.drawable.app_caculator);
        arrayList.add(calculator);
        return arrayList;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch ((event.getKeyCode())) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    mAdapter.setPosition(mAdapter.getPosition() == 6 ? 0 : (mAdapter.getPosition() + 1));
                    Log.i(TAG, "KEYCODE_DPAD_DOWN mAdapter.getPosition() = " + mAdapter.getPosition());
                    mAdapter.notifyDataSetChanged();
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    mAdapter.setPosition(mAdapter.getPosition() == 0 ? 6 : (mAdapter.getPosition() - 1));
                    Log.i(TAG, "KEYCODE_DPAD_UP mAdapter.getPosition() = " + mAdapter.getPosition());
                    mAdapter.notifyDataSetChanged();
                    break;
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_DPAD_CENTER: {
                    launchApp(mAdapter.getPosition());
                    break;
                }
            }
        }
        return super.dispatchKeyEvent(event);
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
        mAdapter.setPosition(position);
        mAdapter.notifyDataSetChanged();
        launchApp(position);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                mChoose.setPressed(true);
                break;
            case KeyEvent.KEYCODE_BACK:
                mBack.setPressed(true);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            Intent intent = new Intent(Launcher.STATUS_BAR_BROADCAST);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            this.sendBroadcast(intent);
            Log.i(TAG, "send status bar broadcast in ToolsActivity");
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
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
        sendBroadcast(intent);
        return true;
    }

    private void launchApp(int flag) {
        Intent intent = new Intent();
        switch (flag) {
            case ICON_SOS:
                intent.setClassName(getResources().getString(R.string.sos_package),
                        getResources().getString(R.string.sos_activity));
                break;
            case ICON_FIREWALL:
                intent.setClassName(getResources().getString(R.string.call_fire_wall_package),
                        getResources().getString(R.string.call_fire_wall_activity));
                break;
            case ICON_RECORDER:
                intent.setClassName(getResources().getString(R.string.sound_recorder_package),
                        getResources().getString(R.string.sound_recorder_activity));
                break;
            case ICON_NOTEBOOK:
                intent.setClassName(getResources().getString(R.string.note_package),
                        getResources().getString(R.string.note_activity));
                break;
            case ICON_BACKUP:
                intent.setClassName(getResources().getString(R.string.backup_package),
                        getResources().getString(R.string.backup_activity));
                break;
            case ICON_FILE:
                intent.setClassName(getResources().getString(R.string.file_manager_package),
                        getResources().getString(R.string.file_manager_activity));
                break;
            case ICON_CALCULATOR:
                intent.setClassName(getResources().getString(R.string.calculator_package),
                        getResources().getString(R.string.calculator_activity));
                break;
        }
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.i(TAG, "App not found");
            Toast.makeText(this, R.string.app_not_found, Toast.LENGTH_LONG).show();
        }
    }
}
