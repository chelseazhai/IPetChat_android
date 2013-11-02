package com.segotech.ipetchat.account.pet;

import java.io.Serializable;

import android.content.res.Resources;

import com.richitec.commontoolkit.CTApplication;
import com.segotech.ipetchat.R;

public class PetBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3288951473132588097L;

	// pet nickname
	private String nickname;
	// pet sex
	private PetSex sex;
	// pet breed
	private String breed;
	// pet age
	private Integer age;
	// pet height
	private Float height;
	// pet weight
	private Float weight;
	// pet district
	private String district;
	// pet place where used to go
	private String placeUsed2Go;

	// PetBean constructor
	public PetBean() {
		super();

		// init sex
		sex = PetSex.MALE;
	}

	public PetBean(String nickname, PetSex sex, String breed, Integer age,
			Float height, Float weight, String district, String placeUsed2Go) {
		// set nickname, sex, breed, age, height, weight, district and place
		// where used to go
		this.nickname = nickname;
		this.sex = sex;
		this.breed = breed;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.district = district;
		this.placeUsed2Go = placeUsed2Go;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public PetSex getSex() {
		return sex;
	}

	public void setSex(PetSex sex) {
		this.sex = sex;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPlaceUsed2Go() {
		return placeUsed2Go;
	}

	public void setPlaceUsed2Go(String placeUsed2Go) {
		this.placeUsed2Go = placeUsed2Go;
	}

	@Override
	public String toString() {
		// init pet description
		StringBuilder _petDescription = new StringBuilder();

		// get application context resource
		Resources _contextRes = CTApplication.getContext().getResources();

		// append pet nickname, sex, breed, age, height, weight, district and
		// place where used to go
		_petDescription.append("pet nickname: ").append(nickname).append(", ");
		_petDescription.append("sex: ").append(sex.name()).append(", ");
		_petDescription.append("breed: ").append(breed).append(", ");
		_petDescription
				.append("age: ")
				.append(String.format(
						_contextRes.getString(R.string.pet_age_value_format),
						age)).append(", ");
		_petDescription
				.append("height: ")
				.append(String.format(
						_contextRes.getString(R.string.pet_height_value_format),
						height)).append(", ");
		_petDescription
				.append("weight: ")
				.append(String.format(
						_contextRes.getString(R.string.pet_weight_value_format),
						weight)).append(", ");
		_petDescription.append("district: ").append(district).append(" and ");
		_petDescription.append("place where used to go: ").append(placeUsed2Go)
				.append("\n");

		return _petDescription.toString();
	}

	// inner class
	// pet sex
	public static enum PetSex {
		MALE, FEMALE
	}

}
