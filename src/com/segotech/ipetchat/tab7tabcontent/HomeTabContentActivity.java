package com.segotech.ipetchat.tab7tabcontent;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.richitec.commontoolkit.user.UserManager;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.pet.PetSex;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatRootNavigationActivity;
import com.segotech.ipetchat.customwidget.NetLoadImageView;

public class HomeTabContentActivity extends IPetChatRootNavigationActivity {

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
		int _petDeviceBatteryProgress = 0;

		// set pet device battery
		((ProgressBar) findViewById(R.id.pet_deviceBattery_progressBar))
				.setProgress(_petDeviceBatteryProgress);
		((TextView) findViewById(R.id.pet_deviceBattery_textView))
				.setText(String.format("%d%%", _petDeviceBatteryProgress));

		// test by ares
		// get pet sports score progress
		int _petSportsScoreProgress = 0;

		// set pet sports score
		((ProgressBar) findViewById(R.id.pet_sportsScore_progressBar))
				.setProgress(_petSportsScoreProgress);
		((TextView) findViewById(R.id.pet_sportsScore_textView)).setText(""
				+ _petSportsScoreProgress);

		// // autoNavi mapView pause
		// if (null != _mAutoNaviMapView) {
		// _mAutoNaviMapView.onPause();
		// }
	}

	// inner class
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

			// go to my pet location activity
			pushActivity(PetLocationActivity.class);
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
