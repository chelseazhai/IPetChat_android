package com.segotech.ipetchat.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.richitec.commontoolkit.user.UserBean;
import com.richitec.commontoolkit.user.UserManager;
import com.richitec.commontoolkit.utils.StringUtils;
import com.segotech.ipetchat.account.user.IPCUserExtension;

public class DeviceServerHttpReqParamUtils {

	// generate pet device server operate param with time and operate
	public static Map<String, String> generatePetDeviceOperateParam(Long time,
			String operate) {
		Map<String, String> _param = new HashMap<String, String>();

		// get login user
		UserBean _loginUser = UserManager.getInstance().getUser();

		// set param
		_param.put("d", IPCUserExtension.getUserPetBindDeviceId(_loginUser)
				.toString());
		_param.put("t", time.toString());
		_param.put("c", "MP");
		_param.put("v", "1.0");
		_param.put("op", operate);

		// get param string list
		ArrayList<String> _paramStringList = new ArrayList<String>();
		for (String _paramKey : _param.keySet()) {
			_paramStringList.add(new StringBuilder(_paramKey).append(
					_param.get(_paramKey)).toString());
		}
		// sorted
		Collections.sort(_paramStringList);

		// get param string
		StringBuilder _paramString = new StringBuilder();
		// append key
		_paramString.append(StringUtils.md5(IPCUserExtension
				.getUserPetBindDeviceAccessPwd(_loginUser)));
		for (String string : _paramStringList) {
			_paramString.append(string);
		}
		// append key again
		_paramString.append(StringUtils.md5(IPCUserExtension
				.getUserPetBindDeviceAccessPwd(_loginUser)));

		// append signature
		_param.put("s", _paramString.toString());

		return _param;
	}

}
