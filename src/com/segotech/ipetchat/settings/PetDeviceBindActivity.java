package com.segotech.ipetchat.settings;

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
import android.widget.EditText;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.richitec.commontoolkit.user.UserBean;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class PetDeviceBindActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = PetDeviceBindActivity.class
			.getCanonicalName();

	// pet device binded device number editText
	private EditText _mPetDeviceBindedDeviceNumberEditText;

	// asynchronous http request progress dialog
	private ProgressDialog _mAsyncHttpReqProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_device_bind_activity_layout);

		// set title
		setTitle(R.string.pet_device_bind_setting_nav_title);

		// set pet device bind save button as right bar button item
		setRightBarButtonItem(new BarButtonItem(this,
				R.string.pdb_bind_button_title,
				new PetDeviceBindBarBtnItemOnClickListener()));

		// get pet device binded device number editText
		_mPetDeviceBindedDeviceNumberEditText = (EditText) findViewById(R.id.pdb_deviceNumber_editText);
	}

	// process pet device bind exception
	private void processBindException() {
		// show pet device bind failed toast
		Toast.makeText(PetDeviceBindActivity.this,
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
	// pet device bind bar button item on click listener
	class PetDeviceBindBarBtnItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get and check pet device binded device number
			String _petDeviceBindedDeviceNumber = _mPetDeviceBindedDeviceNumberEditText
					.getText().toString();
			if (null != _petDeviceBindedDeviceNumber
					&& !"".equalsIgnoreCase(_petDeviceBindedDeviceNumber)) {
				// show pet device bind process dialog
				_mAsyncHttpReqProgressDialog = ProgressDialog
						.show(PetDeviceBindActivity.this,
								null,
								getString(R.string.asyncHttpRequest_progressDialog_message),
								true);

				// pet device bind
				// generate pet device bind post request param
				Map<String, String> _petDeviceBindParam = new HashMap<String, String>();
				_petDeviceBindParam.put("petid", IPCUserExtension
						.getUserPetInfo(UserManager.getInstance().getUser())
						.getId().toString());
				_petDeviceBindParam.put("deviceno",
						_petDeviceBindedDeviceNumber);

				// send pet device bind post http request
				HttpUtils.postSignatureRequest(
						getResources().getString(R.string.server_url)
								+ getResources().getString(
										R.string.petDevice_bind_url),
						PostRequestFormat.URLENCODED, _petDeviceBindParam,
						null, HttpRequestType.ASYNCHRONOUS,
						new PetDeviceBindHttpRequestListener());
			} else {
				Log.e(LOG_TAG, "Pet device bind error, binded device number = "
						+ _petDeviceBindedDeviceNumber);
			}
		}

	}

	// pet device bind http request listener
	class PetDeviceBindHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// close pet device bind process dialog
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
					// get binded device id and password
					Long _deviceId = JSONUtils.getLongFromJSONObject(
							_respJsonData, "device_id");
					String _devicePwd = JSONUtils.getStringFromJSONObject(
							_respJsonData, "device_password");
					Log.d(LOG_TAG,
							"pet device binded successful, binded device id = "
									+ _deviceId + " and password = "
									+ _devicePwd);

					// get the user
					UserBean _user = UserManager.getInstance().getUser();

					// update my pet bind device id and access password
					IPCUserExtension.setUserPetBindDeviceId(_user, _deviceId);
					IPCUserExtension.setUserPetBindDeviceAcessPwd(_user,
							_devicePwd);

					Toast.makeText(PetDeviceBindActivity.this, "设备绑定成功",
							Toast.LENGTH_SHORT).show();

					// finish pet device bind activity
					finish();
					break;

				case 1:
					Log.d(LOG_TAG,
							"pet device bind failed, the device not existed");

					Toast.makeText(PetDeviceBindActivity.this, "设备不存在",
							Toast.LENGTH_SHORT).show();
					break;

				case 2:
					Log.d(LOG_TAG,
							"pet device bind failed, the device had beed binded with another device");

					Toast.makeText(PetDeviceBindActivity.this, "设备已绑定",
							Toast.LENGTH_SHORT).show();
					break;

				case 3:
				case 4:
				case 5:
					Log.d(LOG_TAG, "pet device bind failed");

					Toast.makeText(PetDeviceBindActivity.this, "设备绑定失败",
							Toast.LENGTH_SHORT).show();
					break;

				default:
					Log.e(LOG_TAG,
							"pet device bind failed, bg_server return result is unrecognized");

					processBindException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"pet device bind failed, bg_server return result is null");

				processBindException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// close pet device bind process dialog
			closeAsyncHttpReqProgressDialog();

			Log.e(LOG_TAG,
					"pet device bind failed, send pet device bind post request failed");

			processBindException();
		}

	}

}
