package com.segotech.ipetchat.settings.photo;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class UploadPetPhotoActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = UploadPetPhotoActivity.class
			.getCanonicalName();

	// select photo path key
	public static final String PHOTOALBUM_TITLE_KEY = "photo_album_title_key";
	public static final String PHOTOALBUM_ID_KEY = "photo_album_id_key";
	public static final String SELECT_PHOTOPATH_KEY = "select_photo_path_key";

	private String photoAlbumTitle;
	private Long photoAlbumId;
	private Bitmap selectedPhotoBitmap;

	// asynchronous http request progress dialog
	private ProgressDialog _mAsyncHttpReqProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.upload_pet_photo_activity_layout);

		// set title
		setTitle(R.string.upload_pet_photo_nav_title);

		// set upload pet photo as right bar button item
		setRightBarButtonItem(new BarButtonItem(this, "上传",
				new UploadPetPhotoBarBtnItemOnClickListener()));

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get id of pet left msg for
			// selectedPhotoBitmap = BitmapFactory.decodeFile(_data
			// .getString(SELECT_PHOTOPATH_KEY));

			// test by ares
			try {
				selectedPhotoBitmap = BitmapFactory.decodeFile(_data
						.getString(SELECT_PHOTOPATH_KEY));
			} catch (OutOfMemoryError e) {
				e.printStackTrace();

				System.gc();

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				try {
					selectedPhotoBitmap = BitmapFactory.decodeFile(
							_data.getString(SELECT_PHOTOPATH_KEY), options);
				} catch (OutOfMemoryError e2) {
					e2.printStackTrace();

					System.gc();

					options.inSampleSize = 4;
					selectedPhotoBitmap = BitmapFactory.decodeFile(
							_data.getString(SELECT_PHOTOPATH_KEY), options);
				}
			}

			photoAlbumTitle = _data.getString(PHOTOALBUM_TITLE_KEY);
			photoAlbumId = _data.getLong(PHOTOALBUM_ID_KEY);
		}

		// set upload pet photo imageView image
		if (null != selectedPhotoBitmap) {
			((ImageView) findViewById(R.id.upload_pet_photo_imageView))
					.setImageBitmap(selectedPhotoBitmap);
		}

		// get pet photo album description editText
		EditText _photoAlbumDescriptionEditText = (EditText) findViewById(R.id.pet_photoAlbum_description_editText);

		// check photo album title
		if (null == photoAlbumTitle) {
			// set its editable
			_photoAlbumDescriptionEditText.setEnabled(true);
		} else {
			_photoAlbumDescriptionEditText.setText(photoAlbumTitle);
		}
	}

	// process exception
	private void processException() {
		// show login failed toast
		Toast.makeText(UploadPetPhotoActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// upload pet photo to pet photo album
	private void uploadPetPhoto2Album() {
		// check pet album id
		if (null != photoAlbumId) {
			// send upload pet photo post http request
			// generate upload new photo
			// request param
			Map<String, Object> _uploadNewPhotoParam = new HashMap<String, Object>();
			// set pet info avatar
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			selectedPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
			_uploadNewPhotoParam.put("photo_file", baos.toByteArray());
			_uploadNewPhotoParam.put("galleryid", photoAlbumId.toString());
			_uploadNewPhotoParam.put("username", UserManager.getInstance()
					.getUser().getName());
			_uploadNewPhotoParam.put("name", "");
			_uploadNewPhotoParam.put("type", "");
			// get and photo description
			String _photoDescription = ((EditText) findViewById(R.id.upload_pet_photo_description_editText))
					.getText().toString();
			if (null != _photoDescription
					&& !"".equalsIgnoreCase(_photoDescription)) {
				_uploadNewPhotoParam.put("descritpion", _photoDescription);
			}

			// send create new photo album post http request
			HttpUtils.postRequest(getResources().getString(R.string.server_url)
					+ getResources().getString(R.string.uploadPhoto_url),
					PostRequestFormat.MULTIPARTFORMDATA, _uploadNewPhotoParam,
					null, HttpRequestType.ASYNCHRONOUS,
					new UploadNewPhotoHttpRequestListener());
		}
	}

	// close asynchronous http request process dialog
	private void closeAsyncHttpReqProgressDialog() {
		// check and dismiss asynchronous http request process dialog
		if (null != _mAsyncHttpReqProgressDialog) {
			_mAsyncHttpReqProgressDialog.dismiss();
		}
	}

	// inner class
	// upload pet photo bar button item on click listener
	class UploadPetPhotoBarBtnItemOnClickListener implements OnClickListener {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onClick(View v) {
			// show upload pet photoprocess dialog
			_mAsyncHttpReqProgressDialog = ProgressDialog
					.show(UploadPetPhotoActivity.this,
							null,
							getString(R.string.asyncHttpRequest_progressDialog_message),
							true);

			// check pet photo album title
			if (null == photoAlbumTitle) {
				// get photo album description
				String _photoAlbumDescription = ((EditText) findViewById(R.id.pet_photoAlbum_description_editText))
						.getText().toString();

				// check photo album description
				if (null == _photoAlbumDescription
						|| "".equalsIgnoreCase(_photoAlbumDescription)) {
					_photoAlbumDescription = IPCUserExtension.getUserPetInfo(
							UserManager.getInstance().getUser()).getNickname()
							+ "的相册创建于"
							+ new SimpleDateFormat("MM-dd HH:mm")
									.format(new Date());
				}

				// create new photo album
				// generate create new photo album
				// request param
				Map<String, String> _createNewPhotoAlbumParam = new HashMap<String, String>();
				_createNewPhotoAlbumParam.put("title", _photoAlbumDescription);

				// send create new photo album post http request
				HttpUtils.postSignatureRequest(
						getResources().getString(R.string.server_url)
								+ getResources().getString(
										R.string.createAlbum_url),
						PostRequestFormat.URLENCODED,
						_createNewPhotoAlbumParam, null,
						HttpRequestType.ASYNCHRONOUS,
						new CreateNewPhotoAlbumHttpRequestListener());
			} else {
				// upload new pet photo to album
				uploadPetPhoto2Album();
			}
		}
	}

	// create new photo album http request listener
	class CreateNewPhotoAlbumHttpRequestListener extends OnHttpRequestListener {

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
					// get new created photo album id
					photoAlbumId = JSONUtils.getLongFromJSONObject(
							_respJsonData, "id");

					// upload pet photo to album
					uploadPetPhoto2Album();
					break;

				case 1:
					// close account login process dialog
					closeAsyncHttpReqProgressDialog();

					Log.e(LOG_TAG, "create new photo album failed");

					// show get user all pets info failed toast
					Toast.makeText(UploadPetPhotoActivity.this, "创建相册失败",
							Toast.LENGTH_LONG).show();
					break;

				default:
					// close account login process dialog
					closeAsyncHttpReqProgressDialog();

					Log.e(LOG_TAG,
							"create new photo album failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				// close account login process dialog
				closeAsyncHttpReqProgressDialog();

				Log.e(LOG_TAG,
						"create new photo album failed, bg_server return result is null");

				processException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// close account login process dialog
			closeAsyncHttpReqProgressDialog();

			Log.e(LOG_TAG,
					"create new photo album failed, send create new photo album post request failed");

			// show get user all pets info failed toast
			Toast.makeText(UploadPetPhotoActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

	// upload new pet photo http request listener
	class UploadNewPhotoHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// close account login process dialog
			closeAsyncHttpReqProgressDialog();

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
					// check photo album title and pop upload pet photo activity
					if (null == photoAlbumTitle) {
						popActivity();
					} else {
						popActivityWithResult(RESULT_OK, null);
					}
					break;

				case 1:
				case 2:
				case 3:
				case 5:
					Log.e(LOG_TAG, "upload new photo failed");

					// show get user all pets info failed toast
					Toast.makeText(UploadPetPhotoActivity.this, "上传照片失败",
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"upload new photo failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"upload new photo failed, bg_server return result is null");

				processException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// close account login process dialog
			closeAsyncHttpReqProgressDialog();

			Log.e(LOG_TAG,
					"upload new photo failed, send upload new photo post request failed");

			// show get user all pets info failed toast
			Toast.makeText(UploadPetPhotoActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

}
