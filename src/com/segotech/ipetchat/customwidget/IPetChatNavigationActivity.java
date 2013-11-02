package com.segotech.ipetchat.customwidget;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.richitec.commontoolkit.activityextension.NavigationActivity;
import com.richitec.commontoolkit.customcomponent.BarButtonItem.BarButtonItemStyle;
import com.richitec.commontoolkit.customcomponent.ImageBarButtonItem;
import com.segotech.ipetchat.R;

public class IPetChatNavigationActivity extends NavigationActivity {

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);

		// set back navigation image bar button item as left image bar button
		// item
		setLeftBarButtonItem(new BackNavImgBarButtonItem(this,
				R.drawable.img_back_nav_item, BarButtonItemStyle.RIGHT_GO,
				_mBackBarBtnItemOnClickListener));
	}

	// inner class
	// back navigation image bar button item
	class BackNavImgBarButtonItem extends ImageBarButtonItem {

		public BackNavImgBarButtonItem(Context context, Drawable srcDrawable,
				BarButtonItemStyle barBtnItemStyle,
				OnClickListener btnClickListener) {
			super(
					context,
					srcDrawable,
					barBtnItemStyle,
					null,
					context.getResources()
							.getDrawable(
									com.richitec.commontoolkit.R.drawable.img_rightbarbtnitem_touchdown_bg),
					btnClickListener);
		}

		public BackNavImgBarButtonItem(Context context, int srcId,
				BarButtonItemStyle barBtnItemStyle,
				OnClickListener btnClickListener) {
			this(context, context.getResources().getDrawable(srcId),
					barBtnItemStyle, btnClickListener);
		}

		public BackNavImgBarButtonItem(Context context, Drawable srcDrawable,
				BarButtonItemStyle barBtnItemStyle,
				Drawable normalBackgroundDrawable,
				Drawable pressedBackgroundDrawable,
				OnClickListener btnClickListener) {
			super(context, srcDrawable, barBtnItemStyle,
					normalBackgroundDrawable, pressedBackgroundDrawable,
					btnClickListener);
		}

		public BackNavImgBarButtonItem(Context context, Drawable srcDrawable,
				OnClickListener btnClickListener) {
			super(context, srcDrawable, btnClickListener);
		}

		public BackNavImgBarButtonItem(Context context, int srcId,
				int normalBackgroundResId, int pressedBackgroundResId,
				OnClickListener btnClickListener) {
			super(context, srcId, normalBackgroundResId,
					pressedBackgroundResId, btnClickListener);
		}

		public BackNavImgBarButtonItem(Context context, int srcId,
				OnClickListener btnClickListener) {
			super(context, srcId, btnClickListener);
		}

		public BackNavImgBarButtonItem(Context context, int resId) {
			super(context, resId);
		}

		public BackNavImgBarButtonItem(Context context) {
			super(context);
		}

		@Override
		protected Drawable leftBarBtnItemNormalDrawable() {
			return null;
		}

		@Override
		protected Drawable rightBarBtnItemNormalDrawable() {
			return null;
		}

	}

}
