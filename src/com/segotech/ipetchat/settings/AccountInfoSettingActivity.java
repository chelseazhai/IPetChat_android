package com.segotech.ipetchat.settings;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.richitec.commontoolkit.user.UserManager;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.AccountModifyPwdActivity;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class AccountInfoSettingActivity extends IPetChatNavigationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.account_info_setting_activity_layout);

		// set title
		setTitle(R.string.account_info_setting_nav_title);

		// set account info username item textView text
		((TextView) findViewById(R.id.account_info_username_item_textView))
				.setText(UserManager.getInstance().getUser().getName());

		// set account info modify password item parent relativeLayout on click
		// listener
		findViewById(R.id.account_info_modifypwd_item_parentRelativeLayout)
				.setOnClickListener(
						new AccountInfoModifyPwdItemOnClickListener());
	}

	// inner class
	// account info modify password item on click listener
	class AccountInfoModifyPwdItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// go to account modify password activity
			pushActivity(AccountModifyPwdActivity.class);
		}

	}

}
