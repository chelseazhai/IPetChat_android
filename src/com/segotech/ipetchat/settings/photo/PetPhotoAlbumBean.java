package com.segotech.ipetchat.settings.photo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.richitec.commontoolkit.utils.JSONUtils;

public class PetPhotoAlbumBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7087923440074685199L;

	private static final String LOG_TAG = PetPhotoAlbumBean.class
			.getCanonicalName();

	// id
	private Long id;
	// title
	private String title;
	// cover url
	private String coverUrl;
	// cover image
	private byte[] coverImg;
	// create date timestamp
	private Long timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public byte[] getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(byte[] coverImg) {
		this.coverImg = coverImg;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@SuppressLint("SimpleDateFormat")
	public String getDate() {
		return new SimpleDateFormat("MM月dd").format(new Date(timestamp * 1000));
	}

	public PetPhotoAlbumBean() {
		// nothing to do
	}

	public PetPhotoAlbumBean(JSONObject petPhotoAlbumJSONInfo) {
		// set pet photo album bean attributes
		// check photo album json info
		if (null != petPhotoAlbumJSONInfo) {
			// set pet photo album bean attributes
			// id
			// get and check id value
			Long _idValue = JSONUtils.getLongFromJSONObject(
					petPhotoAlbumJSONInfo, "id");
			if (null != _idValue) {
				id = _idValue;
			}

			// title
			title = JSONUtils.getStringFromJSONObject(petPhotoAlbumJSONInfo,
					"title");

			// cover url
			coverUrl = JSONUtils.getStringFromJSONObject(petPhotoAlbumJSONInfo,
					"cover_url");

			// get and check timestamp value
			Long _timestampValue = JSONUtils.getLongFromJSONObject(
					petPhotoAlbumJSONInfo, "createtime");
			if (null != _timestampValue) {
				timestamp = _timestampValue;
			}
		} else {
			Log.e(LOG_TAG,
					"generate pet photo album info error, photo album json info = "
							+ petPhotoAlbumJSONInfo);
		}
	}

	@Override
	public String toString() {
		//

		return super.toString();
	}

}
