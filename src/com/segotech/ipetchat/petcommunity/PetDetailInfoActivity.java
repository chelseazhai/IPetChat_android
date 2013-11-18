package com.segotech.ipetchat.petcommunity;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.richitec.commontoolkit.customcomponent.BarButtonItem.BarButtonItemStyle;
import com.richitec.commontoolkit.customcomponent.CTMenu;
import com.richitec.commontoolkit.customcomponent.CTMenu.CTMenuOnItemSelectedListener;
import com.richitec.commontoolkit.customcomponent.ImageBarButtonItem;
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
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.petcommunity.petchat.Leave6ReplyMsgActivity;

public class PetDetailInfoActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = PetDetailInfoActivity.class
			.getCanonicalName();

	// pet info key
	public static final String PET_DETAILINFO_PET_KEY = "pet_detailinfo_pet_key";
	public static final String PET_DETAILINFO_CONCERN_KEY = "pet_detailinfo_concern_key";

	// more menu ids
	private static final int ADD2BLACKLIST_MENU = 10;
	private static final int CANCELCONCERN_MENU = 11;

	// more popup menu
	private CTMenu _mMorePopupMenu;

	// pet detail info
	private PetBean _mPetDetailInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_detail_info_activity_layout);

		// set title
		setTitle(R.string.pet_detail_info_nav_title);

		// define pet detail info concern flag
		Boolean _concernPet = false;

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get detail info pet and concern flag
			_mPetDetailInfo = (PetBean) _data
					.getSerializable(PET_DETAILINFO_PET_KEY);
			_concernPet = _data.getBoolean(PET_DETAILINFO_CONCERN_KEY);
		}

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

		// check pet detail info
		if (null != _mPetDetailInfo) {
			// check and set pet avatar
			if (null != _mPetDetailInfo.getAvatar()) {
				((ImageView) findViewById(R.id.pet_avatar_imageView))
						.setImageBitmap(BitmapFactory.decodeByteArray(
								_mPetDetailInfo.getAvatar(), 0,
								_mPetDetailInfo.getAvatar().length));
			}

			// check and set pet nickname
			if (null != _mPetDetailInfo.getNickname()) {
				((TextView) findViewById(R.id.pet_nickname_textView))
						.setText(_mPetDetailInfo.getNickname());
			}

			// check and set pet sex
			if (null != _mPetDetailInfo.getSex()) {
				((ImageView) findViewById(R.id.pet_sex_imageView))
						.setImageResource(PetSex.MALE == _mPetDetailInfo
								.getSex() ? R.drawable.img_male
								: R.drawable.img_female);
			}

			// set pet other info
			// breed
			if (null != _mPetDetailInfo.getBreed()) {
				_petOtherInfoBreed
						.append(_mPetDetailInfo.getBreed().getBreed());
			}

			// age
			if (null != _mPetDetailInfo.getAge()) {
				_petOtherInfoAge = String.format(
						getResources().getString(R.string.pet_age_format),
						_mPetDetailInfo.getAge());
			}

			// height
			if (null != _mPetDetailInfo.getHeight()) {
				_petOtherInfoHeight = String.format(
						getResources().getString(R.string.pet_height_format),
						_mPetDetailInfo.getHeight());
			}

			// weight
			if (null != _mPetDetailInfo.getWeight()) {
				_petOtherInfoWeight = String.format(
						getResources().getString(R.string.pet_weight_format),
						_mPetDetailInfo.getWeight());
			}

			// district
			if (null != _mPetDetailInfo.getDistrict()) {
				_petOtherInfoDistrict.append(_mPetDetailInfo.getDistrict());
			}

			// place used to go
			if (null != _mPetDetailInfo.getPlaceUsed2Go()) {
				_petOtherInfoPlaceUsed2Go.append(_mPetDetailInfo
						.getPlaceUsed2Go());
			}
		}

		// define pet other info array
		String[] _petOtherInfoArray = new String[] {
				_petOtherInfoBreed.toString(), _petOtherInfoAge,
				_petOtherInfoHeight, _petOtherInfoWeight,
				_petOtherInfoDistrict.toString(),
				_petOtherInfoPlaceUsed2Go.toString() };

		// get pet other info parent lineaLayout
		LinearLayout _petOtherInfoParentLinearLayout = (LinearLayout) findViewById(R.id.pet_otherInfo_parent_linearLayout);

		// process each pet other info items
		for (int i = 0; i < _petOtherInfoParentLinearLayout.getChildCount(); i++) {
			// set pet other info item textView text
			((TextView) _petOtherInfoParentLinearLayout.getChildAt(i))
					.setText(_petOtherInfoArray[i]);
		}

		// set leave message button on click listener
		((Button) findViewById(R.id.sendMsg_button))
				.setOnClickListener(new LeaveMsgBtnOnClickListener());

		// check concern flag
		if (!_concernPet) {
			// get concern button
			Button _concernButton = (Button) findViewById(R.id.concern_button);

			// set add add as concern pet button on click listener
			_concernButton
					.setOnClickListener(new AddConcernBtnOnClickListener());

			// show add as concern pet button
			_concernButton.setVisibility(View.VISIBLE);
		} else {
			// init more popup menu
			_mMorePopupMenu = new CTMenu(this);

			// add menu item
			_mMorePopupMenu.add(CANCELCONCERN_MENU,
					R.string.moreMenu_cancelConcernMenuItem);
			_mMorePopupMenu.add(ADD2BLACKLIST_MENU,
					R.string.moreMenu_add2BlacklistMenuItem);

			// set more menu on item selected listener
			_mMorePopupMenu
					.setMenuOnItemSelectedListener(new MoreMenuOnItemSelectedListener());

			// set right image bar button item, more info image bar button item
			setRightBarButtonItem(new ImageBarButtonItem(this,
					R.drawable.img_moremenu_barbuttonitem,
					BarButtonItemStyle.RIGHT_GO,
					new MoreMenuImageBarButtonItemOnClickListener()));
		}
	}

	// process concern, cancel concern pet or add pet to blacklist exception
	private void processException() {
		// show login failed toast
		Toast.makeText(PetDetailInfoActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// more menu image bar button item on click listener
	class MoreMenuImageBarButtonItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// show more menu
			_mMorePopupMenu.showAsDropDown(v);
		}

	}

	// more menu on item selected listener
	class MoreMenuOnItemSelectedListener implements
			CTMenuOnItemSelectedListener {

		@Override
		public boolean onMenuItemSelected(CTMenu menu, int menuItemId) {
			// check popup menu
			if (_mMorePopupMenu == menu) {
				// more menu dismiss
				menu.dismiss();

				// check more menu item id
				switch (menuItemId) {
				case CANCELCONCERN_MENU:
					// cancel concern the pet
					// generate cancel concern pet post
					// request param
					Map<String, String> _cancelConcernPetParam = new HashMap<String, String>();
					_cancelConcernPetParam
							.put(getResources()
									.getString(
											R.string.rbgServer_setPetInfoReqParam_petId),
									_mPetDetailInfo.getId().toString());

					// send cancel concern pet post http
					// request
					HttpUtils.postSignatureRequest(
							getResources().getString(R.string.server_url)
									+ getResources().getString(
											R.string.cancelConcern_pet_url),
							PostRequestFormat.URLENCODED,
							_cancelConcernPetParam, null,
							HttpRequestType.ASYNCHRONOUS,
							new CancelConcernPetHttpRequestListener());
					break;

				case ADD2BLACKLIST_MENU:
					// add the pet to blacklist
					// generate add to blacklist post
					// request param
					Map<String, String> _add2BlacklistParam = new HashMap<String, String>();
					_add2BlacklistParam
							.put(getResources()
									.getString(
											R.string.rbgServer_setPetInfoReqParam_petId),
									_mPetDetailInfo.getId().toString());

					// send add to blacklist post http
					// request
					HttpUtils.postSignatureRequest(
							getResources().getString(R.string.server_url)
									+ getResources().getString(
											R.string.addPet2Blacklist_url),
							PostRequestFormat.URLENCODED, _add2BlacklistParam,
							null, HttpRequestType.ASYNCHRONOUS,
							new Add2BlacklistHttpRequestListener());
					break;
				}
			}

			return false;
		}

	}

	// leave message button on click listener
	class LeaveMsgBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get and check my pet info
			PetBean _myPetInfo = IPCUserExtension.getUserPetInfo(UserManager
					.getInstance().getUser());
			if (null != _myPetInfo
					&& !"".equalsIgnoreCase(_myPetInfo.getNickname())) {
				// define extra data
				Map<String, Object> _extraData = new HashMap<String, Object>();

				// set extra data
				_extraData.put(Leave6ReplyMsgActivity.LEAVEMSG_PETID_KEY,
						_mPetDetailInfo.getId());

				// go to leave or reply message activity
				pushActivity(Leave6ReplyMsgActivity.class, _extraData);
			} else {
				// show get user all pets info failed toast
				Toast.makeText(PetDetailInfoActivity.this,
						"请先设置您的宠物信息并为其添加一个名字", Toast.LENGTH_LONG).show();

				return;
			}
		}

	}

	// add concern button on click listener
	class AddConcernBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// concern pet
			// generate concern pet post
			// request param
			Map<String, String> _concernPetParam = new HashMap<String, String>();
			_concernPetParam.put(
					getResources().getString(
							R.string.rbgServer_setPetInfoReqParam_petId),
					_mPetDetailInfo.getId().toString());

			// send concern pet post http
			// request
			HttpUtils.postSignatureRequest(
					getResources().getString(R.string.server_url)
							+ getResources()
									.getString(R.string.concern_pet_url),
					PostRequestFormat.URLENCODED, _concernPetParam, null,
					HttpRequestType.ASYNCHRONOUS,
					new ConcernPetHttpRequestListener());
		}

	}

	// concern pet http request listener
	class ConcernPetHttpRequestListener extends OnHttpRequestListener {

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
					Log.d(LOG_TAG, "concern pet successful");

					// show get user all pets info failed toast
					Toast.makeText(PetDetailInfoActivity.this, "成功关注宠物",
							Toast.LENGTH_LONG).show();

					// pop pet detail info activity
					popActivityWithResult(RESULT_OK, null);
					break;

				case 1:
				case 2:
				case 4:
					Log.e(LOG_TAG, "concern pet failed, pet error");

					// show get user all pets info failed toast
					Toast.makeText(PetDetailInfoActivity.this, "关注宠物失败",
							Toast.LENGTH_LONG).show();
					break;

				case 3:
					Log.e(LOG_TAG, "concern pet failed, pet is be concerned");

					// show get user all pets info failed toast
					Toast.makeText(PetDetailInfoActivity.this, "宠物已经被关注过了",
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"concern pet failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"concern pet failed, bg_server return result is null");

				processException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"concern pet failed, send concern pet post request failed");

			// show get user all pets info failed toast
			Toast.makeText(PetDetailInfoActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

	// cancel concern pet http request listener
	class CancelConcernPetHttpRequestListener extends OnHttpRequestListener {

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
					Log.d(LOG_TAG, "cancel concern pet successful");

					// show get user all pets info failed toast
					Toast.makeText(PetDetailInfoActivity.this, "成功取消关注此宠物",
							Toast.LENGTH_LONG).show();

					// pop pet detail info activity
					popActivity();
					break;

				case 1:
				case 2:
					Log.e(LOG_TAG, "cancel concern pet failed, pet error");

					// show get user all pets info failed toast
					Toast.makeText(PetDetailInfoActivity.this, "去掉关注宠物失败",
							Toast.LENGTH_LONG).show();
					break;

				case 3:
					Log.e(LOG_TAG,
							"cancel concern pet failed, pet is be concerned");

					// show get user all pets info failed toast
					Toast.makeText(PetDetailInfoActivity.this, "您并未关注此宠物",
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"cancel concern pet failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"cancel concern pet failed, bg_server return result is null");

				processException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"cancel concern pet failed, send concern pet post request failed");

			// show get user all pets info failed toast
			Toast.makeText(PetDetailInfoActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

	// add pet to blacklist http request listener
	class Add2BlacklistHttpRequestListener extends OnHttpRequestListener {

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
					Log.d(LOG_TAG, "add pet to blacklist successful");

					// show get user all pets info failed toast
					Toast.makeText(PetDetailInfoActivity.this, "成功将此宠物添加到黑名单",
							Toast.LENGTH_LONG).show();

					// pop pet detail info activity
					popActivity();
					break;

				case 1:
				case 2:
				case 4:
					Log.e(LOG_TAG, "add pet to blacklist failed, pet error");

					// show get user all pets info failed toast
					Toast.makeText(PetDetailInfoActivity.this, "将宠物添加到黑名单错误",
							Toast.LENGTH_LONG).show();
					break;

				case 3:
					Log.e(LOG_TAG,
							"add pet to blacklist failed, pet is be in blacklist");

					// show get user all pets info failed toast
					Toast.makeText(PetDetailInfoActivity.this, "您已经将此宠物添加到黑名单",
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"add pet to blacklist failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"add pet to blacklist failed, bg_server return result is null");

				processException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"add pet to blacklist failed, send add pet to blacklist post request failed");

			// show get user all pets info failed toast
			Toast.makeText(PetDetailInfoActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

}
