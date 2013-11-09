package com.segotech.ipetchat.petcommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.richitec.commontoolkit.utils.JSONUtils;
import com.segotech.ipetchat.R;
import com.segotech.ipetchat.customwidget.IPetChatNavigationActivity;

public class MessageboxActivity extends IPetChatNavigationActivity {

	// test data
	// test by ares
	private final JSONArray leavedmessages_JSONArray = JSONUtils
			.toJSONArray("[]");
	private final int[] message_leavers_avatars = new int[] {};
	private final String[] message_leaved_timestamps = new String[] {};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set content view
		setContentView(R.layout.messagebox_activity_layout);

		// set title
		setTitle(R.string.message_box_nav_title);

		// define messagebox messages list
		List<Map<String, ?>> _messageboxMessagesList = new ArrayList<Map<String, ?>>();

		// set them
		for (int i = 0; i < leavedmessages_JSONArray.length(); i++) {
			// define leaved message item map
			Map<String, Object> _itemMap = new HashMap<String, Object>();

			// leaver avatar
			// test by ares
			_itemMap.put(
					MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_LEAVER_AVATAR,
					getResources().getDrawable(message_leavers_avatars[i]));

			// leaver nickname
			_itemMap.put(
					MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_LEAVER_NICKNAME,
					JSONUtils.getStringFromJSONObject(JSONUtils
							.getJSONObjectFromJSONArray(
									leavedmessages_JSONArray, i), "nickname"));

			// message content
			_itemMap.put(
					MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_MESSAGE_CONTENT,
					JSONUtils.getStringFromJSONObject(JSONUtils
							.getJSONObjectFromJSONArray(
									leavedmessages_JSONArray, i), "content"));

			// message timestamp
			// test by ares
			_itemMap.put(
					MessageboxMessagesAdapter.MESSAGEBOX_MESSAGES_ITEM_MESSAGE_TIMESTAMP,
					message_leaved_timestamps[i]);

			// add leaved message item map to list
			_messageboxMessagesList.add(_itemMap);
		}

		// set my concern pets listView adapter
		((ListView) findViewById(R.id.messagebox_message_listView))
				.setAdapter(new MessageboxMessagesAdapter(
						this,
						_messageboxMessagesList,
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

}
