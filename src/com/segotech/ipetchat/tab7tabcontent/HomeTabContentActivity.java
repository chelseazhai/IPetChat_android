package com.segotech.ipetchat.tab7tabcontent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.pet.PetSex;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatRootNavigationActivity;
import com.segotech.ipetchat.customwidget.NetLoadImageView;
import com.segotech.ipetchat.utils.DeviceServerHttpReqParamUtils;

public class HomeTabContentActivity extends IPetChatRootNavigationActivity {

	private static final String LOG_TAG = HomeTabContentActivity.class
			.getCanonicalName();

	// // pet location view
	// private View _mPetLocationView;
	//
	// // saved instanceState
	// private Bundle _mSavedInstanceState;
	//
	// // autoNavi mapView and map
	// private MapView _mAutoNaviMapView;
	// private AMap _mAMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// super.onCreate(_mSavedInstanceState = savedInstanceState);
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.home_tab_content_activity_layout);

		// set title
		setTitle(R.string.home_nav_title);

		// bind pet location button on click listener
		((Button) findViewById(R.id.pet_location_button))
				.setOnClickListener(new PetLocationBtnOnClickListener());
	}

	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	//
	// // autoNavi mapView save instance
	// if (null != _mAutoNaviMapView) {
	// _mAutoNaviMapView.onSaveInstanceState(outState);
	// }
	// }
	//
	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	//
	// // autoNavi mapView destroy
	// if (null != _mAutoNaviMapView) {
	// _mAutoNaviMapView.onDestroy();
	// }
	// }
	//
	// @Override
	// protected void onPause() {
	// super.onPause();
	//
	// // autoNavi mapView pause
	// if (null != _mAutoNaviMapView) {
	// _mAutoNaviMapView.onPause();
	// }
	// }

	@Override
	protected void onResume() {
		super.onResume();

		// get my pet info
		PetBean _petInfo = IPCUserExtension.getUserPetInfo(UserManager
				.getInstance().getUser());

		// define pet breed, age, height, weight, district and place used to go
		// string
		StringBuilder _petOtherInfoBreed = new StringBuilder(getResources()
				.getString(R.string.pet_breed_prefix));
		String _petOtherInfoAge = getResources().getString(
				R.string.pet_age_prefix);
		String _petOtherInfoHeight = getResources().getString(
				R.string.pet_height_prefix);
		String _petOtherInfoWeight = getResources().getString(
				R.string.pet_weight_prefix);
		StringBuilder _petOtherInfoDistrict = new StringBuilder(getResources()
				.getString(R.string.pet_district_prefix));
		StringBuilder _petOtherInfoPlaceUsed2Go = new StringBuilder(
				getResources().getString(R.string.pet_placeUsed2Go_prefix));

		// check my pet info
		if (null != _petInfo) {
			// check and set pet avatar
			if (null != _petInfo.getAvatar()) {
				((ImageView) findViewById(R.id.pet_avatar_imageView))
						.setImageBitmap(BitmapFactory.decodeByteArray(
								_petInfo.getAvatar(), 0,
								_petInfo.getAvatar().length));
			} else {
				if (null != _petInfo.getAvatarUrl()) {
					((NetLoadImageView) findViewById(R.id.pet_avatar_imageView))
							.loadUrl(getResources().getString(R.string.img_url)
									+ _petInfo.getAvatarUrl());
				}
			}

			// check and set pet nickname
			if (null != _petInfo.getNickname()) {
				((TextView) findViewById(R.id.pet_nickname_textView))
						.setText(_petInfo.getNickname());
			}

			// check and set pet sex
			if (null != _petInfo.getSex()) {
				((ImageView) findViewById(R.id.pet_sex_imageView))
						.setImageResource(PetSex.MALE == _petInfo.getSex() ? R.drawable.img_male
								: R.drawable.img_female);
			}

			// set pet other info
			// breed
			if (null != _petInfo.getBreed()) {
				_petOtherInfoBreed.append(_petInfo.getBreed().getBreed());
			}

			// age
			if (null != _petInfo.getAge()) {
				_petOtherInfoAge = String.format(
						getResources().getString(R.string.pet_age_format),
						_petInfo.getAge());
			}

			// height
			if (null != _petInfo.getHeight()) {
				_petOtherInfoHeight = String.format(
						getResources().getString(R.string.pet_height_format),
						_petInfo.getHeight());
			}

			// weight
			if (null != _petInfo.getWeight()) {
				_petOtherInfoWeight = String.format(
						getResources().getString(R.string.pet_weight_format),
						_petInfo.getWeight());
			}

			// district
			if (null != _petInfo.getDistrict()) {
				_petOtherInfoDistrict.append(_petInfo.getDistrict());
			}

			// place used to go
			if (null != _petInfo.getPlaceUsed2Go()) {
				_petOtherInfoPlaceUsed2Go.append(_petInfo.getPlaceUsed2Go());
			}
		}

		// set pet other info adapter
		((ListView) findViewById(R.id.pet_info_listView))
				.setAdapter(new ArrayAdapter<String>(this,
						R.layout.pet_info_listview_item_layout, new String[] {
								_petOtherInfoBreed.toString(),
								_petOtherInfoAge, _petOtherInfoHeight,
								_petOtherInfoWeight,
								_petOtherInfoDistrict.toString(),
								_petOtherInfoPlaceUsed2Go.toString() }));

		// test by ares
		// get pet device battery progress
		int _petDeviceBatteryProgress = 88;

		// set pet device battery
		((ProgressBar) findViewById(R.id.pet_deviceBattery_progressBar))
				.setProgress(_petDeviceBatteryProgress);
		((TextView) findViewById(R.id.pet_deviceBattery_textView))
				.setText(String.format("%d%%", _petDeviceBatteryProgress));

		// check pet device bind info
		if (null != IPCUserExtension.getUserPetBindDeviceId(UserManager
				.getInstance().getUser())) {
			// get pet today sports info values
			// first get device server system time
			HttpUtils.getRequest(
					getResources().getString(R.string.deviceServer_url)
							+ getResources().getString(
									R.string.deviceServer_getTime_url), null,
					null, HttpRequestType.ASYNCHRONOUS,
					new GetDeviceServerSystemTimeHttpRequestListener());
		}

		// // autoNavi mapView pause
		// if (null != _mAutoNaviMapView) {
		// _mAutoNaviMapView.onPause();
		// }
	}

	// inner class
	// get device server system time http request listener
	class GetDeviceServerSystemTimeHttpRequestListener extends
			OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get and check http response entity string
			String _respEntityString = HttpUtils
					.getHttpResponseEntityString(response);
			if (null != _respEntityString) {
				// get device server system time
				Long _deviceServerSystemTime = Long
						.parseLong(_respEntityString);

				// get pet today sports info value
				// define get pet today sports info value operation
				JSONObject _getPetTodaySportsInfoValueOperation = new JSONObject();
				try {
					// set get pet today sports info value operation command
					// type
					_getPetTodaySportsInfoValueOperation.put("cmdtype",
							"ARCHIVE_OPERATION");

					// define get pet today sports info value operation archive
					// operation
					JSONObject _getPetTodaySportsInfoValueOperationArchiveOperation = new JSONObject();

					// set get pet today sports info value operation archive
					// operation table name, operation and fields
					_getPetTodaySportsInfoValueOperationArchiveOperation.put(
							"tablename", "dailysummary");
					_getPetTodaySportsInfoValueOperationArchiveOperation.put(
							"operation", "QUERY");
					_getPetTodaySportsInfoValueOperationArchiveOperation.put(
							"field", new String[] { "id", "termid", "daytime",
									"type", "alarmsize", "criticalarm",
									"distance0", "distanceN", "vitality10",
									"vitality1N", "vitality20", "vitality2N",
									"vitality30", "vitality3N", "vitality40",
									"vitality4N" });

					// define get pet today sports info value operation archive
					// operation wherecond
					JSONArray _getPetTodaySportsInfoValueOperationArchiveOperationWherecond = new JSONArray();

					// define get pet today sports info value operation archive
					// operation wherecond begin and end time
					JSONObject _getPetTodaySportsInfoValueOperationArchiveOperationWherecondBeginTime = new JSONObject();
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecondBeginTime
							.put("name", "begintime");
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecondBeginTime
							.put("value", new SimpleDateFormat("yyyy-MM-dd",
									Locale.getDefault()).format(new Date()));
					JSONObject _getPetTodaySportsInfoValueOperationArchiveOperationWherecondEndTime = new JSONObject();
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecondEndTime
							.put("name", "endtime");
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecondEndTime
							.put("value", new SimpleDateFormat("yyyy-MM-dd",
									Locale.getDefault()).format(new Date()));

					// add get pet today sports info value operation archive
					// operation wherecond begin and end time
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecond
							.put(_getPetTodaySportsInfoValueOperationArchiveOperationWherecondBeginTime);
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecond
							.put(_getPetTodaySportsInfoValueOperationArchiveOperationWherecondEndTime);

					// set get pet today sports info value operation archive
					// operation whereconds
					_getPetTodaySportsInfoValueOperationArchiveOperation
							.put("wherecond",
									_getPetTodaySportsInfoValueOperationArchiveOperationWherecond);

					// set get pet today sports info value pet device operation
					// archive operation
					_getPetTodaySportsInfoValueOperation
							.put("archive_operation",
									_getPetTodaySportsInfoValueOperationArchiveOperation);
				} catch (JSONException e) {
					Log.e(LOG_TAG,
							"get pet today sports info value operation error, exception message = "
									+ e.getMessage());

					e.printStackTrace();
				}

				// generate get pet today sports info value param
				Map<String, String> _getPetTodaySportsInfoParam = DeviceServerHttpReqParamUtils
						.generatePetDeviceOperateParam(_deviceServerSystemTime,
								_getPetTodaySportsInfoValueOperation.toString());

				// send get pet pet today sports info value http request
				HttpUtils.postRequest(
						getResources().getString(R.string.deviceServer_url)
								+ getResources().getString(
										R.string.deviceServer_operate_url),
						PostRequestFormat.URLENCODED,
						_getPetTodaySportsInfoParam, null,
						HttpRequestType.ASYNCHRONOUS,
						new GetPetTodaySportsInfoValuesHttpRequestListener());
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// nothing to do
		}

	}

	// get device server pet today sports info values http request listener
	class GetPetTodaySportsInfoValuesHttpRequestListener extends
			OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object status
			String _status = JSONUtils.getStringFromJSONObject(_respJsonData,
					"status");
			// check an process result
			if (null != _status && "SUCCESS".equalsIgnoreCase(_status)) {
				// get and check pet today sports info value operation return
				// archive operation
				JSONObject _getPetTodaySportsInfoValueOperationRetArchiveOperation = JSONUtils
						.getJSONObjectFromJSONObject(_respJsonData,
								"archive_operation");
				if (null != _getPetTodaySportsInfoValueOperationRetArchiveOperation) {
					// get and check pet today sports info value operation
					// return archive operation daily summary
					JSONArray _getPetTodaySportsInfoValueOperationRetArchiveOperationDailySummary = JSONUtils
							.getJSONArrayFromJSONObject(
									_getPetTodaySportsInfoValueOperationRetArchiveOperation,
									"daily_summary");
					if (null != _getPetTodaySportsInfoValueOperationRetArchiveOperationDailySummary
							&& 0 < _getPetTodaySportsInfoValueOperationRetArchiveOperationDailySummary
									.length()) {
						// get and check pet today sports info value operation
						// return archive operation today summary
						JSONObject _getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary = JSONUtils
								.getJSONObjectFromJSONArray(
										_getPetTodaySportsInfoValueOperationRetArchiveOperationDailySummary,
										_getPetTodaySportsInfoValueOperationRetArchiveOperationDailySummary
												.length() - 1);

						// get pet today sports info rest, walk, run and
						// strenuousExercise value
						Double _rest = JSONUtils
								.getDoubleFromJSONObject(
										_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
										"vitality1N")
								- JSONUtils
										.getDoubleFromJSONObject(
												_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
												"vitality10");
						Double _walk = JSONUtils
								.getDoubleFromJSONObject(
										_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
										"vitality2N")
								- JSONUtils
										.getDoubleFromJSONObject(
												_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
												"vitality20");
						Double _run = JSONUtils
								.getDoubleFromJSONObject(
										_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
										"vitality3N")
								- JSONUtils
										.getDoubleFromJSONObject(
												_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
												"vitality30");
						Double _strenuousExercise = JSONUtils
								.getDoubleFromJSONObject(
										_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
										"vitality4N")
								- JSONUtils
										.getDoubleFromJSONObject(
												_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
												"vitality40");

						// get total
						Double _total = (_rest + _walk + _run + _strenuousExercise) / 100;

						// define pet sports score
						Double _petSportsScore = 100 * 2 * (0.9
								* (_rest / _total) / 100 + 1.2
								* (_walk / _total) / 100 + 1.6
								* (_run / _total) / 100 + 1.8 * (_strenuousExercise / _total) / 100);

						// set pet sports score
						((ProgressBar) findViewById(R.id.pet_sportsScore_progressBar))
								.setProgress((int) (100 * _petSportsScore / 360));
						((TextView) findViewById(R.id.pet_sportsScore_textView))
								.setText(_petSportsScore.toString());
					}
				}
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// nothing to do
		}

	}

	// pet location button on click listener
	class PetLocationBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			// // hide pet info view
			// findViewById(R.id.pet_info_viewGroup).setVisibility(View.GONE);
			//
			// // show pet location view if needed
			// if (null == _mPetLocationView) {
			// // inflate pet location viewStub
			// _mPetLocationView = ((ViewStub)
			// findViewById(R.id.pet_location_viewStub))
			// .inflate();
			//
			// // get autoNavi mapView
			// _mAutoNaviMapView = (MapView)
			// findViewById(R.id.pet_location_mapView);
			//
			// // create autoNavi mapView
			// _mAutoNaviMapView.onCreate(_mSavedInstanceState);
			//
			// // init AMap
			// if (null == _mAMap) {
			// _mAMap = _mAutoNaviMapView.getMap();
			// }
			//
			// // bind pet info button on click listener
			// ((Button) findViewById(R.id.pet_info_button))
			// .setOnClickListener(new PetInfoBtnOnClickListener());
			// } else {
			// if (View.VISIBLE != _mPetLocationView.getVisibility()) {
			// _mPetLocationView.setVisibility(View.VISIBLE);
			// }
			// }

			// check pet bind device id
			if (null != IPCUserExtension.getUserPetBindDeviceId(UserManager
					.getInstance().getUser())) {
				// go to my pet location activity
				pushActivity(PetLocationActivity.class);
			} else {
				Toast.makeText(HomeTabContentActivity.this,
						"您的宠物未绑定便携设备，请先绑定宠物便携设备", Toast.LENGTH_SHORT).show();
			}
		}

	}

	// // pet info button on click listener
	// class PetInfoBtnOnClickListener implements OnClickListener {
	//
	// @Override
	// public void onClick(View button) {
	// // get pet info view
	// View _petInfoView = findViewById(R.id.pet_info_viewGroup);
	//
	// // show pet info view if needed
	// if (View.VISIBLE != _petInfoView.getVisibility()) {
	// _petInfoView.setVisibility(View.VISIBLE);
	// }
	//
	// // hide pet location view
	// _mPetLocationView.setVisibility(View.GONE);
	// }
	//
	// }

}
