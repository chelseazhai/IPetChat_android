package com.segotech.ipetchat.petcommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.HttpUtils;
import com.richitec.commontoolkit.utils.HttpUtils.HttpRequestType;
import com.richitec.commontoolkit.utils.HttpUtils.OnHttpRequestListener;
import com.richitec.commontoolkit.utils.HttpUtils.PostRequestFormat;
import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.account.pet.PetBean;
import com.segotech.ipetchat.account.user.IPCUserExtension;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;
import com.segotech.ipetchat.petcommunity.petchat.ChatMsgBean;
import com.segotech.ipetchat.petcommunity.petchat.Leave6ReplyMsgActivity;

public class MessageboxActivity extends IPetChatNavigationActivity {

	private static final String LOG_TAG = MessageboxActivity.class
			.getCanonicalName();

	// messagebox messages info list
	List<ChatMsgBean> _mMessageboxMsgsList = new ArrayList<ChatMsgBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.messagebox_activity_layout);

		// set title
		setTitle(R.string.message_box_nav_title);
	}

	@Override
	protected void onResume() {
		// get and check my pet info
		PetBean _myPetInfo = IPCUserExtension.getUserPetInfo(UserManager
				.getInstance().getUser());
		if (null != _myPetInfo
				&& !"".equalsIgnoreCase(_myPetInfo.getNickname())) {
			// get messagebox messages info
			// generateget messagebox messages info
			// request param
			Map<String, String> _getMessageboxMsgsParam = new HashMap<String, String>();
			_getMessageboxMsgsParam.put(
					getResources().getString(
							R.string.rbgServer_setPetInfoReqParam_petId),
					_myPetInfo.getId().toString());

			// send get messagebox messages info post http request
			HttpUtils.postSignatureRequest(
					getResources().getString(R.string.server_url)
							+ getResources().getString(
									R.string.getLeaveMsgs_url),
					PostRequestFormat.URLENCODED, _getMessageboxMsgsParam,
					null, HttpRequestType.ASYNCHRONOUS,
					new GetMessageboxMsgsInfoHttpRequestListener());
		}

		super.onResume();
	}

	// process get message box messages exception
	private void processGetMessageboxMSGException() {
		// show login failed toast
		Toast.makeText(MessageboxActivity.this,
				R.string.toast_request_exception, Toast.LENGTH_LONG).show();
	}

	// inner class
	// messagebox messages adapter
	class MessageboxMessagesAdapter extends PetCommunityItemListViewAdapter {

		// messagebox messages adapter data keys
		private static final String MESSAGEBOX_MESSAGES_ITEM_LEAVER_AVATAR = "messagebox_messages_item_leaver_avatar";
		private static final String MESSAGEBOX_MESSAGES_ITEM_LEAVER_NICKNAME = "messagebox_messages_item_leaver_nickname";
		private static final String MESSAGEBOX_MESSAGES_ITEM_MESSAGE_CONTENT = "messagebox_messages_item_message_content";
		private static final String MESSAGEBOX_MESSAGES_ITEM_MESSAGE_TIMESTAMP = "messagebox_messages_item_message_timestamp";

		public MessageboxMessagesAdapter(Context context,
				List<Map<String, ?>> data, int itemsLayoutResId,
				String[] dataKeys, int[] itemsComponentResIds) {
			super(context, data, itemsLayoutResId, dataKeys,
					itemsComponentResIds);
		}

	}

	// get messagebox messages info http request listener
	class GetMessageboxMsgsInfoHttpRequestListener extends
			OnHttpRequestListener {

		@Override
		public void onFinished(HttpRequest request, HttpResponse response) {
			// get http response entity string json data
			JSONObject _respJsonData = JSONUtils.toJSONObject(HttpUtils
					.getHttpResponseEntityString(response));

			// get http response entity string json object result
			String _result = JSONUtils
					.getStringFromJSONObject(_respJsonData, getResources()
							.getString(R.string.rbgServer_reqResp_result));

			// reset messagebox messages data list
			List<Map<String, ?>> _messageboxMsgsDataList = new ArrayList<Map<String, ?>>();
			_mMessageboxMsgsList = new ArrayList<ChatMsgBean>();

			// check an process result
			if (null != _result) {
				switch (Integer.parseInt(_result)) {
				case 0:
					// get messagebox messages info array
					JSONArray _messageboxMsgsInfoArray = JSONUtils
							.getJSONArrayFromJSONObject(
									_respJsonData,
									getResources()
											.getString(
													R.string.rbgServer_getAllPetsReqResp_petsList));

					for (int i = 0; i < _messageboxMsgsInfoArray.length(); i++) {
						// define message map
						Map<String, Object> _itemMap = new HashMap<String, Object>();

						// get message and add to message list
						ChatMsgBean _msgInfo = new ChatMsgBean(
								JSONUtils.getJSONObjectFromJSONArray(
										_messageboxMsgsInfoArray, i));
						_mMessageboxMsgsList.add(_msgInfo);

						// check leaver avatar
						if (null != _msgInfo.getAvatar()) {
							_itemMap.put(
									MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_LEAVER_AVATAR,
									BitmapFactory.decodeByteArray(
											_msgInfo.getAvatar(), 0,
											_msgInfo.getAvatar().length));
						}

						// check leaver nickname
						if (null != _msgInfo.getName()) {
							_itemMap.put(
									MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_LEAVER_NICKNAME,
									_msgInfo.getName());
						}

						// check message content
						if (null != _msgInfo.getContent()) {
							_itemMap.put(
									MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_MESSAGE_CONTENT,
									_msgInfo.getContent());
						}

						// check leave date
						if (null != _msgInfo.getTimestamp()) {
							_itemMap.put(
									MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_MESSAGE_TIMESTAMP,
									_msgInfo.getDate());
						}

						// add leaved message item map to list
						_messageboxMsgsDataList.add(_itemMap);
					}
					break;

				case 2:
					Log.e(LOG_TAG,
							"get message box messages info failed, pet error");

					// show get user all pets info failed toast
					Toast.makeText(MessageboxActivity.this, "获取留言信息失败",
							Toast.LENGTH_LONG).show();
					break;

				default:
					Log.e(LOG_TAG,
							"get message box messages info failed, bg_server return result is unrecognized");

					processGetMessageboxMSGException();
					break;
				}
			} else {
				Log.e(LOG_TAG,
						"get message box messages info failed, bg_server return result is null");

				processGetMessageboxMSGException();
			}

			// get messagebox messages listView
			ListView _messageboxMsgsListView = (ListView) findViewById(R.id.messagebox_message_listView);

			// set messagebox messages listView adapter
			_messageboxMsgsListView
					.setAdapter(new MessageboxMessagesAdapter(
							MessageboxActivity.this,
							_messageboxMsgsDataList,
							R.layout.messagebox_message_listview_item_layout,
							new String[] {
									MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_LEAVER_AVATAR,
									MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_LEAVER_NICKNAME,
									MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_MESSAGE_CONTENT,
									MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_MESSAGE_TIMESTAMP },
							new int[] { R.id.message_leaver_avatar_imageView,
									R.id.message_leaver_nickname_textView,
									R.id.leavedmessage_content_textView,
									R.id.message_leaved_timestamp_textView }));

			// set messagebox messages listView on item click listener
			_messageboxMsgsListView
					.setOnItemClickListener(new MessageboxMsgsListViewOnItemClickListener());

			// set messagebox messages listView on item long click listener
			_messageboxMsgsListView.setOnItemLongClickListener(null);
		}

		@Override
		public void onFailed(HttpRequest request, HttpResponse response) {
			Log.e(LOG_TAG,
					"get message box messages info failed, send get message box messages info post request failed");

			// show get user all pets info failed toast
			Toast.makeText(MessageboxActivity.this,
					R.string.toast_request_exception, Toast.LENGTH_LONG).show();
		}

	}

	// message box messages listView on item click listener
	class MessageboxMsgsListViewOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// define extra data
			Map<String, Object> _extraData = new HashMap<String, Object>();

			// set extra data
			_extraData.put(Leave6ReplyMsgActivity.LEAVEMSG_MESSAGEID_KEY,
					_mMessageboxMsgsList.get(arg2).getId());

			// go to leave or reply message activity
			pushActivity(Leave6ReplyMsgActivity.class, _extraData);
		}
	}

}
