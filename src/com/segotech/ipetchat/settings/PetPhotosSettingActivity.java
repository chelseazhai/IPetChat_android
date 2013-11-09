package com.segotech.ipetchat.settings;

import android.os.Bundle;

import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class PetPhotosSettingActivity extends IPetChatNavigationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_photos_setting_activity_layout);

		// set title
		setTitle(R.string.pet_photos_setting_nav_title);

		//
	}

}
