package com.segotech.ipetchat.account.user;

import android.util.Log;

import com.richitec.commontoolkit.user.UserBean;
import com.segotech.ipetchat.account.pet.PetBean;

public class IPCUserExtension {

	private static final String LOG_TAG = IPCUserExtension.class
			.getCanonicalName();

	// set pet chat user pet info
	public static void setUserPetInfo(UserBean user, PetBean petInfo) {
		// check user bean
		if (null != user) {
			// set pet chat user extension attribute pet info
			user.getExtension().put(IPCUserExtAttributes.PET_INFO.name(),
					petInfo);
		} else {
			Log.e(LOG_TAG,
					"Set pet chat user extension attribute pet info error, user = "
							+ user);
		}
	}

	// get pet chat user pet info
	public static PetBean getUserPetInfo(UserBean user) {
		PetBean _ret = null;

		// check user bean
		if (null != user) {
			_ret = (PetBean) user.getExtension().get(
					IPCUserExtAttributes.PET_INFO.name());
		} else {
			Log.e(LOG_TAG,
					"Get pet chat user extension attribute pet info error, user = "
							+ user);
		}

		return _ret;
	}

	// set pet chat user pet bind device id
	public static void setUserPetBindDeviceId(UserBean user,
			Long petBindDeviceId) {
		// check user bean
		if (null != user) {
			// set pet chat user extension attribute pet bind device id
			user.getExtension().put(
					IPCUserExtAttributes.PET_BINDDEVICEID.name(),
					petBindDeviceId);
		} else {
			Log.e(LOG_TAG,
					"Set pet chat user extension attribute pet bind device id error, user = "
							+ user);
		}
	}

	// get pet chat user pet bind device id
	public static Long getUserPetBindDeviceId(UserBean user) {
		Long _ret = null;

		// check user bean
		if (null != user) {
			_ret = (Long) user.getExtension().get(
					IPCUserExtAttributes.PET_BINDDEVICEID.name());
		} else {
			Log.e(LOG_TAG,
					"Get pet chat user extension attribute pet bind device id error, user = "
							+ user);
		}

		return _ret;
	}

	// set pet chat user pet bind device access password
	public static void setUserPetBindDeviceAcessPwd(UserBean user,
			String petBindDeviceAcessPwd) {
		// check user bean
		if (null != user) {
			// set pet chat user extension attribute pet bind device access
			// password
			user.getExtension().put(
					IPCUserExtAttributes.PET_BINDDEVICE_ACCESSPWD.name(),
					petBindDeviceAcessPwd);
		} else {
			Log.e(LOG_TAG,
					"Set pet chat user extension attribute pet bind device access password error, user = "
							+ user);
		}
	}

	// get pet chat user pet bind device id
	public static String getUserPetBindDeviceAccessPwd(UserBean user) {
		String _ret = null;

		// check user bean
		if (null != user) {
			_ret = (String) user.getExtension().get(
					IPCUserExtAttributes.PET_BINDDEVICE_ACCESSPWD.name());
		} else {
			Log.e(LOG_TAG,
					"Get pet chat user extension attribute pet bind device access password error, user = "
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
		PET_INFO, PET_BINDDEVICEID, PET_BINDDEVICE_ACCESSPWD
	}

	// common user local storage attributes
	public static enum ComUserLocalStorageAttributes {
		LOGIN_USERNAME, LOGIN_USERKEY, AUTO_LOGIN
	}

}
