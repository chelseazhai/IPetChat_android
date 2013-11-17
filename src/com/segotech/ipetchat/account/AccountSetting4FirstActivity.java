package com.segotech.ipetchat.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.richitec.commontoolkit.activityextension.NavigationActivity;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.tab7tabcontent.IPetChatTabActivity;

public class AccountSetting4FirstActivity extends NavigationActivity {

	// account setting request code
	private static final int ACCOUNT_REGISTER_REQCODE = 100;
	private static final int ACCOUNT_LOGIN_REQCODE = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_setting4first_activity_layout);

		// set account register and login button on click listener
		((Button) findViewById(R.id.as4f_accountRegister_button))
				.setOnClickListener(new AccountRegisterBtnOnClickListener());
		((Button) findViewById(R.id.as4f_accountLogin_button))
				.setOnClickListener(new AccountLoginBtnOnClickListener());
	}

	@Override
	protected boolean hideNavigationBarWhenOnCreated() {
		// hide navigation bar when its on created
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// check result code
		switch (resultCode) {
		case RESULT_OK:
			// check request code
			switch (requestCode) {
			case ACCOUNT_REGISTER_REQCODE:
				// go to account login activity
				pushActivityForResult(AccountLoginActivity.class,
						ACCOUNT_LOGIN_REQCODE);
				break;

			case ACCOUNT_LOGIN_REQCODE:
				// finish account setting activity and go to iPetChat tab
				// activity
				finish();
				startActivity(new Intent(this, IPetChatTabActivity.class));
				break;
			}
			break;

		default:
			// nothing to do
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// inner class
	// account register button on click listener
	class AccountRegisterBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// register an new account, go to account register activity
			pushActivityForResult(AccountRegisterActivity.class,
					ACCOUNT_REGISTER_REQCODE);
		}

	}

	// account login button on click listener
	class AccountLoginBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// account login, go to account login activity
			pushActivityForResult(AccountLoginActivity.class,
					ACCOUNT_LOGIN_REQCODE);
		}

	}

}
