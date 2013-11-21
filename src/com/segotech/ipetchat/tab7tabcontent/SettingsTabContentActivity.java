package com.segotech.ipetchat.tab7tabcontent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.segotech.ipetchat.petcommunity.BlackListActivity;
import com.segotech.ipetchat.settings.AccountInfoSettingActivity;
import com.segotech.ipetchat.settings.PetDeviceBindActivity;
import com.segotech.ipetchat.settings.PetPhotosSettingActivity;
import com.segotech.ipetchat.settings.PetProfileSettingActivity;

public class SettingsTabContentActivity extends IPetChatRootNavigationActivity {

	// setting items info target activity class array
	@SuppressWarnings("unchecked")
	private final Class<? extends Activity>[] SETTING_ITEMSINFO_TARGET_ACTIVITYCLSES = new Class[] {
			PetProfileSettingActivity.class, PetPhotosSettingActivity.class,
			AccountInfoSettingActivity.class, BlackListActivity.class,
			PetDeviceBindActivity.class };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.settings_tab_content_activity_layout);

		// set title
		setTitle(R.string.settings_tab7nav_title);

		// get setting items listView
		ListView _settingItemsListView = (ListView) findViewById(R.id.st_settingItems_listView);

		// get setting items title array
		String[] _settingItemsTitles = getResources().getStringArray(
				R.array.setting_items_info_array);

		// define setting items data list
		List<Map<String, ?>> _settingItemsDataList = new ArrayList<Map<String, ?>>();

		// process each setting item
		for (int i = 0; i < _settingItemsTitles.length; i++) {
			// define setting item data map
			Map<String, Object> _itemDataMap = new HashMap<String, Object>();

			// set title
			_itemDataMap.put(SettingItemsAdapter.SETTING_ITEM_TITLE,
					_settingItemsTitles[i]);

			// add setting item data map to list
			_settingItemsDataList.add(_itemDataMap);

		}

		// set setting items listView adapter
		_settingItemsListView.setAdapter(new SettingItemsAdapter(this,
				_settingItemsDataList, R.layout.settings_listview_item_layout,
				new String[] { SettingItemsAdapter.SETTING_ITEM_TITLE },
				new int[] { R.id.sti_title_textView }));

		// set setting items listView on item click listener
		_settingItemsListView
				.setOnItemClickListener(new SettingItemsListViewOnItemClickListener());

		// bind account logout button on click listener
		((Button) findViewById(R.id.st_account_logout_button))
				.setOnClickListener(new AccountLogoutBtnOnClickListener());
	}

	// inner class
	// setting items adapter
	class SettingItemsAdapter extends CTListAdapter {

		// setting items adapter data keys
		private static final String SETTING_ITEM_TITLE = "setting_item_title";

		public SettingItemsAdapter(Context context, List<Map<String, ?>> data,
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

	// setting items listView on item click listener
	class SettingItemsListViewOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// go to target activity
			pushActivity(SETTING_ITEMSINFO_TARGET_ACTIVITYCLSES[position]);
		}

	}

	// account logout button on click listener
	class AccountLogoutBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			// show exit pet chat client alert dialog
			new AlertDialog.Builder(SettingsTabContentActivity.this)
					.setTitle(R.string.iPetChat_exitAlertDialog_title)
					.setMessage(R.string.iPetChat_exitAlertDialog_message)
					.setPositiveButton(
							R.string.iPetChat_exitAlertDialog_exitButton_title,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// exit pet chat project
									System.exit(0);
								}
							})
					.setNegativeButton(
							R.string.iPetChat_exitAlertDialog_cancelButton_title,
							null).show();
		}

	}

}
