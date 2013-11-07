package com.segotech.ipetchat.petcommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class BlackListActivity extends IPetChatNavigationActivity {

	// test data
	// test by ares
	private final JSONArray blacklist_pets_JSONArray = JSONUtils
			.toJSONArray("[{\"id\":1, \"nickname\":\"欢欢\"}, {\"id\":2, \"nickname\":\"白白\"}, {\"id\":3, \"nickname\":\"阿豆\"}, {\"id\":4, \"nickname\":\"小黄\"}]");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.blacklist_activity_layout);

		// set title
		setTitle(R.string.blacklist_setting_nav_title);

		// define blacklist pets list
		List<Map<String, ?>> _blacklistPetsList = new ArrayList<Map<String, ?>>();

		// set them
		for (int i = 0; i < blacklist_pets_JSONArray.length(); i++) {
			// define blacklist pet item map
			Map<String, Object> _itemMap = new HashMap<String, Object>();

			// nickname
			_itemMap.put(BlacklistPetsAdapter.BLACKLIST_PETS_ITEM_NICKNAME,
					JSONUtils.getStringFromJSONObject(JSONUtils
							.getJSONObjectFromJSONArray(
									blacklist_pets_JSONArray, i), "nickname"));

			// add blacklist pet item map to list
			_blacklistPetsList.add(_itemMap);
		}

		// set blacklist pets listView adapter
		((ListView) findViewById(R.id.blacklist_pets_listView))
				.setAdapter(new BlacklistPetsAdapter(
						this,
						_blacklistPetsList,
						R.layout.blacklist_pets_listview_item_layout,
						new String[] { BlacklistPetsAdapter.BLACKLIST_PETS_ITEM_NICKNAME },
						new int[] { R.id.blacklist_pet_nickname_textView }));
	}

	// inner class
	// blacklist pets adapter
	class BlacklistPetsAdapter extends PetCommunityItemListViewAdapter {

		// blacklist pets adapter data keys
		private static final String BLACKLIST_PETS_ITEM_NICKNAME = "blacklist_pets_item_nickname";

		public BlacklistPetsAdapter(Context context, List<Map<String, ?>> data,
				int itemsLayoutResId, String[] dataKeys,
				int[] itemsComponentResIds) {
			super(context, data, itemsLayoutResId, dataKeys,
					itemsComponentResIds);
		}

	}

}
