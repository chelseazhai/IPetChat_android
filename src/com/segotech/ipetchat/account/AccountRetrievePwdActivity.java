package com.segotech.ipetchat.account;

import android.os.Bundle;

import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class AccountRetrievePwdActivity extends IPetChatNavigationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_retrieve_pwd_activity_layout);

		// set title
		setTitle(R.string.account_retrieve_password_nav_title);

		//
	}

}
