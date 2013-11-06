package com.segotech.ipetchat.tab7tabcontent;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.richitec.commontoolkit.user.UserManager;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.pet.PetSex;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatRootNavigationActivity;

public class HomeTabContentActivity extends IPetChatRootNavigationActivity {

	private static final String LOG_TAG = HomeTabContentActivity.class
			.getCanonicalName();

	// pet location view
	private View _mPetLocationView;

	// saved instanceState
	private Bundle _mSavedInstanceState;

	// autoNavi mapView and map
	private MapView _mAutoNaviMapView;
	private AMap _mAMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(_mSavedInstanceState = savedInstanceState);

		// set content view
		setContentView(R.layout.home_tab_content_activity_layout);

		// set title
		setTitle(R.string.home_nav_title);

		// bind pet location button on click listener
		((Button) findViewById(R.id.pet_location_button))
				.setOnClickListener(new PetLocationBtnOnClickListener());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// autoNavi mapView save instance
		if (null != _mAutoNaviMapView) {
			_mAutoNaviMapView.onSaveInstanceState(outState);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// autoNavi mapView destroy
		if (null != _mAutoNaviMapView) {
			_mAutoNaviMapView.onDestroy();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// autoNavi mapView pause
		if (null != _mAutoNaviMapView) {
			_mAutoNaviMapView.onPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// get user pet info
		PetBean _petInfo = IPCUserExtension.getUserPetInfo(UserManager
				.getInstance().getUser());

		Log.d(LOG_TAG, "my pet info = " + _petInfo);

		// check user pet info
		if (null != _petInfo) {
			// set pet avatar
			((ImageView) findViewById(R.id.pet_avatar_imageView))
					.setImageBitmap(BitmapFactory.decodeByteArray(
							_petInfo.getAvatar(), 0,
							_petInfo.getAvatar().length));

			// set pet nickname
			((TextView) findViewById(R.id.pet_nickname_textView))
					.setText(_petInfo.getNickname());

			// set pet sex
			((ImageView) findViewById(R.id.pet_sex_imageView))
					.setImageResource(PetSex.MALE == _petInfo.getSex() ? R.drawable.img_male
							: R.drawable.img_female);

			// test by ares
			// get pet device battery progress
			int _petDeviceBatteryProgress = 86;

			// set pet device battery
			((ProgressBar) findViewById(R.id.pet_deviceBattery_progressBar))
					.setProgress(_petDeviceBatteryProgress);
			((TextView) findViewById(R.id.pet_deviceBattery_textView))
					.setText(String.format("%d%%", _petDeviceBatteryProgress));

			// test by ares
			// get pet sports score progress
			int _petSportsScoreProgress = 95;

			// set pet sports score
			((ProgressBar) findViewById(R.id.pet_sportsScore_progressBar))
					.setProgress(_petSportsScoreProgress);
			((TextView) findViewById(R.id.pet_sportsScore_textView)).setText(""
					+ _petSportsScoreProgress);

			// define pet other info list
			List<String> _petOtherInfoList = new ArrayList<String>();

			// set them
			// breed
			_petOtherInfoList.add(getResources().getString(
					R.string.pet_breed_prefix)
					+ _petInfo.getBreed());

			// age
			_petOtherInfoList.add(String.format(
					getResources().getString(R.string.pet_age_format),
					_petInfo.getAge()));

			// height
			_petOtherInfoList.add(String.format(
					getResources().getString(R.string.pet_height_format),
					_petInfo.getHeight()));

			// weight
			_petOtherInfoList.add(String.format(
					getResources().getString(R.string.pet_weight_format),
					_petInfo.getWeight()));

			// district
			_petOtherInfoList.add(getResources().getString(
					R.string.pet_district_prefix)
					+ _petInfo.getDistrict());

			// place used to go
			_petOtherInfoList.add(getResources().getString(
					R.string.pet_placeUsed2Go_prefix)
					+ _petInfo.getPlaceUsed2Go());

			// set pet info listView adapter
			((ListView) findViewById(R.id.pet_info_listView))
					.setAdapter(new ArrayAdapter<String>(this,
							R.layout.pet_info_listview_item_layout,
							_petOtherInfoList));
		}

		// autoNavi mapView pause
		if (null != _mAutoNaviMapView) {
			_mAutoNaviMapView.onPause();
		}
	}

	// inner class
	// pet location button on click listener
	class PetLocationBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			// hide pet info view
			findViewById(R.id.pet_info_viewGroup).setVisibility(View.GONE);

			// show pet location view if needed
			if (null == _mPetLocationView) {
				// inflate pet location viewStub
				_mPetLocationView = ((ViewStub) findViewById(R.id.pet_location_viewStub))
						.inflate();

				// get autoNavi mapView
				_mAutoNaviMapView = (MapView) findViewById(R.id.pet_location_mapView);

				// create autoNavi mapView
				_mAutoNaviMapView.onCreate(_mSavedInstanceState);

				// init AMap
				if (null == _mAMap) {
					_mAMap = _mAutoNaviMapView.getMap();
				}

				// bind pet info button on click listener
				((Button) findViewById(R.id.pet_info_button))
						.setOnClickListener(new PetInfoBtnOnClickListener());
			} else {
				if (View.VISIBLE != _mPetLocationView.getVisibility()) {
					_mPetLocationView.setVisibility(View.VISIBLE);
				}
			}
		}

	}

	// pet info button on click listener
	class PetInfoBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View button) {
			// get pet info view
			View _petInfoView = findViewById(R.id.pet_info_viewGroup);

			// show pet info view if needed
			if (View.VISIBLE != _petInfoView.getVisibility()) {
				_petInfoView.setVisibility(View.VISIBLE);
			}

			// hide pet location view
			_mPetLocationView.setVisibility(View.GONE);
		}

	}

}
