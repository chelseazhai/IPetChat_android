package com.segotech.ipetchat.petcommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.settings.photo.PetPhotoAlbumBean;

public class PetStarsActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = PetStarsActivity.class
			.getCanonicalName();

	// pet stars id array
	private List<Long> _mPetStarsIdList = new ArrayList<Long>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_stars_activity_layout);

		// set title
		setTitle(R.string.petstars_nav_title);

		// get system recommend pet stars info
		// send get system recommend pet stars info post http request
		HttpUtils.postSignatureRequest(
				getResources().getString(R.string.server_url)
						+ getResources().getString(
								R.string.get_petStarsInfo_url),
				PostRequestFormat.URLENCODED, null, null,
				HttpRequestType.ASYNCHRONOUS,
				new GetPetStarsInfoHttpRequestListener());
	}

	// process system recommend pet stars info exception
	private void processException() {
		// show login failed toast
		Toast.makeText(PetStarsActivity.this, R.string.toast_request_exception,
				Toast.LENGTH_LONG).show();
	}

	// inner class
	// get system recommend pet stars info http request listener
	class GetPetStarsInfoHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// define pet stars data list
			List<Map<String, ?>> _petStarsDataList = new ArrayList<Map<String, ?>>();

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get system recommend pet stars info array
					JSONArray _petStarsInfoArray = JSONUtils
							.getJSONArrayFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_getAllPetsReqResp_petsList));

					for (int i = 0; i < _petStarsInfoArray.length(); i++) {
						// define pet star recommendation map
						Map<String, Object> _itemMap = new HashMap<String, Object>();

						// get system recommend pet star
						PetBean _petStar = new PetBean(
								JSONUtils.getJSONObjectFromJSONArray(
										_petStarsInfoArray, i));

						// check avatar
						if (null != _petStar.getAvatar()) {
							_itemMap.put(
									PetStarsAdapter.PET_STARS_ITEM_AVATAR,
									BitmapFactory.decodeByteArray(
											_petStar.getAvatar(), 0,
											_petStar.getAvatar().length));
						} else {
							if (null != _petStar.getAvatarUrl()) {
								_itemMap.put(
										PetStarsAdapter.PET_STARS_ITEM_AVATAR,
										getResources().getString(
												R.string.img_url)
												+ _petStar.getAvatarUrl());
							}
						}

						// check recommendation
						if (null != _petStar.getNickname()) {
							_itemMap.put(
									PetStarsAdapter.PET_STARS_ITEM_RECOMMENDATION,
									_petStar.getNickname());
						}

						// define detail button info map and init
						Map<String, Object> _detailBtnInfoMap = new HashMap<String, Object>();
						_detailBtnInfoMap.put(
								PetCommunityItemListViewAdapter.BTN_TAG_KEY,
								Integer.valueOf(i));
						_detailBtnInfoMap
								.put(PetCommunityItemListViewAdapter.BTN_ONCLICKLISTENER_KEY,
										new PetStarDetailBtnOnClickListener());

						// detail button on click listener
						_itemMap.put(
								PetStarsAdapter.PET_STARS_ITEM_DETAILBTN_INFO,
								_detailBtnInfoMap);

						// add pet star recommendation map to list
						_petStarsDataList.add(_itemMap);
						_mPetStarsIdList.add(_petStar.getId());
					}
					break;

				default:
					Log.e(LOG_TAG,
							"get system recommend pet stars info failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get system recommend pet stars info failed, bg_server return result is null");

				processException();
			}

			// set pet stars listView adapter
			((ListView) findViewById(R.id.petStars_listView))
					.setAdapter(new PetStarsAdapter(
							PetStarsActivity.this,
							_petStarsDataList,
							R.layout.pet_stars_listview_item_layout,
							new String[] {
									PetStarsAdapter.PET_STARS_ITEM_AVATAR,
									PetStarsAdapter.PET_STARS_ITEM_RECOMMENDATION,
									PetStarsAdapter.PET_STARS_ITEM_DETAILBTN_INFO },
							new int[] { R.id.petStar_avatar_imageView,
									R.id.petStar_recommendation_textView,
									R.id.petStar_detail_button }));
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get system recommend pet stars info failed, send get system recommend pet stars info post request failed");

			// show get user all pets info failed toast
			Toast.makeText(PetStarsActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

	// pet stars adapter
	class PetStarsAdapter extends PetCommunityItemListViewAdapter {

		// pet stars adapter data keys
		private static final String PET_STARS_ITEM_AVATAR = "pet_stars_item_avatar";
		private static final String PET_STARS_ITEM_RECOMMENDATION = "pet_stars_item_recommendation";
		private static final String PET_STARS_ITEM_DETAILBTN_INFO = "pet_stars_item_detail_button_info";

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
			// get button tag
			Integer _petStarIndex = (Integer) button.getTag();

			// get pet info
			// generate get pet info post request param
			Map<String, String> _getPetInfoParam = new HashMap<String, String>();
			_getPetInfoParam.put(
					getResources().getString(
							R.string.rbgServer_getPetInfoReqParam_petId),
					_mPetStarsIdList.get(_petStarIndex).toString());

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
					// define pet info and pet photo album cover image path list
					PetBean _petInfo = new PetBean(_respJsonData);
					List<String> _photoAlbumCoverPaths = new ArrayList<String>();

					// get pet photo album list and init pet photo album cover
					// image path array
					JSONArray _petPhotoAlbumsInfoArray = JSONUtils
							.getJSONArrayFromJSONObject(_respJsonData,
									"galleries");
					for (int i = 0; i < _petPhotoAlbumsInfoArray.length(); i++) {
						_photoAlbumCoverPaths.add(new PetPhotoAlbumBean(
								JSONUtils.getJSONObjectFromJSONArray(
										_petPhotoAlbumsInfoArray, i))
								.getCoverUrl());
					}

					// define extra data
					Map<String, Object> _extraData = new HashMap<String, Object>();

					// check pet info and set extra data
					if (null != _petInfo) {
						_extraData.put(
								PetDetailInfoActivity.PET_DETAILINFO_PET_KEY,
								_petInfo);
						_extraData
								.put(PetDetailInfoActivity.PET_DETAILINFO_CONCERN_KEY,
										Boolean.valueOf(false));
						_extraData
								.put(PetDetailInfoActivity.PET_PHOTOALBUM_COVERIMGPATHS_KEY,
										_photoAlbumCoverPaths
												.toArray(new String[] {}));
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
			Toast.makeText(PetStarsActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

}
