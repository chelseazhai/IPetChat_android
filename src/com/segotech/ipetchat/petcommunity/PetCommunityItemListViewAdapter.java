package com.segotech.ipetchat.petcommunity;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.richitec.commontoolkit.customadapter.CTListAdapter;
import com.segotech.ipetchat.R;

public class PetCommunityItemListViewAdapter extends CTListAdapter {

	private static final String LOG_TAG = PetCommunityItemListViewAdapter.class
			.getCanonicalName();

	public PetCommunityItemListViewAdapter(Context context,
			List<Map<String, ?>> data, int itemsLayoutResId, String[] dataKeys,
			int[] itemsComponentResIds) {
		super(context, data, itemsLayoutResId, dataKeys, itemsComponentResIds);
	}

	@Override
	protected void bindView(View view, Map<String, ?> dataMap, String dataKey) {
		// get item data object
		Object _itemData = dataMap.get(dataKey);

		// check view type
		// button
		if (view instanceof Button) {
			try {
				// define item data on click listener and convert item data
				// to on click listener
				OnClickListener _itemOnClickListener = (OnClickListener) _itemData;

				// set button on click listener
				((Button) view).setOnClickListener(_itemOnClickListener);
			} catch (Exception e) {
				e.printStackTrace();

				Log.e(LOG_TAG,
						"Convert item data to on click listener error, item data = "
								+ _itemData);
			}
		}
		// textView
		else if (view instanceof TextView) {
			// set view text
			((TextView) view)
					.setText(null == _itemData ? ""
							: _itemData instanceof SpannableString ? (SpannableString) _itemData
									: _itemData.toString());

			// set view visibility if needed
			view.setVisibility((null != view.getTag() && _mContext
					.getResources().getString(R.string.pet_mood_textview_tag)
					.equalsIgnoreCase(view.getTag().toString()))
					&& "".equalsIgnoreCase((String) ((TextView) view).getText()) ? View.GONE
					: View.VISIBLE);
		}
		// imageView
		else if (view instanceof ImageView) {
			try {
				// define item data bitmap and convert item data to bitmap
				Bitmap _itemDataBitmap = (Bitmap) _itemData;

				// set imageView image
				if (null != _itemDataBitmap) {
					((ImageView) view).setImageBitmap(_itemDataBitmap);
				}
			} catch (Exception e) {
				e.printStackTrace();

				Log.e(LOG_TAG,
						"Convert item data to bitmap error, item data = "
								+ _itemData);
			}
		}
	}

}
