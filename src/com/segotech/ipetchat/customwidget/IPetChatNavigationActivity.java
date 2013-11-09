package com.segotech.ipetchat.customwidget;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.richitec.commontoolkit.activityextension.NavigationActivity;
import com.richitec.commontoolkit.customcomponent.BarButtonItem.BarButtonItemStyle;
import com.richitec.commontoolkit.customcomponent.ImageBarButtonItem;
import com.segotech.ipetchat.R;

public class IPetChatNavigationActivity extends NavigationActivity {

	// navigation activity pop result extra data key
	protected final String POP_RET_EXTRADATA_KEY = "pop_result_extradata_key";

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);

		// set back navigation image bar button item as left image bar button
		// item
		setLeftBarButtonItem(new BackNavImgBarButtonItem(this,
				R.drawable.img_back_nav_item, _mBackBarBtnItemOnClickListener));
	}

	// inner class
	// back navigation image bar button item
	class BackNavImgBarButtonItem extends ImageBarButtonItem {

		public BackNavImgBarButtonItem(Context context, int srcId,
				OnClickListener btnClickListener) {
			super(
					context,
					context.getResources().getDrawable(srcId),
					BarButtonItemStyle.RIGHT_GO,
					null,
					context.getResources()
							.getDrawable(
									com.richitec.commontoolkit.R.drawable.img_rightbarbtnitem_touchdown_bg),
					btnClickListener);
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
