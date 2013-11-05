package com.segotech.ipetchat;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.richitec.commontoolkit.activityextension.AppLaunchActivity;
import com.richitec.commontoolkit.user.UserBean;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.DataStorageUtils;
import com.segotech.ipetchat.account.AccountSetting4FirstActivity;
import com.segotech.ipetchat.account.user.IPCUserExtension.ComUserLocalStorageAttributes;
import com.segotech.ipetchat.tab7tabcontent.IPetChatTabActivity;

public class IPetChatAppLaunchActivity extends AppLaunchActivity {

	@Override
	public Drawable splashImg() {
		// iPetChat application splash
		return getResources().getDrawable(R.drawable.ic_splash);
	}

	@Override
	public Intent intentActivity() {
		// define default target intent activity, pet chat tab activity
		Intent _targetIntentActivity = new Intent(this,
				IPetChatTabActivity.class);

		// get local storage user
		UserBean _localStorageUser = UserManager.getInstance().getUser();

		// check if there is a account at least
		if (null == _localStorageUser.getName()
				|| "".equalsIgnoreCase(_localStorageUser.getName())) {
			// account setting for first activity as target
			_targetIntentActivity = new Intent(this,
					AccountSetting4FirstActivity.class);
		}

		// go to target activity
		return _targetIntentActivity;
	}

	@Override
	public boolean didFinishLaunching() {
		// get login user info from storage and add to user manager
		UserBean _localStorageUser = new UserBean();

		// set user name and key
		_localStorageUser
				.setName(DataStorageUtils
						.getString(ComUserLocalStorageAttributes.LOGIN_USERNAME
								.name()));
		_localStorageUser.setUserKey(DataStorageUtils
				.getString(ComUserLocalStorageAttributes.LOGIN_USERKEY.name()));

		// save user bean and add to user manager
		UserManager.getInstance().setUser(_localStorageUser);

		return false;
	}

}
