package com.segotech.ipetchat;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.richitec.commontoolkit.activityextension.AppLaunchActivity;
import com.segotech.ipetchat.tab7tabcontent.IPetChatTabActivity;

public class IPetChatAppLaunchActivity extends AppLaunchActivity {

	@Override
	public Drawable splashImg() {
		// iPetChat application splash
		return getResources().getDrawable(R.drawable.ic_splash);
	}

	@Override
	public Intent intentActivity() {
		// go to iPetChat tab activity
		return new Intent(this, IPetChatTabActivity.class);
	}

	@Override
	public boolean didFinishLaunching() {
		// TODO Auto-generated method stub
		return false;
	}

}
