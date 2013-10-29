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

		// set logo as left image bar button item
		setLeftBarButtonItem(new LogoImgBarButtonItem(this,
				R.drawable.img_logo, null));
	}

	// inner class
	// logo image bar button item
	class LogoImgBarButtonItem extends ImageBarButtonItem {

		public LogoImgBarButtonItem(Context context, Drawable srcDrawable,
				BarButtonItemStyle barBtnItemStyle,
				OnClickListener btnClickListener) {
			super(context, srcDrawable, barBtnItemStyle, null, null,
					btnClickListener);
		}

		public LogoImgBarButtonItem(Context context, int srcId,
				BarButtonItemStyle barBtnItemStyle,
				OnClickListener btnClickListener) {
			this(context, context.getResources().getDrawable(srcId),
					barBtnItemStyle, btnClickListener);
		}

		public LogoImgBarButtonItem(Context context, Drawable srcDrawable,
				BarButtonItemStyle barBtnItemStyle,
				Drawable normalBackgroundDrawable,
				Drawable pressedBackgroundDrawable,
				OnClickListener btnClickListener) {
			super(context, srcDrawable, barBtnItemStyle,
					normalBackgroundDrawable, pressedBackgroundDrawable,
					btnClickListener);
		}

		public LogoImgBarButtonItem(Context context, Drawable srcDrawable,
				OnClickListener btnClickListener) {
			super(context, srcDrawable, btnClickListener);
		}

		public LogoImgBarButtonItem(Context context, int srcId,
				int normalBackgroundResId, int pressedBackgroundResId,
				OnClickListener btnClickListener) {
			super(context, srcId, normalBackgroundResId,
					pressedBackgroundResId, btnClickListener);
		}

		public LogoImgBarButtonItem(Context context, int srcId,
				OnClickListener btnClickListener) {
			super(context, srcId, btnClickListener);
		}

		public LogoImgBarButtonItem(Context context, int resId) {
			super(context, resId);
		}

		public LogoImgBarButtonItem(Context context) {
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
