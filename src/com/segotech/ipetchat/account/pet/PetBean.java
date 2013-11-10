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

	// pet id
	private Long id;
	// pet avatar location url
	private String avatarUrl;
	// pet avatar
	private byte[] avatar;
	// pet nickname
	private String nickname;
	// pet sex
	private PetSex sex;
	// pet breed
	private PetBreed breed;
	// pet age
	private Integer age;
	// pet height
	private Double height;
	// pet weight
	private Double weight;
	// pet district
	private String district;
	// pet place where used to go
	private String placeUsed2Go;

	// PetBean constructor
	public PetBean() {
		// nothing to do
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
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

	public PetBreed getBreed() {
		return breed;
	}

	public void setBreed(PetBreed breed) {
		this.breed = breed;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
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

		// append pet id, nickname, sex, breed, age, height, weight, district
		// and place where used to go
		_petDescription.append("pet id: ").append(id).append(", ");
		_petDescription.append("nickname: ").append(nickname).append(", ");
		_petDescription.append("sex: ")
				.append(null != sex ? sex.toString() : "").append(", ");
		_petDescription.append("breed: ")
				.append(null != breed ? breed.toString() : "").append(", ");
		_petDescription
				.append("age: ")
				.append(String.format(
						_contextRes.getString(R.string.pet_age_value_format),
						null != age ? age : 0)).append(", ");
		_petDescription
				.append("height: ")
				.append(String.format(
						_contextRes.getString(R.string.pet_height_value_format),
						null != height ? height : 0.0f)).append(", ");
		_petDescription
				.append("weight: ")
				.append(String.format(
						_contextRes.getString(R.string.pet_weight_value_format),
						null != weight ? weight : 0.0f)).append(", ");
		_petDescription.append("district: ").append(district).append(" and ");
		_petDescription.append("place where used to go: ").append(placeUsed2Go)
				.append("\n");

		return _petDescription.toString();
	}

}
