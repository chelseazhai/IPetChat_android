package com.segotech.ipetchat.tab7tabcontent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.pet.PetSex;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatRootNavigationActivity;

public class SportsHealthTabContentActivity extends
		IPetChatRootNavigationActivity {

	private static final String LOG_TAG = SportsHealthTabContentActivity.class
			.getCanonicalName();

	// pet info tableRow data keys
	private static final String PET_INFO_LABEL_KEY = "pet_info_label_key";
	private static final String PET_INFO_VALUE_KEY = "pet_info_value_key";

	// pet sports info history segment view
	private View _mPetSportsInfoHistorySegmentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.sports_health_tab_content_activity_layout);

		// set title
		setTitle(R.string.sports_health_tab7nav_title);

		// set pet sports info segment radioGroup
		((RadioGroup) findViewById(R.id.pet_sportsInfo_segment_radioGroup))
				.setOnCheckedChangeListener(new PetSportsInfoSegmentRadioGroupOnCheckedChangeListener());
	}

	@Override
	protected void onResume() {
		// get user pet info
		PetBean _petInfo = IPCUserExtension.getUserPetInfo(UserManager
				.getInstance().getUser());

		Log.d(LOG_TAG, "my pet info = " + _petInfo);

		// define pet breed, age, height and weight JSONObject and init
		JSONObject _petOtherInfoBreed = new JSONObject();
		JSONObject _petOtherInfoAge = new JSONObject();
		JSONObject _petOtherInfoHeight = new JSONObject();
		JSONObject _petOtherInfoWeight = new JSONObject();

		// set pet other info JSONObject label
		try {
			// breed
			_petOtherInfoBreed.put(PET_INFO_LABEL_KEY, getResources()
					.getString(R.string.pet_breed_label));

			// age
			_petOtherInfoAge.put(PET_INFO_LABEL_KEY,
					getResources().getString(R.string.pet_age_label));

			// height
			_petOtherInfoHeight.put(PET_INFO_LABEL_KEY, getResources()
					.getString(R.string.pet_height_label));

			// weight
			_petOtherInfoWeight.put(PET_INFO_LABEL_KEY, getResources()
					.getString(R.string.pet_weight_label));
		} catch (JSONException e) {
			e.printStackTrace();

			Log.e(LOG_TAG,
					"Put json label with key error, exception message = "
							+ e.getMessage());
		}

		// check user pet info
		if (null != _petInfo) {
			// check and set pet avatar
			if (null != _petInfo.getAvatar()) {
				((ImageView) findViewById(R.id.pet_avatar_imageView))
						.setImageBitmap(BitmapFactory.decodeByteArray(
								_petInfo.getAvatar(), 0,
								_petInfo.getAvatar().length));
			}

			// set pet nickname
			((TextView) findViewById(R.id.pet_nickname_textView))
					.setText(null != _petInfo.getNickname() ? _petInfo
							.getNickname() : "");

			// check and set pet sex
			if (null != _petInfo.getSex()) {
				((ImageView) findViewById(R.id.pet_sex_imageView))
						.setImageResource(PetSex.MALE == _petInfo.getSex() ? R.drawable.img_male
								: R.drawable.img_female);
			}

			// set pet other info JSONObject value
			try {
				// breed
				if (null != _petInfo.getBreed()) {
					_petOtherInfoBreed.put(PET_INFO_VALUE_KEY, _petInfo
							.getBreed().getBreed());
				}

				// age
				if (null != _petInfo.getAge()) {
					_petOtherInfoAge.put(PET_INFO_VALUE_KEY, String.format(
							getResources().getString(
									R.string.pet_age_value_format),
							_petInfo.getAge()));
				}

				// height
				if (null != _petInfo.getHeight()) {
					_petOtherInfoHeight.put(PET_INFO_VALUE_KEY, String.format(
							getResources().getString(
									R.string.pet_height_value_format),
							_petInfo.getHeight()));
				}

				// weight
				if (null != _petInfo.getWeight()) {
					_petOtherInfoWeight.put(PET_INFO_VALUE_KEY, String.format(
							getResources().getString(
									R.string.pet_weight_value_format),
							_petInfo.getWeight()));
				}
			} catch (JSONException e) {
				e.printStackTrace();

				Log.e(LOG_TAG,
						"Put json value with key error, exception message = "
								+ e.getMessage());
			}
		}

		// define pet other info JSONArray and put pet other info breed, age,
		// height and weight to it
		JSONArray _petOtherInfoList = new JSONArray();

		_petOtherInfoList.put(_petOtherInfoBreed);
		_petOtherInfoList.put(_petOtherInfoAge);
		_petOtherInfoList.put(_petOtherInfoHeight);
		_petOtherInfoList.put(_petOtherInfoWeight);

		// get pet info tableRow
		TableRow _petInfoTableRow = (TableRow) findViewById(R.id.pet_info_tableRow);

		// set pet info tableRow data
		for (int i = 0; i < _petOtherInfoList.length(); i++) {
			// get pet info tableRow item linearLayout
			LinearLayout _petInfoTableRowItem = (LinearLayout) _petInfoTableRow
					.getChildAt(i);

			// get pet other info JSONObject
			JSONObject _petOtherInfo = null;
			try {
				_petOtherInfo = (JSONObject) _petOtherInfoList.get(i);
			} catch (JSONException e) {
				e.printStackTrace();

				Log.e(LOG_TAG, "Get json object from json array = "
						+ _petOtherInfoList + " at index = " + i
						+ " error, exception message = " + e.getMessage());
			}

			// set pet info label and value textView text
			((TextView) _petInfoTableRowItem
					.findViewById(R.id.pet_info_label_textView))
					.setText(JSONUtils.getStringFromJSONObject(_petOtherInfo,
							PET_INFO_LABEL_KEY));

			((TextView) _petInfoTableRowItem
					.findViewById(R.id.pet_info_value_textView))
					.setText(JSONUtils.getStringFromJSONObject(_petOtherInfo,
							PET_INFO_VALUE_KEY));

		}

		// test by ares
		// get pet sports score progress
		int _petSportsScoreProgress = 0;

		// set pet sports score
		((ProgressBar) findViewById(R.id.pet_sportsScore_progressBar))
				.setProgress(_petSportsScoreProgress);
		((TextView) findViewById(R.id.pet_sportsScore_textView)).setText(""
				+ _petSportsScoreProgress);

		super.onResume();
	}

	// inner class
	// pet sports info segment radioGroup on checked change listener
	class PetSportsInfoSegmentRadioGroupOnCheckedChangeListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
			// get pet today's sports info view
			View _petTodaySportsInfoView = findViewById(R.id.pet_sportsInfo_todaySegment_viewGroup);

			// check checked segment item radioButton id
			switch (checkedId) {
			case R.id.pet_sportsInfo_today_segment_radio:
				// show pet today sports info view if needed
				if (View.VISIBLE != _petTodaySportsInfoView.getVisibility()) {
					_petTodaySportsInfoView.setVisibility(View.VISIBLE);
				}

				// hide pet history sports info view
				_mPetSportsInfoHistorySegmentView.setVisibility(View.GONE);
				break;

			case R.id.pet_sportsInfo_history_segment_radio:
			default:
				// hide pet today sports info view
				_petTodaySportsInfoView.setVisibility(View.GONE);

				// show pet history sports info view if needed
				if (null == _mPetSportsInfoHistorySegmentView) {
					// inflate pet history sports info viewStub
					_mPetSportsInfoHistorySegmentView = ((ViewStub) findViewById(R.id.pet_sportsInfo_historySegment_viewStub))
							.inflate();

					//
				} else {
					if (View.VISIBLE != _mPetSportsInfoHistorySegmentView
							.getVisibility()) {
						_mPetSportsInfoHistorySegmentView
								.setVisibility(View.VISIBLE);
					}
				}
				break;
			}
		}

	}

}
