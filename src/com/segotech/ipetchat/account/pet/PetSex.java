package com.segotech.ipetchat.account.pet;

import com.richitec.commontoolkit.CTApplication;
import com.segotech.ipetchat.R;

public enum PetSex {

	// male and female
	MALE(0), FEMALE(1);

	// sex value and description
	private Integer sexValue;
	private String sex;

	private PetSex(Integer sexValue) {
		this.sexValue = sexValue;
		this.sex = CTApplication.getContext().getResources()
				.getStringArray(R.array.pet_sex_array)[sexValue];
	}

	public String getSex() {
		return sex;
	}

	public static PetSex getSex(Integer sexValue) {
		PetSex _ret = MALE;

		// check sex value
		if (FEMALE.sexValue == sexValue) {
			_ret = FEMALE;
		}

		return _ret;
	}

	@Override
	public String toString() {
		return sex;
	}

}
