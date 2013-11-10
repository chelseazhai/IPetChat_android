package com.segotech.ipetchat.tab7tabcontent;

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
		super.onResume();

		// get my pet info
		PetBean _petInfo = IPCUserExtension.getUserPetInfo(UserManager
				.getInstance().getUser());

		// define pet breed, age, height and weight label and value JSONObject
		// and init
		JSONObject _petBreedJSON = new JSONObject();
		JSONObject _petAgeJSON = new JSONObject();
		JSONObject _petHeightJSON = new JSONObject();
		JSONObject _petWeightJSON = new JSONObject();

		// set pet other info label
		try {
			// breed
			_petBreedJSON.put(PET_INFO_LABEL_KEY,
					getResources().getString(R.string.pet_breed_label));

			// age
			_petAgeJSON.put(PET_INFO_LABEL_KEY,
					getResources().getString(R.string.pet_age_label));

			// height
			_petHeightJSON.put(PET_INFO_LABEL_KEY,
					getResources().getString(R.string.pet_height_label));

			// weight
			_petWeightJSON.put(PET_INFO_LABEL_KEY,
					getResources().getString(R.string.pet_weight_label));
		} catch (JSONException e) {
			e.printStackTrace();

			Log.e(LOG_TAG,
					"Put pet other info label to json object with key error, exception message = "
							+ e.getMessage());
		}

		// check my pet info
		if (null != _petInfo) {
			// check and set pet avatar
			if (null != _petInfo.getAvatar()) {
				((ImageView) findViewById(R.id.pet_avatar_imageView))
						.setImageBitmap(BitmapFactory.decodeByteArray(
								_petInfo.getAvatar(), 0,
								_petInfo.getAvatar().length));
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

			// set pet other info value
			try {
				// breed
				if (null != _petInfo.getBreed()) {
					_petBreedJSON.put(PET_INFO_VALUE_KEY, _petInfo.getBreed()
							.getBreed());
				}

				// age
				if (null != _petInfo.getAge()) {
					_petAgeJSON.put(PET_INFO_VALUE_KEY, String.format(
							getResources().getString(
									R.string.pet_age_value_format),
							_petInfo.getAge()));
				}

				// height
				if (null != _petInfo.getHeight()) {
					_petHeightJSON.put(PET_INFO_VALUE_KEY, String.format(
							getResources().getString(
									R.string.pet_height_value_format),
							_petInfo.getHeight()));
				}

				// weight
				if (null != _petInfo.getWeight()) {
					_petWeightJSON.put(PET_INFO_VALUE_KEY, String.format(
							getResources().getString(
									R.string.pet_weight_value_format),
							_petInfo.getWeight()));
				}
			} catch (JSONException e) {
				e.printStackTrace();

				Log.e(LOG_TAG,
						"Put pet other info value to json object with key error, exception message = "
								+ e.getMessage());
			}
		}

		// define pet other info JSONObject array and add pet other info breed,
		// age, height and weight to it
		JSONObject[] _petOtherInfoArray = new JSONObject[] { _petBreedJSON,
				_petAgeJSON, _petHeightJSON, _petWeightJSON };

		// get pet other info tableRow
		TableRow _petOtherInfoTableRow = (TableRow) findViewById(R.id.pet_info_tableRow);

		// set pet info tableRow data
		for (int i = 0; i < _petOtherInfoArray.length; i++) {
			// get pet other info tableRow item linearLayout
			LinearLayout _petInfoTableRowItem = (LinearLayout) _petOtherInfoTableRow
					.getChildAt(i);

			// get pet other info JSONObject
			JSONObject _petOtherInfoJSON = _petOtherInfoArray[i];

			// check pet other info JSONObject and set pet other info label,
			// value textView text
			if (null != _petOtherInfoJSON) {
				((TextView) _petInfoTableRowItem
						.findViewById(R.id.pet_info_label_textView))
						.setText(JSONUtils.getStringFromJSONObject(
								_petOtherInfoJSON, PET_INFO_LABEL_KEY));

				((TextView) _petInfoTableRowItem
						.findViewById(R.id.pet_info_value_textView))
						.setText(JSONUtils.getStringFromJSONObject(
								_petOtherInfoJSON, PET_INFO_VALUE_KEY));
			}
		}

		// test by ares
		// get pet sports score progress
		int _petSportsScoreProgress = 0;

		// set pet sports score
		((ProgressBar) findViewById(R.id.pet_sportsScore_progressBar))
				.setProgress(_petSportsScoreProgress);
		((TextView) findViewById(R.id.pet_sportsScore_textView)).setText(""
				+ _petSportsScoreProgress);
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
