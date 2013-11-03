package com.segotech.ipetchat.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.segotech.ipetchat.R;
import com.segotech.ipetchat.constants.GlobalConstant;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.view.SettingMenuItemView;

public class PetInfoSettingActivity extends IPetChatNavigationActivity implements View.OnClickListener{
    private SettingMenuItemView settingSexItem;
    private SettingMenuItemView settingNickname;
    private SettingMenuItemView settingBreed;
    private SettingMenuItemView settingAge;
    private SettingMenuItemView settingColor;
    private SettingMenuItemView settingHeight;
    private SettingMenuItemView settingWeight;
    private SettingMenuItemView settingArea;
    private SettingMenuItemView settingPlayArea;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_info_setting_activity_layout);
        setTitle(getString(R.string.pet_profile));

        settingSexItem = (SettingMenuItemView) findViewById(R.id.setting_sex);
        settingSexItem.setOnClickListener(this);

        settingNickname = (SettingMenuItemView) findViewById(R.id.setting_nickname);
        settingNickname.setOnClickListener(this);

        settingBreed = (SettingMenuItemView) findViewById(R.id.setting_breed);
        settingBreed.setOnClickListener(this);

        settingAge = (SettingMenuItemView) findViewById(R.id.setting_age);
        settingAge.setOnClickListener(this);

        settingColor = (SettingMenuItemView) findViewById(R.id.setting_color);
        settingColor.setOnClickListener(this);

        settingHeight = (SettingMenuItemView) findViewById(R.id.setting_height);
        settingHeight.setOnClickListener(this);

        settingWeight = (SettingMenuItemView) findViewById(R.id.setting_weight);
        settingWeight.setOnClickListener(this);

        settingArea = (SettingMenuItemView) findViewById(R.id.setting_area);
        settingArea.setOnClickListener(this);

        settingPlayArea = (SettingMenuItemView) findViewById(R.id.setting_play_area);
        settingPlayArea.setOnClickListener(this);
	}

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setting_sex) {
            Intent intent = new Intent(this, ProfileItemSelectActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_SEX);
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_SEX);
        } else if (v.getId() == R.id.setting_nickname) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_NICKNAME);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingNickname.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_NAME);
        } else if (v.getId() == R.id.setting_breed) {
            Intent intent = new Intent(this, ProfileItemSelectActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_BREED);
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_BREED);
        } else if (v.getId() == R.id.setting_age) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_AGE);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingAge.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_AGE);
        } else if (v.getId() == R.id.setting_color) {
            Intent intent = new Intent(this, ProfileItemSelectActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_COLOR);
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_COLOR);
        } else if (v.getId() == R.id.setting_height) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_HEIGHT);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingHeight.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_HEIGHT);
        } else if (v.getId() == R.id.setting_weight) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_WEIGHT);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingWeight.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_WEIGHT);
        } else if (v.getId() == R.id.setting_area) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_AREA);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingArea.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_AREA);
        } else if (v.getId() == R.id.setting_play_area) {
            Intent intent = new Intent(this, ProfileTextEditorActivity.class);
            intent.putExtra(GlobalConstant.NameConstant.EDIT_TYPE, GlobalConstant.EditType.EDIT_PLAY_PLACE);
            intent.putExtra(GlobalConstant.NameConstant.VALUE, settingPlayArea.getValue());
            startActivityForResult(intent, GlobalConstant.RequestCode.EDIT_PLAY_PLACE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
