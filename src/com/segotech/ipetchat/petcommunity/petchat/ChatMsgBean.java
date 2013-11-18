package com.segotech.ipetchat.petcommunity.petchat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.JSONUtils;

public class ChatMsgBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8486167030897721754L;

	private static final String LOG_TAG = ChatMsgBean.class.getSimpleName();

	// id
	private Long id;
	// leaver name
	private String name;
	// leaver avatar yrl
	private String avatarUrl;
	// leaver avatar
	private byte[] avatar;
	// leave date timestamp
	private Long timestamp;
	// content
	private String content;
	// type
	private ChatMsgType type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@SuppressLint("SimpleDateFormat")
	public String getDate() {
		return new SimpleDateFormat("yy-MM-dd HH:mm").format(new Date(
				timestamp * 1000));
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ChatMsgType getType() {
		return type;
	}

	public void setType(ChatMsgType type) {
		this.type = type;
	}

	public ChatMsgBean() {
		// nothing to do
	}

	public ChatMsgBean(JSONObject msgJSONInfo) {
		// set pet info bean attributes
		// check message json info
		if (null != msgJSONInfo) {
			// set message info bean attributes
			// id
			// get and check id value
			Long _idValue = JSONUtils.getLongFromJSONObject(msgJSONInfo,
					"msgid");
			if (null != _idValue) {
				id = _idValue;
			}

			// avatar url
			avatarUrl = JSONUtils.getStringFromJSONObject(msgJSONInfo,
					"leaver_avatar");

			// name
			name = JSONUtils.getStringFromJSONObject(msgJSONInfo,
					"leaver_nickname");

			// get and check timestamp value
			Long _timestampValue = JSONUtils.getLongFromJSONObject(msgJSONInfo,
					"leave_timestamp");
			if (null != _timestampValue) {
				timestamp = _timestampValue;
			}

			// content
			content = JSONUtils.getStringFromJSONObject(msgJSONInfo, "content");

			// type
			if (UserManager
					.getInstance()
					.getUser()
					.getName()
					.equalsIgnoreCase(
							JSONUtils.getStringFromJSONObject(msgJSONInfo,
									"author"))) {
				type = ChatMsgType.TO_MSG;
			} else {
				type = ChatMsgType.INCOMING_MSG;
			}
		} else {
			Log.e(LOG_TAG, "generate message info error, message json info = "
					+ msgJSONInfo);
		}
	}

	@Override
	public String toString() {
		//

		return super.toString();
	}

	// chat message type
	public static enum ChatMsgType {
		INCOMING_MSG, TO_MSG
	}

}
