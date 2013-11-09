package com.segotech.ipetchat.settings.profile;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.customwidget.PetProfileDistrictItem;

public class PetProfileDistrictSettingActivity extends
		IPetChatNavigationActivity {

	private static final String LOG_TAG = PetProfileDistrictSettingActivity.class
			.getCanonicalName();

	// pet profile district setting request code
	private static final int PET_PROFILE_DISTRICT_EDITTEXT_SETTING_REQCODE = 300;
	private static final int PET_PROFILE_DISTRICT_SELECTED_SETTING_REQCODE = 301;

	// pet profile custom district value key
	public static final String PET_PROFILE_CUSTOM_DISTRICT_VALUE_KEY = "pet profile custom district value key";

	// pet profile custom district
	private String _mPetProfileCustomDistrict;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_profile_district_setting_activity_layout);

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get pet profile custom district
			_mPetProfileCustomDistrict = _data
					.getString(PET_PROFILE_CUSTOM_DISTRICT_VALUE_KEY);
		}

		// set title
		setTitle(R.string.pet_profile_district_setting_nav_title);

		// get pet profile district items parent linearLayout
		LinearLayout _petProfileDistrictItemsParentLinearLayout = (LinearLayout) findViewById(R.id.pet_profile_district_items_parentLinearlayout);

		// process each pet profile district item
		for (int i = 0; i < _petProfileDistrictItemsParentLinearLayout
				.getChildCount(); i++) {
			// set pet profile district item on click listener
			_petProfileDistrictItemsParentLinearLayout.getChildAt(i)
					.setOnClickListener(
							new PetProfileDistrictItemOnClickListener());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// check request code
		switch (requestCode) {
		case PET_PROFILE_DISTRICT_EDITTEXT_SETTING_REQCODE:
		case PET_PROFILE_DISTRICT_SELECTED_SETTING_REQCODE:
			// check result code
			switch (resultCode) {
			case RESULT_OK:
				// check pop result extra data
				if (null != data) {
					// define pop result extraData
					Map<String, String> _popRetExtraData = new HashMap<String, String>();

					// put pet profile editText text in pop result extra data
					_popRetExtraData.put(POP_RET_EXTRADATA_KEY,
							data.getStringExtra(POP_RET_EXTRADATA_KEY));

					// pop pet profile editText activity
					popActivityWithResult(RESULT_OK, _popRetExtraData);
				}
				break;

			default:
				// nothing to do
				break;
			}
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// inner class
	// pet profile district item on click listener
	class PetProfileDistrictItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// define pet profile district item
			PetProfileDistrictItem _petProfileDistrictItem = null;

			try {
				// get pet profile district item
				_petProfileDistrictItem = (PetProfileDistrictItem) v;

				// define target activity class, extra data and request code
				Class<? extends Activity> _targetActivityCls = null;
				Map<String, Object> _extraData = new HashMap<String, Object>();
				int _requestCode = 0;

				// check pet profile district item then set target activity
				// class, extra data and request code
				if (R.id.pet_profile_custom_district_item == _petProfileDistrictItem
						.getId()) {
					_targetActivityCls = PetProfileEditTextSettingActivity.class;

					_extraData
							.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_TITLE_KEY,
									getTitle());
					_extraData
							.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_HINT_KEY,
									_petProfileDistrictItem.getName());
					_extraData
							.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_TEXT_KEY,
									_mPetProfileCustomDistrict);
					_extraData
							.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_INPUTTYPE_KEY,
									InputType.TYPE_CLASS_TEXT);

					_requestCode = PET_PROFILE_DISTRICT_EDITTEXT_SETTING_REQCODE;
				} else {
					_targetActivityCls = PetProfileDistrictSelectedSettingActivity.class;

					_extraData
							.put(PetProfileDistrictSelectedSettingActivity.PET_PROFILE_DISTRICT_NAME_KEY,
									_petProfileDistrictItem.getName());
					_extraData
							.put(PetProfileDistrictSelectedSettingActivity.PET_PROFILE_DISTRICT_SUBITEMS_KEY,
									_petProfileDistrictItem.getSubitems());

					_requestCode = PET_PROFILE_DISTRICT_SELECTED_SETTING_REQCODE;

				}

				// go to target activity
				pushActivityForResult(_targetActivityCls, _extraData,
						_requestCode);
			} catch (Exception e) {
				Log.e(LOG_TAG,
						"Get pet profile province district item error, exception = "
								+ e.getMessage());

				e.printStackTrace();
			}
		}

	}

}
