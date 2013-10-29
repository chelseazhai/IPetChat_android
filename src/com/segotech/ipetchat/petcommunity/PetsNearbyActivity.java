package com.segotech.ipetchat.petcommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.richitec.commontoolkit.activityextension.NavigationActivity;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;

public class PetsNearbyActivity extends NavigationActivity {

	// test data
	// test by ares
	private final JSONArray pets_nearby_JSONArray = JSONUtils
			.toJSONArray("[{\"avatar\":1, \"nickname\":\"欢欢\", \"sex\":0, \"distance\":100, \"mood\":\"好高兴啊\"}, {\"avatar\":2, \"nickname\":\"白白\", \"sex\":1, \"distance\":100, \"mood\":\"\"}, {\"avatar\":3, \"nickname\":\"阿豆\", \"sex\":1, \"distance\":200, \"mood\":\"@合肥，生命中第一次长途旅行...\"}, {\"avatar\":4, \"nickname\":\"小黄\", \"sex\":1, \"distance\":300, \"mood\":\"没有食欲\"}, {\"avatar\":5, \"nickname\":\"萌萌\", \"sex\":0, \"distance\":300, \"mood\":\"\"}, {\"avatar\":6, \"nickname\":\"小狗\", \"sex\":1, \"distance\":500, \"mood\":\"最近各种倒霉%>_<%\"}, {\"avatar\":7, \"nickname\":\"旺财\", \"sex\":0, \"distance\":1000, \"mood\":\"运动，运动\"}]");
	private final int[] pets_nearby_avatars = new int[] {
			R.drawable.img_demo_petnearby1, R.drawable.img_demo_petnearby2,
			R.drawable.img_demo_petnearby3, R.drawable.img_demo_petnearby4,
			R.drawable.img_demo_petnearby5, R.drawable.img_demo_petnearby6,
			R.drawable.img_demo_petnearby7 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pets_nearby_activity_layout);

		// set title
		setTitle(R.string.pets_nearby_nav_title);

		// define pets nearby list
		List<Map<String, ?>> _petsNearbyList = new ArrayList<Map<String, ?>>();

		// set them
		for (int i = 0; i < pets_nearby_JSONArray.length(); i++) {
			// define pet nearby item map
			Map<String, Object> _itemMap = new HashMap<String, Object>();

			// avatar
			// test by ares
			_itemMap.put(PetsNearbyAdapter.PETS_NEARBY_ITEM_AVATAR,
					getResources().getDrawable(pets_nearby_avatars[i]));

			// nickname
			_itemMap.put(PetsNearbyAdapter.PETS_NEARBY_ITEM_NICKNAME, JSONUtils
					.getStringFromJSONObject(JSONUtils
							.getJSONObjectFromJSONArray(pets_nearby_JSONArray,
									i), "nickname"));

			// sex
			_itemMap.put(
					PetsNearbyAdapter.PETS_NEARBY_ITEM_SEX,
					getResources()
							.getDrawable(
									0 == JSONUtils.getIntegerFromJSONObject(
											JSONUtils
													.getJSONObjectFromJSONArray(
															pets_nearby_JSONArray,
															i), "sex") ? R.drawable.img_male
											: R.drawable.img_female));

			// distance
			_itemMap.put(PetsNearbyAdapter.PETS_NEARBY_ITEM_DISTANCE, String
					.format(getResources().getString(
							R.string.pet_nearby_distance_format), JSONUtils
							.getIntegerFromJSONObject(JSONUtils
									.getJSONObjectFromJSONArray(
											pets_nearby_JSONArray, i),
									"distance")));

			// mood
			_itemMap.put(PetsNearbyAdapter.PETS_NEARBY_ITEM_MOOD, JSONUtils
					.getStringFromJSONObject(JSONUtils
							.getJSONObjectFromJSONArray(pets_nearby_JSONArray,
									i), "mood"));

			// add pet nearby item map to list
			_petsNearbyList.add(_itemMap);
		}

		// set pets nearby listView adapter
		((ListView) findViewById(R.id.pets_nearby_listView))
				.setAdapter(new PetsNearbyAdapter(this, _petsNearbyList,
						R.layout.pets_nearby_listview_item_layout,
						new String[] {
								PetsNearbyAdapter.PETS_NEARBY_ITEM_AVATAR,
								PetsNearbyAdapter.PETS_NEARBY_ITEM_NICKNAME,
								PetsNearbyAdapter.PETS_NEARBY_ITEM_SEX,
								PetsNearbyAdapter.PETS_NEARBY_ITEM_DISTANCE,
								PetsNearbyAdapter.PETS_NEARBY_ITEM_MOOD },
						new int[] { R.id.pet_nearby_avatar_imageView,
								R.id.pet_nearby_nickname_textView,
								R.id.pet_nearby_sex_imageView,
								R.id.pet_nearby_distance_textView,
								R.id.pet_nearby_mood_textView }));
	}

	// inner calss
	// pets nearby adapter
	class PetsNearbyAdapter extends PetCommunityItemListViewAdapter {

		// pets nearby adapter data keys
		private static final String PETS_NEARBY_ITEM_AVATAR = "pets_nearby_item_avatar";
		private static final String PETS_NEARBY_ITEM_NICKNAME = "pets_nearby_item_nickname";
		private static final String PETS_NEARBY_ITEM_SEX = "pets_nearby_item_sex";
		private static final String PETS_NEARBY_ITEM_DISTANCE = "pets_nearby_item_distance";
		private static final String PETS_NEARBY_ITEM_MOOD = "pets_nearby_item_mood";

		public PetsNearbyAdapter(Context context, List<Map<String, ?>> data,
				int itemsLayoutResId, String[] dataKeys,
				int[] itemsComponentResIds) {
			super(context, data, itemsLayoutResId, dataKeys,
					itemsComponentResIds);
		}

	}

}
