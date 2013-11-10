package com.segotech.ipetchat.account;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class AccountRetrievePwdActivity extends IPetChatNavigationActivity {

	// private static final String LOG_TAG = AccountRetrievePwdActivity.class
	// .getCanonicalName();
	//
	// // get account retrieve password finish view stub
	// private AccountReg6ResetPwdViewStub _mAccountRetrievePwdFinishViewStub;
	//
	// // get, verify account retrieve password verification code and finish
	// reset
	// // password view
	// private View _mAccountRetrievePwdGetVerificationCodeView;
	// private View _mAccountRetrievePwdVerifyVerificationCodeView;
	// private View _mAccountRetrievePwdFinishResetPwdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_retrieve_pwd_activity_layout);

		// set title
		setTitle(R.string.account_retrieve_password_nav_title);

		// // get account retrieve password step 1 view
		// _mAccountRetrievePwdGetVerificationCodeView =
		// findViewById(R.id.account_retrieve_password_step1_linearLayout);

		// set get account retrieve password phone verification code button on
		// click listener
		((Button) findViewById(R.id.get_phoneVerificationCode_button))
				.setOnClickListener(new GetPhoneVerificationCodeBtnOnClickListener());
	}

	// inner class
	// get account retrieve passwoed phone verification code button on click
	// listener
	class GetPhoneVerificationCodeBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// get account retrieve password phone number
			String _accountRetrievePwdPhoneNumber = ((EditText) findViewById(R.id.get_phoneVerificationCode_phone_editText))
					.getText().toString();

			// check account retrieve password phone number
			if (null == _accountRetrievePwdPhoneNumber
					|| "".equalsIgnoreCase(_accountRetrievePwdPhoneNumber)) {
				Toast.makeText(
						AccountRetrievePwdActivity.this,
						R.string.toast_get_phoneVerificationCode_phoneNumber_null,
						Toast.LENGTH_SHORT).show();

				return;
			}

			// get account retrieve password phone verification code
			//
		}

	}

}
