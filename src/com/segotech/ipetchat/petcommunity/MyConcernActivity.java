package com.segotech.ipetchat.petcommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.richitec.commontoolkit.customadapter.CTListAdapter;
import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.richitec.commontoolkit.customcomponent.CTToast;
import com.richitec.commontoolkit.customcomponent.ListViewQuickAlphabetBar;
import com.richitec.commontoolkit.customcomponent.ListViewQuickAlphabetBar.OnTouchListener;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.richitec.commontoolkit.utils.PinyinUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class MyConcernActivity extends IPetChatNavigationActivity {

	// test data
	// test by ares
	private final JSONArray my_concern_pets_JSONArray = JSONUtils
			.toJSONArray("[]");
	private final int[] my_concern_pets_avatars = new int[] {};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.my_concern_activity_layout);

		// set title
		setTitle(R.string.my_concern_nav_title);

		// set add new concern pet as right bar button item
		setRightBarButtonItem(new BarButtonItem(this,
				R.string.add_new_concern_pet_button_title,
				new AddNewConcernPetBtnOnClickListener()));

		// define my concern pets list
		List<Map<String, ?>> _myConcernPetsList = new ArrayList<Map<String, ?>>();

		// set them
		for (int i = 0; i < my_concern_pets_JSONArray.length(); i++) {
			// define my concern pet item map
			Map<String, Object> _itemMap = new HashMap<String, Object>();

			// avatar
			// test by ares
			_itemMap.put(MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_AVATAR,
					getResources().getDrawable(my_concern_pets_avatars[i]));

			// nickname
			// get nickname
			String _nickname = JSONUtils.getStringFromJSONObject(JSONUtils
					.getJSONObjectFromJSONArray(my_concern_pets_JSONArray, i),
					"nickname");

			_itemMap.put(MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_NICKNAME,
					_nickname);

			// alphabet index
			_itemMap.put(CTListAdapter.ALPHABET_INDEX,
					generateNicknamePhoneticsString(PinyinUtils
							.pinyins4String(_nickname)));

			// sex
			_itemMap.put(
					MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_SEX,
					getResources().getDrawable(
							0 == JSONUtils.getIntegerFromJSONObject(JSONUtils
									.getJSONObjectFromJSONArray(
											my_concern_pets_JSONArray, i),
									"sex") ? R.drawable.img_male
									: R.drawable.img_female));

			// mood
			_itemMap.put(MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_MOOD,
					JSONUtils.getStringFromJSONObject(JSONUtils
							.getJSONObjectFromJSONArray(
									my_concern_pets_JSONArray, i), "mood"));

			// add my concern pet item map to list
			_myConcernPetsList.add(_itemMap);
		}

		// get my concern pets listView
		ListView _myConcernPetsListView = (ListView) findViewById(R.id.my_concern_pet_listView);

		// set my concern pets listView adapter
		_myConcernPetsListView.setAdapter(new MyConcernPetsAdapter(this,
				_myConcernPetsList,
				R.layout.my_concern_pets_listview_item_layout, new String[] {
						MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_AVATAR,
						MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_NICKNAME,
						MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_SEX,
						MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_MOOD },
				new int[] { R.id.my_concern_pet_avatar_imageView,
						R.id.my_concern_pet_nickname_textView,
						R.id.my_concern_pet_sex_imageView,
						R.id.my_concern_pet_mood_textView }));

		// init my concern pets listView quick alphabet bar and add on touch
		// listener
		new ListViewQuickAlphabetBar(_myConcernPetsListView, new CTToast(
				_myConcernPetsListView.getContext())).setOnTouchListener(null);
	}

	// generate my concern pet nickname phonetics string
	private String generateNicknamePhoneticsString(
			List<List<String>> nicknamePhonetics) {
		StringBuilder _namePhoneticsStringBuilder = null;

		if (null != nicknamePhonetics) {
			// init name phonetics string builder
			_namePhoneticsStringBuilder = new StringBuilder();

			for (List<String> _nameCharPhoneticsList : nicknamePhonetics) {
				_namePhoneticsStringBuilder.append(_nameCharPhoneticsList
						.get(0));
			}
		}

		return null == _namePhoneticsStringBuilder ? null
				: _namePhoneticsStringBuilder.toString();
	}

	// inner calss
	// add new concern pet button on click listener
	class AddNewConcernPetBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// goto add concern pet activity
			pushActivity(AddConcernPetActivity.class);
		}

	}

	// my concern pets adapter
	class MyConcernPetsAdapter extends PetCommunityItemListViewAdapter {

		// my concern adapter data keys
		private static final String MY_CONCERN_PETS_ITEM_AVATAR = "my_concern_pets_item_avatar";
		private static final String MY_CONCERN_PETS_ITEM_NICKNAME = "my_concern_pets_item_nickname";
		private static final String MY_CONCERN_PETS_ITEM_SEX = "my_concern_pets_item_sex";
		private static final String MY_CONCERN_PETS_ITEM_MOOD = "my_concern_pets_item_mood";

		public MyConcernPetsAdapter(Context context, List<Map<String, ?>> data,
				int itemsLayoutResId, String[] dataKeys,
				int[] itemsComponentResIds) {
			super(context, data, itemsLayoutResId, dataKeys,
					itemsComponentResIds);
		}

	}

	class MyConcernPetsListViewQuickAlphabetBarOnTouchListener extends
			OnTouchListener {

		private final String LOG_TAG = MyConcernPetsListViewQuickAlphabetBarOnTouchListener.class
				.getCanonicalName();

		@Override
		protected boolean onTouch(RelativeLayout alphabetPresentRelativeLayout,
				ListView dependentListView, MotionEvent event,
				Character alphabeticalCharacter) {
			// get scroll position
			if (dependentListView.getAdapter() instanceof CTListAdapter) {
				// get dependent listView adapter
				CTListAdapter _commonListAdapter = (CTListAdapter) dependentListView
						.getAdapter();

				for (int i = 0; i < _commonListAdapter.getCount(); i++) {
					// get alphabet index
					@SuppressWarnings("unchecked")
					String _alphabetIndex = (String) ((Map<String, ?>) _commonListAdapter
							.getItem(i)).get(CTListAdapter.ALPHABET_INDEX);

					// check alphabet index
					if (null == _alphabetIndex
							|| _alphabetIndex.startsWith(String.valueOf(
									alphabeticalCharacter).toLowerCase(
									Locale.getDefault()))) {
						// set selection
						dependentListView.setSelection(i);

						break;
					}
				}
			} else {
				Log.e(LOG_TAG, "Dependent listView adapter = "
						+ dependentListView.getAdapter() + " and class name = "
						+ dependentListView.getAdapter().getClass().getName());
			}

			return true;
		}

	}

}
