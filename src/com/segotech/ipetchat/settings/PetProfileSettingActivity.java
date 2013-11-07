package com.segotech.ipetchat.settings;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.richitec.commontoolkit.user.UserManager;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.customwidget.PetProfileItem;

public class PetProfileSettingActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = PetProfileSettingActivity.class
			.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_profile_setting_activity_layout);

		// set title
		setTitle(R.string.pet_profile_setting_nav_title);

		// get user pet info
		PetBean _petInfo = IPCUserExtension.getUserPetInfo(UserManager
				.getInstance().getUser());

		Log.d(LOG_TAG, "my pet info = " + _petInfo);

		// check user pet info
		if (null != _petInfo) {
			// check and set pet avatar
			if (null != _petInfo.getAvatar()) {
				((ImageView) findViewById(R.id.pet_profile_avatar_imageView))
						.setImageBitmap(BitmapFactory.decodeByteArray(
								_petInfo.getAvatar(), 0,
								_petInfo.getAvatar().length));
			}

			// set pet nickname
			((TextView) findViewById(R.id.pet_profile_nickname_item)
					.findViewById(R.id.pet_profile_item_textView))
					.setText(_petInfo.getNickname());

			// set pet sex
			((TextView) findViewById(R.id.pet_profile_sex_item).findViewById(
					R.id.pet_profile_item_textView)).setText(_petInfo.getSex()
					.getSex());

			// set pet breed
			((PetProfileItem) findViewById(R.id.pet_profile_breed_item))
					.setText(_petInfo.getBreed().getBreed());
		}
	}

}
