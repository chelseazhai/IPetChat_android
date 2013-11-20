package com.segotech.ipetchat.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.CTPopupWindow;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.petcommunity.PetCommunityItemListViewAdapter;
import com.segotech.ipetchat.settings.photo.PetPhotoAlbumBean;
import com.segotech.ipetchat.settings.photo.PetPhotosActivity;
import com.segotech.ipetchat.settings.photo.UploadPetPhotoActivity;

public class PetPhotosSettingActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = PetPhotosSettingActivity.class
			.getCanonicalName();

	// pet photo upload
	private static final int CAPTURE_PHOTO = 700;
	private static final int SELECT_PHOTO = 701;

	// pet photo source select popup window
	private PetPhotoSourceSelectPopupWindow _mPetPhotoSourceSelectPopupWindow = new PetPhotoSourceSelectPopupWindow(
			R.layout.petavatar_uploadphotosource_select_popupwindow_layout,
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	// pet photo album info list
	private List<PetPhotoAlbumBean> _mPetPhotoAlbumInfoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_photos_setting_activity_layout);

		// set title
		setTitle(R.string.pet_photos_setting_nav_title);

		// get add new photo album imageView
		ImageView _addNewPhotoAlbumImageView = (ImageView) findViewById(
				R.id.add_new_photoAlbum_layout).findViewById(
				R.id.pet_photoAlbum_coverPhoto_imageView);

		// set add new photo album imageView image resource
		_addNewPhotoAlbumImageView
				.setImageResource(R.drawable.img_new_petalbum_cover);

		// set add new photo album imageView on click listener
		_addNewPhotoAlbumImageView
				.setOnClickListener(new AddNewPetPhotoAlbumImageViewOnClickListener());

		// get pet photo albums listView
		ListView _petPhotoAlbumsListView = (ListView) findViewById(R.id.pet_photoAlbum_listView);

		// set pet photo albums listView on item click listener
		_petPhotoAlbumsListView
				.setOnItemClickListener(new PetPhotoAlbumsListViewOnItemClickListener());

		// set pet photo albums listView on item long click listener
		_petPhotoAlbumsListView.setOnItemLongClickListener(null);
	}

	@Override
	protected void onResume() {
		// get pet photo album
		// send get pet photo album post http request
		HttpUtils.postSignatureRequest(
				getResources().getString(R.string.server_url)
						+ getResources().getString(R.string.getAlbum_url),
				PostRequestFormat.URLENCODED, null, null,
				HttpRequestType.ASYNCHRONOUS,
				new GetPetPhotoAlbumHttpRequestListener());

		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && data != null) {
			switch (requestCode) {
			case CAPTURE_PHOTO:
			case SELECT_PHOTO:
				String picturePath = null;

				Uri uri = data.getData();
				if (uri == null) {
					if (CAPTURE_PHOTO == requestCode) {
						// get bundle
						Bundle bundle = data.getExtras();
						if (null != bundle) {
							// get bitmap
							Bitmap capturePhoto = (Bitmap) bundle.get("data");

							Log.d(LOG_TAG, "capturePhoto = " + capturePhoto);
							return;
						} else {
							Toast.makeText(this, "获取照片出错", Toast.LENGTH_SHORT)
									.show();
							return;
						}
					} else {
						Toast.makeText(this, "获取照片出错", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				} else {
					String[] filePathColumn = { MediaStore.Images.Media.DATA };

					Cursor cursor = getContentResolver().query(uri,
							filePathColumn, null, null, null);
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					picturePath = cursor.getString(columnIndex);
					cursor.close();
				}

				// define extra data
				Map<String, Object> _extraData = new HashMap<String, Object>();

				// set extra data
				_extraData.put(UploadPetPhotoActivity.SELECT_PHOTOPATH_KEY,
						picturePath);

				// go to leave or reply message activity
				pushActivity(UploadPetPhotoActivity.class, _extraData);
				break;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// process get pet photo albums exception
	private void processGetPetPhotoAlbumsException() {
		// show login failed toast
		Toast.makeText(PetPhotosSettingActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// pet photo source select popup window
	class PetPhotoSourceSelectPopupWindow extends CTPopupWindow {

		public PetPhotoSourceSelectPopupWindow(int resource, int width,
				int height, boolean focusable, boolean isBindDefListener) {
			super(resource, width, height, focusable, isBindDefListener);
		}

		public PetPhotoSourceSelectPopupWindow(int resource, int width,
				int height) {
			super(resource, width, height);
		}

		@Override
		protected void bindPopupWindowComponentsListener() {
			// bind talk photo, select photo from photo album and cancel button
			// click listener
			((Button) getContentView().findViewById(R.id.talkPhoto_button))
					.setOnClickListener(new TalkPhotoBtnOnClickListener());

			((Button) getContentView().findViewById(
					R.id.selectPhoto_fromAlbum_button))
					.setOnClickListener(new SelectPhotoFromAlbumBtnOnClickListener());

			((Button) getContentView().findViewById(R.id.selectCancel_button))
					.setOnClickListener(new CancelSelectBtnOnClickListener());
		}

		@Override
		protected void resetPopupWindow() {
			// nothing to do
		}

		// inner class
		// insert phone to contact mode select insert to new contact button on
		// click listener
		class TalkPhotoBtnOnClickListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// dismiss insert phone to contact mode select popup window
				dismiss();

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				// test by ares
				startActivityForResult(intent, CAPTURE_PHOTO);
			}

		}

		// insert phone to contact mode select insert to existed contact button
		// on click listener
		class SelectPhotoFromAlbumBtnOnClickListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// dismiss insert phone to contact mode select popup window
				dismiss();

				// test by ares
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "选择相片"),
						SELECT_PHOTO);
			}

		}

		// cancel select button on click listener
		class CancelSelectBtnOnClickListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// dismiss insert phone to contact mode select popup window with
				// animation
				dismissWithAnimation();
			}

		}

	}

	// add new pet photo album imageView on click listener
	class AddNewPetPhotoAlbumImageViewOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// show pet photo source select popup window with animation
			_mPetPhotoSourceSelectPopupWindow.showAtLocationWithAnimation(v,
					Gravity.CENTER, 0, 0);
		}

	}

	// get pet photo album http request listener
	class GetPetPhotoAlbumHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

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
							PetPhotosSettingActivity.this,
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

			Log.d(LOG_TAG, "@@ = " + response.getStatusLine().getStatusCode());

			// show get user all pets info failed toast
			Toast.makeText(PetPhotosSettingActivity.this,
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

			// go to leave or reply message activity
			pushActivity(PetPhotosActivity.class, _extraData);
		}
	}

}
