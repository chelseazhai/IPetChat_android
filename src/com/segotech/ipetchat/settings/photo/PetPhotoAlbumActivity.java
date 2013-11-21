package com.segotech.ipetchat.settings.photo;

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
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.petcommunity.PetCommunityItemListViewAdapter;

public class PetPhotoAlbumActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = PetPhotoAlbumActivity.class
			.getCanonicalName();

	public static final String PET_NICKNAME_KEY = "pet_nickname_key";
	public static final String PET_ID_KEY = "pet_id_key";

	private String _mPetNickname;
	private Long _mPetId;

	// pet photo album info list
	private List<PetPhotoAlbumBean> _mPetPhotoAlbumInfoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_photo_album_activity_layout);

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			_mPetNickname = _data.getString(PET_NICKNAME_KEY);
			_mPetId = _data.getLong(PET_ID_KEY);
		}

		Log.d(LOG_TAG, "@@, pet id = " + _mPetId + " and pet nickname = "
				+ _mPetNickname);

		// set title
		setTitle(String.format(
				getResources().getString(
						R.string.pet_photo_album_nav_title_format),
				_mPetNickname));

		// get pet photo albums listView
		ListView _petPhotoAlbumsListView = (ListView) findViewById(R.id.pet_photoAlbum_listView);

		// set pet photo albums listView on item click listener
		_petPhotoAlbumsListView
				.setOnItemClickListener(new PetPhotoAlbumsListViewOnItemClickListener());

		// set pet photo albums listView on item long click listener
		_petPhotoAlbumsListView.setOnItemLongClickListener(null);

		// get pet photo album
		// generate get pet photo album request param
		Map<String, String> _getPetPhotoAlbumParam = new HashMap<String, String>();
		_getPetPhotoAlbumParam.put("petid", _mPetId.toString());

		// send get pet photo album post http request
		HttpUtils.postSignatureRequest(
				getResources().getString(R.string.server_url)
						+ getResources().getString(R.string.getAlbum_url),
				PostRequestFormat.URLENCODED, _getPetPhotoAlbumParam, null,
				HttpRequestType.ASYNCHRONOUS,
				new GetPetPhotoAlbumHttpRequestListener());

		// test by ares， ？？
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
	}

	// process get pet photo albums exception
	private void processGetPetPhotoAlbumsException() {
		// show login failed toast
		Toast.makeText(PetPhotoAlbumActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// get pet photo album http request listener
	class GetPetPhotoAlbumHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			Log.d(LOG_TAG, "@@ = " + _respJsonData);

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// reset pet photo album info list
			List<Map<String, ?>> _petPhotoAlbumsDataList = new ArrayList<Map<String, ?>>();
			_mPetPhotoAlbumInfoList = new ArrayList<PetPhotoAlbumBean>();

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get pet photo albums info array
					JSONArray _petPhotoAlbumsInfoArray = JSONUtils
							.getJSONArrayFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_getAllPetsReqResp_petsList));

					for (int i = 0; i < _petPhotoAlbumsInfoArray.length(); i++) {
						// define pet photo album map
						Map<String, Object> _itemMap = new HashMap<String, Object>();

						// get photo album and add to photo album list
						PetPhotoAlbumBean _photoAlbumInfo = new PetPhotoAlbumBean(
								JSONUtils.getJSONObjectFromJSONArray(
										_petPhotoAlbumsInfoArray, i));
						_mPetPhotoAlbumInfoList.add(_photoAlbumInfo);

						// check create date
						if (null != _photoAlbumInfo.getTimestamp()) {
							_itemMap.put(
									PetPhotoAlbumsAdapter.PETPHOTO_ALBUMS_ITEM_TIMESTAMP,
									_photoAlbumInfo.getDate());
						}

						// check cover image
						if (null != _photoAlbumInfo.getCoverImg()) {
							_itemMap.put(
									PetPhotoAlbumsAdapter.PETPHOTO_ALBUMS_ITEM_COVERIMG,
									BitmapFactory.decodeByteArray(
											_photoAlbumInfo.getCoverImg(),
											0,
											_photoAlbumInfo.getCoverImg().length));
						} else {
							if (null != _photoAlbumInfo.getCoverUrl()) {
								_itemMap.put(
										PetPhotoAlbumsAdapter.PETPHOTO_ALBUMS_ITEM_COVERIMG,
										getResources().getString(
												R.string.img_url)
												+ _photoAlbumInfo.getCoverUrl());
							}
						}

						// check description
						if (null != _photoAlbumInfo.getTitle()) {
							_itemMap.put(
									PetPhotoAlbumsAdapter.PETPHOTO_ALBUMS_ITEM_DESCRIPTION,
									_photoAlbumInfo.getTitle());
						}

						// add pet photo album item map to list
						_petPhotoAlbumsDataList.add(_itemMap);
					}
					break;

				default:
					Log.e(LOG_TAG,
							"get pet photo album info failed, bg_server return result is unrecognized");

					processGetPetPhotoAlbumsException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get pet photo album info failed, bg_server return result is null");

				processGetPetPhotoAlbumsException();
			}

			// set pet photo albums listView adapter
			((ListView) findViewById(R.id.pet_photoAlbum_listView))
					.setAdapter(new PetPhotoAlbumsAdapter(
							PetPhotoAlbumActivity.this,
							_petPhotoAlbumsDataList,
							R.layout.pet_photoalbum_listview_item_layout,
							new String[] {
									PetPhotoAlbumsAdapter.PETPHOTO_ALBUMS_ITEM_TIMESTAMP,
									PetPhotoAlbumsAdapter.PETPHOTO_ALBUMS_ITEM_COVERIMG,
									PetPhotoAlbumsAdapter.PETPHOTO_ALBUMS_ITEM_DESCRIPTION },
							new int[] { R.id.pet_photoAlbum_date_textView,
									R.id.pet_photoAlbum_coverPhoto_imageView,
									R.id.pet_photoAlbum_description_textView }));
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get pet photo album info failed, send get pet photo album info post request failed");

			// show get user all pets info failed toast
			Toast.makeText(PetPhotoAlbumActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

	// pet photo albums adapter
	class PetPhotoAlbumsAdapter extends PetCommunityItemListViewAdapter {

		// pet photo albums adapter data keys
		private static final String PETPHOTO_ALBUMS_ITEM_TIMESTAMP = "pet_photo_albums_item_timestamp";
		private static final String PETPHOTO_ALBUMS_ITEM_COVERIMG = "pet_photo_albums_item_cover_image";
		private static final String PETPHOTO_ALBUMS_ITEM_DESCRIPTION = "pet_photo_albums_item_description";

		public PetPhotoAlbumsAdapter(Context context,
				List<Map<String, ?>> data, int itemsLayoutResId,
				String[] dataKeys, int[] itemsComponentResIds) {
			super(context, data, itemsLayoutResId, dataKeys,
					itemsComponentResIds);
		}

	}

	// pet photo albums listView on item click listener
	class PetPhotoAlbumsListViewOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// define extra data
			Map<String, Object> _extraData = new HashMap<String, Object>();

			// set extra data
			_extraData.put(PetPhotosActivity.PHOTOALBUM_ID_KEY,
					_mPetPhotoAlbumInfoList.get(arg2).getId());
			_extraData.put(PetPhotosActivity.PHOTOALBUM_TITLE_KEY,
					_mPetPhotoAlbumInfoList.get(arg2).getTitle());
			_extraData.put(PetPhotosActivity.PHOTOS_PRESENT_KEY,
					Boolean.valueOf(true));

			// go to leave or reply message activity
			pushActivity(PetPhotosActivity.class, _extraData);
		}
	}

}
