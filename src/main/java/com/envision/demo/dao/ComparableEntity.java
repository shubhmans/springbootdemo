package com.envision.demo.dao;

import java.io.Serializable;

public abstract class ComparableEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2459621570027476014L;

	public abstract Integer getId();
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		
		ComparableEntity arg = (ComparableEntity) obj;
		return arg.getId().equals(this.getId());
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

}
