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

	// pet account register or reset password view stub description, password1,
	// password2 editText hint and finish button title attributes
	private String description;
	private String password1EditText1Hint;
	private String password2EditText2Hint;
	private String finishBtnTitle;

	// pet account register or reset password view stub password1, password2 and
	// editText finish button
	private EditText _mPassword1EditText;
	private EditText _mPassword2EditText;
	private Button _mFinishBtn;

	public AccountReg6ResetPwdViewStub(Context context) {
		super(context);

		// init account register or reset password view stub
		init(context, null);
	}

	public AccountReg6ResetPwdViewStub(Context context, AttributeSet attrs) {
		super(context, attrs);

		// init account register or reset password view stub
		init(context, attrs);
	}

	public AccountReg6ResetPwdViewStub(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		// init account register or reset password view stub
		init(context, attrs);
	}

	// init account register or reset password view stub
	private void init(Context context, AttributeSet attrs) {
		// define account register or reset password view stub typedArray
		TypedArray _typedArray = null;

		try {
			// get account register or reset password view stub typedArray
			_typedArray = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.account_register6reset_password_viewstub, 0, 0);

			// get account register or reset password view stub description,
			// password1, password2 editText hint and finish button title
			// attribute
			description = _typedArray
					.getString(R.styleable.account_register6reset_password_viewstub_description);
			password1EditText1Hint = _typedArray
					.getString(R.styleable.account_register6reset_password_viewstub_passwordEditText1Hint);
			password2EditText2Hint = _typedArray
					.getString(R.styleable.account_register6reset_password_viewstub_passwordEditText2Hint);
			finishBtnTitle = _typedArray
					.getString(R.styleable.account_register6reset_password_viewstub_buttonTitle);
		} catch (Exception e) {
			Log.e(LOG_TAG,
					"Get account register or reset password view stub description, password1, password2 editText hint and finish button title attributes error, exception massage = "
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
		// inflate account register or reset password view stub layout
		View _ret = LayoutInflater.from(this.getContext()).inflate(
				R.layout.account_register6reset_password_finish_layout, this);

		// check account register or reset password view stub description
		if (null != description) {
			// set account register or reset password view stub description
			// textView text
			((TextView) findViewById(R.id.accountReg_or_resetPwd_finish_description_textView))
					.setText(description);
		}

		// get account register or reset password view stub password1 editText
		_mPassword1EditText = (EditText) findViewById(R.id.accountReg_or_resetPwd_finish_password1_editText);

		// check account register or reset password view stub password1 editText
		// hint
		if (null != password1EditText1Hint) {
			// set account register or reset password view stub password1
			// editText hint
			_mPassword1EditText.setHint(password1EditText1Hint);
		}

		// get account register or reset password view stub password2 editText
		_mPassword2EditText = (EditText) findViewById(R.id.accountReg_or_resetPwd_finish_password2_editText);

		// check account register or reset password view stub password2 editText
		// hint
		if (null != password2EditText2Hint) {
			// set account register or reset password view stub password2
			// editText hint
			_mPassword2EditText.setHint(password2EditText2Hint);
		}

		// get account register or reset password view stub finish button
		_mFinishBtn = (Button) findViewById(R.id.accountReg_or_resetPwd_finish_button);

		// check account register or reset password view stub finish button
		// title
		if (null != finishBtnTitle) {
			// set account register or reset password view stub finish button
			// title
			_mFinishBtn.setText(finishBtnTitle);
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
			_mFinishBtn.setOnClickListener(finishBtnOnClickListener);
		}
	}

}
