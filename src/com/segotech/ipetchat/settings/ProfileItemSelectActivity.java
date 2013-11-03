package com.segotech.ipetchat.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.constants.GlobalConstant;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 13-11-2.
 */
public class ProfileItemSelectActivity extends IPetChatNavigationActivity implements AdapterView.OnItemClickListener{
    private ListView listView;
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile_item_select);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(GlobalConstant.NameConstant.EDIT_TYPE, -1);
            initView(type);
        }
    }

    private void initView(int type) {
        switch (type) {
            case GlobalConstant.EditType.EDIT_SEX:
                String[] sex = getResources().getStringArray(R.array.sex);
                List<String> sexArray = Arrays.asList(sex);
                listView.setAdapter(new ArrayAdapter<String>(this,
                        R.layout.pet_setting_listview_item_layout,
                        sexArray));
                break;

            case GlobalConstant.EditType.EDIT_BREED:
                String[] breed = getResources().getStringArray(R.array.dog_breed);
                List<String> breedArray = Arrays.asList(breed);
                listView.setAdapter(new ArrayAdapter<String>(this,
                        R.layout.pet_setting_listview_item_layout,
                        breedArray));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String value = (String) parent.getItemAtPosition(position);
        Intent intent = new Intent();
        intent.putExtra(GlobalConstant.NameConstant.VALUE, value);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
