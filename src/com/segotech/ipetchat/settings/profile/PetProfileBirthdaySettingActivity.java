package com.segotech.ipetchat.settings.profile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.richitec.commontoolkit.user.UserManager;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.utils.DateStringUtils;

public class PetProfileBirthdaySettingActivity extends
		IPetChatNavigationActivity {

	// pet profile birthday key
	public static final String PET_PROFILE_BIRTHDAY_KEY = "pet_profile_birthday_key";

	// pet birthday datePicker and display textView
	private DatePicker _mBirthdayDatePicker;
	private TextView _mBirthdayDisplayTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_profile_birthday_setting_activity_layout);

		// define pet birthday
		Long _petBirthday = 0L;

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get prt birthday
			_petBirthday = _data.getLong(PET_PROFILE_BIRTHDAY_KEY);
		}

		// set title
		setTitle("生日");

		// set pet profile birthday save button as right bar button item
		setRightBarButtonItem(new BarButtonItem(this,
				R.string.ppes_save_button_title,
				new PetProfileBirthdaySaveBtnOnClickListener()));

		// get pet birthday datePicker and its display textView
		_mBirthdayDatePicker = (DatePicker) findViewById(R.id.ppbs_birthday_datePicker);
		_mBirthdayDisplayTextView = (TextView) findViewById(R.id.ppbs_birthdayDisplay_textView);

		// set pet birthday display textView text
		_mBirthdayDisplayTextView.setText(new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault()).format(_petBirthday));

		// get pet birthday calendar
		Calendar _petBirthdayCalendar = Calendar.getInstance(Locale
				.getDefault());
		_petBirthdayCalendar.setTimeInMillis(_petBirthday);

		// init pet birthday datePicker
		_mBirthdayDatePicker.init(_petBirthdayCalendar.get(Calendar.YEAR),
				_petBirthdayCalendar.get(Calendar.MONTH),
				_petBirthdayCalendar.get(Calendar.DAY_OF_MONTH),
				new PetProfileBirthdayDatePickerOnDateChangedListener());

		// set max date
		_mBirthdayDatePicker.setMaxDate(System.currentTimeMillis());
	}

	// inner class
	// pet profile birthday save button on click listener
	class PetProfileBirthdaySaveBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get my pet info
			PetBean _petInfo = IPCUserExtension.getUserPetInfo(UserManager
					.getInstance().getUser());

			// set pet birthday
			_petInfo.setBirthday(DateStringUtils.shortDateString2Date(
					_mBirthdayDisplayTextView.getText().toString()).getTime());

			// define pop result extraData
			Map<String, String> _popRetExtraData = new HashMap<String, String>();

			// put pet profile pet age in pop result extra data
			_popRetExtraData.put(POP_RET_EXTRADATA_KEY, _petInfo.getAge()
					.toString());

			// pop pet profile editText activity
			popActivityWithResult(RESULT_OK, _popRetExtraData);
		}

	}

	// pet profile birthday date picker on date changed listener
	class PetProfileBirthdayDatePickerOnDateChangedListener implements
			OnDateChangedListener {

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// update pet birthday display textView
			_mBirthdayDisplayTextView.setText(year + "-"
					+ String.format("%02d", monthOfYear + 1) + "-"
					+ String.format("%02d", dayOfMonth));
		}

	}

}
