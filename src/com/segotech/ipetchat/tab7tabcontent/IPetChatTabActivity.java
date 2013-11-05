package com.segotech.ipetchat.tab7tabcontent;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.CTTabSpecIndicator;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.pet.PetBreed;
import com.segotech.ipetchat.account.pet.PetSex;
import com.segotech.ipetchat.account.user.IPCUserExtension;

@SuppressWarnings("deprecation")
public class IPetChatTabActivity extends TabActivity {

	private static final String LOG_TAG = IPetChatTabActivity.class
			.getCanonicalName();

	// my pet info
	private PetBean _mPetInfo;

	// tab widget item content array
	private final int[][] TAB_WIDGETITEM_CONTENTS = new int[][] {
			{ R.string.home_tab_title, R.drawable.home_tab_icon },
			{ R.string.sports_health_tab7nav_title,
					R.drawable.sportshealth_tab_icon },
			{ R.string.community_tab7nav_title, R.drawable.community_tab_icon },
			{ R.string.settings_tab7nav_title, R.drawable.settings_tab_icon } };

	// current tab index, default is home tab
	private int _mCurrentTabIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.ipet_chat_tab_activity_layout);

		// get tabHost
		TabHost _tabHost = getTabHost();

		// define tabSpec
		TabSpec _tabSpec;

		// set tab indicator and content
		// home
		_tabSpec = _tabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[0][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[0][0],
								TAB_WIDGETITEM_CONTENTS[0][1]))
				.setContent(
						new Intent().setClass(this,
								HomeTabContentActivity.class));
		_tabHost.addTab(_tabSpec);

		// sports and health
		_tabSpec = _tabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[1][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[1][0],
								TAB_WIDGETITEM_CONTENTS[1][1]))
				.setContent(
						new Intent().setClass(this,
								SportsHealthTabContentActivity.class));
		_tabHost.addTab(_tabSpec);

		// community
		_tabSpec = _tabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[2][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[2][0],
								TAB_WIDGETITEM_CONTENTS[2][1]))
				.setContent(
						new Intent().setClass(this,
								CommunityTabContentActivity.class));
		_tabHost.addTab(_tabSpec);

		// settings
		_tabSpec = _tabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[3][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[3][0],
								TAB_WIDGETITEM_CONTENTS[3][1]))
				.setContent(
						new Intent().setClass(this,
								SettingsTabContentActivity.class));
		_tabHost.addTab(_tabSpec);

		// set current tab
		_tabHost.setCurrentTab(_mCurrentTabIndex);

		// test by ares
		Log.d(LOG_TAG,
				"" + _tabHost.getCurrentTabView() + " and "
						+ _tabHost.getCurrentView() + " and "
						+ _tabHost.getTabContentView());
	}

	@Override
	protected void onResume() {
		// get user all pets info
		// send get user all pets info post http request
		HttpUtils.postSignatureRequest(
				getResources().getString(R.string.server_url)
						+ getResources()
								.getString(R.string.get_allPetsInfo_url),
				PostRequestFormat.URLENCODED, null, null,
				HttpRequestType.ASYNCHRONOUS,
				new GetAllPetsInfoHttpRequestListener());

		super.onResume();
	}

	// inner class
	// get user all pets info http request listener
	class GetAllPetsInfoHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get the number and list from http response json data
			Integer _petsNumber = JSONUtils.getIntegerFromJSONObject(
					_respJsonData,
					getResources().getString(
							R.string.rbgServer_getMyPetsReq_resp_number));

			JSONArray _petsInfoArray = JSONUtils.getJSONArrayFromJSONObject(
					_respJsonData,
					getResources().getString(
							R.string.rbgServer_getMyPetsReq_resp_list));

			Log.d(LOG_TAG, "my pets info array count = " + _petsNumber
					+ " and array = " + _petsInfoArray);

			// check and process pets info array
			if (0 != _petsNumber) {
				for (int i = 0; i < _petsNumber; i++) {
					// get first only currently
					if (0 == i) {
						// get pet info JSONObject
						JSONObject _petInfoJSONObject = JSONUtils
								.getJSONObjectFromJSONArray(_petsInfoArray, i);

						// check pet info
						if (null == _mPetInfo) {
							// init pet info
							_mPetInfo = new PetBean();
						}

						// set pet info bean attributes
						// id
						_mPetInfo
								.setId(JSONUtils
										.getLongFromJSONObject(
												_petInfoJSONObject,
												getResources()
														.getString(
																R.string.rbgServer_getMyPetsReq_resp_id)));
						// avatar
						ByteArrayOutputStream _baos = new ByteArrayOutputStream();
						BitmapFactory.decodeResource(getResources(),
								R.drawable.img_demo_pet).compress(
								Bitmap.CompressFormat.PNG, 100, _baos);
						byte[] _avatarByteArray = _baos.toByteArray();
						_mPetInfo.setAvatar(_avatarByteArray);
						// nickname
						_mPetInfo
								.setNickname(JSONUtils
										.getStringFromJSONObject(
												_petInfoJSONObject,
												getResources()
														.getString(
																R.string.rbgServer_getMyPetsReq_resp_nickname)));
						// sex
						_mPetInfo
								.setSex(PetSex.getSex(JSONUtils
										.getIntegerFromJSONObject(
												_petInfoJSONObject,
												getResources()
														.getString(
																R.string.rbgServer_getMyPetsReq_resp_sex))));
						// breed
						_mPetInfo
								.setBreed(PetBreed.getBreed(JSONUtils
										.getIntegerFromJSONObject(
												_petInfoJSONObject,
												getResources()
														.getString(
																R.string.rbgServer_getMyPetsReq_resp_breed))));
						// age
						_mPetInfo
								.setAge(JSONUtils
										.getIntegerFromJSONObject(
												_petInfoJSONObject,
												getResources()
														.getString(
																R.string.rbgServer_getMyPetsReq_resp_age)));
						// height
						_mPetInfo
								.setHeight(JSONUtils
										.getDoubleFromJSONObject(
												_petInfoJSONObject,
												getResources()
														.getString(
																R.string.rbgServer_getMyPetsReq_resp_height))
										.floatValue());
						// weight
						_mPetInfo
								.setWeight(JSONUtils
										.getDoubleFromJSONObject(
												_petInfoJSONObject,
												getResources()
														.getString(
																R.string.rbgServer_getMyPetsReq_resp_weight))
										.floatValue());
						// district
						_mPetInfo
								.setDistrict(JSONUtils
										.getStringFromJSONObject(
												_petInfoJSONObject,
												getResources()
														.getString(
																R.string.rbgServer_getMyPetsReq_resp_district)));
						// place where used to go
						_mPetInfo
								.setPlaceUsed2Go(JSONUtils
										.getStringFromJSONObject(
												_petInfoJSONObject,
												getResources()
														.getString(
																R.string.rbgServer_getMyPetsReq_resp_placeUsed2Go)));

						Log.d(LOG_TAG, "get pet info = " + _mPetInfo);

						// set pet info as extension of user
						IPCUserExtension.setUserPetInfo(UserManager
								.getInstance().getUser(), _mPetInfo);
						
						Log.d(LOG_TAG, "my pet info = " + IPCUserExtension.getUserPetInfo(UserManager
								.getInstance().getUser()));

						//
						//
					} else {
						// nothing to do
						break;
					}
				}
			} else {
				Log.w(LOG_TAG, "there is no user pets info");

				// show there is no pets info toast
				Toast.makeText(IPetChatTabActivity.this, R.string.toast_no_pet,
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get user all pets info failed, send get user all pets info post request failed");

			// show get user all pets info failed toast
			Toast.makeText(IPetChatTabActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

}
