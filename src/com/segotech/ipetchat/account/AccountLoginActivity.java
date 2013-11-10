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

	// asynchronous http request progress dialog
	private ProgressDialog _mAsyncHttpReqProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_login_activity_layout);

		// set title
		setTitle(R.string.account_login_nav_title);

		// auto complete login phone
		((EditText) findViewById(R.id.account_login_phone_editText))
				.setText(UserManager.getInstance().getUser().getName());

		// set forget account login password button on click listener
		((Button) findViewById(R.id.forget_account_loginPwd_button))
				.setOnClickListener(new ForgetPwdBtnOnClickListener());

		// set account login confirm button on click listener
		((Button) findViewById(R.id.account_login_confirm_button))
				.setOnClickListener(new LoginConfirmBtnOnClickListener());
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
	class ForgetPwdBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// go to account retrieve password activity
			pushActivity(AccountRetrievePwdActivity.class);
		}

	}

	// account login confirm button on click listener
	class LoginConfirmBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account login phone number
			String _accountLoginPhoneNumber = ((EditText) findViewById(R.id.account_login_phone_editText))
					.getText().toString();

			// check account login phone number
			if (null == _accountLoginPhoneNumber
					|| "".equalsIgnoreCase(_accountLoginPhoneNumber)) {
				Toast.makeText(AccountLoginActivity.this,
						R.string.toast_login_userName_null, Toast.LENGTH_SHORT)
						.show();

				return;
			}

			// get account login password
			String _accountLoginPassword = ((EditText) findViewById(R.id.account_login_password_editText))
					.getText().toString();

			// check account login password
			if (null == _accountLoginPassword
					|| "".equalsIgnoreCase(_accountLoginPassword)) {
				Toast.makeText(AccountLoginActivity.this,
						R.string.toast_login_password_null, Toast.LENGTH_SHORT)
						.show();

				return;
			}

			// show account login process dialog
			_mAsyncHttpReqProgressDialog = ProgressDialog
					.show(AccountLoginActivity.this,
							null,
							getString(R.string.asyncHttpRequest_progressDialog_message),
							true);

			// account login confirm
			// generate account login confirm post request param
			Map<String, String> _accountLoginConfirmParam = new HashMap<String, String>();
			_accountLoginConfirmParam.put(
					getResources().getString(
							R.string.rbgServer_accountLogin_phone),
					_accountLoginPhoneNumber);
			_accountLoginConfirmParam.put(
					getResources().getString(
							R.string.rbgServer_accountLogin_password),
					StringUtils.md5(_accountLoginPassword));

			// send account login confirm post http request
			HttpUtils.postRequest(getResources().getString(R.string.server_url)
					+ getResources().getString(R.string.account_login_url),
					PostRequestFormat.URLENCODED, _accountLoginConfirmParam,
					null, HttpRequestType.ASYNCHRONOUS,
					new AccountLoginHttpRequestListener());
		}

	}

	// account login confirm http request listener
	class AccountLoginHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// close account login process dialog
			closeAsyncHttpReqProgressDialog();

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
					// get account login name and response user key
					String _loginName = ((EditText) findViewById(R.id.account_login_phone_editText))
							.getText().toString();
					String _responseLoginUserKey = JSONUtils
							.getStringFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_accountLoginReq_resp_userKey));

					// save login user bean and add it to user manager
					UserManager.getInstance().setUser(
							new UserBean(_loginName, null,
									_responseLoginUserKey));

					// add login name and user key to local data storage
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
					Log.d(LOG_TAG, "account login failed, user not existed");

					Toast.makeText(AccountLoginActivity.this,
							R.string.toast_login_user_notExisted,
							Toast.LENGTH_SHORT).show();
					break;

				case 2:
					Log.d(LOG_TAG,
							"account login failed, user login password is wrong");

					Toast.makeText(AccountLoginActivity.this,
							R.string.toast_login_user_loginPwd_wrong,
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
