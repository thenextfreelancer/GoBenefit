package com.gobenefit.entity;

import java.io.Serializable;

public interface Entity extends Serializable {

	default public Serializable getId() {
		return null;
	}

}
