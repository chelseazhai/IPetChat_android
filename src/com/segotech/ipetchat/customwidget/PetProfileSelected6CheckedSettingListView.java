package com.segotech.ipetchat.customwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.segotech.ipetchat.R;

public class PetProfileSelected6CheckedSettingListView extends ListView {

	public PetProfileSelected6CheckedSettingListView(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PetProfileSelected6CheckedSettingListView(Context context,
			AttributeSet attrs) {
		super(context, attrs);
	}

	public PetProfileSelected6CheckedSettingListView(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// check motion event action
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// get and check touch listView item index
			int _touchItemIndex = pointToPosition((int) ev.getX(),
					(int) ev.getY());

			if (AdapterView.INVALID_POSITION != _touchItemIndex) {
				// check touch item index
				if (0 == _touchItemIndex) {
					// check touch item index again
					if (getAdapter().getCount() - 1 == _touchItemIndex) {
						// single item
						setSelector(R.drawable.pet_profile_selected6checked_setting_single_item_bg);
					} else {
						// first item of multiple
						setSelector(R.drawable.pet_profile_selected6checked_setting_topmultiple_item_bg);
					}
				} else if (getAdapter().getCount() - 1 == _touchItemIndex) {
					// last item of multiple
					setSelector(R.drawable.pet_profile_selected6checked_setting_bottommultiple_item_bg);
				} else {
					// middle item of multiple
					setSelector(R.drawable.pet_profile_selected6checked_setting_middlemultiple_item_bg);
				}
			}
			break;

		case MotionEvent.ACTION_UP:
			// nothing to do
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}

}
