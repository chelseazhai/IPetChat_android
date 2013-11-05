package com.segotech.ipetchat.account.user;

import android.util.Log;

import com.richitec.commontoolkit.user.UserBean;
import com.segotech.ipetchat.account.pet.PetBean;

public class IPCUserExtension {

	private static final String LOG_TAG = IPCUserExtension.class
			.getCanonicalName();

	// set pet chat user pet info
	public static void setUserNickname(UserBean user, PetBean petInfo) {
		// check user bean
		if (null != user) {
			// set pet chat user extension attribute
			user.getExtension().put(IPCUserExtAttributes.PET_INFO.name(),
					petInfo);
		} else {
			Log.e(LOG_TAG,
					"Set pet chat user extension attribute error, user = "
							+ user);
		}
	}

	// get pet chat user pet info
	public static PetBean getUserNickname(UserBean user) {
		PetBean _ret = null;

		// check user bean
		if (null != user) {
			_ret = (PetBean) user.getExtension().get(
					IPCUserExtAttributes.PET_INFO.name());
		} else {
			Log.e(LOG_TAG,
					"Get pet chat user extension attribute error, user = "
							+ user);
		}

		return _ret;
	}

	// inner class
	// common user attributes
	public static enum ComUserAttributes {
		NAME, PASSWORD, USER_KEY
	}

	// pet chat user extension attributes
	public static enum IPCUserExtAttributes {
		PET_INFO
	}

	// common user local storage attributes
	public static enum ComUserLocalStorageAttributes {
		LOGIN_USERNAME, LOGIN_USERKEY, AUTO_LOGIN
	}

}
