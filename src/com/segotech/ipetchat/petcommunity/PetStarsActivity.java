package com.segotech.ipetchat.petcommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class PetStarsActivity extends IPetChatNavigationActivity {

	// test data
	// test by ares
	private final JSONArray petstars_JSONArray = JSONUtils
			.toJSONArray("[{\"avatar\":1, \"recommendation\":\"欢欢，是一个聪明的姑娘\"}, {\"avatar\":2, \"recommendation\":\"白白，是一个帅气的小伙\"}, {\"avatar\":3, \"recommendation\":\"阿豆，霸气彻漏\"}, {\"avatar\":4, \"recommendation\":\"小黄，被称作万人迷\"}, {\"avatar\":5, \"recommendation\":\"萌萌，好萌好可爱\"}]");
	private final int[] petstars_avatars = new int[] {
			R.drawable.img_demo_petstar1, R.drawable.img_demo_petstar2,
			R.drawable.img_demo_petstar3, R.drawable.img_demo_petstar4,
			R.drawable.img_demo_petstar5 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_stars_activity_layout);

		// set title
		setTitle(R.string.petstars_nav_title);

		// define pet stars list
		List<Map<String, ?>> _petStarsList = new ArrayList<Map<String, ?>>();

		// set them
		for (int i = 0; i < petstars_JSONArray.length(); i++) {
			// define pet star recommendation map
			Map<String, Object> _itemMap = new HashMap<String, Object>();

			// avatar
			// test by ares
			_itemMap.put(PetStarsAdapter.PET_STARS_ITEM_AVATAR, getResources()
					.getDrawable(petstars_avatars[i]));

			// recommendation
			_itemMap.put(PetStarsAdapter.PET_STARS_ITEM_RECOMMENDATION,
					JSONUtils.getStringFromJSONObject(JSONUtils
							.getJSONObjectFromJSONArray(petstars_JSONArray, i),
							"recommendation"));

			// detail button on click listener
			_itemMap.put(
					PetStarsAdapter.PET_STARS_ITEM_DETAILBTN_ONCLICKLISTENER,
					new PetStarDetailBtnOnClickListener());

			// add pet star recommendation map to list
			_petStarsList.add(_itemMap);
		}

		// set pet stars listView adapter
		((ListView) findViewById(R.id.petStars_listView))
				.setAdapter(new PetStarsAdapter(
						this,
						_petStarsList,
						R.layout.pet_stars_listview_item_layout,
						new String[] {
								PetStarsAdapter.PET_STARS_ITEM_AVATAR,
								PetStarsAdapter.PET_STARS_ITEM_RECOMMENDATION,
								PetStarsAdapter.PET_STARS_ITEM_DETAILBTN_ONCLICKLISTENER },
						new int[] { R.id.petStar_avatar_imageView,
								R.id.petStar_recommendation_textView,
								R.id.petStar_detail_button }));
	}

	// inner calss
	// pet stars adapter
	class PetStarsAdapter extends PetCommunityItemListViewAdapter {

		// pet stars adapter data keys
		private static final String PET_STARS_ITEM_AVATAR = "pet_stars_item_avatar";
		private static final String PET_STARS_ITEM_RECOMMENDATION = "pet_stars_item_recommendation";
		private static final String PET_STARS_ITEM_DETAILBTN_ONCLICKLISTENER = "pet_stars_item_detail_button_on_click_listener";

		public PetStarsAdapter(Context context, List<Map<String, ?>> data,
				int itemsLayoutResId, String[] dataKeys,
				int[] itemsComponentResIds) {
			super(context, data, itemsLayoutResId, dataKeys,
					itemsComponentResIds);
		}

	}

	// pet star detail button on click listener
	class PetStarDetailBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			// TODO Auto-generated method stub

		}

	}

}
