package com.segotech.ipetchat.settings;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.richitec.commontoolkit.user.UserManager;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.customwidget.PetProfileItem;

public class PetProfileSettingActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = PetProfileSettingActivity.class
			.getCanonicalName();

	// pet profile avatar imageView
	private ImageView _mPetProfileImageView;

	// pet profile item: nickname, sex, breed, age, height, weight, district and
	// place used to go
	private PetProfileItem _mPetProfileNicknameItem;
	private PetProfileItem _mPetProfileSexItem;
	private PetProfileItem _mPetProfileBreedItem;
	private PetProfileItem _mPetProfileAgeItem;
	private PetProfileItem _mPetProfileHeightItem;
	private PetProfileItem _mPetProfileWeightItem;
	private PetProfileItem _mPetProfileDistrictItem;
	private PetProfileItem _mPetProfilePlaceUsed2GoItem;

	// pet profile editText item onclick listener
	private PetProfileEditTextItemOnClickListener _mPetProfileEditTextItemOnClickListener = new PetProfileEditTextItemOnClickListener();

	// pet profile checked item onclick listener
	private PetProfileCheckedItemOnClickListener _mPetProfileCheckedItemOnClickListener = new PetProfileCheckedItemOnClickListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_profile_setting_activity_layout);

		// set title
		setTitle(R.string.pet_profile_setting_nav_title);

		// set pet profile avatar item on click listener
		findViewById(R.id.pet_profile_avatar_item).setOnClickListener(
				new PetProfileAvatarItemOnClickListener());

		// get pet profile avatar imageView
		_mPetProfileImageView = (ImageView) findViewById(R.id.pet_profile_avatar_imageView);

		// get pet profile item and set its on click listener
		// nickname
		_mPetProfileNicknameItem = (PetProfileItem) findViewById(R.id.pet_profile_nickname_item);
		_mPetProfileNicknameItem
				.setOnClickListener(_mPetProfileEditTextItemOnClickListener);

		// sex
		_mPetProfileSexItem = (PetProfileItem) findViewById(R.id.pet_profile_sex_item);
		_mPetProfileSexItem
				.setOnClickListener(_mPetProfileCheckedItemOnClickListener);

		// breed
		_mPetProfileBreedItem = (PetProfileItem) findViewById(R.id.pet_profile_breed_item);
		_mPetProfileBreedItem
				.setOnClickListener(_mPetProfileCheckedItemOnClickListener);

		// age
		_mPetProfileAgeItem = (PetProfileItem) findViewById(R.id.pet_profile_age_item);
		_mPetProfileAgeItem
				.setOnClickListener(_mPetProfileEditTextItemOnClickListener);

		// height
		_mPetProfileHeightItem = (PetProfileItem) findViewById(R.id.pet_profile_height_item);
		_mPetProfileHeightItem
				.setOnClickListener(_mPetProfileEditTextItemOnClickListener);

		// weight
		_mPetProfileWeightItem = (PetProfileItem) findViewById(R.id.pet_profile_weight_item);
		_mPetProfileWeightItem
				.setOnClickListener(_mPetProfileEditTextItemOnClickListener);

		// district
		_mPetProfileDistrictItem = (PetProfileItem) findViewById(R.id.pet_profile_district_item);
		_mPetProfileDistrictItem
				.setOnClickListener(new PetProfileDistrictItemOnClickListener());

		// place used to go
		_mPetProfilePlaceUsed2GoItem = (PetProfileItem) findViewById(R.id.pet_profile_placeUsed2Go_item);
		_mPetProfilePlaceUsed2GoItem
				.setOnClickListener(_mPetProfileEditTextItemOnClickListener);

		// get user pet info
		PetBean _petInfo = IPCUserExtension.getUserPetInfo(UserManager
				.getInstance().getUser());

		Log.d(LOG_TAG, "my pet info = " + _petInfo);

		// check user pet info
		if (null != _petInfo) {
			// check and set pet avatar
			if (null != _petInfo.getAvatar()) {
				_mPetProfileImageView.setImageBitmap(BitmapFactory
						.decodeByteArray(_petInfo.getAvatar(), 0,
								_petInfo.getAvatar().length));
			}

			// set pet nickname
			_mPetProfileNicknameItem.setText(_petInfo.getNickname());

			// set pet sex
			_mPetProfileSexItem.setText(_petInfo.getSex().getSex());

			// set pet breed
			_mPetProfileBreedItem.setText(_petInfo.getBreed().getBreed());

			// set pet age
			_mPetProfileAgeItem.setText(String.format(
					getResources().getString(R.string.pet_age_value_format),
					_petInfo.getAge()));

			// set pet height
			_mPetProfileHeightItem.setText(String.format(getResources()
					.getString(R.string.pet_height_value_format), _petInfo
					.getHeight()));

			// set pet weight
			_mPetProfileWeightItem.setText(String.format(getResources()
					.getString(R.string.pet_weight_value_format), _petInfo
					.getWeight()));

			// set pet district
			_mPetProfileDistrictItem.setText(_petInfo.getDistrict());

			// set pet place used to go
			_mPetProfilePlaceUsed2GoItem.setText(_petInfo.getPlaceUsed2Go());
		}
	}

	// inner class
	// pet profile avatar item on click listener
	class PetProfileAvatarItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Log.d(LOG_TAG, "PetProfileAvatarItemOnClickListener");

			//
		}

	}

	// pet profile editText item on click listener
	class PetProfileEditTextItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Log.d(LOG_TAG, "PetProfileEditTextItemOnClickListener, item id = "
					+ v.getId());

			//
		}

	}

	// pet profile checked item on click listener
	class PetProfileCheckedItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Log.d(LOG_TAG, "PetProfileCheckedItemOnClickListener, item id = "
					+ v.getId());

			//
		}

	}

	// pet profile district item on click listener
	class PetProfileDistrictItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Log.d(LOG_TAG, "PetProfileDistrictItemOnClickListener");

			//
		}

	}

}
