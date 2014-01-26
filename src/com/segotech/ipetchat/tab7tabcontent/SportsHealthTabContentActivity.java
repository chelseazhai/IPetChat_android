package com.segotech.ipetchat.tab7tabcontent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.CommonUtils;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.pet.PetSex;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatRootNavigationActivity;
import com.segotech.ipetchat.customwidget.NetLoadImageView;
import com.segotech.ipetchat.utils.DeviceServerHttpReqParamUtils;

public class SportsHealthTabContentActivity extends
		IPetChatRootNavigationActivity {

	private static final String LOG_TAG = SportsHealthTabContentActivity.class
			.getCanonicalName();

	// pet info tableRow data keys
	private static final String PET_INFO_LABEL_KEY = "pet_info_label_key";
	private static final String PET_INFO_VALUE_KEY = "pet_info_value_key";

	// pet sports info chart colors
	private static final int[] PET_SPORTSINFO_CHART_COLORS = new int[] {
			R.color.pet_rest, R.color.pet_walk, R.color.pet_run,
			R.color.pet_strenuousExercise };

	// pet sports info chart renderer
	private DefaultRenderer _mPetSportsInfoRenderer;

	// pet sports info history segment view
	private View _mPetSportsInfoHistorySegmentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.sports_health_tab_content_activity_layout);

		// set title
		setTitle(R.string.sports_and_health_tab7nav_title);

		// set pet sports info segment radioGroup on checked change listener
		((RadioGroup) findViewById(R.id.pet_sportsInfo_segment_radioGroup))
				.setOnCheckedChangeListener(new PetSportsInfoSegmentRadioGroupOnCheckedChangeListener());

		// initialize pet sports info renderer
		_mPetSportsInfoRenderer = new DefaultRenderer();

		// set attributes
		_mPetSportsInfoRenderer.setApplyBackgroundColor(true);
		for (int _color : PET_SPORTSINFO_CHART_COLORS) {
			// generate each series renderer, set draw color and add to chart
			// renderer
			SimpleSeriesRenderer _seriesRenderer = new SimpleSeriesRenderer();
			_seriesRenderer.setColor(getResources().getColor(_color));

			_mPetSportsInfoRenderer.addSeriesRenderer(_seriesRenderer);
		}
		_mPetSportsInfoRenderer.setZoomEnabled(false);
		_mPetSportsInfoRenderer.setZoomButtonsVisible(false);
		_mPetSportsInfoRenderer.setShowLabels(false);
		_mPetSportsInfoRenderer.setShowLegend(false);
		_mPetSportsInfoRenderer.setPanEnabled(false);
		_mPetSportsInfoRenderer.setStartAngle(270);
		_mPetSportsInfoRenderer.setScale(1.4f);
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

		// check pet device bind info
		if (null != IPCUserExtension.getUserPetBindDeviceId(UserManager
				.getInstance().getUser())) {
			// get pet today or history sports info values
			// first get device server system time
			HttpUtils.getRequest(
					getResources().getString(R.string.deviceServer_url)
							+ getResources().getString(
									R.string.deviceServer_getTime_url), null,
					null, HttpRequestType.ASYNCHRONOUS,
					new GetDeviceServerSystemTimeHttpRequestListener());
		}
	}

	// generate pet today sports info pie chart with values and score
	@SuppressWarnings("unchecked")
	private void generatePetTodaySportsInfoPieChart(List<Double> sportsValues,
			Double sportsScore) {
		// pet today sports info value textViews
		final int[] PET_TODAY_SPORTSINFO_VALUE_TEXTVIEWS = new int[] {
				R.id.pet_today_rest_sportsInfo_value_textView,
				R.id.pet_today_walk_sportsInfo_value_textView,
				R.id.pet_today_run_sportsInfo_value_textView,
				R.id.pet_today_strenuousExercise_sportsInfo_value_textView };

		// check pet today sports score then set pet sports score progressBar
		// progress and textView text
		if (null != sportsScore) {
			((ProgressBar) findViewById(R.id.pet_sportsScore_progressBar))
					.setProgress((int) (100 * sportsScore / 500));
			((TextView) findViewById(R.id.pet_sportsScore_textView))
					.setText(sportsScore.toString());
		}

		// get pet today sports info pie chart frameLayout
		FrameLayout _petTodaySportsInfoPieChartFrameLayout = (FrameLayout) findViewById(R.id.pet_today_sportsInfo_pieChart_frameLayout);
		_petTodaySportsInfoPieChartFrameLayout.removeAllViews();

		// define pet today sports info pie chart category series
		CategorySeries _categorySeries = new CategorySeries("");
		// check pet today sports info values and set default if needed
		if (null == sportsValues || sportsValues.isEmpty()
				|| PET_SPORTSINFO_CHART_COLORS.length != sportsValues.size()) {
			sportsValues = (List<Double>) CommonUtils.array2List(new Double[] {
					25.0, 25.0, 25.0, 25.0 });
		}

		// set pet today sports info pie chart category series value and value
		// textView text
		for (int i = 0; i < sportsValues.size(); i++) {
			_categorySeries.add("", sportsValues.get(i));

			((TextView) findViewById(PET_TODAY_SPORTSINFO_VALUE_TEXTVIEWS[i]))
					.setText(String.format("%1$,.0f%%", sportsValues.get(i)));
		}

		// add pet today sports info pie chart to its parent
		_petTodaySportsInfoPieChartFrameLayout.addView(ChartFactory
				.getPieChartView(SportsHealthTabContentActivity.this,
						_categorySeries, _mPetSportsInfoRenderer));
	}

	// inner class
	// get device server system time http request listener
	class GetDeviceServerSystemTimeHttpRequestListener extends
			OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get and check http response entity string
			String _respEntityString = HttpUtils
					.getHttpResponseEntityString(response);
			if (null != _respEntityString) {
				// get device server system time
				Long _deviceServerSystemTime = Long
						.parseLong(_respEntityString);

				// get pet today sports info value
				// define get pet today sports info value operation
				JSONObject _getPetTodaySportsInfoValueOperation = new JSONObject();
				try {
					// set get pet today sports info value operation command
					// type
					_getPetTodaySportsInfoValueOperation.put("cmdtype",
							"ARCHIVE_OPERATION");

					// define get pet today sports info value operation archive
					// operation
					JSONObject _getPetTodaySportsInfoValueOperationArchiveOperation = new JSONObject();

					// set get pet today sports info value operation archive
					// operation table name, operation and fields
					_getPetTodaySportsInfoValueOperationArchiveOperation.put(
							"tablename", "dailysummary");
					_getPetTodaySportsInfoValueOperationArchiveOperation.put(
							"operation", "QUERY");
					_getPetTodaySportsInfoValueOperationArchiveOperation.put(
							"field", new String[] { "id", "termid", "daytime",
									"type", "alarmsize", "criticalarm",
									"distance0", "distanceN", "vitality10",
									"vitality1N", "vitality20", "vitality2N",
									"vitality30", "vitality3N", "vitality40",
									"vitality4N" });

					// define get pet today sports info value operation archive
					// operation wherecond
					JSONArray _getPetTodaySportsInfoValueOperationArchiveOperationWherecond = new JSONArray();

					// define get pet today sports info value operation archive
					// operation wherecond begin and end time
					JSONObject _getPetTodaySportsInfoValueOperationArchiveOperationWherecondBeginTime = new JSONObject();
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecondBeginTime
							.put("name", "begintime");
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecondBeginTime
							.put("value", new SimpleDateFormat("yyyy-MM-dd",
									Locale.getDefault()).format(new Date()));
					JSONObject _getPetTodaySportsInfoValueOperationArchiveOperationWherecondEndTime = new JSONObject();
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecondEndTime
							.put("name", "endtime");
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecondEndTime
							.put("value", new SimpleDateFormat("yyyy-MM-dd",
									Locale.getDefault()).format(new Date()));

					// add get pet today sports info value operation archive
					// operation wherecond begin and end time
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecond
							.put(_getPetTodaySportsInfoValueOperationArchiveOperationWherecondBeginTime);
					_getPetTodaySportsInfoValueOperationArchiveOperationWherecond
							.put(_getPetTodaySportsInfoValueOperationArchiveOperationWherecondEndTime);

					// set get pet today sports info value operation archive
					// operation whereconds
					_getPetTodaySportsInfoValueOperationArchiveOperation
							.put("wherecond",
									_getPetTodaySportsInfoValueOperationArchiveOperationWherecond);

					// set get pet today sports info value pet device operation
					// archive operation
					_getPetTodaySportsInfoValueOperation
							.put("archive_operation",
									_getPetTodaySportsInfoValueOperationArchiveOperation);
				} catch (JSONException e) {
					Log.e(LOG_TAG,
							"get pet today sports info value operation error, exception message = "
									+ e.getMessage());

					e.printStackTrace();
				}

				// generate get pet today sports info value param
				Map<String, String> _getPetTodaySportsInfoParam = DeviceServerHttpReqParamUtils
						.generatePetDeviceOperateParam(_deviceServerSystemTime,
								_getPetTodaySportsInfoValueOperation.toString());

				// send get pet pet today sports info value http request
				HttpUtils.postRequest(
						getResources().getString(R.string.deviceServer_url)
								+ getResources().getString(
										R.string.deviceServer_operate_url),
						PostRequestFormat.URLENCODED,
						_getPetTodaySportsInfoParam, null,
						HttpRequestType.ASYNCHRONOUS,
						new GetPetTodaySportsInfoValuesHttpRequestListener());
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// nothing to do
		}

	}

	// get device server pet today sports info values http request listener
	class GetPetTodaySportsInfoValuesHttpRequestListener extends
			OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object status
			String _status = JSONUtils.getStringFromJSONObject(_respJsonData,
					"status");
			// check an process result
			if (null != _status && "SUCCESS".equalsIgnoreCase(_status)) {
				// get and check pet today sports info value operation return
				// archive operation
				JSONObject _getPetTodaySportsInfoValueOperationRetArchiveOperation = JSONUtils
						.getJSONObjectFromJSONObject(_respJsonData,
								"archive_operation");
				if (null != _getPetTodaySportsInfoValueOperationRetArchiveOperation) {
					// get and check pet today sports info value operation
					// return archive operation daily summary
					JSONArray _getPetTodaySportsInfoValueOperationRetArchiveOperationDailySummary = JSONUtils
							.getJSONArrayFromJSONObject(
									_getPetTodaySportsInfoValueOperationRetArchiveOperation,
									"daily_summary");
					if (null != _getPetTodaySportsInfoValueOperationRetArchiveOperationDailySummary
							&& 0 < _getPetTodaySportsInfoValueOperationRetArchiveOperationDailySummary
									.length()) {
						// get and check pet today sports info value operation
						// return archive operation today summary
						JSONObject _getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary = JSONUtils
								.getJSONObjectFromJSONArray(
										_getPetTodaySportsInfoValueOperationRetArchiveOperationDailySummary,
										_getPetTodaySportsInfoValueOperationRetArchiveOperationDailySummary
												.length() - 1);

						// get pet today sports info rest, walk, run and
						// strenuousExercise value
						Double _rest = JSONUtils
								.getDoubleFromJSONObject(
										_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
										"vitality1N")
								- JSONUtils
										.getDoubleFromJSONObject(
												_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
												"vitality10");
						Double _walk = JSONUtils
								.getDoubleFromJSONObject(
										_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
										"vitality2N")
								- JSONUtils
										.getDoubleFromJSONObject(
												_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
												"vitality20");
						Double _run = JSONUtils
								.getDoubleFromJSONObject(
										_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
										"vitality3N")
								- JSONUtils
										.getDoubleFromJSONObject(
												_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
												"vitality30");
						Double _strenuousExercise = JSONUtils
								.getDoubleFromJSONObject(
										_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
										"vitality4N")
								- JSONUtils
										.getDoubleFromJSONObject(
												_getPetTodaySportsInfoValueOperationRetArchiveOperationTodaySummary,
												"vitality40");

						// get total
						Double _total = (_rest + _walk + _run + _strenuousExercise) / 100;

						// generate pet today sports info pie chart
						List<Double> _petTodaySportsInfoValues = new ArrayList<Double>();
						_petTodaySportsInfoValues.add(_rest / _total);
						_petTodaySportsInfoValues.add(_walk / _total);
						_petTodaySportsInfoValues.add(_run / _total);
						_petTodaySportsInfoValues.add(_strenuousExercise
								/ _total);
						generatePetTodaySportsInfoPieChart(
								_petTodaySportsInfoValues, 200.0);
					}
				}
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			// nothing to do
		}

	}

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
