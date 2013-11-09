package com.segotech.ipetchat.account;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

		// auto complete login user name
		((EditText) findViewById(R.id.login_name_editText))
				.setText(DataStorageUtils
						.getString(ComUserLocalStorageAttributes.LOGIN_USERNAME
								.name()));

		// set forget user login password button on click listener
		((Button) findViewById(R.id.forget_pwd_btn))
				.setOnClickListener(new ForgetPwdBtnOnClickListener());

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
	// forget user login password button on click listener
	class ForgetPwdBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// go to account retrieve password activity
			pushActivity(AccountRetrievePwdActivity.class);
		}

	}

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
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object result and userKey
			String _result = JSONUtils.getStringFromJSONObject(_respJsonData,
					getResources()
							.getString(R.string.rbgServer_req_resp_result));

			// check result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get login user name and response userKey
					String _loginName = ((EditText) findViewById(R.id.login_name_editText))
							.getText().toString();
					String _responseLoginUserKey = JSONUtils
							.getStringFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_loginReq_resp_userkey));

					// save user bean and add to user manager
					UserManager.getInstance().setUser(
							new UserBean(_loginName, null,
									_responseLoginUserKey));

					// add to data storage
					DataStorageUtils
							.putObject(
									ComUserLocalStorageAttributes.LOGIN_USERNAME
											.name(), _loginName);
					DataStorageUtils.putObject(
							ComUserLocalStorageAttributes.LOGIN_USERKEY.name(),
							_responseLoginUserKey);

					// pop account login activity and go to iPetChat tab
					// activity
					popActivityWithResult(RESULT_OK, null);
					break;

				case 1:
					Log.d(LOG_TAG, "login failed, user not existed");

					Toast.makeText(AccountLoginActivity.this,
							R.string.toast_login_user_notExisted,
							Toast.LENGTH_SHORT).show();
					break;

				case 2:
					Log.d(LOG_TAG, "login failed, user login password is wrong");

					Toast.makeText(AccountLoginActivity.this,
							R.string.toast_login_user_loginPwd_wrong,
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
