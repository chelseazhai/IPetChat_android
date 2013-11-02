package com.segotech.ipetchat.customwidget;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.richitec.commontoolkit.activityextension.NavigationActivity;
import com.richitec.commontoolkit.customcomponent.ImageBarButtonItem;
import com.segotech.ipetchat.R;

public class IPetChatRootNavigationActivity extends NavigationActivity {

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

		public LogoImgBarButtonItem(Context context, int srcId,
				OnClickListener btnClickListener) {
			super(context, srcId, btnClickListener);
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
