package com.segotech.ipetchat.tab7tabcontent;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Intent;
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

	// tab widget item content array
	private final int[][] TAB_WIDGETITEM_CONTENTS = new int[][] {
			{ R.string.home_tab_title, R.drawable.home_tab_icon },
			{ R.string.sports_health_tab7nav_title,
					R.drawable.sportshealth_tab_icon },
			{ R.string.community_tab7nav_title, R.drawable.community_tab_icon },
			{ R.string.settings_tab7nav_title, R.drawable.settings_tab_icon } };

	// tab host
	private TabHost _mTabHost;

	// my pet info
	private PetBean _mPetInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.ipet_chat_tab_activity_layout);

		// get tabHost
		_mTabHost = getTabHost();

		// define tabSpec
		TabSpec _tabSpec;

		// set tab indicator and content
		// home
		_tabSpec = _mTabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[0][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[0][0],
								TAB_WIDGETITEM_CONTENTS[0][1]))
				.setContent(
						new Intent().setClass(this,
								HomeTabContentActivity.class));
		_mTabHost.addTab(_tabSpec);

		// sports and health
		_tabSpec = _mTabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[1][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[1][0],
								TAB_WIDGETITEM_CONTENTS[1][1]))
				.setContent(
						new Intent().setClass(this,
								SportsHealthTabContentActivity.class));
		_mTabHost.addTab(_tabSpec);

		// community
		_tabSpec = _mTabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[2][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[2][0],
								TAB_WIDGETITEM_CONTENTS[2][1]))
				.setContent(
						new Intent().setClass(this,
								CommunityTabContentActivity.class));
		_mTabHost.addTab(_tabSpec);

		// settings
		_tabSpec = _mTabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[3][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[3][0],
								TAB_WIDGETITEM_CONTENTS[3][1]))
				.setContent(
						new Intent().setClass(this,
								SettingsTabContentActivity.class));
		_mTabHost.addTab(_tabSpec);

		// get user all pets info
		// send get user all pets info post http request
		HttpUtils.postSignatureRequest(
				getResources().getString(R.string.server_url)
						+ getResources().getString(R.string.get_allPets_url),
				PostRequestFormat.URLENCODED, null, null,
				HttpRequestType.ASYNCHRONOUS,
				new GetAllPetsInfoHttpRequestListener());
	}

	// process get user all pets info exception
	private void processGetPetsException() {
		// show login failed toast
		Toast.makeText(IPetChatTabActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// get user all pets info http request listener
	class GetAllPetsInfoHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object result and user key
			String _result = JSONUtils.getStringFromJSONObject(_respJsonData,
					getResources()
							.getString(R.string.rbgServer_req_resp_result));

			// check result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get user all pets info array
					JSONArray _petsInfoArray = JSONUtils
							.getJSONArrayFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_getAllPetsReq_resp_list));

					// check user all pets info array
					if (0 != _petsInfoArray.length()) {
						// process user all pets info array and get the header
						for (int i = 0; i < _petsInfoArray.length(); i++) {
							// get the header
							JSONObject _petInfoJSONObject = JSONUtils
									.getJSONObjectFromJSONArray(_petsInfoArray,
											0);

							// check my pet info
							if (null == _mPetInfo) {
								// init my pet info
								_mPetInfo = new PetBean();
							}

							// check and set pet info bean attributes
							// id
							_mPetInfo
									.setId(JSONUtils
											.getLongFromJSONObject(
													_petInfoJSONObject,
													getResources()
															.getString(
																	R.string.rbgServer_getAllPetsReq_resp_pet_id)));

							// avatar url
							_mPetInfo
									.setAvatarUrl(JSONUtils
											.getStringFromJSONObject(
													_petInfoJSONObject,
													getResources()
															.getString(
																	R.string.rbgServer_getAllPetsReq_resp_pet_avatarUrl)));

							// nickname
							_mPetInfo
									.setNickname(JSONUtils
											.getStringFromJSONObject(
													_petInfoJSONObject,
													getResources()
															.getString(
																	R.string.rbgServer_getAllPetsReq_resp_pet_nickname)));

							// sex
							// get and check sex value
							Integer _sexValue = JSONUtils
									.getIntegerFromJSONObject(
											_petInfoJSONObject,
											getResources()
													.getString(
															R.string.rbgServer_getAllPetsReq_resp_pet_sex));
							if (null != _sexValue) {
								_mPetInfo.setSex(PetSex.getSex(_sexValue));
							}

							// breed
							// get and check breed value
							Integer _breedValue = JSONUtils
									.getIntegerFromJSONObject(
											_petInfoJSONObject,
											getResources()
													.getString(
															R.string.rbgServer_getAllPetsReq_resp_pet_breed));
							if (null != _breedValue) {
								_mPetInfo.setBreed(PetBreed
										.getBreed(_breedValue));
							}

							// age
							_mPetInfo
									.setAge(JSONUtils
											.getIntegerFromJSONObject(
													_petInfoJSONObject,
													getResources()
															.getString(
																	R.string.rbgServer_getAllPetsReq_resp_pet_age)));

							// height
							_mPetInfo
									.setHeight(JSONUtils
											.getDoubleFromJSONObject(
													_petInfoJSONObject,
													getResources()
															.getString(
																	R.string.rbgServer_getAllPetsReq_resp_pet_height)));

							// weight
							_mPetInfo
									.setWeight(JSONUtils
											.getDoubleFromJSONObject(
													_petInfoJSONObject,
													getResources()
															.getString(
																	R.string.rbgServer_getAllPetsReq_resp_pet_weight)));

							// district
							_mPetInfo
									.setDistrict(JSONUtils
											.getStringFromJSONObject(
													_petInfoJSONObject,
													getResources()
															.getString(
																	R.string.rbgServer_getAllPetsReq_resp_pet_district)));

							// place where used to go
							_mPetInfo
									.setPlaceUsed2Go(JSONUtils
											.getStringFromJSONObject(
													_petInfoJSONObject,
													getResources()
															.getString(
																	R.string.rbgServer_getAllPetsReq_resp_pet_placeUsed2Go)));

							Log.d(LOG_TAG, "Got my pet info = " + _mPetInfo);

							// set got pet info as extension of user
							IPCUserExtension.setUserPetInfo(UserManager
									.getInstance().getUser(), _mPetInfo);

							// test by ares
							// set current tab
							_mTabHost.setCurrentTab(1);
							_mTabHost.setCurrentTab(0);
						}
					} else {
						Log.w(LOG_TAG,
								"There is no pet info, please insert a pet");

						// show there is no pet info toast
						Toast.makeText(IPetChatTabActivity.this,
								R.string.toast_no_pet, Toast.LENGTH_LONG)
								.show();
					}
					break;

				default:
					Log.e(LOG_TAG,
							"get user all pets info failed, bg_server return result is unrecognized");

					processGetPetsException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get user all pets info failed, bg_server return result is null");

				processGetPetsException();
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
