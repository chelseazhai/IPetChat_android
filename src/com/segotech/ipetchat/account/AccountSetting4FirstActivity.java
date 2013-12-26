package com.segotech.ipetchat.account;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.richitec.commontoolkit.activityextension.NavigationActivity;
import com.richitec.commontoolkit.user.UserBean;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.DataStorageUtils;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.user.IPCUserExtension.ComUserLocalStorageAttributes;
import com.segotech.ipetchat.tab7tabcontent.IPetChatTabActivity;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class AccountSetting4FirstActivity extends NavigationActivity {

	private static final String LOG_TAG = AccountSetting4FirstActivity.class
			.getCanonicalName();

	// account setting request code
	private static final int ACCOUNT_REGISTER_REQCODE = 100;
	private static final int ACCOUNT_LOGIN_REQCODE = 101;

	// qq connect oAuth
	private Tencent _mQQConnectOAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_setting4first_activity_layout);

		// set account register and login button on click listener
		((Button) findViewById(R.id.as4f_accountRegister_button))
				.setOnClickListener(new AccountRegisterBtnOnClickListener());
		((Button) findViewById(R.id.as4f_accountLogin_button))
				.setOnClickListener(new AccountLoginBtnOnClickListener());

		// third party oAuth login
		// set sina weibo oAuth login button on click listener
		((Button) findViewById(R.id.as4f_sinaWeiboOAuthLogin_button))
				.setOnClickListener(new SinaWeiboOAuthLoginBtnOnClickListener());

		// set qq connect oAuth login button on click listener
		((Button) findViewById(R.id.as4f_qqConnectOAuthLogin_button))
				.setOnClickListener(new QQConnectOAuthLoginBtnOnClickListener());
	}

	@Override
	protected boolean hideNavigationBarWhenOnCreated() {
		// hide navigation bar when its on created
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// check result code
		switch (resultCode) {
		case RESULT_OK:
			// check request code
			switch (requestCode) {
			case ACCOUNT_REGISTER_REQCODE:
				// go to account login activity
				pushActivityForResult(AccountLoginActivity.class,
						ACCOUNT_LOGIN_REQCODE);
				break;

			case ACCOUNT_LOGIN_REQCODE:
				// finish account setting activity and go to iPetChat tab
				// activity
				finish();
				startActivity(new Intent(this, IPetChatTabActivity.class));
				break;
			}
			break;

		default:
			// nothing to do
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);

		// check qq connect oAuth
		if (null != _mQQConnectOAuth) {
			// _mQQConnectOAuth.
		}
	}

	// process third party oAuth login exception
	private void processThirdPartyOAuthLoginException() {
		// show third party oAuth login failed toast
		Toast.makeText(AccountSetting4FirstActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// account register button on click listener
	class AccountRegisterBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// register an new account, go to account register activity
			pushActivityForResult(AccountRegisterActivity.class,
					ACCOUNT_REGISTER_REQCODE);
		}

	}

	// account login button on click listener
	class AccountLoginBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// account login, go to account login activity
			pushActivityForResult(AccountLoginActivity.class,
					ACCOUNT_LOGIN_REQCODE);
		}

	}

	// sina weibo oAuth login button on click listener
	class SinaWeiboOAuthLoginBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// new sina weibo oAuth
			WeiboAuth _sinaWeiboOAuth = new WeiboAuth(
					AccountSetting4FirstActivity.this,
					getResources()
							.getString(
									R.string.thirdPartyOAuthLogin_sinaWeiboOAuth_appKey),
					getResources()
							.getString(
									R.string.thirdPartyOAuthLogin_sinaWeiboOAuth_redirectUrl),
					getResources().getString(
							R.string.thirdPartyOAuthLogin_sinaWeiboOAuth_scope));

			// web oAuth
			_sinaWeiboOAuth.anthorize(new SinaWeiboOAuthListener());
		}

		// inner class
		// sina weibo oAuth listener
		class SinaWeiboOAuthListener implements WeiboAuthListener {

			@Override
			public void onComplete(Bundle extraValues) {
				// get token from extra values
				Oauth2AccessToken _accessToken = Oauth2AccessToken
						.parseAccessToken(extraValues);
				if (_accessToken.isSessionValid()) {
					// thirdParty oAuth login
					// generate thirdParty oAuth login post request param
					Map<String, String> _thirdPartyOAuthLoginParam = new HashMap<String, String>();
					_thirdPartyOAuthLoginParam
							.put(getResources()
									.getString(
											R.string.rbgServer_thirdPartyOAuthLoginReqParam_identifier),
									_accessToken.getUid());

					// send thirdParty oAuth login post http request
					HttpUtils
							.postRequest(
									getResources().getString(
											R.string.server_url)
											+ getResources()
													.getString(
															R.string.thirdParty_oAuth_login_url),
									PostRequestFormat.URLENCODED,
									_thirdPartyOAuthLoginParam,
									null,
									HttpRequestType.ASYNCHRONOUS,
									new ThirdPartyOAuthLoginHttpRequestListener());
				} else {
					// 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
					String code = extraValues.getString("code");
					String message = "Sina weibo auth failed";
					if (!TextUtils.isEmpty(code)) {
						message = message + "\nObtained the code: " + code;
					}

					Toast.makeText(AccountSetting4FirstActivity.this, message,
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onCancel() {
				// nothing to do
			}

			@Override
			public void onWeiboException(WeiboException e) {
				Log.e(LOG_TAG, "Sina weibo oAuth error, exception message = "
						+ e.getMessage());

				Toast.makeText(AccountSetting4FirstActivity.this,
						"Sina weibo oAuth exception : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}

		}

	}

	// qq connect oAuth login button on click listener
	class QQConnectOAuthLoginBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// new qq connect oAuth
			_mQQConnectOAuth = Tencent
					.createInstance(
							getResources()
									.getString(
											R.string.thirdPartyOAuthLogin_qqConnectOAuth_appId),
							AccountSetting4FirstActivity.this
									.getApplicationContext());

			// oAuth
			if (!_mQQConnectOAuth.isSessionValid()) {
				_mQQConnectOAuth
						.login(AccountSetting4FirstActivity.this,
								getResources()
										.getString(
												R.string.thirdPartyOAuthLogin_qqConnectOAuth_scope),
								new QQConnectOAuthListener());
			}
		}

		// inner class
		// qq connect oAuth listener
		class QQConnectOAuthListener implements IUiListener {

			@Override
			public void onComplete(JSONObject responseJsonObject) {
				Log.d(LOG_TAG, "qq connect oAuth response json object = "
						+ responseJsonObject);

				// get and check result from response json object
				Integer _result = JSONUtils
						.getIntegerFromJSONObject(
								responseJsonObject,
								getResources()
										.getString(
												R.string.thirdPartyOAuthLogin_qqConnectOAuth_respJson_result));
				if (0 == _result) {
					// thirdParty oAuth login
					// generate thirdParty oAuth login post request param
					Map<String, String> _thirdPartyOAuthLoginParam = new HashMap<String, String>();
					_thirdPartyOAuthLoginParam
							.put(getResources()
									.getString(
											R.string.rbgServer_thirdPartyOAuthLoginReqParam_identifier),
									JSONUtils
											.getStringFromJSONObject(
													responseJsonObject,
													getResources()
															.getString(
																	R.string.thirdPartyOAuthLogin_qqConnectOAuth_respJson_openId)));

					// send thirdParty oAuth login post http request
					HttpUtils
							.postRequest(
									getResources().getString(
											R.string.server_url)
											+ getResources()
													.getString(
															R.string.thirdParty_oAuth_login_url),
									PostRequestFormat.URLENCODED,
									_thirdPartyOAuthLoginParam,
									null,
									HttpRequestType.ASYNCHRONOUS,
									new ThirdPartyOAuthLoginHttpRequestListener());
				} else {
					// 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
					String message = "QQ connect auth failed,";
					if (!TextUtils
							.isEmpty(JSONUtils
									.getStringFromJSONObject(
											responseJsonObject,
											getResources()
													.getString(
															R.string.thirdPartyOAuthLogin_qqConnectOAuth_respJson_message)))) {
						message = message
								+ JSONUtils
										.getStringFromJSONObject(
												responseJsonObject,
												getResources()
														.getString(
																R.string.thirdPartyOAuthLogin_qqConnectOAuth_respJson_message))
								+ "\nObtained the code: " + _result;
					}

					Toast.makeText(AccountSetting4FirstActivity.this, message,
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onCancel() {
				// nothing to do
			}

			@Override
			public void onError(UiError e) {
				Log.e(LOG_TAG, "QQ connect oAuth error, error code = "
						+ e.errorCode + ", message = " + e.errorMessage
						+ " and detail info = " + e.errorDetail);

				Toast.makeText(AccountSetting4FirstActivity.this,
						"QQ connect oAuth exception : " + e.errorMessage,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	// third party oAuth login http request listener
	class ThirdPartyOAuthLoginHttpRequestListener extends OnHttpRequestListener {

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
					// get http response entity string json object username and
					// userkey
					String _thirdPartyOAuthLoginReqRespUserName = JSONUtils
							.getStringFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_thirdPartyOAuthLoginReqResp_userName));
					String _thirdPartyOAuthLoginReqRespUserKey = JSONUtils
							.getStringFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_al_confirmLoginReqResp_userKey));

					// generate thirdParty oAuth login user bean with username
					// and userkey and add it to user manager
					UserManager.getInstance().setUser(
							new UserBean(_thirdPartyOAuthLoginReqRespUserName,
									null, _thirdPartyOAuthLoginReqRespUserKey));

					// add thirdParty oAuth login username and userkey to local
					// data storage
					DataStorageUtils
							.putObject(
									ComUserLocalStorageAttributes.LOGIN_USERNAME
											.name(),
									_thirdPartyOAuthLoginReqRespUserName);
					DataStorageUtils.putObject(
							ComUserLocalStorageAttributes.LOGIN_USERKEY.name(),
							_thirdPartyOAuthLoginReqRespUserKey);

					// finish account setting activity and go to iPetChat tab
					// activity
					finish();
					startActivity(new Intent(AccountSetting4FirstActivity.this,
							IPetChatTabActivity.class));
					break;

				case 1:
				case 2:
					Log.d(LOG_TAG, "third party oAuth login failed");

					Toast.makeText(AccountSetting4FirstActivity.this,
							R.string.toast_thirdPartyOAuthLogin_loginFailed,
							Toast.LENGTH_SHORT).show();
					break;

				default:
					Log.e(LOG_TAG,
							"third party oAuth login failed, bg_server return result is unrecognized");

					processThirdPartyOAuthLoginException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"third party oAuth login failed, bg_server return result is null");

				processThirdPartyOAuthLoginException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"third party oAuth login failed, send third party oAuth login post request failed");

			processThirdPartyOAuthLoginException();
		}

	}

}
