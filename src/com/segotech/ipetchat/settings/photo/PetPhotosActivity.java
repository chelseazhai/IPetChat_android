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
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.BarButtonItem.BarButtonItemStyle;
import com.richitec.commontoolkit.customcomponent.CTMenu;
import com.richitec.commontoolkit.customcomponent.CTMenu.CTMenuOnItemSelectedListener;
import com.richitec.commontoolkit.customcomponent.CTPopupWindow;
import com.richitec.commontoolkit.customcomponent.ImageBarButtonItem;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class PetPhotosActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = PetPhotosActivity.class
			.getCanonicalName();

	// pet photo album key
	public static final String PHOTOALBUM_ID_KEY = "photo_album_id_key";
	public static final String PHOTOALBUM_TITLE_KEY = "photo_album_title_key";
	public static final String PHOTOS_PRESENT_KEY = "photos_present_key";

	// pet photo upload
	private static final int CAPTURE_PHOTO = 800;
	private static final int SELECT_PHOTO = 801;
	private static final int ADD_NEW_PHOTO = 803;

	// more menu ids
	private static final int ADD_NEWPHOTO_MENU = 20;
	private static final int SETASALBUMCOVER_MENU = 21;

	// more popup menu
	private CTMenu _mMorePopupMenu;

	private Boolean _isPhotosPresent = false;

	// pet photo source select popup window
	private PetPhotoSourceSelectPopupWindow _mPetPhotoSourceSelectPopupWindow = new PetPhotoSourceSelectPopupWindow(
			R.layout.petavatar_uploadphotosource_select_popupwindow_layout,
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	private Long photoAlbumId;
	private String photoAlbumTitle;

	// pet photo album photos info list
	private List<PetPhotoBean> _mPetPhotoAlbumPhotosInfoList;

	private Integer _mSelectedPetPhotoIndex = null;

	// pet photo load webView
	private WebView _mPetPhotoLoadWebView;

	// gesture detector
	private GestureDetector _mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_photos_activity_layout);

		// set title
		setTitle("相册详情");

		// init more popup menu
		_mMorePopupMenu = new CTMenu(this);

		// add menu item
		_mMorePopupMenu.add(ADD_NEWPHOTO_MENU, "添加照片");
		_mMorePopupMenu.add(SETASALBUMCOVER_MENU, "设为封面");

		// set more menu on item selected listener
		_mMorePopupMenu
				.setMenuOnItemSelectedListener(new MoreMenuOnItemSelectedListener());

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get id of pet left msg for
			photoAlbumId = _data.getLong(PHOTOALBUM_ID_KEY);
			photoAlbumTitle = _data.getString(PHOTOALBUM_TITLE_KEY);
			_isPhotosPresent = _data.getBoolean(PHOTOS_PRESENT_KEY);
		}

		// set right image bar button item, more info image bar button item
		if (!_isPhotosPresent) {
			setRightBarButtonItem(new ImageBarButtonItem(this,
					R.drawable.img_moremenu_barbuttonitem,
					BarButtonItemStyle.RIGHT_GO,
					new MoreMenuImageBarButtonItemOnClickListener()));
		}

		// get pet photo load webView
		_mPetPhotoLoadWebView = (WebView) findViewById(R.id.pet_photo_webView);

		// init gesture detector
		_mGestureDetector = new GestureDetector(
				_mPetPhotoLoadWebView.getContext(),
				new PetPhotoWebViewOnFlingGestureListener());

		// set pet photo load webView on touch listener
		_mPetPhotoLoadWebView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return _mGestureDetector.onTouchEvent(event);
			}
		});

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
				pushActivityForResult(UploadPetPhotoActivity.class, _extraData,
						ADD_NEW_PHOTO);
				break;

			case ADD_NEW_PHOTO:
				// get pet photo album photos
				getPetPhotoAlbumPhotos();
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

	// process exception
	private void processException() {
		// show login failed toast
		Toast.makeText(PetPhotosActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// show pet photo
	private void showPetPhoto(String photoPath, final String photoDescription) {
		final LinearLayout _loadingLinearLayout = (LinearLayout) findViewById(R.id.pet_photo_loading_linearLayout);
		if (View.VISIBLE != _loadingLinearLayout.getVisibility()) {
			_loadingLinearLayout.setVisibility(View.VISIBLE);
		}

		// get webView setting
		WebSettings _webViewSetting = _mPetPhotoLoadWebView.getSettings();

		// size fit
		_webViewSetting.setUseWideViewPort(true);
		_webViewSetting.setLoadWithOverviewMode(true);

		// define pet photo image html
		StringBuilder data = new StringBuilder("<html><body>");
		data.append("<img src = \""
				+ getResources().getString(R.string.img_url) + photoPath
				+ "\">");
		data.append("</body></html>");

		// load pet photo image url
		_mPetPhotoLoadWebView.loadData(data.toString(), "text/html", "UTF-8");

		// add web chrome client for loading progress changed
		_mPetPhotoLoadWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);

				// set pet photo loading progressBar progress
				((ProgressBar) findViewById(R.id.pet_photo_loading_progressBar))
						.setProgress(newProgress);

				// set pet photo loading textView text
				((TextView) findViewById(R.id.pet_photo_loading_textView))
						.setText(getResources().getString(
								R.string.petPhotoLoading_textView_textHeader)
								+ newProgress + "%");

				// check pet photo page loading completed
				if (Integer.parseInt(getResources().getString(
						R.string.petPhotoLoading_progressBar_max)) == newProgress) {
					// pet photo loading completed, remove pet photo loading
					// linearLayout
					_loadingLinearLayout.setVisibility(View.GONE);

					// get photo description textView
					TextView _photoDescriptionTextView = (TextView) findViewById(R.id.pet_photo_description_textView);

					// check photo description
					if (null == photoDescription
							|| "".equalsIgnoreCase(photoDescription)) {
						_photoDescriptionTextView.setVisibility(View.GONE);
					} else {
						_photoDescriptionTextView.setText(photoDescription);
						_photoDescriptionTextView.setVisibility(View.VISIBLE);
					}
				}
			}

		});
	}

	// inner class
	// more menu image bar button item on click listener
	class MoreMenuImageBarButtonItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// show more menu
			_mMorePopupMenu.showAsDropDown(v);
		}

	}

	// more menu on item selected listener
	class MoreMenuOnItemSelectedListener implements
			CTMenuOnItemSelectedListener {

		@Override
		public boolean onMenuItemSelected(CTMenu menu, int menuItemId) {
			// check popup menu
			if (_mMorePopupMenu == menu) {
				// more menu dismiss
				menu.dismiss();

				// check more menu item id
				switch (menuItemId) {
				case ADD_NEWPHOTO_MENU:
					// add new pet photo in the photo album
					// show pet photo source select popup window with animation
					_mPetPhotoSourceSelectPopupWindow
							.showAtLocationWithAnimation(_mPetPhotoLoadWebView,
									Gravity.CENTER, 0, 0);
					break;

				case SETASALBUMCOVER_MENU:
					// check
					if (null != _mPetPhotoAlbumPhotosInfoList
							&& _mPetPhotoAlbumPhotosInfoList.size() > 0) {
						// set as photo album cover
						// generate set as photo album cover post request param
						Map<String, String> _setAsPhotoAlbumCoverParam = new HashMap<String, String>();
						_setAsPhotoAlbumCoverParam.put("galleryid",
								photoAlbumId.toString());
						_setAsPhotoAlbumCoverParam.put(
								"coverurl",
								_mPetPhotoAlbumPhotosInfoList.get(
										_mSelectedPetPhotoIndex).getPath());

						// send set as photo album cover post http request
						HttpUtils
								.postSignatureRequest(
										getResources().getString(
												R.string.server_url)
												+ getResources()
														.getString(
																R.string.setPhotoAlbumCover_url),
										PostRequestFormat.URLENCODED,
										_setAsPhotoAlbumCoverParam,
										null,
										HttpRequestType.ASYNCHRONOUS,
										new SetPhotoAlbumCoverHttpRequestListener());
					}
					break;
				}
			}

			return false;
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
						// define pet photo info
						PetPhotoBean _petPhoto;

						// check selected pet photo
						if (null == _mSelectedPetPhotoIndex) {
							// get the first pet photo of photo album
							_petPhoto = _mPetPhotoAlbumPhotosInfoList
									.get(_mSelectedPetPhotoIndex = 0);
						} else {
							// get the last pet photo of photo album
							_petPhoto = _mPetPhotoAlbumPhotosInfoList
									.get(_mSelectedPetPhotoIndex = (_mPetPhotoAlbumPhotosInfoList
											.size() - 1));
						}

						// show the first photo
						showPetPhoto(_petPhoto.getPath(),
								_petPhoto.getDescription());
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

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get pet photo album photos info failed, bg_server return result is null");

				processException();
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

	class PetPhotoWebViewOnFlingGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// define consumed result
			boolean _consumed = true;

			// define fling minimum distance and velocity
			final int FLING_MIN_DISTANCE = 50, FLING_MIN_VELOCITY = 100;

			// compare motion event x position
			if (e1.getX() == e2.getX()) {
				_consumed = false;
			} else {
				// // get display child index
				// int _displayedChildIndex = _mInstructionViewFlipper
				// .getDisplayedChild();

				// check fling direction and strength
				if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
						&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					// next
					if (++_mSelectedPetPhotoIndex < _mPetPhotoAlbumPhotosInfoList
							.size()) {
						// get pet next photo
						PetPhotoBean _nextPhoto = _mPetPhotoAlbumPhotosInfoList
								.get(_mSelectedPetPhotoIndex);

						// show pet next photo
						showPetPhoto(_nextPhoto.getPath(),
								_nextPhoto.getDescription());
					} else {
						_mSelectedPetPhotoIndex--;
					}
				} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
						&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					// previous
					if (--_mSelectedPetPhotoIndex >= 0) {
						// get pet previous photo
						PetPhotoBean _nextPhoto = _mPetPhotoAlbumPhotosInfoList
								.get(_mSelectedPetPhotoIndex);

						// show pet previous photo
						showPetPhoto(_nextPhoto.getPath(),
								_nextPhoto.getDescription());
					} else {
						_mSelectedPetPhotoIndex++;
					}
				}
			}

			return _consumed;
		}

	}

	// set pet photo album cover http request listener
	class SetPhotoAlbumCoverHttpRequestListener extends OnHttpRequestListener {

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
					Log.d(LOG_TAG, "set pet photo album cover successful");

					// show get user all pets info failed toast
					Toast.makeText(PetPhotosActivity.this, "成功将此相片设为相册的封面",
							Toast.LENGTH_LONG).show();
					break;

				case 1:
				case 2:
					Log.e(LOG_TAG, "set pet photo album cover failed");

					// show get user all pets info failed toast
					Toast.makeText(PetPhotosActivity.this, "设置相册封面失败",
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"set pet photo album cover failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"set pet photo album cover failed, bg_server return result is null");

				processException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"set pet photo album cover failed, send set pet photo album cover post request failed");

			// show get user all pets info failed toast
			Toast.makeText(PetPhotosActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

}
