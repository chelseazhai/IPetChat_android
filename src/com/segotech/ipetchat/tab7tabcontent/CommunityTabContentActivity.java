package com.segotech.ipetchat.tab7tabcontent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.richitec.commontoolkit.customadapter.CTListAdapter;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.petcommunity.MessageboxActivity;
import com.segotech.ipetchat.petcommunity.MyConcernActivity;
import com.segotech.ipetchat.petcommunity.PetStarsActivity;
import com.segotech.ipetchat.petcommunity.PetsNearbyActivity;

public class CommunityTabContentActivity extends IPetChatNavigationActivity {

	// pet community info item icon resource id array
	private final int[] PET_COMMUNITY_INFO_ICONS = new int[] {
			R.drawable.img_petstar, R.drawable.img_petnearby,
			R.drawable.img_concern, R.drawable.img_messagebox };

	// pet community info item target activity class array
	@SuppressWarnings("unchecked")
	private final Class<? extends Activity>[] PET_COMMUNITY_INFO_TARGET_ACTIVITYCLSES = new Class[] {
			PetStarsActivity.class, PetsNearbyActivity.class,
			MyConcernActivity.class, MessageboxActivity.class };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.community_tab_content_activity_layout);

		// set title
		setTitle(R.string.community_tab7nav_title);

		// get pet community info listView
		ListView _petCommunityInfoListView = (ListView) findViewById(R.id.pet_community_info_listView);

		// define pet community info list
		List<Map<String, ?>> _petCommunityInfoList = new ArrayList<Map<String, ?>>();

		// get pet community info title array
		String[] _titles = getResources().getStringArray(
				R.array.community_info_array);

		// set them
		for (int i = 0; i < _titles.length; i++) {
			// define pet community info map
			Map<String, Object> _itemMap = new HashMap<String, Object>();

			// icon
			_itemMap.put(PetCommunityInfoAdapter.PET_COMMUNITY_INFO_ITEM_ICON,
					getResources().getDrawable(PET_COMMUNITY_INFO_ICONS[i]));

			// title
			_itemMap.put(PetCommunityInfoAdapter.PET_COMMUNITY_INFO_ITEM_TITLE,
					_titles[i]);

			// add pet community info map to list
			_petCommunityInfoList.add(_itemMap);

		}

		// set pet community info listView adapter
		_petCommunityInfoListView
				.setAdapter(new PetCommunityInfoAdapter(
						this,
						_petCommunityInfoList,
						R.layout.pet_community_info_listview_item_layout,
						new String[] {
								PetCommunityInfoAdapter.PET_COMMUNITY_INFO_ITEM_ICON,
								PetCommunityInfoAdapter.PET_COMMUNITY_INFO_ITEM_TITLE },
						new int[] {
								R.id.pet_community_info_item_icon_imageView,
								R.id.pet_community_info_item_title_textView }));

		// set pet community info listView on item click listener
		_petCommunityInfoListView
				.setOnItemClickListener(new PetCommunityInfoListViewOnItemClickListener());
	}

	// inner class
	// pet community info adapter
	class PetCommunityInfoAdapter extends CTListAdapter {

		private final String LOG_TAG = PetCommunityInfoAdapter.class
				.getCanonicalName();

		// pet community info adapter data keys
		private static final String PET_COMMUNITY_INFO_ITEM_ICON = "pet_community_info_item_icon";
		private static final String PET_COMMUNITY_INFO_ITEM_TITLE = "pet_community_info_item_title";

		public PetCommunityInfoAdapter(Context context,
				List<Map<String, ?>> data, int itemsLayoutResId,
				String[] dataKeys, int[] itemsComponentResIds) {
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
			// imageView
			else if (view instanceof ImageView) {
				try {
					// define item data drawable and convert item data to
					// drawable
					Drawable _itemDrawable = (Drawable) _itemData;

					// set imageView image
					((ImageView) view).setImageDrawable(_itemDrawable);
				} catch (Exception e) {
					e.printStackTrace();

					Log.e(LOG_TAG,
							"Convert item data to drawable error, item data = "
									+ _itemData);
				}
			}
		}

	}

	// pet community info listView on item click listener
	class PetCommunityInfoListViewOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// go to target activity
			CommunityTabContentActivity.this
					.pushActivity(PET_COMMUNITY_INFO_TARGET_ACTIVITYCLSES[position]);
		}
	}

}
