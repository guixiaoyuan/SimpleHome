package com.sprd.simple.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sprd.simple.adapter.FamilyAdapter;
import com.sprd.simple.launcher.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SPRD on 2016/7/19.
 */
public class FamilyWorkspaceFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "FamilyWorkspaceFragment";
    public static final String DATABASE = "FamilyNumber";
    private GridView mGridView = null;
    private FamilyAdapter mFamilyAdapter;
    private String[] spData = new String[2];
    private static String[] mStrArray = new String[2];
    private ArrayList<HashMap<String, Object>> mMenuList;

    // Pictures res before add phoneNumber
    int picAddRes[] = {R.drawable.family_add1_background, R.drawable.family_add2_background,
            R.drawable.family_add3_background, R.drawable.family_add4_background,
            R.drawable.family_add5_background, R.drawable.family_add6_background};

    // Pictures res after add
    int picRes[] = {R.drawable.family_1_background, R.drawable.family_2_background,
            R.drawable.family_3_background, R.drawable.family_4_background,
            R.drawable.family_5_background, R.drawable.family_6_background};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_pager, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.first_grid_view);

        mMenuList = new ArrayList<HashMap<String, Object>>();
        reLoadData();
        mFamilyAdapter = new FamilyAdapter(getActivity(), mMenuList);
        mGridView.setAdapter(mFamilyAdapter);

        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
        mGridView.setOnItemSelectedListener(this);
        return rootView;
    }

    private void reLoadData() {
        Log.d(TAG, "reLoadData");
        if (mMenuList != null) {
            mMenuList.clear();
        }
        for (int i = 0; i < 6; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            SharedPreferences sp = getActivity().getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
            if (sp.contains(i + "") == true) {
                // Contact has added in the family list
                getData(getActivity(), i, spData.length);
                map.put("ItemImage", picRes[i]);
                map.put("ItemName", mStrArray[0]);
                Log.d(TAG, "ItemName = " + mStrArray[0]);
            } else {
                // Family list is empty
                map.put("ItemImage", picAddRes[i]);
                map.put("ItemName", getResources().getString(R.string.family) + (i + 1));
            }
            mMenuList.add(map);
        }
        if (mFamilyAdapter != null) {
            mFamilyAdapter.notifyDataSetChanged();
        }
    }

    /**
     * get the data from SharedPreference
     *
     * @param context
     * @param position
     * @param arrayLength
     * @return
     */
    public String[] getData(Context context, int position, int arrayLength) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
            mStrArray = new String[arrayLength];
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString(position + "", ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                mStrArray[i] = jsonArray.getString(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStrArray;
    }

    /**
     * save the data to SharedPreference
     *
     * @param contexts
     * @param position
     * @param strings
     */
    public void saveData(Context contexts, int position, String[] strings) {
        SharedPreferences sp = contexts.getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        for (String str : strings) {
            jsonArray.put(str);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(position + "", jsonArray.toString());
        editor.commit();
        editor.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences sp = getActivity().getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
        if (sp.contains(position + "") == true) {
            getData(getActivity(), position, spData.length);
            Intent intent = new Intent(Intent.ACTION_CALL);
            Log.d(TAG, "strArray[1] = " + mStrArray[1]);
            Uri data = Uri.parse("tel:" + mStrArray[1]);
            intent.setData(data);
            startActivity(intent);
        } else {
            Intent contactsIntent = new Intent(Intent.ACTION_PICK);
            contactsIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(contactsIntent, position);
            Log.d(TAG, "startActivity " + position);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        SharedPreferences sp = getActivity().getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
        //Family added contact or not
        if (sp.contains(position + "") == true) {
            getData(getActivity(), position, spData.length);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.remove_title);
            builder.setMessage(R.string.remove_info);
            builder.setPositiveButton(R.string.remove_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sp = getActivity().getSharedPreferences(DATABASE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove(position + "");
                    editor.commit();
                    // data change update view
                    reLoadData();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(R.string.remove_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            Intent contactsIntent = new Intent(Intent.ACTION_PICK);
            contactsIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(contactsIntent, position);
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "position = " + position);
        mFamilyAdapter.setSelectPosition(position);
        mFamilyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onActivityResult(int position, int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                Log.d(TAG, "RESULT_OK = " + Activity.RESULT_OK + " position = " + position);
                Uri uri = data.getData();
                Log.d(TAG, "uri = " + uri);

                ContentResolver contentResolver = getActivity().getContentResolver();
                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                }

                //get the app_phone number
                String phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DATA1));
                Log.d(TAG, "phoneNum = " + phoneNum);

                //get the name
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                Log.d(TAG, "name = " + name);

                // Close cursor
                cursor.close();

                //save the data
                spData[0] = name;
                spData[1] = phoneNum;
                saveData(getActivity(), position, spData);

                // data change update view
                reLoadData();
                break;
            case Activity.RESULT_CANCELED:
                // Do nothing in the contact select list
                Log.d(TAG, "RESULT_CANCELED");
                break;
            default:
                break;
        }
    }
}
