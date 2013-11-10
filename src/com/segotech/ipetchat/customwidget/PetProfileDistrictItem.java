package com.segotech.ipetchat.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.segotech.ipetchat.R;

public class PetProfileDistrictItem extends RelativeLayout {

	private static final String LOG_TAG = PetProfileDistrictItem.class
			.getCanonicalName();

	// pet profile district item text and sub items
	private String text;
	private String[] subitems;

	public PetProfileDistrictItem(Context context) {
		super(context);

		// init pet profile district item
		init(context, null);
	}

	public PetProfileDistrictItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		// init pet profile district item
		init(context, attrs);
	}

	public PetProfileDistrictItem(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		// init pet profile district item
		init(context, attrs);
	}

	// init pet profile district item
	private void init(Context context, AttributeSet attrs) {
		// inflate pet profile district item layout
		LayoutInflater.from(context).inflate(
				R.layout.pet_profile_district_item_layout, this);

		// define pet profile district item typedArray
		TypedArray _typedArray = null;

		try {
			// get pet profile district item typedArray
			_typedArray = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.pet_profile_district_item, 0, 0);

			// get pet profile district item text and sub items attribute
			text = _typedArray
					.getString(R.styleable.pet_profile_district_item_text);
			subitems = getResources()
					.getStringArray(
							_typedArray
									.getResourceId(
											R.styleable.pet_profile_district_item_subitems,
											-1));
		} catch (Exception e) {
			Log.e(LOG_TAG,
					"Get pet profile district item text or sub items error, exception message = "
							+ e.getMessage());

			e.printStackTrace();
		} finally {
			// recycle pet profile district item typedArray
			if (null != _typedArray) {
				_typedArray.recycle();
			}
		}

		// check pet profile district item text
		if (null != text) {
			// set pet profile district item text textView text
			((TextView) findViewById(R.id.pet_profile_district_item_textView))
					.setText(text);
		}

		// set pet profile district item clickable
		setClickable(true);
	}

	public String[] getSubitems() {
		return subitems;
	}

	public String getName() {
		return text;
	}

}
