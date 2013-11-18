package com.segotech.ipetchat.petcommunity.petchat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.petcommunity.petchat.ChatMsgBean.ChatMsgType;

public class Leave6ReplyMsgActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = Leave6ReplyMsgActivity.class
			.getCanonicalName();

	// leave message key
	public static final String LEAVEMSG_PETID_KEY = "leave_msg_petid_key";
	public static final String LEAVEMSG_MESSAGEID_KEY = "leave_msg_messageid_key";

	// id of pet left msg for
	private Long _mPetId;

	// message id
	private Long _mMsgId;

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
			_mPetId = _data.getLong(LEAVEMSG_PETID_KEY);

			// get and check message id
			_mMsgId = _data.getLong(LEAVEMSG_MESSAGEID_KEY, -101);
			if (-101 == _mMsgId) {
				_mMsgId = null;
			}
		}

		// get message listView
		_mMsgListView = (ListView) findViewById(R.id.msgs_listView);

		// set message adapter
		_mMsgListView.setAdapter(new MessageAdapter(this,
				new ArrayList<Map<String, ?>>(),
				R.layout.messages_listview_item_layout, new String[] {
						MessageAdapter.MESSAGE_ITEM_SENDTIME,
						MessageAdapter.MESSAGE_ITEM_LEAVER_AVATAR,
						MessageAdapter.MESSAGE_ITEM_LEAVER_NICKNAME,
						MessageAdapter.MESSAGE_ITEM_CONTENT,
						MessageAdapter.MESSAGE_ITEM_TYPE }, new int[] {
						R.id.tv_sendtime, R.id.iv_userhead, R.id.tv_username,
						R.id.tv_chatcontent }));

		// set messages listView on scroll listener
		_mMsgListView.setOnScrollListener(null);

		// set message send button on click listener
		((Button) findViewById(R.id.msg_send_button))
				.setOnClickListener(new MSGSendBtnOnClickListener());

		// check message id
		if (null != _mMsgId) {
			// get message info
			// generate get message detail info post
			// request param
			Map<String, String> _getMsgDetailInfoParam = new HashMap<String, String>();
			_getMsgDetailInfoParam.put("msgid", _mMsgId.toString());

			// send get message detail info post http
			// request
			HttpUtils.postSignatureRequest(
					getResources().getString(R.string.server_url)
							+ getResources()
									.getString(R.string.getLeaveMsg_url),
					PostRequestFormat.URLENCODED, _getMsgDetailInfoParam, null,
					HttpRequestType.ASYNCHRONOUS,
					new GetMsgDetailInfoHttpRequestListener());
		}
	}

	// generate message info map
	@SuppressLint("SimpleDateFormat")
	private Map<String, Object> generateMsgInfoMap(ChatMsgBean message) {
		Map<String, Object> _ret = new HashMap<String, Object>();

		// set message leaver nickname, avatar, content and send date
		_ret.put(
				MessageAdapter.MESSAGE_ITEM_LEAVER_NICKNAME,
				message.getName().equalsIgnoreCase(
						IPCUserExtension.getUserPetInfo(
								UserManager.getInstance().getUser())
								.getNickname()) ? "我" : message.getName());
		_ret.put(MessageAdapter.MESSAGE_ITEM_SENDTIME, new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(new Date(
				message.getTimestamp() * 1000)));
		_ret.put(MessageAdapter.MESSAGE_ITEM_CONTENT, message.getContent());
		_ret.put(MessageAdapter.MESSAGE_ITEM_TYPE, message.getType());

		return _ret;
	}

	// process leave message, get message detail info or reply message exception
	private void processException() {
		// show login failed toast
		Toast.makeText(Leave6ReplyMsgActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// define view holer object
			ViewHolder _viewHolder;

			// check convert view
			if (null == convertView) {
				convertView = _mLayoutInflater
						.inflate(_mItemsLayoutResId, null);

				// define convent view present viewGroup
				ViewGroup _presentViewGroup = null;

				// check message type and init convent view present viewGroup
				switch ((ChatMsgType) _mData.get(position).get(
						_mDataKeys[_mItemsComponentResIds.length])) {
				case INCOMING_MSG:
					_presentViewGroup = (ViewGroup) convertView
							.findViewById(R.id.incoming_message);
					break;

				case TO_MSG:
				default:
					_presentViewGroup = (ViewGroup) convertView
							.findViewById(R.id.self_message);
					break;
				}

				// show the present viewGroup
				_presentViewGroup.setVisibility(View.VISIBLE);

				// init view holder and set its views for holding
				_viewHolder = new ViewHolder();
				// set item component view subViews
				for (int i = 0; i < _mItemsComponentResIds.length; i++) {
					_viewHolder.views4Holding.append(_mItemsComponentResIds[i],
							_presentViewGroup
									.findViewById(_mItemsComponentResIds[i]));
				}

				// set tag
				convertView.setTag(_viewHolder);
			} else {
				// get view holder
				_viewHolder = (ViewHolder) convertView.getTag();
			}

			// set item component view subViews
			for (int i = 0; i < _mItemsComponentResIds.length; i++) {
				// bind item component view data
				// bindView(convertView.findViewById(_mItemsComponentResIds[i]),
				// _mData.get(position), _mDataKeys[i]);
				bindView(
						_viewHolder.views4Holding
								.get(_mItemsComponentResIds[i]),
						_mData.get(position), _mDataKeys[i]);
			}

			return convertView;
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

		// inner class
		class ViewHolder {
			// views for holding
			SparseArray<View> views4Holding;

			public ViewHolder() {
				super();

				// init views sparse array for holding
				views4Holding = new SparseArray<View>();
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

			// generate new message info
			ChatMsgBean _leaveMsg = new ChatMsgBean();

			// set its attributes
			_leaveMsg.setName("我");
			_leaveMsg.setContent(_content);
			_leaveMsg.setTimestamp(System.currentTimeMillis() / 1000);
			_leaveMsg.setType(ChatMsgType.TO_MSG);

			// append data
			((MessageAdapter) _mMsgListView.getAdapter())
					.appendData(generateMsgInfoMap(_leaveMsg));

			// leave or reply message
			// define post url suffix
			String _urlSuffix = null;

			// define leave or reply message post http request listener
			OnHttpRequestListener _leave6ReplyMsgHttpRequestListener = null;

			// generate leave or reply message post
			// request param
			Map<String, String> _leave6ReplyMSGParam = new HashMap<String, String>();
			_leave6ReplyMSGParam.put("content", _content);
			if (null == _mMsgId) {
				_urlSuffix = getResources().getString(R.string.leaveMsg_url);

				_leave6ReplyMSGParam.put(
						getResources().getString(
								R.string.rbgServer_setPetInfoReqParam_petId),
						_mPetId.toString());

				_leave6ReplyMsgHttpRequestListener = new LeaveMSGHttpRequestListener();
			} else {
				_urlSuffix = getResources().getString(R.string.replyMsg_url);

				_leave6ReplyMSGParam.put("msgid", _mMsgId.toString());

				_leave6ReplyMsgHttpRequestListener = new ReplyMSGHttpRequestListener();
			}

			// send leave or reply message post http
			// request
			HttpUtils.postSignatureRequest(
					getResources().getString(R.string.server_url) + _urlSuffix,
					PostRequestFormat.URLENCODED, _leave6ReplyMSGParam, null,
					HttpRequestType.ASYNCHRONOUS,
					_leave6ReplyMsgHttpRequestListener);
		}

	}

	// get message detail info http request listener
	class GetMsgDetailInfoHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get messages info array
					JSONArray _msgsInfoArray = JSONUtils
							.getJSONArrayFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_getAllPetsReqResp_petsList));

					// define got message detail info data list
					List<Map<String, ?>> _msgDetailInfosDataList = new ArrayList<Map<String, ?>>();

					for (int i = 0; i < _msgsInfoArray.length(); i++) {
						// get message detail info and add to message list
						ChatMsgBean _msgDetailInfo = new ChatMsgBean(
								JSONUtils.getJSONObjectFromJSONArray(
										_msgsInfoArray, i));

						// add message detail info item map to list
						_msgDetailInfosDataList
								.add(generateMsgInfoMap(_msgDetailInfo));
					}

					// append data
					((MessageAdapter) _mMsgListView.getAdapter())
							.appendData(_msgDetailInfosDataList);
					break;

				case 2:
					Log.e(LOG_TAG, "get message detail info failed, pet error");

					// show get user all pets info failed toast
					Toast.makeText(Leave6ReplyMsgActivity.this, "获取留言信息失败",
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"get message detail info failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get message detail info failed, bg_server return result is null");

				processException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get message detail info failed, send get message detail info post request failed");

			// show get user all pets info failed toast
			Toast.makeText(Leave6ReplyMsgActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

	// leave message http request listener
	class LeaveMSGHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					Log.d(LOG_TAG, "leave message successful");

					// get leave message id
					//
					break;

				case 1:
				case 2:
				case 3:
					Log.e(LOG_TAG, "leave message failed");

					// show get user all pets info failed toast
					Toast.makeText(Leave6ReplyMsgActivity.this, "留言失败",
							Toast.LENGTH_LONG).show();

					// pop leave or reply message activity
					popActivity();
					break;

				default:
					Log.e(LOG_TAG,
							"leave message failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"leave message failed, bg_server return result is null");

				processException();
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

	// reply message http request listener
	class ReplyMSGHttpRequestListener extends OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					Log.d(LOG_TAG, "reply message successful");
					break;

				case 1:
				case 2:
				case 3:
					Log.e(LOG_TAG, "reply message failed");

					// show get user all pets info failed toast
					Toast.makeText(Leave6ReplyMsgActivity.this, "回复留言失败",
							Toast.LENGTH_LONG).show();

					// pop leave or reply message activity
					popActivity();
					break;

				default:
					Log.e(LOG_TAG,
							"reply message failed, bg_server return result is unrecognized");

					processException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"reply message failed, bg_server return result is null");

				processException();
			}
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"reply message for pet failed, send reply message post request failed");

			// show get user all pets info failed toast
			Toast.makeText(Leave6ReplyMsgActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

}
