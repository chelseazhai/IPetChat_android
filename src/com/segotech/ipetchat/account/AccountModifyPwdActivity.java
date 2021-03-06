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
import android.widget.EditText;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.richitec.commontoolkit.utils.StringUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class AccountModifyPwdActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = AccountModifyPwdActivity.class
			.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_modify_pwd_activity_layout);

		// set title
		setTitle(R.string.account_modify_password_nav_title);

		// set done modify account password button as right bar button item
		setRightBarButtonItem(new BarButtonItem(this,
				R.string.done_modify_account_password_button_title,
				new DoneModifyPwdBtnOnClickListener()));
	}

	// process account modify password exception
	private void processAccountModifyPwdException() {
		// show account modify password failed toast
		Toast.makeText(AccountModifyPwdActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// done modify password button on click listener
	class DoneModifyPwdBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get origin password
			String _originPassword = ((EditText) findViewById(R.id.origin_pwd_editText))
					.getText().toString();

			// check origin password
			if (null == _originPassword || _originPassword.equalsIgnoreCase("")) {
				Toast.makeText(AccountModifyPwdActivity.this,
						R.string.toast_accountOriginPassword_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// get reset new password
			String _resetNewPwd = ((EditText) findViewById(R.id.modify_pwd_editText))
					.getText().toString();

			// check reset new password
			if (null == _resetNewPwd || _resetNewPwd.equalsIgnoreCase("")) {
				Toast.makeText(AccountModifyPwdActivity.this,
						R.string.toast_accountRestNewPassword_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// get reset new confirmation password
			String _resetNewConfirmationPwd = ((EditText) findViewById(R.id.modify_confirmationPwd_editText))
					.getText().toString();

			// check reset new confirmation password
			if (null == _resetNewConfirmationPwd
					|| _resetNewConfirmationPwd.equalsIgnoreCase("")) {
				Toast.makeText(AccountModifyPwdActivity.this,
						R.string.toast_accountRestNewConfirmationPassword_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// check account modify password user two input password
			if (!_resetNewPwd.equalsIgnoreCase(_resetNewConfirmationPwd)) {
				Toast.makeText(AccountModifyPwdActivity.this,
						R.string.toast_accountModifyPwd_twoPwd_notMatched,
						Toast.LENGTH_LONG).show();

				return;
			}

			// done modify password
			// generate done modify password post request param
			Map<String, String> _doneModifyPwdParam = new HashMap<String, String>();
			_doneModifyPwdParam
					.put(getResources().getString(
							R.string.rbgServer_amp_changePwdReqParam_originPwd),
							StringUtils.md5(_originPassword));
			_doneModifyPwdParam.put(
					getResources().getString(
							R.string.rbgServer_amp_changePwdReqParam_newPwd),
					_resetNewPwd);
			_doneModifyPwdParam
					.put(getResources()
							.getString(
									R.string.rbgServer_amp_changePwdReqParam_newConfirmationPwd),
							_resetNewConfirmationPwd);

			// send done modify password post http request
			HttpUtils.postSignatureRequest(
					getResources().getString(R.string.server_url)
							+ getResources().getString(
									R.string.account_modifyPwd_url),
					PostRequestFormat.URLENCODED, _doneModifyPwdParam, null,
					HttpRequestType.ASYNCHRONOUS,
					new DoneModifyPwdHttpRequestListener());
		}

	}

	// done modify password http request listener
	class DoneModifyPwdHttpRequestListener extends OnHttpRequestListener {

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
					Log.d(LOG_TAG, "account modify password finish successful");

					// pop account modify password activity
					popActivity();

					break;

				case 2:
					Log.d(LOG_TAG,
							"done modify password failed, user two input password not matched");

					Toast.makeText(AccountModifyPwdActivity.this,
							R.string.toast_accountModifyPwd_twoPwd_notMatched,
							Toast.LENGTH_LONG).show();
					break;

				case 4:
					Log.d(LOG_TAG,
							"done modify password failed, origin password is wrong");

					Toast.makeText(
							AccountModifyPwdActivity.this,
							R.string.toast_accountModifyPwd_originPassword_wrong,
							Toast.LENGTH_LONG).show();
					break;

				case 1:
				case 3:
				default:
					Log.e(LOG_TAG,
							"done modify password failed, bg_server return result is unrecognized");

					processAccountModifyPwdException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"done modify password failed, bg_server return result is null");

				processAccountModifyPwdException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"done modify password failed, send done modify password post request failed");

			processAccountModifyPwdException();
		}

	}

}
