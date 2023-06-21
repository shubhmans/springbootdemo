package com.envision.demo.enums;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {
	ADMIN(Names.admin),
	EDITOR(Names.editor),
	CUSTOMER(Names.customer);
	
	public class Names {
		public static final String admin = "admin";
		public static final String editor = "editor";
		public static final String customer = "customer";
	}
	
	private final String value;
	
	private RoleType(String value) {
		this.value = value;
	}
	
	public static RoleType getRoleFromStr(String name) {
		for(RoleType roleType: RoleType.values()) {
			if (roleType.value.equals(name)) {
				return roleType;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.value;
	}

	@Override
	public String getAuthority() {
		return this.value;
	}
}
