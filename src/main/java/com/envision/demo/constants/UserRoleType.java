package com.envision.demo.constants;

public final class UserRoleType {
	
	private UserRoleType() {
		throw new IllegalStateException("UserRoleType");
	}
	
	public static final String ADMIN = "ROLE_ADMIN";
	public static final String EDITOR = "ROLE_EDITOR";
	public static final String CUSTOMER = "ROLE_CUSTOMER";
}
