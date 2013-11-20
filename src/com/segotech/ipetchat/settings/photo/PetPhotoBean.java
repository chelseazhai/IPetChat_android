package com.segotech.ipetchat.settings.photo;

import java.io.Serializable;

import org.json.JSONObject;

import android.util.Log;

import com.richitec.commontoolkit.utils.JSONUtils;

public class PetPhotoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5295936565255686206L;

	private static final String LOG_TAG = PetPhotoBean.class.getCanonicalName();

	// id
	private Long id;
	// name
	private String name;
	// type
	private String type;
	// path
	private String path;
	// description
	private String description;
	// create date timestamp
	private Long timestamp;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public PetPhotoBean() {
		// nothing to do
	}

	public PetPhotoBean(JSONObject petPhotoJSONInfo) {
		// set pet photo bean attributes
		// check photo json info
		if (null != petPhotoJSONInfo) {
			// set pet photo bean attributes
			// id
			// get and check id value
			Long _idValue = JSONUtils.getLongFromJSONObject(petPhotoJSONInfo,
					"id");
			if (null != _idValue) {
				id = _idValue;
			}

			// name
			name = JSONUtils.getStringFromJSONObject(petPhotoJSONInfo, "name");

			// type
			type = JSONUtils.getStringFromJSONObject(petPhotoJSONInfo, "type");

			// name
			name = JSONUtils.getStringFromJSONObject(petPhotoJSONInfo, "name");

			// path
			path = JSONUtils.getStringFromJSONObject(petPhotoJSONInfo, "path");

			// description
			description = JSONUtils.getStringFromJSONObject(petPhotoJSONInfo,
					"description");

			// get and check timestamp value
			Long _timestampValue = JSONUtils.getLongFromJSONObject(
					petPhotoJSONInfo, "createtime");
			if (null != _timestampValue) {
				timestamp = _timestampValue;
			}
		} else {
			Log.e(LOG_TAG, "generate pet photo info error, photo json info = "
					+ petPhotoJSONInfo);
		}
	}

	@Override
	public String toString() {
		//

		return super.toString();
	}

}
