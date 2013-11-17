package com.segotech.ipetchat.petcommunity.petchat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.richitec.commontoolkit.customadapter.CTListAdapter;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class Leave6ReplyMsgActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = Leave6ReplyMsgActivity.class
			.getCanonicalName();

	// leave message key
	public static final String LEAVEMSG_PETID_KEY = "leave_msg_petid_key";
	public static final String LEAVEMSG_MESSAGE_KEY = "leave_msg_message_key";

	// id of pet left msg for
	private Long _petId;

	// message listView
	private ListView _mMsgListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.leave6reply_msg_activity_layout);

		// set title
		setTitle(R.string.leave_or_reply_msg_nav_title);

		// get the intent extra data
		final Bundle _data = getIntent().getExtras();

		// check the data bundle
		if (null != _data) {
			// get id of pet left msg for
			_petId = _data.getLong(LEAVEMSG_PETID_KEY);
		}

		// get message listView
		_mMsgListView = (ListView) findViewById(R.id.msgs_listView);

		// set message adapter
		_mMsgListView.setAdapter(new MessageAdapter(this,
				new ArrayList<Map<String, ?>>(), R.layout.self_msg_layout,
				new String[] { MessageAdapter.MESSAGE_ITEM_SENDTIME,
						MessageAdapter.MESSAGE_ITEM_LEAVER_AVATAR,
						MessageAdapter.MESSAGE_ITEM_LEAVER_NICKNAME,
						MessageAdapter.MESSAGE_ITEM_CONTENT }, new int[] {
						R.id.tv_sendtime, R.id.iv_userhead, R.id.tv_username,
						R.id.tv_chatcontent }));

		// set messages listView on scroll listener
		_mMsgListView.setOnScrollListener(null);

		// set message send button on click listener
		((Button) findViewById(R.id.msg_send_button))
				.setOnClickListener(new MSGSendBtnOnClickListener());
	}

	// inner class
	// message adapter
	class MessageAdapter extends CTListAdapter {

		// message adapter data keys
		public static final String MESSAGE_ITEM_SENDTIME = "message_sendtime";
		public static final String MESSAGE_ITEM_LEAVER_AVATAR = "message_leaver_avatar";
		public static final String MESSAGE_ITEM_LEAVER_NICKNAME = "message_leaver_nickname";
		public static final String MESSAGE_ITEM_CONTENT = "message_content";
		public static final String MESSAGE_ITEM_TYPE = "message_type";

		public MessageAdapter(Context context, List<Map<String, ?>> data,
				int itemsLayoutResId, String[] dataKeys,
				int[] itemsComponentResIds) {
			super(context, data, itemsLayoutResId, dataKeys,
					itemsComponentResIds);
		}

		@Override
		protected void bindView(View view, Map<String, ?> dataMap,
				String dataKey) {
			// get item data object
			Object _itemData = dataMap.get(dataKey);

			// check view type
			if (view instanceof TextView) {
				// set view text
				((TextView) view)
						.setText(null == _itemData ? ""
								: _itemData instanceof SpannableString ? (SpannableString) _itemData
										: _itemData.toString());
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

	// messages listView on scroll listener
	class MessagesListViewOnScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// nothing to do
		}

	}

	// message send button on click listener
	class MSGSendBtnOnClickListener implements OnClickListener {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onClick(View v) {
			// get message content
			String _content = ((EditText) findViewById(R.id.msg_editText))
					.getText().toString();

			// check message content
			if (null == _content || "".equalsIgnoreCase(_content)) {
				Toast.makeText(Leave6ReplyMsgActivity.this, "请输入留言",
						Toast.LENGTH_SHORT).show();

				return;
			} else {
				((EditText) findViewById(R.id.msg_editText)).setText("");
			}

			// generate new message
			Map<String, String> _messageMap = new HashMap<String, String>();
			_messageMap.put(MessageAdapter.MESSAGE_ITEM_SENDTIME,
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()));
			_messageMap.put(MessageAdapter.MESSAGE_ITEM_LEAVER_NICKNAME, "我");
			_messageMap.put(MessageAdapter.MESSAGE_ITEM_CONTENT, _content);

			// append data
			((MessageAdapter) _mMsgListView.getAdapter())
					.appendData(_messageMap);

			// leave message
			// generate leave message post
			// request param
			Map<String, String> _leaveMSGParam = new HashMap<String, String>();
			_leaveMSGParam.put(
					getResources().getString(
							R.string.rbgServer_setPetInfoReqParam_petId),
					_petId.toString());
			_leaveMSGParam.put("content", _content);

			// send leave message post http
			// request
			HttpUtils.postSignatureRequest(
					getResources().getString(R.string.server_url)
							+ getResources().getString(R.string.leaveMsg_url),
					PostRequestFormat.URLENCODED, _leaveMSGParam, null,
					HttpRequestType.ASYNCHRONOUS,
					new LeaveMSGHttpRequestListener());
		}

	}

	// leave message http request listener
	class LeaveMSGHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			Log.d(LOG_TAG, "_respJsonData = " + _respJsonData);

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					//
					break;

				default:
					//
					break;
				}
			} else {
				//
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"leave message for pet failed, send leave message for pet post request failed");

			// show get user all pets info failed toast
			Toast.makeText(Leave6ReplyMsgActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();

		}

	}

}
