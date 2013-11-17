package com.segotech.ipetchat.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.segotech.ipetchat.R;

public class AccountReg6ResetPwdViewStub extends FrameLayout {

	private static final String LOG_TAG = AccountReg6ResetPwdViewStub.class
			.getCanonicalName();

	// account register or reset password viewStub description, password1,
	// password2 editText hint and finish button title attributes
	private String description;
	private String password1EditText1Hint;
	private String password2EditText2Hint;
	private String finishButtonTitle;

	// account register or reset password viewStub password1, password2 and
	// editText finish button
	private EditText _mPassword1EditText;
	private EditText _mPassword2EditText;
	private Button _mFinishButton;

	public AccountReg6ResetPwdViewStub(Context context) {
		super(context);

		// init account register or reset password viewStub
		init(context, null);
	}

	public AccountReg6ResetPwdViewStub(Context context, AttributeSet attrs) {
		super(context, attrs);

		// init account register or reset password viewStub
		init(context, attrs);
	}

	public AccountReg6ResetPwdViewStub(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		// init account register or reset password viewStub
		init(context, attrs);
	}

	// init account register or reset password viewStub
	private void init(Context context, AttributeSet attrs) {
		// define account register or reset password viewStub typedArray
		TypedArray _typedArray = null;

		try {
			// get account register or reset password viewStub typedArray
			_typedArray = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.account_register6resetpwd_viewstub, 0, 0);

			// get account register or reset password viewStub description,
			// password1, password2 editText hint and finish button title
			// attribute
			description = _typedArray
					.getString(R.styleable.account_register6resetpwd_viewstub_description);
			password1EditText1Hint = _typedArray
					.getString(R.styleable.account_register6resetpwd_viewstub_passwordEditText1Hint);
			password2EditText2Hint = _typedArray
					.getString(R.styleable.account_register6resetpwd_viewstub_passwordEditText2Hint);
			finishButtonTitle = _typedArray
					.getString(R.styleable.account_register6resetpwd_viewstub_buttonTitle);
		} catch (Exception e) {
			Log.e(LOG_TAG,
					"Get account register or reset password viewStub description, password1, password2 editText hint and finish button title attributes error, exception massage = "
							+ e.getMessage());

			e.printStackTrace();
		} finally {
			// recycle account register or reset password view stub typedArray
			if (null != _typedArray) {
				_typedArray.recycle();
			}
		}

		// set visibility gone
		setVisibility(View.GONE);
	}

	public View inflate() {
		// inflate account register or reset password viewStub layout
		View _ret = LayoutInflater.from(this.getContext()).inflate(
				R.layout.account_register6resetpwd_layout, this);

		// check account register or reset password viewStub description
		if (null != description) {
			// set account register or reset password viewStub description
			// textView text
			((TextView) findViewById(R.id.accountRegister_or_resetPwd_description_textView))
					.setText(description);
		}

		// get account register or reset password viewStub password1 editText
		_mPassword1EditText = (EditText) findViewById(R.id.accountRegister_or_resetPwd_password1_editText);

		// check account register or reset password viewStub password1 editText
		// hint
		if (null != password1EditText1Hint) {
			// set account register or reset password viewStub password1
			// editText hint
			_mPassword1EditText.setHint(password1EditText1Hint);
		}

		// get account register or reset password viewStub password2 editText
		_mPassword2EditText = (EditText) findViewById(R.id.accountRegister_or_resetPwd_password2_editText);

		// check account register or reset password viewStub password2 editText
		// hint
		if (null != password2EditText2Hint) {
			// set account register or reset password viewStub password2
			// editText hint
			_mPassword2EditText.setHint(password2EditText2Hint);
		}

		// get account register or reset password viewStub finish button
		_mFinishButton = (Button) findViewById(R.id.accountRegister_or_resetPwd_finish_button);

		// check account register or reset password viewStub finish button title
		if (null != finishButtonTitle) {
			// set account register or reset password viewStub finish button
			// title
			_mFinishButton.setText(finishButtonTitle);
		}

		// set visibility visible
		setVisibility(View.VISIBLE);

		return _ret;
	}

	public String getPassword1EditTextText() {
		String _password1EditTextText = null;

		// check password1 editText
		if (null != _mPassword1EditText) {
			_password1EditTextText = _mPassword1EditText.getText().toString();
		}

		return _password1EditTextText;
	}

	public String getPassword2EditTextText() {
		String _password2EditTextText = null;

		// check password2 editText
		if (null != _mPassword2EditText) {
			_password2EditTextText = _mPassword2EditText.getText().toString();
		}

		return _password2EditTextText;
	}

	public void setFinishBtnOnClickListener(
			OnClickListener finishBtnOnClickListener) {
		// check finish button on click listener
		if (null != finishBtnOnClickListener) {
			_mFinishButton.setOnClickListener(finishBtnOnClickListener);
		}
	}

}
