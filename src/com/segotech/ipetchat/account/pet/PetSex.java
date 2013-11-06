package com.segotech.ipetchat.account.pet;

public enum PetSex {

	// male and female
	MALE(0), FEMALE(1);

	// sex value
	private Integer sexValue;

	private PetSex(Integer sexValue) {
		this.sexValue = sexValue;
	}

	public static PetSex getSex(Integer sexValue) {
		PetSex _ret = MALE;

		// check sex value
		if (FEMALE.sexValue == sexValue) {
			_ret = FEMALE;
		}

		return _ret;
	}

}
