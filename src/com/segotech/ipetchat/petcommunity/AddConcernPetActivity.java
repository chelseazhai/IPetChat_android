package com.segotech.ipetchat.petcommunity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.richitec.commontoolkit.customcomponent.BarButtonItem;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class AddConcernPetActivity extends IPetChatNavigationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.add_concern_pet_activity_layout);

		// set title
		setTitle(R.string.add_concern_pet_nav_title);

		// set find account with number as right bar button item
		setRightBarButtonItem(new BarButtonItem(this,
				R.string.add_concern_pet_findAccount_button_text,
				new FindAccountWithNumberBtnOnClickListener()));
	}

	// inner calss
	// find account with number button on click listener
	class FindAccountWithNumberBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}

	}

}
