package com.segotech.ipetchat.petcommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.settings.photo.PetPhotoAlbumBean;

public class AddConcernPetActivity extends IPetChatNavigationActivity {

	// add concern pet request code
	private static final int ADD_CONCERN_PET_REQCODE = 400;

	private static final String LOG_TAG = AddConcernPetActivity.class
			.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.add_concern_pet_activity_layout);

		// set title
		setTitle(R.string.add_concern_pet_nav_title);

		// set find account with number as right bar button item
		setRightBarButtonItem(new BarButtonItem(this,
				R.string.add_concern_pet_findAccount_button_text,
				new FindAccountWithNumberBtnOnClickListener()));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// check result code
		switch (resultCode) {
		case RESULT_OK:
			// pop add concern pet activity
			popActivity();
			break;

		default:
			// nothing to do
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// process search pets info exception
	private void processSearchPetsException() {
		// show login failed toast
		Toast.makeText(AddConcernPetActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner calss
	// find account with number button on click listener
	class FindAccountWithNumberBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get search phone number
			String _phoneNumber = ((EditText) findViewById(R.id.add_concern_pet_number_textEdit))
					.getText().toString();

			if (null == _phoneNumber || "".equalsIgnoreCase(_phoneNumber)) {
				Toast.makeText(
						AddConcernPetActivity.this,
						R.string.toast_getPhoneVerificationCode_phoneNumber_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// compare search phone number with account login name
			if (UserManager.getInstance().getUser().getName()
					.equalsIgnoreCase(_phoneNumber)) {
				Toast.makeText(AddConcernPetActivity.this, "不可以将自己添加到关注",
						Toast.LENGTH_SHORT).show();

				return;
			}

			// search pets
			// generate search pets post
			// request param
			Map<String, String> _searchPetsParam = new HashMap<String, String>();
			_searchPetsParam
					.put(getResources()
							.getString(
									R.string.rbgServer_getPhoneVerificationCodeReqParam_phone),
							_phoneNumber);

			// send search pets post http
			// request
			HttpUtils
					.postSignatureRequest(
							getResources().getString(R.string.server_url)
									+ getResources().getString(
											R.string.search_pet_url),
							PostRequestFormat.URLENCODED, _searchPetsParam,
							null, HttpRequestType.ASYNCHRONOUS,
							new SearchPetsHttpRequestListener());
		}

	}

	// search pets http request listener
	class SearchPetsHttpRequestListener extends OnHttpRequestListener {

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
					// get search pets info array
					JSONArray _searchPetsInfoArray = JSONUtils
							.getJSONArrayFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_getAllPetsReqResp_petsList));

					// check search pets info array
					if (0 != _searchPetsInfoArray.length()) {
						// process search pets info array and get the
						// header
						for (int i = 0; i < _searchPetsInfoArray.length(); i++) {
							// get the header
							if (0 == i) {
								// get the header pet json info
								JSONObject _searchPetInfoJSONObject = JSONUtils
										.getJSONObjectFromJSONArray(
												_searchPetsInfoArray, 0);

								// define pet info and pet photo album cover
								// image path list
								PetBean _petInfo = new PetBean(
										_searchPetInfoJSONObject);
								List<String> _photoAlbumCoverPaths = new ArrayList<String>();

								// get pet photo album list and init pet photo
								// album cover
								// image path array
								JSONArray _petPhotoAlbumsInfoArray = JSONUtils
										.getJSONArrayFromJSONObject(
												_searchPetInfoJSONObject,
												"galleries");
								// test by ares
								if (null != _petPhotoAlbumsInfoArray) {
									for (int j = 0; j < _petPhotoAlbumsInfoArray
											.length(); j++) {
										_photoAlbumCoverPaths
												.add(new PetPhotoAlbumBean(
														JSONUtils
																.getJSONObjectFromJSONArray(
																		_petPhotoAlbumsInfoArray,
																		j))
														.getCoverUrl());
									}
								}

								// define extra data
								Map<String, Object> _extraData = new HashMap<String, Object>();

								// check pet info and set extra data
								if (null != _petInfo) {
									_extraData
											.put(PetDetailInfoActivity.PET_DETAILINFO_PET_KEY,
													_petInfo);
									_extraData
											.put(PetDetailInfoActivity.PET_DETAILINFO_CONCERN_KEY,
													Boolean.valueOf(false));
									if (0 == _searchPetsInfoArray.length() - 1) {
										_extraData
												.put(PetDetailInfoActivity.PET_PHOTOALBUM_COVERIMGPATHS_KEY,
														_photoAlbumCoverPaths
																.toArray(new String[] {}));
									}
								}

								// go to pet detail info activity
								pushActivityForResult(
										PetDetailInfoActivity.class,
										_extraData, ADD_CONCERN_PET_REQCODE);

								// break immediately
								break;
							}
						}
					} else {
						Log.w(LOG_TAG, "there is no pet info");

						// show there is no pet info toast
						Toast.makeText(AddConcernPetActivity.this,
								R.string.toast_no_pet, Toast.LENGTH_LONG)
								.show();
					}
					break;

				default:
					Log.e(LOG_TAG,
							"search pets info failed, bg_server return result is unrecognized");

					processSearchPetsException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"search pets info failed, bg_server return result is null");

				processSearchPetsException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"search pets info failed, send search pets info post request failed");

			// show get user all pets info failed toast
			Toast.makeText(AddConcernPetActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

}
