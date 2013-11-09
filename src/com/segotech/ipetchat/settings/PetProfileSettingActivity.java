package com.segotech.ipetchat.settings;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.richitec.commontoolkit.user.UserManager;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.pet.PetBreed;
import com.segotech.ipetchat.account.pet.PetSex;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.customwidget.PetProfileSettingItem;
import com.segotech.ipetchat.settings.profile.PetProfileCheckedSettingActivity;
import com.segotech.ipetchat.settings.profile.PetProfileDistrictSettingActivity;
import com.segotech.ipetchat.settings.profile.PetProfileEditTextSettingActivity;

public class PetProfileSettingActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = PetProfileSettingActivity.class
			.getCanonicalName();

	// pet profile setting request code
	private static final int PET_PROFILE_NICKNAME_EDITTEXT_SETTING_REQCODE = 200;
	private static final int PET_PROFILE_AGE_EDITTEXT_SETTING_REQCODE = 201;
	private static final int PET_PROFILE_HEIGHT_EDITTEXT_SETTING_REQCODE = 202;
	private static final int PET_PROFILE_WEIGHT_EDITTEXT_SETTING_REQCODE = 203;
	private static final int PET_PROFILE_PLACEUSED2GO_EDITTEXT_SETTING_REQCODE = 204;
	private static final int PET_PROFILE_SEX_CHECKED_SETTING_REQCODE = 206;
	private static final int PET_PROFILE_BREED_CHECKED_SETTING_REQCODE = 207;
	private static final int PET_PROFILE_DISTRICT_SETTING_REQCODE = 209;

	// user pet info
	private PetBean _mPetInfo;

	// pet profile avatar imageView
	private ImageView _mPetProfileAvatarImageView;

	// pet profile setting item: nickname, sex, breed, age, height, weight,
	// district and place used to go
	private PetProfileSettingItem _mPetProfileNicknameSettingItem;
	private PetProfileSettingItem _mPetProfileSexSettingItem;
	private PetProfileSettingItem _mPetProfileBreedSettingItem;
	private PetProfileSettingItem _mPetProfileAgeSettingItem;
	private PetProfileSettingItem _mPetProfileHeightSettingItem;
	private PetProfileSettingItem _mPetProfileWeightSettingItem;
	private PetProfileSettingItem _mPetProfileDistrictSettingItem;
	private PetProfileSettingItem _mPetProfilePlaceUsed2GoSettingItem;

	// pet profile editText setting item on click listener
	private PetProfileEditTextSettingItemOnClickListener _mPetProfileEditTextSettingItemOnClickListener = new PetProfileEditTextSettingItemOnClickListener();

	// pet profile checked setting item on click listener
	private PetProfileCheckedSettingItemOnClickListener _mPetProfileCheckedSettingItemOnClickListener = new PetProfileCheckedSettingItemOnClickListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_profile_setting_activity_layout);

		// set title
		setTitle(R.string.pet_profile_setting_nav_title);

		// set pet profile avatar setting item on click listener
		findViewById(R.id.pet_profile_avatar_setting_item).setOnClickListener(
				new PetProfileAvatarSettingItemOnClickListener());

		// get pet profile avatar imageView
		_mPetProfileAvatarImageView = (ImageView) findViewById(R.id.pet_profile_avatar_imageView);

		// get pet profile item and set its on click listener
		// nickname
		_mPetProfileNicknameSettingItem = (PetProfileSettingItem) findViewById(R.id.pet_profile_nickname_setting_item);
		_mPetProfileNicknameSettingItem
				.setOnClickListener(_mPetProfileEditTextSettingItemOnClickListener);

		// sex
		_mPetProfileSexSettingItem = (PetProfileSettingItem) findViewById(R.id.pet_profile_sex_setting_item);
		_mPetProfileSexSettingItem
				.setOnClickListener(_mPetProfileCheckedSettingItemOnClickListener);

		// breed
		_mPetProfileBreedSettingItem = (PetProfileSettingItem) findViewById(R.id.pet_profile_breed_setting_item);
		_mPetProfileBreedSettingItem
				.setOnClickListener(_mPetProfileCheckedSettingItemOnClickListener);

		// age
		_mPetProfileAgeSettingItem = (PetProfileSettingItem) findViewById(R.id.pet_profile_age_setting_item);
		_mPetProfileAgeSettingItem
				.setOnClickListener(_mPetProfileEditTextSettingItemOnClickListener);

		// height
		_mPetProfileHeightSettingItem = (PetProfileSettingItem) findViewById(R.id.pet_profile_height_setting_item);
		_mPetProfileHeightSettingItem
				.setOnClickListener(_mPetProfileEditTextSettingItemOnClickListener);

		// weight
		_mPetProfileWeightSettingItem = (PetProfileSettingItem) findViewById(R.id.pet_profile_weight_setting_item);
		_mPetProfileWeightSettingItem
				.setOnClickListener(_mPetProfileEditTextSettingItemOnClickListener);

		// district
		_mPetProfileDistrictSettingItem = (PetProfileSettingItem) findViewById(R.id.pet_profile_district_setting_item);
		_mPetProfileDistrictSettingItem
				.setOnClickListener(new PetProfileDistrictSettingItemOnClickListener());

		// place used to go
		_mPetProfilePlaceUsed2GoSettingItem = (PetProfileSettingItem) findViewById(R.id.pet_profile_placeUsed2Go_setting_item);
		_mPetProfilePlaceUsed2GoSettingItem
				.setOnClickListener(_mPetProfileEditTextSettingItemOnClickListener);

		// get user pet info
		_mPetInfo = IPCUserExtension.getUserPetInfo(UserManager.getInstance()
				.getUser());

		Log.d(LOG_TAG, "my pet info = " + _mPetInfo);

		// check user pet info
		if (null != _mPetInfo) {
			// check and set pet avatar
			if (null != _mPetInfo.getAvatar()) {
				_mPetProfileAvatarImageView.setImageBitmap(BitmapFactory
						.decodeByteArray(_mPetInfo.getAvatar(), 0,
								_mPetInfo.getAvatar().length));
			}

			// check and set pet nickname
			if (null != _mPetInfo.getNickname()) {
				_mPetProfileNicknameSettingItem
						.setText(_mPetInfo.getNickname());
			}

			// check and set pet sex
			if (null != _mPetInfo.getSex()) {
				_mPetProfileSexSettingItem.setText(_mPetInfo.getSex().getSex());
			}

			// check and set pet breed
			if (null != _mPetInfo.getBreed()) {
				_mPetProfileBreedSettingItem.setText(_mPetInfo.getBreed()
						.getBreed());
			}

			// check and set pet age
			if (null != _mPetInfo.getAge()) {
				_mPetProfileAgeSettingItem.setText(String.format(getResources()
						.getString(R.string.pet_age_value_format), _mPetInfo
						.getAge()));
			}

			// check and set pet height
			if (null != _mPetInfo.getHeight()) {
				_mPetProfileHeightSettingItem.setText(String.format(
						getResources().getString(
								R.string.pet_height_value_format),
						_mPetInfo.getHeight()));
			}

			// check and set pet weight
			if (null != _mPetInfo.getWeight()) {
				_mPetProfileWeightSettingItem.setText(String.format(
						getResources().getString(
								R.string.pet_weight_value_format),
						_mPetInfo.getWeight()));
			}

			// check and set pet district
			if (null != _mPetInfo.getDistrict()) {
				_mPetProfileDistrictSettingItem
						.setText(_mPetInfo.getDistrict());
			}

			// check and set pet place used to go
			if (null != _mPetInfo.getPlaceUsed2Go()) {
				_mPetProfilePlaceUsed2GoSettingItem.setText(_mPetInfo
						.getPlaceUsed2Go());
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// check and reset pet info
		_mPetInfo = null != _mPetInfo ? _mPetInfo : new PetBean();

		// check result code
		switch (resultCode) {
		case RESULT_OK:
			// check pop result extra data
			if (null != data) {
				// check request code and set user set pet profile setting item
				switch (requestCode) {
				case PET_PROFILE_NICKNAME_EDITTEXT_SETTING_REQCODE:
					// get setting nickname
					String _nickname = data
							.getStringExtra(POP_RET_EXTRADATA_KEY);

					_mPetProfileNicknameSettingItem.setText(_nickname);
					_mPetInfo.setNickname(_nickname);
					break;

				case PET_PROFILE_SEX_CHECKED_SETTING_REQCODE:
					// get setting sex and its value
					String _sex = data.getStringExtra(POP_RET_EXTRADATA_KEY);
					Integer _sexValue = data
							.getIntExtra(
									PetProfileCheckedSettingActivity.PET_PROFILE_CHECKED_SETTING_CHECKED_INDEX_KEY,
									0);

					_mPetProfileSexSettingItem.setText(_sex);
					_mPetInfo.setSex(PetSex.getSex(_sexValue));
					break;

				case PET_PROFILE_BREED_CHECKED_SETTING_REQCODE:
					// get setting breed and its value
					String _breed = data.getStringExtra(POP_RET_EXTRADATA_KEY);
					Integer _breedValue = data
							.getIntExtra(
									PetProfileCheckedSettingActivity.PET_PROFILE_CHECKED_SETTING_CHECKED_INDEX_KEY,
									0);

					_mPetProfileBreedSettingItem.setText(_breed);
					_mPetInfo.setBreed(PetBreed.getBreed(_breedValue));
					break;

				case PET_PROFILE_AGE_EDITTEXT_SETTING_REQCODE:
					// get setting age
					Integer _age = Integer.parseInt(data
							.getStringExtra(POP_RET_EXTRADATA_KEY));

					_mPetProfileAgeSettingItem.setText(String.format(
							getResources().getString(
									R.string.pet_age_value_format), _age));
					_mPetInfo.setAge(_age);
					break;

				case PET_PROFILE_HEIGHT_EDITTEXT_SETTING_REQCODE:
					// get setting height
					Float _height = Float.parseFloat(data
							.getStringExtra(POP_RET_EXTRADATA_KEY));

					_mPetProfileHeightSettingItem
							.setText(String.format(
									getResources().getString(
											R.string.pet_height_value_format),
									_height));
					_mPetInfo.setHeight(_height);
					break;

				case PET_PROFILE_WEIGHT_EDITTEXT_SETTING_REQCODE:
					// get setting weight
					Float _weight = Float.parseFloat(data
							.getStringExtra(POP_RET_EXTRADATA_KEY));

					_mPetProfileWeightSettingItem
							.setText(String.format(
									getResources().getString(
											R.string.pet_weight_value_format),
									_weight));
					_mPetInfo.setWeight(_weight);
					break;

				case PET_PROFILE_DISTRICT_SETTING_REQCODE:
					// get setting district
					String _district = data
							.getStringExtra(POP_RET_EXTRADATA_KEY);

					_mPetProfileDistrictSettingItem.setText(_district);
					_mPetInfo.setDistrict(_district);
					break;

				case PET_PROFILE_PLACEUSED2GO_EDITTEXT_SETTING_REQCODE:
					// get setting place used to go
					String _placeUsed2Go = data
							.getStringExtra(POP_RET_EXTRADATA_KEY);

					_mPetProfilePlaceUsed2GoSettingItem.setText(_placeUsed2Go);
					_mPetInfo.setPlaceUsed2Go(_placeUsed2Go);
					break;
				}
			}

			// save pet info
			IPCUserExtension.setUserPetInfo(
					UserManager.getInstance().getUser(), _mPetInfo);
			break;

		default:
			// nothing to do
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// inner class
	// pet profile avatar setting item on click listener
	class PetProfileAvatarSettingItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Log.d(LOG_TAG, "PetProfileAvatarSettingItemOnClickListener");

			//
		}

	}

	// pet profile editText setting item on click listener
	class PetProfileEditTextSettingItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// define pet profile setting item, editText text, input type and
			// request code
			PetProfileSettingItem _petProfileSettingItem = null;
			String _editTextText = "";
			int _editTextInputType = InputType.TYPE_CLASS_TEXT;
			int _requestCode = 0;

			// check setting item id then set pet profile setting item, editText
			// text, input type and request code
			switch (v.getId()) {
			case R.id.pet_profile_nickname_setting_item:
				_petProfileSettingItem = _mPetProfileNicknameSettingItem;
				_editTextText = null != (_editTextText = _mPetProfileNicknameSettingItem
						.getText()) ? _editTextText : "";
				_requestCode = PET_PROFILE_NICKNAME_EDITTEXT_SETTING_REQCODE;
				break;

			case R.id.pet_profile_age_setting_item:
				_petProfileSettingItem = _mPetProfileAgeSettingItem;
				_editTextText = null != _mPetInfo && null != _mPetInfo.getAge() ? _mPetInfo
						.getAge().toString() : "";
				_editTextInputType = InputType.TYPE_CLASS_NUMBER;
				_requestCode = PET_PROFILE_AGE_EDITTEXT_SETTING_REQCODE;
				break;

			case R.id.pet_profile_height_setting_item:
				_petProfileSettingItem = _mPetProfileHeightSettingItem;
				_editTextText = null != _mPetInfo
						&& null != _mPetInfo.getHeight() ? _mPetInfo
						.getHeight().toString() : "";
				_editTextInputType = InputType.TYPE_CLASS_NUMBER;
				_requestCode = PET_PROFILE_HEIGHT_EDITTEXT_SETTING_REQCODE;
				break;

			case R.id.pet_profile_weight_setting_item:
				_petProfileSettingItem = _mPetProfileWeightSettingItem;
				_editTextText = null != _mPetInfo
						&& null != _mPetInfo.getWeight() ? _mPetInfo
						.getWeight().toString() : "";
				_editTextInputType = InputType.TYPE_CLASS_NUMBER;
				_requestCode = PET_PROFILE_WEIGHT_EDITTEXT_SETTING_REQCODE;
				break;

			case R.id.pet_profile_placeUsed2Go_setting_item:
				_petProfileSettingItem = _mPetProfilePlaceUsed2GoSettingItem;
				_editTextText = null != (_editTextText = _mPetProfilePlaceUsed2GoSettingItem
						.getText()) ? _editTextText : "";
				_requestCode = PET_PROFILE_PLACEUSED2GO_EDITTEXT_SETTING_REQCODE;
				break;
			}

			// define extra data
			Map<String, Object> _extraData = new HashMap<String, Object>();

			// check pet profile setting item and set extra data
			if (null != _petProfileSettingItem) {
				_extraData
						.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_TITLE_KEY,
								_petProfileSettingItem.getLabel());
				_extraData
						.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_HINT_KEY,
								_petProfileSettingItem.getLabel());
				_extraData
						.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_TEXT_KEY,
								_editTextText);
				_extraData
						.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_INPUTTYPE_KEY,
								_editTextInputType);
			}

			// go to target activity
			pushActivityForResult(PetProfileEditTextSettingActivity.class,
					_extraData, _requestCode);
		}
	}

	// pet profile checked setting item on click listener
	class PetProfileCheckedSettingItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// define pet profile checked setting item, its index, sub items and
			// request code
			PetProfileSettingItem _petProfileSettingItem = null;
			int _petProfileCheckedSettingItemIndex = 0;
			String[] _petProfileCheckedSettingItemSubItems = null;
			int _requestCode = 0;

			// check setting item id then set pet profile checked setting item,
			// its index, sub items and request code
			switch (v.getId()) {
			case R.id.pet_profile_sex_setting_item:
				_petProfileSettingItem = _mPetProfileSexSettingItem;
				_petProfileCheckedSettingItemIndex = (null != _mPetInfo && null != _mPetInfo
						.getSex()) ? _mPetInfo.getSex().getValue() : -1;
				_petProfileCheckedSettingItemSubItems = getResources()
						.getStringArray(R.array.pet_sex_array);
				_requestCode = PET_PROFILE_SEX_CHECKED_SETTING_REQCODE;
				break;

			case R.id.pet_profile_breed_setting_item:
				_petProfileSettingItem = _mPetProfileBreedSettingItem;
				_petProfileCheckedSettingItemIndex = (null != _mPetInfo && null != _mPetInfo
						.getBreed()) ? _mPetInfo.getBreed().getValue() : -1;
				_petProfileCheckedSettingItemSubItems = getResources()
						.getStringArray(R.array.pet_breed_array);
				_requestCode = PET_PROFILE_BREED_CHECKED_SETTING_REQCODE;
				break;
			}

			// define extra data
			Map<String, Object> _extraData = new HashMap<String, Object>();

			// check pet profile setting item, sub items and set extra data
			if (null != _petProfileSettingItem
					&& null != _petProfileCheckedSettingItemSubItems) {
				// put pet profile checked setting name, selected index and sub
				// items in extra data
				_extraData
						.put(PetProfileCheckedSettingActivity.PET_PROFILE_CHECKED_SETTING_NAME_KEY,
								_petProfileSettingItem.getLabel());
				_extraData
						.put(PetProfileCheckedSettingActivity.PET_PROFILE_CHECKED_SETTING_CHECKED_INDEX_KEY,
								_petProfileCheckedSettingItemIndex);
				_extraData
						.put(PetProfileCheckedSettingActivity.PET_PROFILE_CHECKED_SETTING_SUBITEMS_KEY,
								_petProfileCheckedSettingItemSubItems);
			}

			// go to target activity
			pushActivityForResult(PetProfileCheckedSettingActivity.class,
					_extraData, _requestCode);
		}
	}

	// pet profile district setting item on click listener
	class PetProfileDistrictSettingItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// define extra data
			Map<String, String> _extraData = new HashMap<String, String>();

			// define pet profile district setting item text
			String _petProfileDistrictSettingItemText = _mPetProfileDistrictSettingItem
					.getText();

			// put pet profile district in extra data
			_extraData
					.put(PetProfileDistrictSettingActivity.PET_PROFILE_CUSTOM_DISTRICT_VALUE_KEY,
							null != (_petProfileDistrictSettingItemText = _mPetProfileDistrictSettingItem
									.getText()) ? _petProfileDistrictSettingItemText
									: "");

			// go to target activity
			pushActivityForResult(PetProfileDistrictSettingActivity.class,
					_extraData, PET_PROFILE_DISTRICT_SETTING_REQCODE);
		}

	}

}
