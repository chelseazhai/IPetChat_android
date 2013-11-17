package com.segotech.ipetchat.petcommunity.petchat;

import java.io.Serializable;

import android.util.Log;

public class ChatMsgEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8486167030897721754L;

	private static final String LOG_TAG = ChatMsgEntity.class.getSimpleName();

	// id
	private Long id;
	// name
	private String name;
	// date
	private String date;
	// content
	private String content;
	// is coming msg flag
	private boolean isComMsg = true;

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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean getMsgType() {
		return isComMsg;
	}

	public void setMsgType(boolean isComMsg) {
		this.isComMsg = isComMsg;
	}

	public ChatMsgEntity() {
		// nothing to do
	}

	public ChatMsgEntity(String name, String date, String content,
			boolean isComMsg) {
		this.name = name;
		this.date = date;
		this.content = content;
		this.isComMsg = isComMsg;
	}

	@Override
	public String toString() {
		Log.d(LOG_TAG, "msg");

		return super.toString();
	}

}
