package com.segotech.ipetchat.tab7tabcontent;

import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.utils.DeviceServerHttpReqParamUtils;

public class PetLocationActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = PetLocationActivity.class
			.getCanonicalName();

	// pet name key
	public static final String PET_NAME_KEY = "pet_name_key";

	// autoNavi mapView and map
	private MapView _mAutoNaviMapView;
	private AMap _mAMap;

	// my pet location maker
	private Marker _mPetLocationMaker;

	// autoNavi map geo code search
	private GeocodeSearch _mGeocoderSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_location_activity_layout);

		// set title
		setTitle(String.format(
				getResources()
						.getString(R.string.pet_location_nav_title_format),
				IPCUserExtension.getUserPetInfo(
						UserManager.getInstance().getUser()).getNickname()));

		// hide pet info button
		findViewById(R.id.pet_info_button).setVisibility(View.GONE);

		// get autoNavi mapView
		_mAutoNaviMapView = (MapView) findViewById(R.id.pet_location_mapView);

		// create autoNavi mapView
		_mAutoNaviMapView.onCreate(savedInstanceState);

		// initialize AMap
		if (null == _mAMap) {
			_mAMap = _mAutoNaviMapView.getMap();
		}

		// initialize geo coder search
		_mGeocoderSearch = new GeocodeSearch(this);

		// set its on geo code search listener
		_mGeocoderSearch
				.setOnGeocodeSearchListener(new AutoNaviGeocodeSearchOnGeocodeSearchListener());

		// set autoNavi map location source
		_mAMap.setLocationSource(new AutoNaviMapLocationSource());

		// set my pet location button and enable my pet location
		_mAMap.getUiSettings().setMyLocationButtonEnabled(true);
		_mAMap.setMyLocationEnabled(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// autoNavi mapView save instance
		_mAutoNaviMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// autoNavi mapView pause
		_mAutoNaviMapView.onPause();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// autoNavi mapView pause
		_mAutoNaviMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// autoNavi mapView destroy
		_mAutoNaviMapView.onDestroy();
	}

	// inner class
	// autoNavi map location source
	class AutoNaviMapLocationSource implements LocationSource {

		@Override
		public void activate(OnLocationChangedListener listener) {
			Log.d(LOG_TAG, "AutoNavi map locate my pet location");

			// check pet device bind info
			if (null != IPCUserExtension.getUserPetBindDeviceId(UserManager
					.getInstance().getUser())) {
				// get pet location
				// first get device server system time
				HttpUtils.getRequest(
						getResources().getString(R.string.deviceServer_url)
								+ getResources().getString(
										R.string.deviceServer_getTime_url),
						null, null, HttpRequestType.ASYNCHRONOUS,
						new GetDeviceServerSystemTimeHttpRequestListener());
			}
		}

		@Override
		public void deactivate() {
			// nothing to do
		}

	}

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

				// get pet location
				// define get pet location operation
				JSONObject _getPetLocationOperation = new JSONObject();
				try {
					// set get pet location operation command type
					_getPetLocationOperation
							.put("cmdtype", "ARCHIVE_OPERATION");

					// define get pet location operation archive operation
					JSONObject _getPetLocationOperationArchiveOperation = new JSONObject();

					// set get pet location operation archive operation table
					// name, operation and fields
					_getPetLocationOperationArchiveOperation.put("tablename",
							"latestsdata");
					_getPetLocationOperationArchiveOperation.put("operation",
							"QUERY");
					_getPetLocationOperationArchiveOperation.put("field",
							new String[] { "id", "termid", "x", "y", "posid",
									"speed", "height", "direction", "termtime",
									"servtime", "ioport", "status", "alarm",
									"fence", "fenceid", "mainvoltage",
									"cellvoltage", "temperature", "distance",
									"vitality", "address" });

					// set get pet location pet device operation archive
					// operation
					_getPetLocationOperation.put("archive_operation",
							_getPetLocationOperationArchiveOperation);
				} catch (JSONException e) {
					Log.e(LOG_TAG,
							"get pet location operation error, exception message = "
									+ e.getMessage());

					e.printStackTrace();
				}

				// generate get pet location param
				Map<String, String> _getPetLocationParam = DeviceServerHttpReqParamUtils
						.generatePetDeviceOperateParam(_deviceServerSystemTime,
								_getPetLocationOperation.toString());

				// send get pet location http request
				HttpUtils.postRequest(
						getResources().getString(R.string.deviceServer_url)
								+ getResources().getString(
										R.string.deviceServer_operate_url),
						PostRequestFormat.URLENCODED, _getPetLocationParam,
						null, HttpRequestType.ASYNCHRONOUS,
						new GetPetLocationHttpRequestListener());
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// nothing to do
		}

	}

	// get pet location http request listener
	class GetPetLocationHttpRequestListener extends OnHttpRequestListener {

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
				// get and check pet location operation return archive operation
				JSONObject _getPetLocationOperationRetArchiveOperation = JSONUtils
						.getJSONObjectFromJSONObject(_respJsonData,
								"archive_operation");
				if (null != _getPetLocationOperationRetArchiveOperation) {
					// get and check pet location operation return archive
					// operation track datas
					JSONArray _getPetLocationOperationRetArchiveOperationTrackDatas = JSONUtils
							.getJSONArrayFromJSONObject(
									_getPetLocationOperationRetArchiveOperation,
									"track_sdata");
					if (null != _getPetLocationOperationRetArchiveOperationTrackDatas
							&& 0 < _getPetLocationOperationRetArchiveOperationTrackDatas
									.length()) {
						// get and check pet location operation return archive
						// operation track data
						JSONObject _getPetLocationOperationRetArchiveOperationTrackData = JSONUtils
								.getJSONObjectFromJSONArray(
										_getPetLocationOperationRetArchiveOperationTrackDatas,
										0);

						// get and check pet location operation return archive
						// operation track data latitude, longitude
						LatLng _petLocationLatLng = new LatLng(
								JSONUtils.getDoubleFromJSONObject(
										_getPetLocationOperationRetArchiveOperationTrackData,
										"y") / 1000000,
								JSONUtils
										.getDoubleFromJSONObject(
												_getPetLocationOperationRetArchiveOperationTrackData,
												"x") / 1000000);

						// set regeocode asynchronous request
						_mGeocoderSearch
								.getFromLocationAsyn(new RegeocodeQuery(
										new LatLonPoint(
												_petLocationLatLng.latitude,
												_petLocationLatLng.longitude),
										0, GeocodeSearch.AMAP));

						// autoNavi map move to pet location and zoom to 18.0
						_mAMap.moveCamera(CameraUpdateFactory
								.changeLatLng(_petLocationLatLng));
						_mAMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));

						// generate my pet location point maker
						_mAMap.clear();
						_mPetLocationMaker = _mAMap
								.addMarker(new MarkerOptions()
										.anchor(0.5f, 0.5f)
										.position(_petLocationLatLng)
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.img_pet_location_marker)));

						// add it to map info window
						_mPetLocationMaker.showInfoWindow();
					}
				}
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// nothing to do
		}

	}

	// autoNavi map geo code search on geo code search listener
	class AutoNaviGeocodeSearchOnGeocodeSearchListener implements
			OnGeocodeSearchListener {

		@Override
		public void onGeocodeSearched(GeocodeResult result, int code) {
			// nothing to do
		}

		@Override
		public void onRegeocodeSearched(RegeocodeResult result, int code) {
			Log.d(LOG_TAG, "onRegeocodeSearched - result = " + result
					+ " and code = " + code);

			// check the code
			if (0 == code) {
				// check the result
				if (result != null
						&& result.getRegeocodeAddress() != null
						&& result.getRegeocodeAddress().getFormatAddress() != null) {
					Log.d(LOG_TAG, "Regeocode address = "
							+ result.getRegeocodeAddress().getFormatAddress());

					// set pet location maker title
					_mPetLocationMaker.setTitle(result.getRegeocodeAddress()
							.getFormatAddress());
					// Toast.makeText(PetLocationActivity.this,
					// result.getRegeocodeAddress().getFormatAddress(),
					// Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							PetLocationActivity.this,
							getResources().getString(
									R.string.toast_autoNaviMap_reGeo_noResult),
							Toast.LENGTH_SHORT).show();
				}
			} else if (27 == code) {
				Toast.makeText(
						PetLocationActivity.this,
						getResources().getString(
								R.string.toast_autoNaviMap_reGeo_error_network),
						Toast.LENGTH_SHORT).show();
			} else if (32 == code) {
				Toast.makeText(
						PetLocationActivity.this,
						getResources().getString(
								R.string.toast_autoNaviMap_reGeo_error_key),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						PetLocationActivity.this,
						getResources().getString(
								R.string.toast_autoNaviMap_reGeo_error_other),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

}
