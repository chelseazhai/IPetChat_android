package com.segotech.ipetchat.tab7tabcontent;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.richitec.commontoolkit.customcomponent.CTTabSpecIndicator;
import com.segotech.ipetchat.R;

@SuppressWarnings("deprecation")
public class IPetChatTabActivity extends TabActivity {

	// tab widget item content array
	private final int[][] TAB_WIDGETITEM_CONTENTS = new int[][] {
			{ R.string.home_tab_title, R.drawable.home_tab_icon },
			{ R.string.sports_health_tab7nav_title,
					R.drawable.sportshealth_tab_icon },
			{ R.string.community_tab7nav_title, R.drawable.community_tab_icon },
			{ R.string.settings_tab7nav_title, R.drawable.settings_tab_icon } };

	// current tab index, default is home tab
	private int _mCurrentTabIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.ipet_chat_tab_activity_layout);

		// get tabHost
		TabHost _tabHost = getTabHost();

		// define tabSpec
		TabSpec _tabSpec;

		// set tab indicator and content
		// home
		_tabSpec = _tabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[0][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[0][0],
								TAB_WIDGETITEM_CONTENTS[0][1]))
				.setContent(
						new Intent().setClass(this,
								HomeTabContentActivity.class));
		_tabHost.addTab(_tabSpec);

		// sports and health
		_tabSpec = _tabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[1][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[1][0],
								TAB_WIDGETITEM_CONTENTS[1][1]))
				.setContent(
						new Intent().setClass(this,
								SportsHealthTabContentActivity.class));
		_tabHost.addTab(_tabSpec);

		// community
		_tabSpec = _tabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[2][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[2][0],
								TAB_WIDGETITEM_CONTENTS[2][1]))
				.setContent(
						new Intent().setClass(this,
								CommunityTabContentActivity.class));
		_tabHost.addTab(_tabSpec);

		// settings
		_tabSpec = _tabHost
				.newTabSpec(
						getResources().getString(TAB_WIDGETITEM_CONTENTS[3][0]))
				.setIndicator(
						new CTTabSpecIndicator(this,
								TAB_WIDGETITEM_CONTENTS[3][0],
								TAB_WIDGETITEM_CONTENTS[3][1]))
				.setContent(
						new Intent().setClass(this,
								SettingsTabContentActivity.class));
		_tabHost.addTab(_tabSpec);

		// set current tab
		_tabHost.setCurrentTab(_mCurrentTabIndex);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
