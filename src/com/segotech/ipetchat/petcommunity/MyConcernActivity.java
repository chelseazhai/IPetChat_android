package com.segotech.ipetchat.petcommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.richitec.commontoolkit.customadapter.CTListAdapter;
import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.richitec.commontoolkit.customcomponent.CTToast;
import com.richitec.commontoolkit.customcomponent.ListViewQuickAlphabetBar;
import com.richitec.commontoolkit.customcomponent.ListViewQuickAlphabetBar.OnTouchListener;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.richitec.commontoolkit.utils.PinyinUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.pet.PetSex;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class MyConcernActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = MyConcernActivity.class
			.getCanonicalName();

	// my concern pets id list
	private List<Long> _mMyConcernPetsIdList = new ArrayList<Long>();

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
	}

	@Override
	protected void onResume() {
		// get my concern pets info
		// send get my concern pets info post http request
		HttpUtils.postSignatureRequest(
				getResources().getString(R.string.server_url)
						+ getResources().getString(
								R.string.get_myConcernPetsInfo_url),
				PostRequestFormat.URLENCODED, null, null,
				HttpRequestType.ASYNCHRONOUS,
				new GetMyConcernPetsInfoHttpRequestListener());

		super.onResume();
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

	// process get my concern pets or get pet detail info exception
	private void processException() {
		// show login failed toast
		Toast.makeText(MyConcernActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// get my concern pets info http request listener
	class GetMyConcernPetsInfoHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// define my concern pets data list
			List<Map<String, ?>> _myConcernPetsDataList = new ArrayList<Map<String, ?>>();

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get my concern pets info array
					JSONArray _myConcernPetsInfoArray = JSONUtils
							.getJSONArrayFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_getAllPetsReqResp_petsList));

					for (int i = 0; i < _myConcernPetsInfoArray.length(); i++) {
						// define my concern pet map
						Map<String, Object> _itemMap = new HashMap<String, Object>();

						// get my concern pet
						PetBean _concernPet = new PetBean(
								JSONUtils.getJSONObjectFromJSONArray(
										_myConcernPetsInfoArray, i));

						// check avatar
						if (null != _concernPet.getAvatar()) {
							_itemMap.put(
									MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_AVATAR,
									BitmapFactory.decodeByteArray(
											_concernPet.getAvatar(), 0,
											_concernPet.getAvatar().length));
						}

						// check nickname
						String _nickname = _concernPet.getNickname();
						if (null == _nickname) {
							_nickname = "未知";
						}
						_itemMap.put(
								MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_NICKNAME,
								_nickname);

						// alphabet index
						_itemMap.put(CTListAdapter.ALPHABET_INDEX,
								generateNicknamePhoneticsString(PinyinUtils
										.pinyins4String(_nickname)));

						// check sex
						if (null != _concernPet.getSex()) {
							_itemMap.put(
									MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_SEX,
									getResources()
											.getDrawable(
													PetSex.MALE == _concernPet
															.getSex() ? R.drawable.img_male
															: R.drawable.img_female));
						}

						// check mood
						_itemMap.put(
								MyConcernPetsAdapter.MY_CONCERN_PETS_ITEM_MOOD,
								"");

						// add my concern pet item map to list
						_myConcernPetsDataList.add(_itemMap);
						_mMyConcernPetsIdList.add(_concernPet.getId());
					}
					break;

				default:
					Log.e(LOG_TAG,
							"get my concern pets info failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get my concern pets info failed, bg_server return result is null");

				processException();
			}

			// get my concern pets listView
			ListView _myConcernPetsListView = (ListView) findViewById(R.id.my_concern_pet_listView);

			// set my concern pets listView adapter
			_myConcernPetsListView.setAdapter(new MyConcernPetsAdapter(
					MyConcernActivity.this, _myConcernPetsDataList,
					R.layout.my_concern_pets_listview_item_layout,
					new String[] {
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
					_myConcernPetsListView.getContext()))
					.setOnTouchListener(null);

			// set my concern pets listView on item click listener
			_myConcernPetsListView
					.setOnItemClickListener(new MyConcernPetsListViewOnItemClickListener());
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get my concern pets info failed, send get my concern pets info post request failed");

			// show get user all pets info failed toast
			Toast.makeText(MyConcernActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

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

	// my concern pets listView quick alphabet bar on touch listener
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

	// my concern pets listView on item click listener
	class MyConcernPetsListViewOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// get pet info
			// generate get pet info post request param
			Map<String, String> _getPetInfoParam = new HashMap<String, String>();
			_getPetInfoParam.put(
					getResources().getString(
							R.string.rbgServer_getPetInfoReqParam_petId),
					_mMyConcernPetsIdList.get(arg2).toString());

			// send get pet info post http request
			HttpUtils.postSignatureRequest(
					getResources().getString(R.string.server_url)
							+ getResources()
									.getString(R.string.get_petInfo_url),
					PostRequestFormat.URLENCODED, _getPetInfoParam, null,
					HttpRequestType.ASYNCHRONOUS,
					new GetPetInfoHttpRequestListener());
		}

	}

	// get pet info http request listener
	class GetPetInfoHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// define pet info
					PetBean _petInfo = new PetBean(_respJsonData);

					// define extra data
					Map<String, Object> _extraData = new HashMap<String, Object>();

					// check pet info and set extra data
					if (null != _petInfo) {
						_extraData.put(
								PetDetailInfoActivity.PET_DETAILINFO_PET_KEY,
								_petInfo);
						_extraData
								.put(PetDetailInfoActivity.PET_DETAILINFO_CONCERN_KEY,
										true);
					}

					// go to pet detail info activity
					pushActivity(PetDetailInfoActivity.class, _extraData);
					break;

				default:
					Log.e(LOG_TAG,
							"get pet info failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get pet info failed, bg_server return result is null");

				processException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get pet info failed, send get pet info post request failed");

			// show get user all pets info failed toast
			Toast.makeText(MyConcernActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

}
