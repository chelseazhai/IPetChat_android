package com.segotech.ipetchat.settings.photo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.CTPopupWindow;
import com.richitec.commontoolkit.customcomponent.ImageBarButtonItem;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class PetPhotosActivity extends IPetChatNavigationActivity implements
		OnGestureListener {

	private static final String LOG_TAG = PetPhotosActivity.class
			.getCanonicalName();

	// pet photo album key
	public static final String PHOTOALBUM_ID_KEY = "photo_album_id_key";
	public static final String PHOTOALBUM_TITLE_KEY = "photo_album_title_key";

	// pet photo upload
	private static final int CAPTURE_PHOTO = 800;
	private static final int SELECT_PHOTO = 801;

	// pet photo source select popup window
	private PetPhotoSourceSelectPopupWindow _mPetPhotoSourceSelectPopupWindow = new PetPhotoSourceSelectPopupWindow(
			R.layout.petavatar_uploadphotosource_select_popupwindow_layout,
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	private Long photoAlbumId;
	private String photoAlbumTitle;

	// pet photo album photos info list
	private List<PetPhotoBean> _mPetPhotoAlbumPhotosInfoList;

	private GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_photos_activity_layout);

		// set title
		setTitle("相册详情");

		// set add pet photo as right bar button item
		setRightBarButtonItem(new ImageBarButtonItem(this,
				android.R.drawable.ic_input_add,
				new AddPetPhotoBarBtnItemOnClickListener()));

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get id of pet left msg for
			photoAlbumId = _data.getLong(PHOTOALBUM_ID_KEY);
			photoAlbumTitle = _data.getString(PHOTOALBUM_TITLE_KEY);
		}

		// init gesture detector
		detector = new GestureDetector(this, this);

		// get pet photo album photos
		getPetPhotoAlbumPhotos();
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
				_extraData.put(UploadPetPhotoActivity.PHOTOALBUM_TITLE_KEY,
						photoAlbumTitle);
				_extraData.put(UploadPetPhotoActivity.PHOTOALBUM_ID_KEY,
						photoAlbumId);

				// go to leave or reply message activity
				pushActivity(UploadPetPhotoActivity.class, _extraData);
				break;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// get pet photo album photos
	private void getPetPhotoAlbumPhotos() {
		// get pet photo album photos
		// generate get pet photo album photos
		// request param
		Map<String, String> _getPhotoAlbumPhotosParam = new HashMap<String, String>();
		_getPhotoAlbumPhotosParam.put("galleryid", photoAlbumId.toString());

		// send get pet photo album photos post http request
		HttpUtils
				.postSignatureRequest(
						getResources().getString(R.string.server_url)
								+ getResources().getString(
										R.string.getAlbumPhotos_url),
						PostRequestFormat.URLENCODED,
						_getPhotoAlbumPhotosParam, null,
						HttpRequestType.ASYNCHRONOUS,
						new GetPetPhotoAlbumPhotosHttpRequestListener());
	}

	// process get pet photo album photos exception
	private void processGetPetPhotoAlbumPhotosException() {
		// show login failed toast
		Toast.makeText(PetPhotosActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// show pet photo
	private void showPetPhoto(String photoPath, String photoDescription) {
		// load pet photo by webview
		((WebView) findViewById(R.id.pet_photo_webView)).loadUrl(getResources()
				.getString(R.string.server_url)
				+ getResources().getString(R.string.img_url) + photoPath);

		// get photo description textView
		TextView _photoDescriptionTextView = (TextView) findViewById(R.id.pet_photo_description_textView);

		// check photo description
		if (null == photoDescription || "".equalsIgnoreCase(photoDescription)) {
			_photoDescriptionTextView.setVisibility(View.GONE);
		} else {
			_photoDescriptionTextView.setText(photoDescription);
		}
	}

	// inner class
	// add pet photo bar button item on click listener
	class AddPetPhotoBarBtnItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// show pet photo source select popup window with animation
			_mPetPhotoSourceSelectPopupWindow.showAtLocationWithAnimation(v,
					Gravity.CENTER, 0, 0);
		}

	}

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

	// get pet photo album photos http request listener
	class GetPetPhotoAlbumPhotosHttpRequestListener extends
			OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			Log.d(LOG_TAG, "_respJsonData = " + _respJsonData);

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// reset pet photo album photos info list
			_mPetPhotoAlbumPhotosInfoList = new ArrayList<PetPhotoBean>();

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get pet photo albums info array
					JSONArray _petPhotoAlbumPhotosInfoArray = JSONUtils
							.getJSONArrayFromJSONObject(_respJsonData, "photos");

					for (int i = 0; i < _petPhotoAlbumPhotosInfoArray.length(); i++) {
						// get photo album photo and add to photo album photos
						// list
						PetPhotoBean _photoAlbumPhotoInfo = new PetPhotoBean(
								JSONUtils.getJSONObjectFromJSONArray(
										_petPhotoAlbumPhotosInfoArray, i));
						_mPetPhotoAlbumPhotosInfoList.add(_photoAlbumPhotoInfo);
					}

					// show pet photo
					if (0 < _mPetPhotoAlbumPhotosInfoList.size()) {
						// get the first pet photo of photo album
						PetPhotoBean _firstPhoto = _mPetPhotoAlbumPhotosInfoList
								.get(0);

						// show the first photo
						showPetPhoto(_firstPhoto.getPath(),
								_firstPhoto.getDescription());
					} else {
						Log.d(LOG_TAG, "there is no pet photos in the album");

						// there is no pet photos in the album
						Toast.makeText(PetPhotosActivity.this, "此相册没有相片",
								Toast.LENGTH_SHORT);
					}
					break;

				case 1:
					Log.e(LOG_TAG,
							"get pet photo album photos failed, album id is null");

					// show get user all pets info failed toast
					Toast.makeText(PetPhotosActivity.this, "获取相册相片失败",
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"get pet photo album photos info failed, bg_server return result is unrecognized");

					processGetPetPhotoAlbumPhotosException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get pet photo album photos info failed, bg_server return result is null");

				processGetPetPhotoAlbumPhotosException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get pet photo album photos info failed, send get pet photo album photos info post request failed");

			// show get user all pets info failed toast
			Toast.makeText(PetPhotosActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			Log.d(LOG_TAG, "next");

			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			Log.d(LOG_TAG, "previous");

			return true;
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
