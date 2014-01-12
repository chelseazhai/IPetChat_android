package com.segotech.ipetchat.settings.profile;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class PetProfileCheckedSettingActivity extends
		IPetChatNavigationActivity {

	private static final String LOG_TAG = PetProfileCheckedSettingActivity.class
			.getCanonicalName();

	// pet profile checked setting name, checked index and sub items key
	public static final String PET_PROFILE_CHECKED_SETTING_NAME_KEY = "pet_profile_checked_setting_name_key";
	public static final String PET_PROFILE_CHECKED_SETTING_CHECKED_INDEX_KEY = "pet_profile_checked_setting_checked_index_key";
	public static final String PET_PROFILE_CHECKED_SETTING_SUBITEMS_KEY = "pet_profile_checked_setting_subitems_key";

	// pet profile checked setting sub item array
	private String[] _mPetProfileCheckedSettingSubItemArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_profile_selected6checked_setting_activity_layout);

		// define pet profile checked setting name and checked index
		String _checkedSettingName = "";
		Integer _checkedIndex = 0;

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get checked setting name, sub items and checked index
			_checkedSettingName = _data
					.getString(PET_PROFILE_CHECKED_SETTING_NAME_KEY);
			_mPetProfileCheckedSettingSubItemArray = _data
					.getStringArray(PET_PROFILE_CHECKED_SETTING_SUBITEMS_KEY);
			_checkedIndex = _data
					.getInt(PET_PROFILE_CHECKED_SETTING_CHECKED_INDEX_KEY);
		}

		// set title
		setTitle(_checkedSettingName);

		// get pet profile checked setting item listView
		ListView _petProfileCheckedSettingItemListView = (ListView) findViewById(R.id.pet_profile_selected6checked_setting_listView);

		// set pet profile checked setting item adapter
		_petProfileCheckedSettingItemListView
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_single_choice,
						_mPetProfileCheckedSettingSubItemArray));

		// set its choice mode
		_petProfileCheckedSettingItemListView
				.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// set checked item
		_petProfileCheckedSettingItemListView.setItemChecked(_checkedIndex,
				true);

		// set pet profile checked setting item listView on item click listener
		_petProfileCheckedSettingItemListView
				.setOnItemClickListener(new PetProfileCheckedSettingItemListViewOnItemClickListener());
	}

	// inner class
	// pet profile checked item listView on item click listener
	class PetProfileCheckedSettingItemListViewOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// check clicked item index and set background
			if (0 == id) {
				// first
				view.setBackgroundResource(R.drawable.pet_profile_selected6checked_setting_topmultiple_item_bg);
			} else if (_mPetProfileCheckedSettingSubItemArray.length - 1 == id) {
				// last
				view.setBackgroundResource(R.drawable.pet_profile_selected6checked_setting_bottommultiple_item_bg);
			}

			// define pet profile checked setting item checkedTextView
			CheckedTextView _petProfilCheckedSettingItemCheckedTextView = null;

			try {
				// get pet profile checked setting item checkedTextView
				_petProfilCheckedSettingItemCheckedTextView = (CheckedTextView) view;

				// set selected and toggle
				_petProfilCheckedSettingItemCheckedTextView.setSelected(true);
				_petProfilCheckedSettingItemCheckedTextView.toggle();
			} catch (Exception e) {
				Log.e(LOG_TAG,
						"Get pet profile checked setting item value checkedTextView error, exception message = "
								+ e.getMessage());
			}

			// define pop result extraData
			Map<String, Object> _popRetExtraData = new HashMap<String, Object>();

			// check pet profile checked setting item checkedTextView and put
			// pet profile checked text in pop result extra data
			if (null != _petProfilCheckedSettingItemCheckedTextView) {
				// get and check pet profile checked setting item
				// checkedTextView text
				String _petProfilCheckedSettingItemText = _petProfilCheckedSettingItemCheckedTextView
						.getText().toString();
				if (null != _petProfilCheckedSettingItemText) {
					_popRetExtraData.put(POP_RET_EXTRADATA_KEY,
							_petProfilCheckedSettingItemText);
				}
			}

			// pet profile checked item index in pop result extra data
			_popRetExtraData.put(PET_PROFILE_CHECKED_SETTING_CHECKED_INDEX_KEY,
					position);

			// pop pet profile checked setting activity
			popActivityWithResult(RESULT_OK, _popRetExtraData);
		}

	}

}
