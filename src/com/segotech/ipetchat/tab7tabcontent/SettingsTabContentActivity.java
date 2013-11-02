package com.segotech.ipetchat.tab7tabcontent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.richitec.commontoolkit.customadapter.CTListAdapter;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatRootNavigationActivity;

public class SettingsTabContentActivity extends IPetChatRootNavigationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.settings_tab_content_activity_layout);

		// set title
		setTitle(R.string.settings_tab7nav_title);

		// get settings listView
		ListView _settingsListView = (ListView) findViewById(R.id.settings_listView);

		// define settings list
		List<Map<String, ?>> _settingsList = new ArrayList<Map<String, ?>>();

		// get setting title array
		String[] _titles = getResources()
				.getStringArray(R.array.settings_array);

		// set them
		for (int i = 0; i < _titles.length; i++) {
			// define setting map
			Map<String, Object> _itemMap = new HashMap<String, Object>();

			// title
			_itemMap.put(SettingsAdapter.SETTING_ITEM_TITLE, _titles[i]);

			// add setting map to list
			_settingsList.add(_itemMap);

		}

		// set settings listView adapter
		_settingsListView.setAdapter(new SettingsAdapter(this, _settingsList,
				R.layout.settings_listview_item_layout,
				new String[] { SettingsAdapter.SETTING_ITEM_TITLE },
				new int[] { R.id.setting_item_title_textView }));

		// set settings listView on item click listener
		_settingsListView
				.setOnItemClickListener(new SettingsListViewOnItemClickListener());

		// bind account logout button on click listener
		((Button) findViewById(R.id.account_logout_button))
				.setOnClickListener(new AccountLogoutBtnOnClickListener());
	}

	// inner class
	// settings adapter
	class SettingsAdapter extends CTListAdapter {

		// settings adapter data keys
		private static final String SETTING_ITEM_TITLE = "setting_item_title";

		public SettingsAdapter(Context context, List<Map<String, ?>> data,
				int itemsLayoutResId, String[] dataKeys,
				int[] itemsComponentResIds) {
			super(context, data, itemsLayoutResId, dataKeys,
					itemsComponentResIds);
		}

		@Override
		protected void bindView(View view, Map<String, ?> dataMap,
				String dataKey) {
			// get item data object
			Object _itemData = dataMap.get(dataKey);

			// check view type
			// textView
			if (view instanceof TextView) {
				// set view text
				((TextView) view)
						.setText(null == _itemData ? ""
								: _itemData instanceof SpannableString ? (SpannableString) _itemData
										: _itemData.toString());
			}
		}

	}

	// settings listView on item click listener
	class SettingsListViewOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

		}

	}

	// account logout button on click listener
	class AccountLogoutBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			//
		}

	}

}
