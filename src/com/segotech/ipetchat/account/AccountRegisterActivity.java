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
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.AccountReg6ResetPwdViewStub;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class AccountRegisterActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = AccountRegisterActivity.class
			.getCanonicalName();

	// get account register finish view stub
	private AccountReg6ResetPwdViewStub _mAccountRegFinishViewStub;

	// get, verify account register verification code and finish register view
	private View _mAccountRegGetVerificationCodeView;
	private View _mAccountRegVerifyVerificationCodeView;
	private View _mAccountRegFinishRegisterView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_register_activity_layout);

		// set title
		setTitle(R.string.account_register_nav_title);

		// get account register step 1 view
		_mAccountRegGetVerificationCodeView = findViewById(R.id.account_register_step1_linearLayout);

		// set get account register phone verification code button on click
		// listener
		((Button) findViewById(R.id.get_phoneVerificationCode_button))
				.setOnClickListener(new GetPhoneVerificationCodeBtnOnClickListener());
	}

	// process account register exception
	private void processAccountRegisterException() {
		// show account register failed toast
		Toast.makeText(AccountRegisterActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// get account register phone verification code button on click listener
	class GetPhoneVerificationCodeBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account register phone number
			String _accountRegPhoneNumber = ((EditText) findViewById(R.id.get_phoneVerificationCode_phone_editText))
					.getText().toString();

			// check account register phone number
			if (null == _accountRegPhoneNumber
					|| "".equalsIgnoreCase(_accountRegPhoneNumber)) {
				Toast.makeText(
						AccountRegisterActivity.this,
						R.string.toast_get_phoneVerificationCode_phoneNumber_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// get account register phone verification code
			// generate get account register phone verification code post
			// request param
			Map<String, String> _getAccountRegVerificationCodeParam = new HashMap<String, String>();
			_getAccountRegVerificationCodeParam.put(
					getResources().getString(
							R.string.rbgServer_getVerificationCode_phone),
					_accountRegPhoneNumber);

			// send get account register phone verification code post http
			// request
			HttpUtils.postRequest(
					getResources().getString(R.string.server_url)
							+ getResources().getString(
									R.string.get_phoneVerificationCode_url),
					PostRequestFormat.URLENCODED,
					_getAccountRegVerificationCodeParam, null,
					HttpRequestType.ASYNCHRONOUS,
					new GetPhoneVerificationCodeHttpRequestListener());
		}

	}

	// get account register phone verification code http request listener
	class GetPhoneVerificationCodeHttpRequestListener extends
			OnHttpRequestListener {

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
					Log.d(LOG_TAG,
							"get account register phone verification code successful");

					// goto account register step 2 - verify verification code
					// hide account register step 1 view
					findViewById(R.id.account_register_step1_linearLayout)
							.setVisibility(View.GONE);

					// show account register step 2 view if needed
					if (null == _mAccountRegVerifyVerificationCodeView) {
						// inflate account register step 2 viewStub
						_mAccountRegVerifyVerificationCodeView = ((ViewStub) findViewById(R.id.account_register_step2_viewStub))
								.inflate();

						// set account register verify verification code button
						// on click listener
						((Button) findViewById(R.id.verify_verificationCode_button))
								.setOnClickListener(new VerifyVerificationCodeBtnOnClickListener());
					} else {
						if (View.VISIBLE != _mAccountRegVerifyVerificationCodeView
								.getVisibility()) {
							_mAccountRegVerifyVerificationCodeView
									.setVisibility(View.VISIBLE);
						}
					}
					break;

				case 1:
					Log.d(LOG_TAG,
							"get account register phone verification code failed, register phone number is null");

					Toast.makeText(
							AccountRegisterActivity.this,
							R.string.toast_get_phoneVerificationCode_phoneNumber_null,
							Toast.LENGTH_LONG).show();
					break;

				case 2:
					Log.d(LOG_TAG,
							"get account register phone verification code failed, register phone number is invalid");

					Toast.makeText(
							AccountRegisterActivity.this,
							R.string.toast_get_phoneVerificationCode_phoneNumber_invalid,
							Toast.LENGTH_LONG).show();
					break;

				case 3:
					Log.d(LOG_TAG,
							"get account register phone verification code failed, register phone number is existed");

					Toast.makeText(
							AccountRegisterActivity.this,
							R.string.toast_account_register_phoneNumber_existed,
							Toast.LENGTH_LONG).show();
					break;

				case 1001:
					Log.d(LOG_TAG,
							"get account register phone verification code failed, bg_server internal error");

					Toast.makeText(
							AccountRegisterActivity.this,
							R.string.toast_request_remoteBackgroundServer_exception,
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"get account register phone verification code failed, bg_server return result is unrecognized");

					processAccountRegisterException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get account register phone verification code failed, bg_server return result is null");

				processAccountRegisterException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get account register phone verification code failed, send get account register phone verification code post request failed");

			processAccountRegisterException();
		}

	}

	// verify account register verification code button on click listener
	class VerifyVerificationCodeBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account register verification code
			String _accountRegVerificationCode = ((EditText) findViewById(R.id.verify_verificationCode_verificationCode_editText))
					.getText().toString();

			// check account register verification code
			if (null == _accountRegVerificationCode
					|| "".equalsIgnoreCase(_accountRegVerificationCode)) {
				Toast.makeText(AccountRegisterActivity.this,
						R.string.toast_verify_verificationCode_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// verify account register verification code
			// generate verify account register verification code post request
			// param
			Map<String, String> _verifyAccountRegVerificationCodeParam = new HashMap<String, String>();
			_verifyAccountRegVerificationCodeParam.put(getResources()
					.getString(R.string.rbgServer_verifyVerificationCode_code),
					_accountRegVerificationCode);

			// send verify account register verification code post http request
			HttpUtils.postRequest(
					getResources().getString(R.string.server_url)
							+ getResources().getString(
									R.string.verify_verificationCode_url),
					PostRequestFormat.URLENCODED,
					_verifyAccountRegVerificationCodeParam, null,
					HttpRequestType.ASYNCHRONOUS,
					new VerifyVerificationCodeHttpRequestListener());
		}

	}

	// verify verification code http request listener
	class VerifyVerificationCodeHttpRequestListener extends
			OnHttpRequestListener {

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
					Log.d(LOG_TAG,
							"verify account register verification code successful");

					// goto account register step 3 - finish register
					// hide account register step 2 view
					_mAccountRegVerifyVerificationCodeView
							.setVisibility(View.GONE);

					// show account register step 3 view if needed
					if (null == _mAccountRegFinishRegisterView) {
						// get account register finish view stub
						_mAccountRegFinishViewStub = (AccountReg6ResetPwdViewStub) findViewById(R.id.account_register_step3_viewStub);

						// inflate account register step 3 viewStub
						_mAccountRegFinishRegisterView = _mAccountRegFinishViewStub
								.inflate();

						// set account register finish button on click listener
						_mAccountRegFinishViewStub
								.setFinishBtnOnClickListener(new RegisterFinishBtnOnClickListener());
					} else {
						if (View.VISIBLE != _mAccountRegFinishRegisterView
								.getVisibility()) {
							_mAccountRegFinishRegisterView
									.setVisibility(View.VISIBLE);
						}
					}
					break;

				case 1:
					Log.d(LOG_TAG,
							"verify account register verification code failed, verification code is null");

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_verify_verificationCode_null,
							Toast.LENGTH_LONG).show();
					break;

				case 2:
					Log.d(LOG_TAG,
							"verify account register verification code failed, verification code is wrong");

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_verify_verificationCode_wrong,
							Toast.LENGTH_LONG).show();
					break;

				case 6:
					Log.d(LOG_TAG,
							"verify account register verification code failed, verify account register verification code http request session timeout");

					// goto account register step 1 - get account register phone
					// verification code
					// hide account register step 2 view
					_mAccountRegVerifyVerificationCodeView
							.setVisibility(View.GONE);
					// show account register step 1 view
					_mAccountRegGetVerificationCodeView
							.setVisibility(View.VISIBLE);

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_verify_verificationCode_timeout,
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"verify account register verification code failed, bg_server return result is unrecognized");

					processAccountRegisterException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"verify account register verification code failed, bg_server return result is null");

				processAccountRegisterException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"verify account register verification code failed, send verify account register verification code post request failed");

			processAccountRegisterException();
		}

	}

	// register finish button on click listener
	class RegisterFinishBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account register password
			String _accountRegPassword = _mAccountRegFinishViewStub
					.getPassword1EditTextText();

			// check account register password
			if (null == _accountRegPassword
					|| "".equalsIgnoreCase(_accountRegPassword)) {
				Toast.makeText(AccountRegisterActivity.this,
						R.string.toast_account_register_password_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// get account register confirmation password
			String _accountRegConfirmationPwd = _mAccountRegFinishViewStub
					.getPassword2EditTextText();

			// check account register confirmation password
			if (null == _accountRegConfirmationPwd
					|| "".equalsIgnoreCase(_accountRegConfirmationPwd)) {
				Toast.makeText(AccountRegisterActivity.this,
						R.string.toast_account_register_confirmationPwd_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// check account register user two input password
			if (!_accountRegPassword
					.equalsIgnoreCase(_accountRegConfirmationPwd)) {
				Toast.makeText(AccountRegisterActivity.this,
						R.string.toast_account_register_twoPwd_notMatched,
						Toast.LENGTH_LONG).show();

				return;
			}

			// account finish register
			// generate account finish register post request param
			Map<String, String> _accountFinishRegisterParam = new HashMap<String, String>();
			_accountFinishRegisterParam.put(
					getResources().getString(
							R.string.rbgServer_accountReg_password),
					_accountRegPassword);
			_accountFinishRegisterParam.put(
					getResources().getString(
							R.string.rbgServer_accountReg_confirmationPwd),
					_accountRegConfirmationPwd);

			// send account finish register post http request
			HttpUtils.postRequest(getResources().getString(R.string.server_url)
					+ getResources().getString(R.string.account_register_url),
					PostRequestFormat.URLENCODED, _accountFinishRegisterParam,
					null, HttpRequestType.ASYNCHRONOUS,
					new RegisterFinishHttpRequestListener());
		}

	}

	// register finish http request listener
	class RegisterFinishHttpRequestListener extends OnHttpRequestListener {

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
					Log.d(LOG_TAG, "account register finish successful");

					// pop account register activity and go to account login
					// activity
					popActivityWithResult(RESULT_OK, null);

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_account_register_successful,
							Toast.LENGTH_LONG).show();
					break;

				case 5:
					Log.d(LOG_TAG,
							"account register finish failed, user two input password not matched");

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_account_register_twoPwd_notMatched,
							Toast.LENGTH_LONG).show();
					break;

				case 6:
					Log.d(LOG_TAG,
							"account register finish failed, account register finish http request session timeout");

					// goto account register step 1 - get account register phone
					// verification code
					// hide account register step 3 view
					_mAccountRegFinishRegisterView.setVisibility(View.GONE);
					// show account register step 1 view
					_mAccountRegGetVerificationCodeView
							.setVisibility(View.VISIBLE);

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_account_register_timeout,
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"account register finish failed, bg_server return result is unrecognized");

					processAccountRegisterException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"account register finish failed, bg_server return result is null");

				processAccountRegisterException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"account register finish failed, send account register finish post request failed");

			processAccountRegisterException();
		}

	}

}
