package com.segotech.ipetchat.account;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.richitec.commontoolkit.user.UserBean;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.DataStorageUtils;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.richitec.commontoolkit.utils.StringUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.user.IPCUserExtension.ComUserLocalStorageAttributes;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class AccountLoginActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = AccountLoginActivity.class
			.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_login_activity_layout);

		// set title
		setTitle(R.string.account_login_nav_title);

		// auto complete login user name, password and remember login user pwd
		// toggle buton
		((EditText) findViewById(R.id.login_name_editText))
				.setText(DataStorageUtils
						.getString(ComUserLocalStorageAttributes.LOGIN_USERNAME
								.name()));
		((EditText) findViewById(R.id.login_pwd_editText))
				.setText(DataStorageUtils
						.getString(ComUserLocalStorageAttributes.LOGIN_PASSWORD
								.name()));
		((ToggleButton) findViewById(R.id.remember_pwd_toggleBtn))
				.setChecked(DataStorageUtils
						.getBoolean(ComUserLocalStorageAttributes.REMEMBER_LOGIN_PASSWORD
								.name()));

		// set user login confirm button on click listener
		((Button) findViewById(R.id.login_confirm_btn))
				.setOnClickListener(new LoginConfirmBtnOnClickListener());
	}

	// process login exception
	private void processLoginException() {
		// show login failed toast
		Toast.makeText(AccountLoginActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// user login confirm button on click listener
	class LoginConfirmBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get login user name
			String _loginUserName = ((EditText) findViewById(R.id.login_name_editText))
					.getText().toString();
			// check login user name
			if (null == _loginUserName || _loginUserName.equalsIgnoreCase("")) {
				Toast.makeText(AccountLoginActivity.this,
						R.string.toast_login_userName_null, Toast.LENGTH_SHORT)
						.show();

				return;
			}

			// get login password
			String _loginPassword = ((EditText) findViewById(R.id.login_pwd_editText))
					.getText().toString();
			// check login password
			if (null == _loginPassword || _loginPassword.equalsIgnoreCase("")) {
				Toast.makeText(AccountLoginActivity.this,
						R.string.toast_login_password_null, Toast.LENGTH_SHORT)
						.show();

				return;
			}

			// login confirm
			// generate user login post request param
			Map<String, String> _loginParam = new HashMap<String, String>();
			_loginParam.put(
					getResources().getString(R.string.rbgServer_userLoginName),
					_loginUserName);
			_loginParam.put(
					getResources().getString(R.string.rbgServer_userLoginPwd),
					StringUtils.md5(_loginPassword));

			// send user login post http request
			HttpUtils.postRequest(getResources().getString(R.string.server_url)
					+ getResources().getString(R.string.user_login_url),
					PostRequestFormat.URLENCODED, _loginParam, null,
					HttpRequestType.ASYNCHRONOUS,
					new UserLoginHttpRequestListener());
		}

	}

	// user login confirm http request listener
	class UserLoginHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string
			String _respEntityString = HttpUtils
					.getHttpResponseEntityString(response);

			// test by ares
			Log.d(LOG_TAG, "user login http request response entity string"
					+ _respEntityString);

			// get http response entity string json object result and userKey
			String _result = JSONUtils.getStringFromJSONObject(JSONUtils
					.toJSONObject(_respEntityString),
					getResources()
							.getString(R.string.rbgServer_req_resp_result));

			// check result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get login user name, password, remember login user pwd
					// flag and response userKey
					String _loginName = ((EditText) findViewById(R.id.login_name_editText))
							.getText().toString();
					String _loginPwd = ((EditText) findViewById(R.id.login_pwd_editText))
							.getText().toString();
					boolean _isRememberLoginPwd = ((ToggleButton) findViewById(R.id.remember_pwd_toggleBtn))
							.isChecked();
					String _responseLoginUserKey = JSONUtils
							.getStringFromJSONObject(
									JSONUtils.toJSONObject(_respEntityString),
									getResources()
											.getString(
													R.string.rbgServer_loginReq_resp_userkey));

					// save user bean and add to user manager
					UserManager.getInstance().setUser(
							new UserBean(_loginName, _loginPwd,
									_responseLoginUserKey));

					// add to data storage
					DataStorageUtils
							.putObject(
									ComUserLocalStorageAttributes.LOGIN_USERNAME
											.name(), _loginName);
					DataStorageUtils
							.putObject(
									ComUserLocalStorageAttributes.LOGIN_PASSWORD
											.name(),
									_isRememberLoginPwd ? _loginPwd : "");
					DataStorageUtils
							.putObject(
									ComUserLocalStorageAttributes.REMEMBER_LOGIN_PASSWORD
											.name(), _isRememberLoginPwd);
					// DataStorageUtils
					// .putObject(
					// IMeetingAppLaunchActivity.LOGIN_USERKEY_STORAGE_KEY,
					// _responseLoginUserKey);
					//
					// // check goto activity
					// if (AppAccountStatus.ESTABLISHING ==
					// _mCurrentAppAccountStatus) {
					// Log.d(LOG_TAG, "login successful");
					//
					// // check remember user login password flag
					// if (_isRememberLoginPwd) {
					// // update main activity class name from storage
					// DataStorageUtils
					// .putObject(
					// IMeetingAppLaunchActivity.MAINACTIVITY_STORAGE_KEY,
					// TalkingGroupHistoryListActivity.class
					// .getName());
					// }
					//
					// // go to talking group history list activity
					// finish();
					// startActivity(new Intent(AccountSettingActivity.this,
					// TalkingGroupHistoryListActivity.class));
					//
					// Toast.makeText(AccountSettingActivity.this,
					// R.string.toast_login_successful,
					// Toast.LENGTH_LONG).show();
					// } else {
					// Log.d(LOG_TAG, "user account reset successful");
					//
					// // set my talking group history list activity need to
					// // refresh
					// TalkingGroupHistoryListActivity.TALKINGGROUP_HISTORYLIST_NEED2REFRESH
					// = true;
					//
					// // pop to setting activity
					// popActivity();
					//
					// Toast.makeText(AccountSettingActivity.this,
					// R.string.toast_userAccount_reset_successful,
					// Toast.LENGTH_SHORT).show();
					// }
					break;

				case 1:
					Log.d(LOG_TAG,
							"login failed, user name or password is wrong");

					Toast.makeText(AccountLoginActivity.this,
							R.string.toast_login_userName6Pwd_wrong,
							Toast.LENGTH_SHORT).show();
					break;

				default:
					Log.e(LOG_TAG,
							"login failed, bg_server return result is unrecognized");

					processLoginException();
					break;
				}
			} else {
				Log.e(LOG_TAG, "login failed, bg_server return result is null");

				processLoginException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG, "login failed, send user login post request failed");

			processLoginException();
		}

	}

}
