package com.segotech.ipetchat.tab7tabcontent;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.richitec.commontoolkit.customcomponent.CTTabSpecIndicator;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.segotech.ipetchat.R;

@SuppressWarnings("deprecation")
public class IPetChatTabActivity extends TabActivity {

	private static final String LOG_TAG = IPetChatTabActivity.class
			.getCanonicalName();

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
		// get user all pets info
		// send get user all pets info post http request
		HttpUtils.postSignatureRequest(
				getResources().getString(R.string.server_url)
						+ getResources()
								.getString(R.string.get_allPetsInfo_url),
				PostRequestFormat.URLENCODED, null, null,
				HttpRequestType.ASYNCHRONOUS,
				new GetAllPetsInfoHttpRequestListener());

		super.onResume();
	}

	// inner class
	// get user all pets info http request listener
	class GetAllPetsInfoHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string
			String _respEntityString = HttpUtils
					.getHttpResponseEntityString(response);

			Log.d(LOG_TAG,
					"get user all pets info http response entity string = "
							+ _respEntityString);

			//
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get user all pets info failed, send get user all pets info post request failed");

			//
		}

	}

}
