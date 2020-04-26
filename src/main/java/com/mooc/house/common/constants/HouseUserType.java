package com.mooc.house.common.constants;

public enum HouseUserType {
	SALE(1),BOOKNARK(2);
	
	public final Integer value;
	
	private HouseUserType(Integer value) {
		this.value = value;
	}
}
