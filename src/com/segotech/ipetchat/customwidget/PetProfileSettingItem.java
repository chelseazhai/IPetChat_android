package com.segotech.ipetchat.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.segotech.ipetchat.R;

public class PetProfileSettingItem extends RelativeLayout {

	private static final String LOG_TAG = PetProfileSettingItem.class
			.getCanonicalName();

	// pet profile setting item label and text
	private String label;
	private String text;

	public PetProfileSettingItem(Context context) {
		super(context);

		// init pet profile setting item
		init(context, null);
	}

	public PetProfileSettingItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		// init pet profile setting item
		init(context, attrs);
	}

	public PetProfileSettingItem(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		// init pet profile setting item
		init(context, attrs);
	}

	// init pet profile setting item
	private void init(Context context, AttributeSet attrs) {
		// inflate pet profile setting item layout
		LayoutInflater.from(context).inflate(
				R.layout.pet_profile_setting_item_layout, this);

		// define pet profile setting item typedArray
		TypedArray _typedArray = null;

		try {
			// get pet profile setting item typedArray
			_typedArray = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.pet_profile_setting_item, 0, 0);

			// get pet profile item setting label attribute
			label = _typedArray
					.getString(R.styleable.pet_profile_setting_item_label);
		} catch (Exception e) {
			Log.e(LOG_TAG,
					"Get pet profile setting item label attribute error, exception massage = "
							+ e.getMessage());

			e.printStackTrace();
		} finally {
			// recycle pet profile setting item typedArray
			if (null != _typedArray) {
				_typedArray.recycle();
			}
		}

		// check pet profile setting item label
		if (null != label) {
			// set pet profile setting item label textView text
			((TextView) findViewById(R.id.pet_profile_setting_item_label_textView))
					.setText(label);
		}

		// set pet profile setting item clickable
		setClickable(true);
	}

	public String getLabel() {
		return label;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;

		// set pet profile setting item text textView text
		((TextView) findViewById(R.id.pet_profile_setting_item_textView))
				.setText(text);
	}

}
