package com.segotech.ipetchat.tab7tabcontent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class SportsHealthTabContentActivity extends IPetChatNavigationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.sports_health_tab_content_activity_layout);

		// set title
		setTitle(R.string.sports_health_tab7nav_title);

		// test by ares
		// find demo sports and health image view
		final ImageView _demoSportsHealthImgView = (ImageView) findViewById(R.id.demo_imageView);

		// histody button
		((Button) findViewById(R.id.histody_button))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						_demoSportsHealthImgView
								.setImageResource(R.drawable.img_demo_sportshealth_history);
					}
				});

		// today button
		((Button) findViewById(R.id.today_button))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						_demoSportsHealthImgView
								.setImageResource(R.drawable.img_demo_sportshealth_today);
					}
				});
	}

}
