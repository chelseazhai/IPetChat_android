package com.segotech.ipetchat.account;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.richitec.commontoolkit.activityextension.NavigationActivity;
import com.segotech.ipetchat.R;

public class AccountSetting4FirstActivity extends NavigationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_setting4first_activity_layout);

		// set account register and login button on click listener
		((Button) findViewById(R.id.account_register_button))
				.setOnClickListener(new AccountRegisterBtnOnClickListener());
		((Button) findViewById(R.id.account_login_button))
				.setOnClickListener(new AccountLoginBtnOnClickListener());
	}

	@Override
	protected boolean hideNavigationBarWhenOnCreated() {
		// hide navigation bar when its on created
		return true;
	}

	// inner class
	// account register button on click listener
	class AccountRegisterBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// register an new account, go to account register activity
			pushActivity(AccountRegisterActivity.class);
		}

	}

	// account login button on click listener
	class AccountLoginBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// account login, go to account login activity
			pushActivity(AccountLoginActivity.class);
		}

	}

}
