package com.segotech.ipetchat.tab7tabcontent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class SportsHealthTabContentActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = SportsHealthTabContentActivity.class
			.getCanonicalName();

	// test data
	// test by ares
	private final JSONObject demo_pet_JSONInfo = JSONUtils
			.toJSONObject("{\"avatar\":1, \"nickname\":\"乐乐\", \"sex\":1, \"deviceBattery\":85, \"sportsScore\":90, \"breed\":\"金毛\", \"age\":24, \"height\":50, \"weight\":25, \"district\":\"江苏南京\", \"placeUsed2Go\":\"玄武湖公园\"}");
	private final int demo_pet_avatar = R.drawable.img_demo_pet;

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

		// test by ares
		// set pet avatar
		((ImageView) findViewById(R.id.pet_avatar_imageView))
				.setImageResource(demo_pet_avatar);

		// set pet nickname
		((TextView) findViewById(R.id.pet_nickname_textView)).setText(JSONUtils
				.getStringFromJSONObject(demo_pet_JSONInfo, "nickname"));

		// set pet sex
		((ImageView) findViewById(R.id.pet_sex_imageView))
				.setImageResource(0 == JSONUtils.getIntegerFromJSONObject(
						demo_pet_JSONInfo, "sex") ? R.drawable.img_male
						: R.drawable.img_female);

		// define pet other info JSONArray
		JSONArray _petOtherInfoList = new JSONArray();

		// set them
		try {
			// breed
			JSONObject _petOtherInfoBreed = new JSONObject();

			_petOtherInfoBreed.put(PET_INFO_LABEL_KEY, getResources()
					.getString(R.string.pet_breed_label));
			_petOtherInfoBreed.put(PET_INFO_VALUE_KEY, JSONUtils
					.getStringFromJSONObject(demo_pet_JSONInfo, "breed"));

			// add to list
			_petOtherInfoList.put(_petOtherInfoBreed);

			// age
			JSONObject _petOtherInfoAge = new JSONObject();

			_petOtherInfoAge.put(PET_INFO_LABEL_KEY,
					getResources().getString(R.string.pet_age_label));
			_petOtherInfoAge.put(PET_INFO_VALUE_KEY,
					String.format(
							getResources().getString(
									R.string.pet_age_value_format), JSONUtils
									.getIntegerFromJSONObject(
											demo_pet_JSONInfo, "age")));

			// add to list
			_petOtherInfoList.put(_petOtherInfoAge);

			// height
			JSONObject _petOtherInfoHeight = new JSONObject();

			_petOtherInfoHeight.put(PET_INFO_LABEL_KEY, getResources()
					.getString(R.string.pet_height_label));
			_petOtherInfoHeight.put(PET_INFO_VALUE_KEY, String.format(
					getResources().getString(R.string.pet_height_value_format),
					JSONUtils.getIntegerFromJSONObject(demo_pet_JSONInfo,
							"height")));

			// add to list
			_petOtherInfoList.put(_petOtherInfoHeight);

			// weight
			JSONObject _petOtherInfoWeight = new JSONObject();

			_petOtherInfoWeight.put(PET_INFO_LABEL_KEY, getResources()
					.getString(R.string.pet_weight_label));
			_petOtherInfoWeight.put(PET_INFO_VALUE_KEY, String.format(
					getResources().getString(R.string.pet_weight_value_format),
					JSONUtils.getIntegerFromJSONObject(demo_pet_JSONInfo,
							"weight")));

			// add to list
			_petOtherInfoList.put(_petOtherInfoWeight);
		} catch (JSONException e) {
			e.printStackTrace();

			Log.e(LOG_TAG,
					"Put json value with key error, exception message = "
							+ e.getMessage());
		}

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

		// set pet sports info segment radioGroup
		((RadioGroup) findViewById(R.id.pet_sportsInfo_segment_radioGroup))
				.setOnCheckedChangeListener(new PetSportsInfoSegmentRadioGroupOnCheckedChangeListener());

		// get pet sports score progress
		int _petSportsScoreProgress = JSONUtils.getIntegerFromJSONObject(
				demo_pet_JSONInfo, "sportsScore");

		// set pet sports score
		((ProgressBar) findViewById(R.id.pet_sportsScore_progressBar))
				.setProgress(_petSportsScoreProgress);
		((TextView) findViewById(R.id.pet_sportsScore_textView)).setText(""
				+ _petSportsScoreProgress);

		//
		//
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
