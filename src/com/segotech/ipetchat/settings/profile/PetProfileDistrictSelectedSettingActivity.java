package com.segotech.ipetchat.settings.profile;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class PetProfileDistrictSelectedSettingActivity extends
		IPetChatNavigationActivity {

	private static final String LOG_TAG = PetProfileDistrictSelectedSettingActivity.class
			.getCanonicalName();

	// pet profile district name and sub items key
	public static final String PET_PROFILE_DISTRICT_NAME_KEY = "pet_profile_district_name_key";
	public static final String PET_PROFILE_DISTRICT_SUBITEMS_KEY = "pet_profile_district_subitems_key";

	// pet profile district name
	private String _mPetProfileDistrictName = "";

	// pet profile district sub item array
	private String[] _mPetProfileDistrictSubItemArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_profile_selected6checked_setting_activity_layout);

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get district name and sub items
			_mPetProfileDistrictName = _data
					.getString(PET_PROFILE_DISTRICT_NAME_KEY);
			_mPetProfileDistrictSubItemArray = _data
					.getStringArray(PET_PROFILE_DISTRICT_SUBITEMS_KEY);
		}

		// set title
		setTitle(String
				.format(getResources()
						.getString(
								R.string.pet_profile_district_selected_setting_nav_title_format),
						_mPetProfileDistrictName));

		// get pet profile district selected item listView
		ListView _petProfileDistrictSelectedItemListView = (ListView) findViewById(R.id.pet_profile_selected6checked_setting_listView);

		// set pet profile district selected item adapter
		_petProfileDistrictSelectedItemListView
				.setAdapter(new ArrayAdapter<String>(
						this,
						R.layout.pet_profile_district_selected_listview_item_layout,
						_mPetProfileDistrictSubItemArray));

		// set pet profile district selected item listView on item click
		// listener
		_petProfileDistrictSelectedItemListView
				.setOnItemClickListener(new PetProfileDistrictSelectedItemListViewOnItemClickListener());
	}

	// inner class
	// pet profile district selected item listView on item click listener
	class PetProfileDistrictSelectedItemListViewOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// check clicked item index and set background
			if (0 == id) {
				// first
				view.setBackgroundResource(R.drawable.pet_profile_selected6checked_setting_topmultiple_item_bg);
			} else if (_mPetProfileDistrictSubItemArray.length - 1 == id) {
				// last
				view.setBackgroundResource(R.drawable.pet_profile_selected6checked_setting_bottommultiple_item_bg);
			}

			// define pet profile district selected text
			StringBuilder _petProfileDistrictSelectedText = new StringBuilder(
					_mPetProfileDistrictName);

			try {
				// get pet profile district selected setting item value textView
				_petProfileDistrictSelectedText.append(" ").append(
						((TextView) view).getText().toString());
			} catch (Exception e) {
				Log.e(LOG_TAG,
						"Get pet profile district selected setting item value textView error, exception message = "
								+ e.getMessage());
			}

			// define pop result extraData
			Map<String, String> _popRetExtraData = new HashMap<String, String>();

			// put pet profile district selected text in pop result extra data
			_popRetExtraData.put(POP_RET_EXTRADATA_KEY,
					_petProfileDistrictSelectedText.toString());

			// pop pet profile district selected setting activity
			popActivityWithResult(RESULT_OK, _popRetExtraData);
		}

	}

}
