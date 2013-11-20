package com.segotech.ipetchat.tab7tabcontent;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.CTTabSpecIndicator;
import com.richitec.commontoolkit.user.UserBean;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.utils.IPetChatUtils;

@SuppressWarnings("deprecation")
public class IPetChatTabActivity extends TabActivity {

	private static final String LOG_TAG = IPetChatTabActivity.class
			.getCanonicalName();

	// tab widget item and content class array, home, sports and health,
	// community and settings
	private final Object[][] TAB_WIDGETITEMS7CONTENTCLS = new Object[][] {
			{ R.string.home_tab_tag, R.string.home_tab_title,
					R.drawable.home_tab_icon, HomeTabContentActivity.class },
			{ R.string.sports_and_health_tab_tag,
					R.string.sports_and_health_tab7nav_title,
					R.drawable.sportshealth_tab_icon,
					SportsHealthTabContentActivity.class },
			{ R.string.community_tab_tag, R.string.community_tab7nav_title,
					R.drawable.community_tab_icon,
					CommunityTabContentActivity.class },
			{ R.string.settings_tab_tag, R.string.settings_tab7nav_title,
					R.drawable.settings_tab_icon,
					SettingsTabContentActivity.class } };

	// tab host
	private TabHost _mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.ipet_chat_tab_activity_layout);

		// get tabHost
		_mTabHost = getTabHost();

		// set tab indicator and content
		for (int i = 0; i < TAB_WIDGETITEMS7CONTENTCLS.length; i++) {
			try {
				// get tab spec tag, indicator label, icon resource and content
				// class tag
				String _tag = getResources().getString(
						(Integer) TAB_WIDGETITEMS7CONTENTCLS[i][0]);

				// label resource
				Integer _labelRes = (Integer) TAB_WIDGETITEMS7CONTENTCLS[i][1];

				// icon resource
				Integer _iconRes = (Integer) TAB_WIDGETITEMS7CONTENTCLS[i][2];

				// content class
				Class<?> _contentCls = (Class<?>) TAB_WIDGETITEMS7CONTENTCLS[i][3];

				Log.d(LOG_TAG, "tab spec tag = " + _tag
						+ ", indicator label resource = " + _labelRes
						+ ", icon resource = " + _iconRes
						+ " and content class = " + _contentCls);

				// new tab spec and add to tab host
				TabSpec _tabSpec = _mTabHost
						.newTabSpec(_tag)
						.setIndicator(
								new CTTabSpecIndicator(this, _labelRes,
										_iconRes))
						.setContent(new Intent().setClass(this, _contentCls));

				_mTabHost.addTab(_tabSpec);
			} catch (Exception e) {
				Log.e(LOG_TAG,
						"new tab spec error, exception message = "
								+ e.getMessage());

				e.printStackTrace();
			}
		}

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

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get user all pets info array
					JSONArray _petsInfoArray = JSONUtils
							.getJSONArrayFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_getAllPetsReqResp_petsList));

					// check user all pets info array
					if (0 != _petsInfoArray.length()) {
						// process user got all pets info array and get the
						// header
						for (int i = 0; i < _petsInfoArray.length(); i++) {
							// get the header
							if (0 == i) {
								// get the header pet json info
								JSONObject _petInfoJSONObject = JSONUtils
										.getJSONObjectFromJSONArray(
												_petsInfoArray, 0);

								// get the user
								UserBean _user = UserManager.getInstance()
										.getUser();

								// get my pet info
								PetBean _petInfo = IPCUserExtension
										.getUserPetInfo(_user);

								// check my pet info
								if (null == _petInfo) {
									// init my pet info
									_petInfo = new PetBean(_petInfoJSONObject);

									// set got pet info as extension of user
									IPCUserExtension.setUserPetInfo(_user,
											_petInfo);
								} else {
									// update my pet info
									_petInfo.updatePetInfo(_petInfoJSONObject);
								}

								// check pet avatar
								if (null != _petInfo.getAvatarUrl()) {
									// get pet avater bitmap
									Bitmap _petAvatarBitmap = IPetChatUtils
											.getHttpBitmap(getResources()
													.getString(
															R.string.server_url)
													+ getResources().getString(
															R.string.img_url)
													+ _petInfo.getAvatarUrl());

									Log.d(LOG_TAG,
											"pet avatar url = "
													+ getResources()
															.getString(
																	R.string.server_url)
													+ getResources().getString(
															R.string.img_url)
													+ _petInfo.getAvatarUrl()
													+ " and _petAvatarBitmap = "
													+ _petAvatarBitmap);

									try {
										_petInfo.setAvatar(IPetChatUtils
												.getImage(getResources()
														.getString(
																R.string.server_url)
														+ getResources()
																.getString(
																		R.string.img_url)
														+ _petInfo
																.getAvatarUrl()));
									} catch (NotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									// ByteArrayOutputStream baos = new
									// ByteArrayOutputStream();
									// _petAvatarBitmap.compress(
									// Bitmap.CompressFormat.PNG, 100,
									// baos);
									//
									// // set pet avatar
									// _petInfo.setAvatar(baos.toByteArray());
								}

								Log.d(LOG_TAG, "Got my pet info = " + _petInfo);

								// test by ares
								// set current tab, work around
								_mTabHost.setCurrentTab(1);
								_mTabHost.setCurrentTab(0);

								// break immediately
								break;
							}
						}
					} else {
						Log.w(LOG_TAG,
								"there is no pet info, please insert a pet");

						// show there is no pet info toast
						Toast.makeText(IPetChatTabActivity.this,
								R.string.toast_gap_no_pet, Toast.LENGTH_LONG)
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
