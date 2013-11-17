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

public class AccountRetrievePwdActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = AccountRetrievePwdActivity.class
			.getCanonicalName();

	// get account finish reset password view stub
	private AccountReg6ResetPwdViewStub _mFinishResetPwdViewStub;

	// get, verify account retrieve password phone verification code and finish
	// reset password view
	private View _mGetPhoneVerificationCodeView;
	private View _mVerifyVerificationCodeView;
	private View _mFinishResetPwdView;

	// asynchronous http request progress dialog
	private ProgressDialog _mAsyncHttpReqProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_retrieve_pwd_activity_layout);

		// set title
		setTitle(R.string.account_retrieve_password_nav_title);

		// get account retrieve password get phone verification code step view
		_mGetPhoneVerificationCodeView = findViewById(R.id.arp_getPhoneVerificationCodeStep_linearLayout);

		// set get account retrieve password phone verification code button on
		// click listener
		((Button) findViewById(R.id.getPhoneVerificationCode_button))
				.setOnClickListener(new GetPhoneVerificationCodeBtnOnClickListener());
	}

	// process account retrieve password exception
	private void processAccountRetrievePwdException() {
		// show account retrieve password failed toast
		Toast.makeText(AccountRetrievePwdActivity.this,
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
	// get account retrieve password phone verification code button on click
	// listener
	class GetPhoneVerificationCodeBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account retrieve password phone number
			String _accountRetrievePwdPhoneNumber = ((EditText) findViewById(R.id.getPhoneVerificationCode_phone_editText))
					.getText().toString();

			// check account retrieve password phone number
			if (null == _accountRetrievePwdPhoneNumber
					|| "".equalsIgnoreCase(_accountRetrievePwdPhoneNumber)) {
				Toast.makeText(
						AccountRetrievePwdActivity.this,
						R.string.toast_getPhoneVerificationCode_phoneNumber_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// show get account retrieve password phone verification code
			// process dialog
			_mAsyncHttpReqProgressDialog = ProgressDialog
					.show(AccountRetrievePwdActivity.this,
							null,
							getString(R.string.asyncHttpRequest_progressDialog_message),
							true);

			// get account retrieve password phone verification code
			// generate get account retrieve password phone verification code
			// post request param
			Map<String, String> _getAccountRetrievePwdVerificationCodeParam = new HashMap<String, String>();
			_getAccountRetrievePwdVerificationCodeParam
					.put(getResources()
							.getString(
									R.string.rbgServer_getPhoneVerificationCodeReqParam_phone),
							_accountRetrievePwdPhoneNumber);
			_getAccountRetrievePwdVerificationCodeParam
					.put(getResources()
							.getString(
									R.string.rbgServer_getPhoneVerificationCodeReqParam_type),
							getResources()
									.getString(
											R.string.rbgServer_arp_getRetrievePwdPhoneVerificationCodeReqParam_type));

			// send get account retrieve password phone verification code post
			// http request
			HttpUtils.postRequest(
					getResources().getString(R.string.server_url)
							+ getResources().getString(
									R.string.get_phoneVerificationCode_url),
					PostRequestFormat.URLENCODED,
					_getAccountRetrievePwdVerificationCodeParam, null,
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
							"get account retrieve password phone verification code successful");

					// go to account retrieve password verify verification code
					// step
					// hide account retrieve password get phone verification
					// code step view
					_mGetPhoneVerificationCodeView.setVisibility(View.GONE);

					// show account retrieve password verify verification code
					// step view if needed
					if (null == _mVerifyVerificationCodeView) {
						// inflate account retrieve password verify verification
						// code step viewStub
						_mVerifyVerificationCodeView = ((ViewStub) findViewById(R.id.arp_verifyVerificationCodeStep_viewStub))
								.inflate();

						// set account retrieve password verify verification
						// code button on click listener
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
							"get account retrieve password phone verification code failed, register phone number is null");

					Toast.makeText(
							AccountRetrievePwdActivity.this,
							R.string.toast_getPhoneVerificationCode_phoneNumber_null,
							Toast.LENGTH_LONG).show();
					break;

				case 2:
					Log.d(LOG_TAG,
							"get account retrieve password phone verification code failed, register phone number is invalid");

					Toast.makeText(
							AccountRetrievePwdActivity.this,
							R.string.toast_getPhoneVerificationCode_phoneNumber_invalid,
							Toast.LENGTH_LONG).show();
					break;

				case 4:
					Log.d(LOG_TAG,
							"get account retrieve password phone verification code failed, account with phone number not existed");

					Toast.makeText(
							AccountRetrievePwdActivity.this,
							R.string.toast_arp_accountWithPhoneNumber_notExisted,
							Toast.LENGTH_LONG).show();
					break;

				case 1001:
					Log.d(LOG_TAG,
							"get account retrieve password phone verification code failed, bg_server internal error");

					Toast.makeText(
							AccountRetrievePwdActivity.this,
							R.string.toast_request_remoteBackgroundServer_exception,
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"get account retrieve password phone verification code failed, bg_server return result is unrecognized");

					processAccountRetrievePwdException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get account retrieve password phone verification code failed, bg_server return result is null");

				processAccountRetrievePwdException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// close get account register phone verification code process dialog
			closeAsyncHttpReqProgressDialog();

			Log.e(LOG_TAG,
					"get account retrieve password phone verification code failed, send get account retrieve password phone verification code post request failed");

			processAccountRetrievePwdException();
		}

	}

	// verify account retrieve password verification code button on click
	// listener
	class VerifyVerificationCodeBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account retrieve password verification code
			String _accountRetrievePwdVerificationCode = ((EditText) findViewById(R.id.verifyVerificationCode_verificationCode_editText))
					.getText().toString();

			// check account retrieve password verification code
			if (null == _accountRetrievePwdVerificationCode
					|| "".equalsIgnoreCase(_accountRetrievePwdVerificationCode)) {
				Toast.makeText(
						AccountRetrievePwdActivity.this,
						R.string.toast_verifyVerificationCode_verificationCode_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// show verify account retrieve password verification code process
			// dialog
			_mAsyncHttpReqProgressDialog = ProgressDialog
					.show(AccountRetrievePwdActivity.this,
							null,
							getString(R.string.asyncHttpRequest_progressDialog_message),
							true);

			// verify account retrieve password verification code
			// generate verify account retrieve password verification code post
			// request param
			Map<String, String> _verifyAccountRetrievePwdVerificationCodeParam = new HashMap<String, String>();
			_verifyAccountRetrievePwdVerificationCodeParam
					.put(getResources()
							.getString(
									R.string.rbgServer_verifyVerificationCodeReqParam_verificationCode),
							_accountRetrievePwdVerificationCode);

			// send verify account retrieve password verification code post http
			// request
			HttpUtils.postRequest(
					getResources().getString(R.string.server_url)
							+ getResources().getString(
									R.string.verify_verificationCode_url),
					PostRequestFormat.URLENCODED,
					_verifyAccountRetrievePwdVerificationCodeParam, null,
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
							"verify account retrieve password verification code successful");

					// go to account retrieve password finish reset password
					// step
					// hide account retrieve password verify verification code
					// step view
					_mVerifyVerificationCodeView.setVisibility(View.GONE);

					// show account retrieve password finish reset password step
					// view if needed
					if (null == _mFinishResetPwdView) {
						// get account retrieve password finish reset password
						// viewStub
						_mFinishResetPwdViewStub = (AccountReg6ResetPwdViewStub) findViewById(R.id.arp_finishResetPwdStep_viewStub);

						// inflate account retrieve password finish register
						// step viewStub
						_mFinishResetPwdView = _mFinishResetPwdViewStub
								.inflate();

						// set account retrieve password finish register button
						// on click listener
						_mFinishResetPwdViewStub
								.setFinishBtnOnClickListener(new FinishResetPwdBtnOnClickListener());
					} else {
						if (View.VISIBLE != _mFinishResetPwdView
								.getVisibility()) {
							_mFinishResetPwdView.setVisibility(View.VISIBLE);
						}
					}
					break;

				case 1:
					Log.d(LOG_TAG,
							"verify account retrieve password verification code failed, verification code is null");

					Toast.makeText(
							AccountRetrievePwdActivity.this,
							R.string.toast_verifyVerificationCode_verificationCode_null,
							Toast.LENGTH_LONG).show();
					break;

				case 2:
					Log.d(LOG_TAG,
							"verify account retrieve password verification code failed, verification code is wrong");

					Toast.makeText(
							AccountRetrievePwdActivity.this,
							R.string.toast_verifyVerificationCode_verificationCode_wrong,
							Toast.LENGTH_LONG).show();
					break;

				case 6:
					Log.d(LOG_TAG,
							"verify account retrieve password verification code failed, verify account retrieve password verification code http request session timeout");

					// go to account retrieve password get phone verification
					// code step
					// hide account retrieve password verify verification code
					// view
					_mVerifyVerificationCodeView.setVisibility(View.GONE);
					// show account retrieve password get phone verification
					// code step view
					_mGetPhoneVerificationCodeView.setVisibility(View.VISIBLE);

					Toast.makeText(
							AccountRetrievePwdActivity.this,
							R.string.toast_verifyVerificationCode_verificationCode_timeout,
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"verify account retrieve password verification code failed, bg_server return result is unrecognized");

					processAccountRetrievePwdException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"verify account retrieve password verification code failed, bg_server return result is null");

				processAccountRetrievePwdException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// close verify verification code process dialog
			closeAsyncHttpReqProgressDialog();

			Log.e(LOG_TAG,
					"verify account retrieve password verification code failed, send verify account retrieve password verification code post request failed");

			processAccountRetrievePwdException();
		}

	}

	// account retrieve password finish reset password button on click listener
	class FinishResetPwdBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account reset password
			String _accountResetPassword = _mFinishResetPwdViewStub
					.getPassword1EditTextText();

			// check account reset password
			if (null == _accountResetPassword
					|| "".equalsIgnoreCase(_accountResetPassword)) {
				Toast.makeText(AccountRetrievePwdActivity.this,
						R.string.toast_arp_newPwd_null, Toast.LENGTH_SHORT)
						.show();

				return;
			}

			// get account reset confirmation password
			String _accountResetConfirmationPwd = _mFinishResetPwdViewStub
					.getPassword2EditTextText();

			// check account reset confirmation password
			if (null == _accountResetConfirmationPwd
					|| "".equalsIgnoreCase(_accountResetConfirmationPwd)) {
				Toast.makeText(AccountRetrievePwdActivity.this,
						R.string.toast_arp_newConfirmationPwd_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// check account retrieve password user two input password
			if (!_accountResetPassword
					.equalsIgnoreCase(_accountResetConfirmationPwd)) {
				Toast.makeText(AccountRetrievePwdActivity.this,
						R.string.toast_arp_twoNewPwd_notMatched,
						Toast.LENGTH_LONG).show();

				return;
			}

			// show finish reset password process dialog
			_mAsyncHttpReqProgressDialog = ProgressDialog
					.show(AccountRetrievePwdActivity.this,
							null,
							getString(R.string.asyncHttpRequest_progressDialog_message),
							true);

			// account finish reset password
			// generate account finish reset password post request param
			Map<String, String> _accountFinishResetPwdParam = new HashMap<String, String>();
			_accountFinishResetPwdParam
					.put(getResources()
							.getString(
									R.string.rbgServer_arp_finishResetPwdReqParam_newPassword),
							_accountResetPassword);
			_accountFinishResetPwdParam
					.put(getResources()
							.getString(
									R.string.rbgServer_arp_finishResetPwdReqParam_newConfirmationPwd),
							_accountResetConfirmationPwd);

			// send account finish reset password post http request
			HttpUtils.postRequest(getResources().getString(R.string.server_url)
					+ getResources().getString(R.string.account_resetPwd_url),
					PostRequestFormat.URLENCODED, _accountFinishResetPwdParam,
					null, HttpRequestType.ASYNCHRONOUS,
					new FinishResetPwdHttpRequestListener());
		}

	}

	// finish reset password http request listener
	class FinishResetPwdHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// close finish reset password process dialog
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
					Log.d(LOG_TAG, "account reset password finish successful");

					// pop account retrieve password activity
					popActivity();

					Toast.makeText(AccountRetrievePwdActivity.this,
							R.string.toast_arp_resetPwd_successful,
							Toast.LENGTH_LONG).show();
					break;

				case 5:
					Log.d(LOG_TAG,
							"account retrieve password finish failed, user two input new password not matched");

					Toast.makeText(AccountRetrievePwdActivity.this,
							R.string.toast_arp_twoNewPwd_notMatched,
							Toast.LENGTH_LONG).show();
					break;

				case 6:
					Log.d(LOG_TAG,
							"account retrieve password finish failed, account reset password finish http request session timeout");

					// go to account retrieve password get phone verification
					// code step
					// hide account retrieve password finish reset password step
					// view
					_mFinishResetPwdView.setVisibility(View.GONE);
					// show account retrieve password get phone verification
					// code view
					_mGetPhoneVerificationCodeView.setVisibility(View.VISIBLE);

					Toast.makeText(AccountRetrievePwdActivity.this,
							R.string.toast_arp_resetPwd_timeout,
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"account retrieve password finish failed, bg_server return result is unrecognized");

					processAccountRetrievePwdException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"account retrieve password finish failed, bg_server return result is null");

				processAccountRetrievePwdException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// close finish reset password process dialog
			closeAsyncHttpReqProgressDialog();

			Log.e(LOG_TAG,
					"account retrieve password finish failed, send account reset password finish post request failed");

			processAccountRetrievePwdException();
		}

	}

}
