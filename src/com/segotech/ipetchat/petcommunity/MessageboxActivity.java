package com.segotech.ipetchat.petcommunity;

import android.os.Bundle;

import com.richitec.commontoolkit.activityextension.NavigationActivity;
import com.segotech.ipetchat.R;

public class MessageboxActivity extends NavigationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.messagebox_activity_layout);

		// set title
		setTitle(R.string.message_box_nav_title);

		//
	}

}
