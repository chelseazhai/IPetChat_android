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

	// get account finish register view stub
	private AccountReg6ResetPwdViewStub _mFinishRegisterViewStub;

	// get, verify account register phone verification code and finish register
	// view
	private View _mGetPhoneVerificationCodeView;
	private View _mVerifyVerificationCodeView;
	private View _mFinishRegisterView;

	// asynchronous http request progress dialog
	private ProgressDialog _mAsyncHttpReqProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_register_activity_layout);

		// set title
		setTitle(R.string.account_register_nav_title);

		// get account register get phone verification code step view
		_mGetPhoneVerificationCodeView = findViewById(R.id.ar_getPhoneVerificationCodeStep_linearLayout);

		// set get account register get phone verification code button on click
		// listener
		((Button) findViewById(R.id.getPhoneVerificationCode_button))
				.setOnClickListener(new GetPhoneVerificationCodeBtnOnClickListener());
	}

	// process account register exception
	private void processAccountRegisterException() {
		// show account register failed toast
		Toast.makeText(AccountRegisterActivity.this,
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
	// get account register get phone verification code button on click listener
	class GetPhoneVerificationCodeBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account register phone number
			String _accountRegPhoneNumber = ((EditText) findViewById(R.id.getPhoneVerificationCode_phone_editText))
					.getText().toString();

			// check account register phone number
			if (null == _accountRegPhoneNumber
					|| "".equalsIgnoreCase(_accountRegPhoneNumber)) {
				Toast.makeText(
						AccountRegisterActivity.this,
						R.string.toast_getPhoneVerificationCode_phoneNumber_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// show get account register phone verification code process dialog
			_mAsyncHttpReqProgressDialog = ProgressDialog
					.show(AccountRegisterActivity.this,
							null,
							getString(R.string.asyncHttpRequest_progressDialog_message),
							true);

			// get account register phone verification code
			// generate get account register phone verification code post
			// request param
			Map<String, String> _getAccountRegVerificationCodeParam = new HashMap<String, String>();
			_getAccountRegVerificationCodeParam
					.put(getResources()
							.getString(
									R.string.rbgServer_getPhoneVerificationCodeReqParam_phone),
							_accountRegPhoneNumber);
			_getAccountRegVerificationCodeParam
					.put(getResources()
							.getString(
									R.string.rbgServer_getPhoneVerificationCodeReqParam_type),
							getResources()
									.getString(
											R.string.rbgServer_ar_getRegisterPhoneVerificationCodeReqParam_type));

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
			// close get account register phone verification code process dialog
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
					Log.d(LOG_TAG,
							"get account register phone verification code successful");

					// go to account register verify verification code step
					// hide account register get phone verification code step
					// view
					_mGetPhoneVerificationCodeView.setVisibility(View.GONE);

					// show account register verify verification code step view
					// if needed
					if (null == _mVerifyVerificationCodeView) {
						// inflate account register verify verification code
						// step viewStub
						_mVerifyVerificationCodeView = ((ViewStub) findViewById(R.id.ar_verifyVerificationCodeStep_viewStub))
								.inflate();

						// set account register verify verification code button
						// on click listener
						((Button) findViewById(R.id.verifyVerificationCode_button))
								.setOnClickListener(new VerifyVerificationCodeBtnOnClickListener());
					} else {
						if (View.VISIBLE != _mVerifyVerificationCodeView
								.getVisibility()) {
							_mVerifyVerificationCodeView
									.setVisibility(View.VISIBLE);
						}
					}
					break;

				case 1:
					Log.d(LOG_TAG,
							"get account register phone verification code failed, register phone number is null");

					Toast.makeText(
							AccountRegisterActivity.this,
							R.string.toast_getPhoneVerificationCode_phoneNumber_null,
							Toast.LENGTH_LONG).show();
					break;

				case 2:
					Log.d(LOG_TAG,
							"get account register phone verification code failed, register phone number is invalid");

					Toast.makeText(
							AccountRegisterActivity.this,
							R.string.toast_getPhoneVerificationCode_phoneNumber_invalid,
							Toast.LENGTH_LONG).show();
					break;

				case 3:
					Log.d(LOG_TAG,
							"get account register phone verification code failed, register phone number is existed");

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_ar_accountWithPhoneNumber_existed,
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
			// close get account register phone verification code process dialog
			closeAsyncHttpReqProgressDialog();

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
			String _accountRegVerificationCode = ((EditText) findViewById(R.id.verifyVerificationCode_verificationCode_editText))
					.getText().toString();

			// check account register verification code
			if (null == _accountRegVerificationCode
					|| "".equalsIgnoreCase(_accountRegVerificationCode)) {
				Toast.makeText(
						AccountRegisterActivity.this,
						R.string.toast_verifyVerificationCode_verificationCode_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// show verify account register verification code process dialog
			_mAsyncHttpReqProgressDialog = ProgressDialog
					.show(AccountRegisterActivity.this,
							null,
							getString(R.string.asyncHttpRequest_progressDialog_message),
							true);

			// verify account register verification code
			// generate verify account register verification code post request
			// param
			Map<String, String> _verifyAccountRegVerificationCodeParam = new HashMap<String, String>();
			_verifyAccountRegVerificationCodeParam
					.put(getResources()
							.getString(
									R.string.rbgServer_verifyVerificationCodeReqParam_verificationCode),
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
			// close verify verification code process dialog
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
					Log.d(LOG_TAG,
							"verify account register verification code successful");

					// go to account register finish register step
					// hide account register verify verification code step view
					_mVerifyVerificationCodeView.setVisibility(View.GONE);

					// show account register finish register step view if needed
					if (null == _mFinishRegisterView) {
						// get account register finish register viewStub
						_mFinishRegisterViewStub = (AccountReg6ResetPwdViewStub) findViewById(R.id.ar_finishRegisterStep_viewStub);

						// inflate account register finish register step
						// viewStub
						_mFinishRegisterView = _mFinishRegisterViewStub
								.inflate();

						// set account register finish register button on click
						// listener
						_mFinishRegisterViewStub
								.setFinishBtnOnClickListener(new FinishRegisterBtnOnClickListener());
					} else {
						if (View.VISIBLE != _mFinishRegisterView
								.getVisibility()) {
							_mFinishRegisterView.setVisibility(View.VISIBLE);
						}
					}
					break;

				case 1:
					Log.d(LOG_TAG,
							"verify account register verification code failed, verification code is null");

					Toast.makeText(
							AccountRegisterActivity.this,
							R.string.toast_verifyVerificationCode_verificationCode_null,
							Toast.LENGTH_LONG).show();
					break;

				case 2:
					Log.d(LOG_TAG,
							"verify account register verification code failed, verification code is wrong");

					Toast.makeText(
							AccountRegisterActivity.this,
							R.string.toast_verifyVerificationCode_verificationCode_wrong,
							Toast.LENGTH_LONG).show();
					break;

				case 6:
					Log.d(LOG_TAG,
							"verify account register verification code failed, verify account register verification code http request session timeout");

					// go to account register get phone verification code step
					// hide account register verify verification code view
					_mVerifyVerificationCodeView.setVisibility(View.GONE);
					// show account register get phone verification code step
					// view
					_mGetPhoneVerificationCodeView.setVisibility(View.VISIBLE);

					Toast.makeText(
							AccountRegisterActivity.this,
							R.string.toast_verifyVerificationCode_verificationCode_timeout,
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
			// close verify verification code process dialog
			closeAsyncHttpReqProgressDialog();

			Log.e(LOG_TAG,
					"verify account register verification code failed, send verify account register verification code post request failed");

			processAccountRegisterException();
		}

	}

	// account register finish register button on click listener
	class FinishRegisterBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account register password
			String _accountRegPassword = _mFinishRegisterViewStub
					.getPassword1EditTextText();

			// check account register password
			if (null == _accountRegPassword
					|| "".equalsIgnoreCase(_accountRegPassword)) {
				Toast.makeText(AccountRegisterActivity.this,
						R.string.toast_ar_password_null, Toast.LENGTH_SHORT)
						.show();

				return;
			}

			// get account register confirmation password
			String _accountRegConfirmationPwd = _mFinishRegisterViewStub
					.getPassword2EditTextText();

			// check account register confirmation password
			if (null == _accountRegConfirmationPwd
					|| "".equalsIgnoreCase(_accountRegConfirmationPwd)) {
				Toast.makeText(AccountRegisterActivity.this,
						R.string.toast_ar_confirmationPwd_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// check account register user two input password
			if (!_accountRegPassword
					.equalsIgnoreCase(_accountRegConfirmationPwd)) {
				Toast.makeText(AccountRegisterActivity.this,
						R.string.toast_ar_twoPwd_notMatched, Toast.LENGTH_LONG)
						.show();

				return;
			}

			// show finish register process dialog
			_mAsyncHttpReqProgressDialog = ProgressDialog
					.show(AccountRegisterActivity.this,
							null,
							getString(R.string.asyncHttpRequest_progressDialog_message),
							true);

			// account finish register
			// generate account finish register post request param
			Map<String, String> _accountFinishRegisterParam = new HashMap<String, String>();
			_accountFinishRegisterParam
					.put(getResources()
							.getString(
									R.string.rbgServer_ar_finishRegisterReqParam_password),
							_accountRegPassword);
			_accountFinishRegisterParam
					.put(getResources()
							.getString(
									R.string.rbgServer_ar_finishRegisterReqParam_confirmationPwd),
							_accountRegConfirmationPwd);

			// send account finish register post http request
			HttpUtils.postRequest(getResources().getString(R.string.server_url)
					+ getResources().getString(R.string.account_register_url),
					PostRequestFormat.URLENCODED, _accountFinishRegisterParam,
					null, HttpRequestType.ASYNCHRONOUS,
					new FinishRegisterHttpRequestListener());
		}

	}

	// finish register http request listener
	class FinishRegisterHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// close finish register process dialog
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
					Log.d(LOG_TAG, "account register finish successful");

					// pop account register activity and go to account login
					// activity
					popActivityWithResult(RESULT_OK, null);

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_ar_register_successful,
							Toast.LENGTH_LONG).show();
					break;

				case 5:
					Log.d(LOG_TAG,
							"account register finish failed, user two input password not matched");

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_ar_twoPwd_notMatched,
							Toast.LENGTH_LONG).show();
					break;

				case 6:
					Log.d(LOG_TAG,
							"account register finish failed, account register finish http request session timeout");

					// go to account register get phone verification code step
					// hide account register finish register step view
					_mFinishRegisterView.setVisibility(View.GONE);
					// show account register get phone verification code view
					_mGetPhoneVerificationCodeView.setVisibility(View.VISIBLE);

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_ar_register_timeout,
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
			// close finish register process dialog
			closeAsyncHttpReqProgressDialog();

			Log.e(LOG_TAG,
					"account register finish failed, send account register finish post request failed");

			processAccountRegisterException();
		}

	}

}
