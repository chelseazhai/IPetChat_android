package com.segotech.ipetchat.account;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

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
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class AccountRegisterActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = AccountRegisterActivity.class
			.getCanonicalName();

	// account register step 2 and step 3 view
	private View _mAccountRegisterStep2View;
	private View _mAccountRegisterStep3View;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_register_activity_layout);

		// set title
		setTitle(R.string.account_register_nav_title_text);

		// set get register phone verification code button on click listener
		((Button) findViewById(R.id.get_verificationCode_btn))
				.setOnClickListener(new GetVerificationCodeBtnOnClickListener());
	}

	// process account register exception
	private void processAccountRegisterException() {
		// show account register failed toast
		Toast.makeText(AccountRegisterActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// get register phone verification code button on click listener
	class GetVerificationCodeBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get register phone number
			String _registerPhoneNumber = ((EditText) findViewById(R.id.registerPhone_editText))
					.getText().toString();

			// check register phone number
			if (null == _registerPhoneNumber
					|| _registerPhoneNumber.equalsIgnoreCase("")) {
				Toast.makeText(AccountRegisterActivity.this,
						R.string.toast_accountRegister_phoneNumber_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// get register phone verification code
			// generate get register phone verification code post request param
			Map<String, String> _getRegVerificationCodeParam = new HashMap<String, String>();
			_getRegVerificationCodeParam.put(
					getResources().getString(
							R.string.rbgServer_accountRegisterPhoneNumber),
					_registerPhoneNumber);

			// send get register phone verification code post http request
			HttpUtils
					.postRequest(
							getResources().getString(R.string.server_url)
									+ getResources()
											.getString(
													R.string.get_registerPhone_verification_code_url),
							PostRequestFormat.URLENCODED,
							_getRegVerificationCodeParam, null,
							HttpRequestType.ASYNCHRONOUS,
							new GetVerificationCodeHttpRequestListener());
		}

	}

	// get register phone verification code http request listener
	class GetVerificationCodeHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string
			String _respEntityString = HttpUtils
					.getHttpResponseEntityString(response);

			// test by ares
			Log.d(LOG_TAG, "response entity string" + _respEntityString);

			// get http response entity string json object result and userKey
			String _result = JSONUtils.getStringFromJSONObject(JSONUtils
					.toJSONObject(_respEntityString),
					getResources()
							.getString(R.string.rbgServer_req_resp_result));

			// check result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					Log.d(LOG_TAG,
							"get register phone verification code successful");

					// goto account register step 2 - verify verification code
					// hide account register step 1 view
					findViewById(R.id.account_register_step1_linearLayout)
							.setVisibility(View.GONE);

					// show account register step 2 view if needed
					if (null == _mAccountRegisterStep2View) {
						// inflate account register step 2 viewStub
						_mAccountRegisterStep2View = ((ViewStub) findViewById(R.id.account_register_step2_viewStub))
								.inflate();

						//
					} else {
						if (View.VISIBLE != _mAccountRegisterStep2View
								.getVisibility()) {
							_mAccountRegisterStep2View
									.setVisibility(View.VISIBLE);
						}
					}
					break;

				case 2:
					Log.d(LOG_TAG,
							"get register phone verification code failed, register phone number is invalid");

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_accountRegister_phoneNumber_invalid,
							Toast.LENGTH_LONG).show();
					break;

				case 3:
					Log.d(LOG_TAG,
							"get register phone verification code failed, register phone number is existed");

					Toast.makeText(AccountRegisterActivity.this,
							R.string.toast_accountRegister_phoneNumber_existed,
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"get register phone verification code failed, bg_server return result is unrecognized");

					processAccountRegisterException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get register phone verification code failed, bg_server return result is null");

				processAccountRegisterException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get register phone verification code failed, send get register phone verification code post request failed");

			processAccountRegisterException();
		}

	}

}
