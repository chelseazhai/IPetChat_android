package com.segotech.ipetchat.account;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.app.ProgressDialog;
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

	// account login username(phone number)
	private String _mAccountLoginUsername;

	// asynchronous http request progress dialog
	private ProgressDialog _mAsyncHttpReqProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_login_activity_layout);

		// set title
		setTitle(R.string.account_login_nav_title);

		// auto complete login username(phone number)
		((EditText) findViewById(R.id.al_username_editText))
				.setText(UserManager.getInstance().getUser().getName());

		// set forget account login password button on click listener
		((Button) findViewById(R.id.al_forgetLoginPwd_button))
				.setOnClickListener(new ForgetLoginPwdBtnOnClickListener());

		// set account confirm login button on click listener
		((Button) findViewById(R.id.al_confirmLogin_button))
				.setOnClickListener(new ConfirmLoginBtnOnClickListener());
	}

	// process login exception
	private void processLoginException() {
		// show login failed toast
		Toast.makeText(AccountLoginActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// close asynchronous http request process dialog
	private void closeAsyncHttpReqProgressDialog() {
		// check and dismiss asynchronous http request process dialog
		if (null != _mAsyncHttpReqProgressDialog) {
			_mAsyncHttpReqProgressDialog.dismiss();
		}
	}

	// inner class
	// forget account login password button on click listener
	class ForgetLoginPwdBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// go to account retrieve password activity
			pushActivity(AccountRetrievePwdActivity.class);
		}

	}

	// account login confirm button on click listener
	class ConfirmLoginBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account login username(phone number)
			_mAccountLoginUsername = ((EditText) findViewById(R.id.al_username_editText))
					.getText().toString();

			// check account login username(phone number)
			if (null == _mAccountLoginUsername
					|| "".equalsIgnoreCase(_mAccountLoginUsername)) {
				Toast.makeText(AccountLoginActivity.this,
						R.string.toast_al_username_null, Toast.LENGTH_SHORT)
						.show();

				return;
			}

			// get account login password
			String _accountLoginPwd = ((EditText) findViewById(R.id.al_loginPwd_editText))
					.getText().toString();

			// check account login password
			if (null == _accountLoginPwd
					|| "".equalsIgnoreCase(_accountLoginPwd)) {
				Toast.makeText(AccountLoginActivity.this,
						R.string.toast_al_loginPwd_null, Toast.LENGTH_SHORT)
						.show();

				return;
			}

			// show account login process dialog
			_mAsyncHttpReqProgressDialog = ProgressDialog
					.show(AccountLoginActivity.this,
							null,
							getString(R.string.asyncHttpRequest_progressDialog_message),
							true);

			// account confirm login
			// generate account confirm login post request param
			Map<String, String> _accountConfirmLoginParam = new HashMap<String, String>();
			_accountConfirmLoginParam
					.put(getResources()
							.getString(
									R.string.rbgServer_al_confirmLoginReqParam_username),
							_mAccountLoginUsername);
			_accountConfirmLoginParam
					.put(getResources()
							.getString(
									R.string.rbgServer_al_confirmLoginReqParam_loginPwd),
							StringUtils.md5(_accountLoginPwd));

			// send account confirm login post http request
			HttpUtils.postRequest(getResources().getString(R.string.server_url)
					+ getResources().getString(R.string.account_login_url),
					PostRequestFormat.URLENCODED, _accountConfirmLoginParam,
					null, HttpRequestType.ASYNCHRONOUS,
					new AccountLoginHttpRequestListener());
		}

	}

	// account confirm login http request listener
	class AccountLoginHttpRequestListener extends OnHttpRequestListener {

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
					// get http response entity string json object userkey
					String _confirmLoginReqRespUserKey = JSONUtils
							.getStringFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_al_confirmLoginReqResp_userKey));

					// generate account login user bean with username and
					// userkey and add it to user manager
					UserManager.getInstance().setUser(
							new UserBean(_mAccountLoginUsername, null,
									_confirmLoginReqRespUserKey));

					// add account login username and userkey to local data
					// storage
					DataStorageUtils
							.putObject(
									ComUserLocalStorageAttributes.LOGIN_USERNAME
											.name(), _mAccountLoginUsername);
					DataStorageUtils.putObject(
							ComUserLocalStorageAttributes.LOGIN_USERKEY.name(),
							_confirmLoginReqRespUserKey);

					// pop account login activity and go to iPetChat tab
					// activity
					popActivityWithResult(RESULT_OK, null);
					break;

				case 1:
					Log.d(LOG_TAG, "account login failed, user not existed");

					Toast.makeText(AccountLoginActivity.this,
							R.string.toast_al_user_notExisted,
							Toast.LENGTH_SHORT).show();
					break;

				case 2:
					Log.d(LOG_TAG,
							"account login failed, user login password is wrong");

					Toast.makeText(AccountLoginActivity.this,
							R.string.toast_al_loginPwd_wrong,
							Toast.LENGTH_SHORT).show();
					break;

				default:
					Log.e(LOG_TAG,
							"account login failed, bg_server return result is unrecognized");

					processLoginException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"account login failed, bg_server return result is null");

				processLoginException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// close account login process dialog
			closeAsyncHttpReqProgressDialog();

			Log.e(LOG_TAG,
					"account login failed, send account login confirm post request failed");

			processLoginException();
		}

	}

}
