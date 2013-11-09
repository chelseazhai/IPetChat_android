package com.segotech.ipetchat.account.pet;

import com.richitec.commontoolkit.CTApplication;
import com.segotech.ipetchat.R;

public enum PetBreed {

	// golden retriever, husky, teddy, samoyed, pomeranian, schnauzer, rough
	// collie, chow, pekingese and others
	GOLDEN_RETRIEVER(0), HUSKY(1), TEDDY(2), SAMOYED(3), POMERANIAN(4), SCHNAUZER(
			5), ROUGH_COLLIE(6), CHOW(7), PEKINGESE(8), OTHERS(9);

	// breed value and description
	private Integer breedValue;
	private String breed;

	private PetBreed(Integer breedValue) {
		this.breedValue = breedValue;
		this.breed = CTApplication.getContext().getResources()
				.getStringArray(R.array.pet_breed_array)[breedValue];
	}

	public String getBreed() {
		return breed;
	}

	public Integer getValue() {
		return breedValue;
	}

	public static PetBreed getBreed(Integer breedValue) {
		PetBreed _ret = OTHERS;

		// check breed value
		if (GOLDEN_RETRIEVER.breedValue == breedValue) {
			_ret = GOLDEN_RETRIEVER;
		} else if (HUSKY.breedValue == breedValue) {
			_ret = HUSKY;
		} else if (TEDDY.breedValue == breedValue) {
			_ret = TEDDY;
		} else if (SAMOYED.breedValue == breedValue) {
			_ret = SAMOYED;
		} else if (POMERANIAN.breedValue == breedValue) {
			_ret = POMERANIAN;
		} else if (SCHNAUZER.breedValue == breedValue) {
			_ret = SCHNAUZER;
		} else if (ROUGH_COLLIE.breedValue == breedValue) {
			_ret = ROUGH_COLLIE;
		} else if (CHOW.breedValue == breedValue) {
			_ret = CHOW;
		} else if (PEKINGESE.breedValue == breedValue) {
			_ret = PEKINGESE;
		}

		return _ret;
	}

	@Override
	public String toString() {
		return breed;
	}

}
