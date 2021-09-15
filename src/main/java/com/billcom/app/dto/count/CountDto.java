package com.billcom.app.dto.count;

public class CountDto {
	private String name;
	private float value = 0;

	public CountDto(String name, float value) {
		this.name = name;
		this.value = value;
	}

	public CountDto() {
		this.value = 0;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CountDto [name=" + name + ", value=" + value + "]";
	}



}