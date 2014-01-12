package com.segotech.ipetchat.settings;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.CTPopupWindow;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.pet.PetBreed;
import com.segotech.ipetchat.account.pet.PetSex;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.customwidget.NetLoadImageView;
import com.segotech.ipetchat.customwidget.PetProfileSettingItem;
import com.segotech.ipetchat.settings.profile.PetProfileBirthdaySettingActivity;
import com.segotech.ipetchat.settings.profile.PetProfileCheckedSettingActivity;
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

	// pet avatar upload
	private static final int CAPTURE_PHOTO = 600;
	private static final int SELECT_PHOTO = 601;
	private static final int CROP_IMAGE = 602;

	// my pet info
	private PetBean _mPetInfo;

	// pet profile avatar imageView
	private ImageView _mPetProfileAvatarImageView;

	private Bitmap tmpBitmap;

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

	// pet avatar upload photo source select popup window
	private PetAvatarUploadPhotoSourceSelectPopupWindow _mPetAvatarUploadPhotoSourceSelectPopupWindow = new PetAvatarUploadPhotoSourceSelectPopupWindow(
			R.layout.petavatar_uploadphotosource_select_popupwindow_layout,
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	// pet profile editText setting item on click listener
	private PetProfileEditTextSettingItemOnClickListener _mPetProfileEditTextSettingItemOnClickListener = new PetProfileEditTextSettingItemOnClickListener();

	// pet profile checked setting item on click listener
	private PetProfileCheckedSettingItemOnClickListener _mPetProfileCheckedSettingItemOnClickListener = new PetProfileCheckedSettingItemOnClickListener();

	// pet profile birthday setting item on click listener
	private PetProfileBirthdaySettingItemOnClickListener _mPetProfileBirthdaySettingItemOnClickListener = new PetProfileBirthdaySettingItemOnClickListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_profile_setting_activity_layout);

		// set title
		setTitle(R.string.pet_profile_setting_nav_title);

		// set pet profile avatar setting item on click listener
		findViewById(R.id.pp_avatar_setting_item).setOnClickListener(
				new PetProfileAvatarSettingItemOnClickListener());

		// get pet profile avatar imageView
		_mPetProfileAvatarImageView = (ImageView) findViewById(R.id.pp_avatar_imageView);

		// get pet profile item and set its on click listener
		// nickname
		_mPetProfileNicknameSettingItem = (PetProfileSettingItem) findViewById(R.id.pp_nickname_setting_item);
		_mPetProfileNicknameSettingItem
				.setOnClickListener(_mPetProfileEditTextSettingItemOnClickListener);

		// sex
		_mPetProfileSexSettingItem = (PetProfileSettingItem) findViewById(R.id.pp_sex_setting_item);
		_mPetProfileSexSettingItem
				.setOnClickListener(_mPetProfileCheckedSettingItemOnClickListener);

		// breed
		_mPetProfileBreedSettingItem = (PetProfileSettingItem) findViewById(R.id.pp_breed_setting_item);
		_mPetProfileBreedSettingItem
				.setOnClickListener(_mPetProfileCheckedSettingItemOnClickListener);

		// age
		_mPetProfileAgeSettingItem = (PetProfileSettingItem) findViewById(R.id.pp_age_setting_item);
		_mPetProfileAgeSettingItem
				.setOnClickListener(_mPetProfileBirthdaySettingItemOnClickListener);

		// height
		_mPetProfileHeightSettingItem = (PetProfileSettingItem) findViewById(R.id.pp_height_setting_item);
		_mPetProfileHeightSettingItem
				.setOnClickListener(_mPetProfileEditTextSettingItemOnClickListener);

		// weight
		_mPetProfileWeightSettingItem = (PetProfileSettingItem) findViewById(R.id.pp_weight_setting_item);
		_mPetProfileWeightSettingItem
				.setOnClickListener(_mPetProfileEditTextSettingItemOnClickListener);

		// district
		_mPetProfileDistrictSettingItem = (PetProfileSettingItem) findViewById(R.id.pp_district_setting_item);
		_mPetProfileDistrictSettingItem
				.setOnClickListener(new PetProfileDistrictSettingItemOnClickListener());

		// place used to go
		_mPetProfilePlaceUsed2GoSettingItem = (PetProfileSettingItem) findViewById(R.id.pp_placeUsed2Go_setting_item);
		_mPetProfilePlaceUsed2GoSettingItem
				.setOnClickListener(_mPetProfileEditTextSettingItemOnClickListener);

		// get my pet info
		_mPetInfo = IPCUserExtension.getUserPetInfo(UserManager.getInstance()
				.getUser());

		// check my pet info and set pet profile item info
		if (null != _mPetInfo) {
			// get pet avatar data and check
			byte[] _avatarData = _mPetInfo.getAvatar();
			if (null != _avatarData) {
				_mPetProfileAvatarImageView.setImageBitmap(BitmapFactory
						.decodeByteArray(_avatarData, 0, _avatarData.length));
			} else {
				if (null != _mPetInfo.getAvatarUrl()) {
					((NetLoadImageView) findViewById(R.id.pp_avatar_imageView))
							.loadUrl(getResources().getString(R.string.img_url)
									+ _mPetInfo.getAvatarUrl());
				}
			}

			// get pet nickname and check
			String _nickname = _mPetInfo.getNickname();
			if (null != _nickname) {
				_mPetProfileNicknameSettingItem.setText(_nickname);
			}

			// get pet sex and check
			PetSex _sex = _mPetInfo.getSex();
			if (null != _sex) {
				_mPetProfileSexSettingItem.setText(_sex.getSex());
			}

			// get pet breed and check
			PetBreed _breed = _mPetInfo.getBreed();
			if (null != _breed) {
				_mPetProfileBreedSettingItem.setText(_breed.getBreed());
			}

			// get pet age and check
			Integer _age = _mPetInfo.getAge();
			if (null != _age) {
				_mPetProfileAgeSettingItem.setText(String.format(getResources()
						.getString(R.string.pet_age_value_format), _age));
			}

			// get pet height and check
			Double _height = _mPetInfo.getHeight();
			if (null != _mPetInfo.getHeight()) {
				_mPetProfileHeightSettingItem.setText(String.format(
						getResources().getString(
								R.string.pet_height_value_format), _height));
			}

			// get pet weight and check
			Double _weight = _mPetInfo.getWeight();
			if (null != _weight) {
				_mPetProfileWeightSettingItem.setText(String.format(
						getResources().getString(
								R.string.pet_weight_value_format), _weight));
			}

			// get pet district and check
			String _district = _mPetInfo.getDistrict();
			if (null != _district) {
				_mPetProfileDistrictSettingItem.setText(_district);
			}

			// get pet place used to go and check
			String _placeUsed2Go = _mPetInfo.getPlaceUsed2Go();
			if (null != _placeUsed2Go) {
				_mPetProfilePlaceUsed2GoSettingItem.setText(_placeUsed2Go);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// check and init my pet info
		if (null == _mPetInfo) {
			_mPetInfo = new PetBean();
		}

		if (resultCode == Activity.RESULT_OK && data != null) {
			switch (requestCode) {
			case CAPTURE_PHOTO:
			case SELECT_PHOTO:
				Uri uri = data.getData();
				if (uri == null) {
					if (CAPTURE_PHOTO == requestCode) {
						// get bundle
						Bundle bundle = data.getExtras();
						if (null != bundle) {
							// get bitmap
							Bitmap capturePhoto = (Bitmap) bundle.get("data");

							Log.d(LOG_TAG, "capturePhoto = " + capturePhoto);
							return;
						} else {
							Toast.makeText(this, "获取照片出错", Toast.LENGTH_SHORT)
									.show();
							return;
						}
					} else {
						Toast.makeText(this, "获取照片出错", Toast.LENGTH_SHORT)
								.show();
						return;
					}
				}
				cropPhoto(uri, 500);
				return;

			case CROP_IMAGE:
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					Bitmap photo = bundle.getParcelable("data");
					if (photo != null) {
						if (tmpBitmap != null) {
							_mPetProfileAvatarImageView
									.setImageResource(R.drawable.img_pet_profile_avatar);
							tmpBitmap.recycle();
						}
						tmpBitmap = photo;
						_mPetProfileAvatarImageView.setImageBitmap(tmpBitmap);

						// set pet info avatar
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						tmpBitmap
								.compress(Bitmap.CompressFormat.PNG, 100, baos);
						_mPetInfo.setAvatar(baos.toByteArray());

						// upload pet avatar post http request
						// generate upload pet avatar request param
						Map<String, Object> _uploadPetAvaterParam = new HashMap<String, Object>();
						tmpBitmap
								.compress(Bitmap.CompressFormat.JPEG, 50, baos);
						_uploadPetAvaterParam.put("avatar_file",
								baos.toByteArray());
						if (null != _mPetInfo.getId()) {
							_uploadPetAvaterParam
									.put(getResources()
											.getString(
													R.string.rbgServer_setPetInfoReqParam_petId),
											_mPetInfo.getId().toString());
						}
						_uploadPetAvaterParam.put("username", UserManager
								.getInstance().getUser().getName());

						// send upload pet avatar post http request
						HttpUtils.postRequest(
								getResources().getString(R.string.server_url)
										+ getResources().getString(
												R.string.uploadPetAvatar_url),
								PostRequestFormat.MULTIPARTFORMDATA,
								_uploadPetAvaterParam, null,
								HttpRequestType.ASYNCHRONOUS,
								new UploadPetAvatarHttpRequestListener());
					}
				}
				return;
			}
		}

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
					Double _height = Double.parseDouble(data
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
					Double _weight = Double.parseDouble(data
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

			// save my pet info
			IPCUserExtension.setUserPetInfo(
					UserManager.getInstance().getUser(), _mPetInfo);

			// set pet info
			// generate set pet info post request param
			Map<String, String> _setPetInfoParam = new HashMap<String, String>();
			if (null != _mPetInfo.getId()) {
				_setPetInfoParam.put(
						getResources().getString(
								R.string.rbgServer_setPetInfoReqParam_petId),
						_mPetInfo.getId().toString());
			}
			if (null != _mPetInfo.getNickname()) {
				_setPetInfoParam
						.put(getResources()
								.getString(
										R.string.rbgServer_setPetInfoReqParam_petNickname),
								_mPetInfo.getNickname());
			}
			if (null != _mPetInfo.getSex()) {
				_setPetInfoParam.put(
						getResources().getString(
								R.string.rbgServer_setPetInfoReqParam_petSex),
						_mPetInfo.getSex().getValue().toString());
			}
			if (null != _mPetInfo.getBreed()) {
				_setPetInfoParam
						.put(getResources().getString(
								R.string.rbgServer_setPetInfoReqParam_petBreed),
								_mPetInfo.getBreed().getValue().toString());
			}
			if (null != _mPetInfo.getBirthday()) {
				_setPetInfoParam
						.put(getResources()
								.getString(
										R.string.rbgServer_setPetInfoReqParam_petBirthday),
								_mPetInfo.getBirthday().toString());
			}
			if (null != _mPetInfo.getAge()) {
				_setPetInfoParam.put(
						getResources().getString(
								R.string.rbgServer_setPetInfoReqParam_petAge),
						_mPetInfo.getAge().toString());
			}
			if (null != _mPetInfo.getHeight()) {
				_setPetInfoParam
						.put(getResources()
								.getString(
										R.string.rbgServer_setPetInfoReqParam_petHeight),
								_mPetInfo.getHeight().toString());
			}
			if (null != _mPetInfo.getWeight()) {
				_setPetInfoParam
						.put(getResources()
								.getString(
										R.string.rbgServer_setPetInfoReqParam_petWeight),
								_mPetInfo.getWeight().toString());
			}
			if (null != _mPetInfo.getDistrict()) {
				_setPetInfoParam
						.put(getResources()
								.getString(
										R.string.rbgServer_setPetInfoReqParam_petDistrict),
								_mPetInfo.getDistrict());
			}
			if (null != _mPetInfo.getPlaceUsed2Go()) {
				_setPetInfoParam
						.put(getResources()
								.getString(
										R.string.rbgServer_setPetInfoReqParam_petPlaceUsed2Go),
								_mPetInfo.getPlaceUsed2Go());
			}

			// send set pet info post http request
			HttpUtils.postSignatureRequest(
					getResources().getString(R.string.server_url)
							+ getResources()
									.getString(R.string.set_petInfo_url),
					PostRequestFormat.URLENCODED, _setPetInfoParam, null,
					HttpRequestType.ASYNCHRONOUS,
					new SetPetInfoHttpRequestListener());
			break;

		default:
			// nothing to do
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void cropPhoto(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, CROP_IMAGE);
	}

	// process set pet info exception
	private void processSetPetInfoException() {
		// show set pet info failed toast
		Toast.makeText(PetProfileSettingActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// pet avatar upload photo source select popup window
	class PetAvatarUploadPhotoSourceSelectPopupWindow extends CTPopupWindow {

		public PetAvatarUploadPhotoSourceSelectPopupWindow(int resource,
				int width, int height, boolean focusable,
				boolean isBindDefListener) {
			super(resource, width, height, focusable, isBindDefListener);
		}

		public PetAvatarUploadPhotoSourceSelectPopupWindow(int resource,
				int width, int height) {
			super(resource, width, height);
		}

		@Override
		protected void bindPopupWindowComponentsListener() {
			// bind talk photo, select photo from photo album and cancel button
			// click listener
			((Button) getContentView().findViewById(R.id.talkPhoto_button))
					.setOnClickListener(new TalkPhotoBtnOnClickListener());

			((Button) getContentView().findViewById(
					R.id.selectPhoto_fromAlbum_button))
					.setOnClickListener(new SelectPhotoFromAlbumBtnOnClickListener());

			((Button) getContentView().findViewById(R.id.selectCancel_button))
					.setOnClickListener(new CancelSelectBtnOnClickListener());
		}

		@Override
		protected void resetPopupWindow() {
			// nothing to do
		}

		// inner class
		// insert phone to contact mode select insert to new contact button on
		// click listener
		class TalkPhotoBtnOnClickListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// dismiss insert phone to contact mode select popup window
				dismiss();

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				// test by ares
				// pushActivityForResult(intent, CAPTURE_PHOTO);
				startActivityForResult(intent, CAPTURE_PHOTO);
			}

		}

		// insert phone to contact mode select insert to existed contact button
		// on click listener
		class SelectPhotoFromAlbumBtnOnClickListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// dismiss insert phone to contact mode select popup window
				dismiss();

				// test by ares
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "选择相片"),
						SELECT_PHOTO);
			}

		}

		// cancel select button on click listener
		class CancelSelectBtnOnClickListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// dismiss insert phone to contact mode select popup window with
				// animation
				dismissWithAnimation();
			}

		}

	}

	// pet profile avatar setting item on click listener
	class PetProfileAvatarSettingItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// show pet avatar upload photo source select popup window
			// with animation
			_mPetAvatarUploadPhotoSourceSelectPopupWindow
					.showAtLocationWithAnimation(v, Gravity.CENTER, 0, 0);
		}

	}

	// pet profile editText setting item on click listener
	class PetProfileEditTextSettingItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// define pet profile setting item, editText text, input type,
			// comment and request code
			PetProfileSettingItem _petProfileSettingItem = null;
			String _editTextText = "";
			int _editTextInputType = InputType.TYPE_CLASS_TEXT;
			String _comment = null;
			int _requestCode = 0;

			// check setting item id then set pet profile setting item, editText
			// text, input type and request code
			switch (v.getId()) {
			case R.id.pp_nickname_setting_item:
				_petProfileSettingItem = _mPetProfileNicknameSettingItem;
				_editTextText = null != (_editTextText = _mPetProfileNicknameSettingItem
						.getText()) ? _editTextText : "";
				_requestCode = PET_PROFILE_NICKNAME_EDITTEXT_SETTING_REQCODE;
				break;

			case R.id.pp_age_setting_item:
				_petProfileSettingItem = _mPetProfileAgeSettingItem;
				_editTextText = null != _mPetInfo && null != _mPetInfo.getAge() ? _mPetInfo
						.getAge().toString() : "";
				_editTextInputType = InputType.TYPE_CLASS_NUMBER;
				_comment = getResources().getString(
						R.string.ppes_petAge_comment);
				_requestCode = PET_PROFILE_AGE_EDITTEXT_SETTING_REQCODE;
				break;

			case R.id.pp_height_setting_item:
				_petProfileSettingItem = _mPetProfileHeightSettingItem;
				_editTextText = null != _mPetInfo
						&& null != _mPetInfo.getHeight() ? _mPetInfo
						.getHeight().toString() : "";
				_editTextInputType = InputType.TYPE_CLASS_NUMBER;
				_comment = getResources().getString(
						R.string.ppes_petHeight_comment);
				_requestCode = PET_PROFILE_HEIGHT_EDITTEXT_SETTING_REQCODE;
				break;

			case R.id.pp_weight_setting_item:
				_petProfileSettingItem = _mPetProfileWeightSettingItem;
				_editTextText = null != _mPetInfo
						&& null != _mPetInfo.getWeight() ? _mPetInfo
						.getWeight().toString() : "";
				_editTextInputType = InputType.TYPE_CLASS_NUMBER;
				_comment = getResources().getString(
						R.string.ppes_petWeight_comment);
				_requestCode = PET_PROFILE_WEIGHT_EDITTEXT_SETTING_REQCODE;
				break;

			case R.id.pp_placeUsed2Go_setting_item:
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
				if (null != _comment) {
					_extraData
							.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_COMMENT_KEY,
									_comment);
				}
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
			case R.id.pp_sex_setting_item:
				_petProfileSettingItem = _mPetProfileSexSettingItem;
				_petProfileCheckedSettingItemIndex = (null != _mPetInfo && null != _mPetInfo
						.getSex()) ? _mPetInfo.getSex().getValue() : -1;
				_petProfileCheckedSettingItemSubItems = getResources()
						.getStringArray(R.array.pet_sex_array);
				_requestCode = PET_PROFILE_SEX_CHECKED_SETTING_REQCODE;
				break;

			case R.id.pp_breed_setting_item:
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

	// pet profile birthday setting item on click listener
	class PetProfileBirthdaySettingItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// define pet profile birthday setting pet birthday and request code
			Long _petProfileBirthdaySettingPetBirthday = _mPetInfo
					.getBirthday();
			int _requestCode = PET_PROFILE_AGE_EDITTEXT_SETTING_REQCODE;

			// define extra data
			Map<String, Object> _extraData = new HashMap<String, Object>();

			// check pet profile birthday setting pet birthday and set extra
			// data
			if (null != _petProfileBirthdaySettingPetBirthday) {
				// put pet profile birthday setting pet birthday in extra data
				_extraData
						.put(PetProfileBirthdaySettingActivity.PET_PROFILE_BIRTHDAY_KEY,
								_petProfileBirthdaySettingPetBirthday);
			}

			// go to target activity
			pushActivityForResult(PetProfileBirthdaySettingActivity.class,
					_extraData, _requestCode);
		}

	}

	// pet profile district setting item on click listener
	class PetProfileDistrictSettingItemOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			// // define extra data
			// Map<String, String> _extraData = new HashMap<String, String>();
			//
			// // define pet profile district setting item text
			// String _petProfileDistrictSettingItemText =
			// _mPetProfileDistrictSettingItem
			// .getText();
			//
			// // put pet profile district in extra data
			// _extraData
			// .put(PetProfileDistrictSettingActivity.PET_PROFILE_CUSTOM_DISTRICT_VALUE_KEY,
			// null != (_petProfileDistrictSettingItemText =
			// _mPetProfileDistrictSettingItem
			// .getText()) ? _petProfileDistrictSettingItemText
			// : "");
			//
			// // go to target activity
			// pushActivityForResult(PetProfileDistrictSettingActivity.class,
			// _extraData, PET_PROFILE_DISTRICT_SETTING_REQCODE);

			// test by ares
			// get pet profile district setting item text
			String _district = _mPetProfileDistrictSettingItem.getText();

			// define extra data
			Map<String, Object> _extraData = new HashMap<String, Object>();

			// set extra data
			_extraData
					.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_TITLE_KEY,
							_mPetProfileDistrictSettingItem.getLabel());
			_extraData
					.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_HINT_KEY,
							_mPetProfileDistrictSettingItem.getLabel());
			_extraData
					.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_TEXT_KEY,
							null != _district ? _district : "");
			_extraData
					.put(PetProfileEditTextSettingActivity.PET_PROFILE_EDITTEXT_INPUTTYPE_KEY,
							InputType.TYPE_CLASS_TEXT);

			// go to target activity
			pushActivityForResult(PetProfileEditTextSettingActivity.class,
					_extraData, PET_PROFILE_DISTRICT_SETTING_REQCODE);
		}

	}

	// set pet info http request listener
	class SetPetInfoHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object result and user key
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// check result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get response pet id
					Long _responsePetId = JSONUtils
							.getLongFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_setPetInfoReqResp_petId));

					Log.d(LOG_TAG, "Response pet id = " + _responsePetId);

					// set new add pet id or update existed pet id
					_mPetInfo.setId(_responsePetId);
					break;

				case 1:
				case 3:
				case 4:
					Log.d(LOG_TAG, "set pet info failed");

					Toast.makeText(PetProfileSettingActivity.this,
							R.string.toast_set_petInfo_error,
							Toast.LENGTH_SHORT).show();
					break;

				default:
					Log.e(LOG_TAG,
							"set pet info failed, bg_server return result is unrecognized");

					processSetPetInfoException();
					break;
				}
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"set pet info failed, send set pet info post request failed");

			processSetPetInfoException();
		}

	}

	// upload pet avatar http request listener
	class UploadPetAvatarHttpRequestListener extends OnHttpRequestListener {

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
					// nothing to do
					break;

				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					Log.e(LOG_TAG, "upload pet avatar failed");

					// show get user all pets info failed toast
					Toast.makeText(PetProfileSettingActivity.this, "上传头像失败",
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"upload pet avtar failed, bg_server return result is unrecognized");

					processSetPetInfoException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"upload pet avatar failed, bg_server return result is null");

				processSetPetInfoException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"upload pet avatar failed, send upload pet avatar post request failed");

			// show get user all pets info failed toast
			Toast.makeText(PetProfileSettingActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

}
