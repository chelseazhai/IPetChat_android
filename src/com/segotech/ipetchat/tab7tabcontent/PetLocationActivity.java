package com.segotech.ipetchat.tab7tabcontent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

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

			// get my pet location latitude and longitude(sample: Nanjing city,
			// hunan road)
			LatLng _petLocationLatLng = new LatLng(32.06886390393097,
					118.77526876415256);

			// set regeocode asynchronous request
			_mGeocoderSearch.getFromLocationAsyn(new RegeocodeQuery(
					new LatLonPoint(_petLocationLatLng.latitude,
							_petLocationLatLng.longitude), 0,
					GeocodeSearch.AMAP));

			// autoNavi map move to pet location and zoom to 18.0
			_mAMap.moveCamera(CameraUpdateFactory
					.changeLatLng(_petLocationLatLng));
			_mAMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));

			// generate my pet location point maker
			_mPetLocationMaker = _mAMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 0.5f)
					.position(_petLocationLatLng)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.img_pet_location_marker)));

			// add it to map info window
			_mPetLocationMaker.showInfoWindow();
		}

		@Override
		public void deactivate() {
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

			Log.d(LOG_TAG, "Regeocode address = "
					+ result.getRegeocodeAddress().getFormatAddress());

			// set pet location maker title
			_mPetLocationMaker.setTitle(result.getRegeocodeAddress()
					.getFormatAddress());
		}

	}

}
