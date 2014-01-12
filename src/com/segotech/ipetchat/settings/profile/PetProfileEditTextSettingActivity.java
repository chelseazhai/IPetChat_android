package com.segotech.ipetchat.settings.profile;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class PetProfileEditTextSettingActivity extends
		IPetChatNavigationActivity {

	// pet profile editText title, hint, text, input type and comment key
	public static final String PET_PROFILE_EDITTEXT_TITLE_KEY = "pet_profile_edittext_title_key";
	public static final String PET_PROFILE_EDITTEXT_HINT_KEY = "pet_profile_edittext_hint_key";
	public static final String PET_PROFILE_EDITTEXT_TEXT_KEY = "pet_profile_edittext_text_key";
	public static final String PET_PROFILE_EDITTEXT_INPUTTYPE_KEY = "pet_profile_edittext_inputtype_key";
	public static final String PET_PROFILE_EDITTEXT_COMMENT_KEY = "pet_profile_edittext_comment_key";

	// pet profile editText
	private EditText _mPetProfileEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.pet_profile_edittext_setting_activity_layout);

		// define title, editText hint, text, input type and comment
		String _title = "";
		String _editTextHint = "";
		String _editTextText = "";
		int _exitTextInputType = InputType.TYPE_CLASS_TEXT;
		String _comment = null;

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get title, hint and input type
			_title = _data.getString(PET_PROFILE_EDITTEXT_TITLE_KEY);
			_editTextHint = _data.getString(PET_PROFILE_EDITTEXT_HINT_KEY);
			_editTextText = _data.getString(PET_PROFILE_EDITTEXT_TEXT_KEY);
			_exitTextInputType = _data
					.getInt(PET_PROFILE_EDITTEXT_INPUTTYPE_KEY);
			_comment = _data.getString(PET_PROFILE_EDITTEXT_COMMENT_KEY);
		}

		// set title
		setTitle(_title);

		// set pet profile editText save button as right bar button item
		setRightBarButtonItem(new BarButtonItem(this,
				R.string.ppes_save_button_title,
				new PetProfileEditTextSaveBtnOnClickListener()));

		// get pet profile editText
		_mPetProfileEditText = (EditText) findViewById(R.id.ppes_editText);

		// set its hint, text and input type
		_mPetProfileEditText.setHint(_editTextHint);
		_mPetProfileEditText.setText(_editTextText);
		_mPetProfileEditText.setInputType(_exitTextInputType);

		// check comment
		if (null != _comment) {
			// get pet profile editText comment textView
			TextView _commentTextView = (TextView) findViewById(R.id.ppes_comment_textView);

			// set pet profile editText comment textView text
			_commentTextView.setText(_comment);

			// show pet profile editText comment textView
			_commentTextView.setVisibility(View.VISIBLE);
		}
	}

	// inner class
	// pet profile editText save button on click listener
	class PetProfileEditTextSaveBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// define pop result extraData
			Map<String, String> _popRetExtraData = new HashMap<String, String>();

			// put pet profile editText text in pop result extra data
			_popRetExtraData.put(POP_RET_EXTRADATA_KEY, _mPetProfileEditText
					.getText().toString());

			// pop pet profile editText activity
			popActivityWithResult(RESULT_OK, _popRetExtraData);
		}

	}

}
