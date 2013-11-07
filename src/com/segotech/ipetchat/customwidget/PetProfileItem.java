package com.segotech.ipetchat.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.segotech.ipetchat.R;

public class PetProfileItem extends RelativeLayout {

	private static final String LOG_TAG = PetProfileItem.class
			.getCanonicalName();

	// pet profile item label and text
	private String label;
	private String text;

	public PetProfileItem(Context context) {
		super(context);

		// init pet profile item
		init(context, null);
	}

	public PetProfileItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		// init pet profile item
		init(context, attrs);
	}

	public PetProfileItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// init pet profile item
		init(context, attrs);
	}

	// init pet profile item
	private void init(Context context, AttributeSet attrs) {
		// inflate pet profile item layout
		LayoutInflater.from(context).inflate(R.layout.pet_profile_item_layout,
				this);

		// define pet profile item typedArray
		TypedArray _typedArray = null;

		try {
			// get pet profile item typedArray
			_typedArray = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.pet_profile_item, 0, 0);

			// get pet profile item label and text attribute
			label = _typedArray.getString(R.styleable.pet_profile_item_label);
			text = _typedArray.getString(R.styleable.pet_profile_item_text);
		} catch (Exception e) {
			Log.e(LOG_TAG, "" + e.getMessage());

			e.printStackTrace();
		} finally {
			// recycle pet profile item typedArray
			if (null != _typedArray) {
				_typedArray.recycle();
			}
		}

		// get pet profile item label and text textView
		TextView _labelTextView = (TextView) findViewById(R.id.pet_profile_itemLabel_textView);
		TextView _textTextView = (TextView) findViewById(R.id.pet_profile_item_textView);

		// set profile item label and text
		if (null != label) {
			_labelTextView.setText(label);
		}
		if (null != text) {
			_textTextView.setText(text);
		}

		// set pet profile item clickable
		setClickable(true);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;

		// set pet profile item text textView text
		((TextView) findViewById(R.id.pet_profile_item_textView)).setText(text);
	}

}
