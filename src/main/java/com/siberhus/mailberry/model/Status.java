package com.siberhus.mailberry.model;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

public class Status {

	public static final String ACTIVE = "ACT";
	public static final String INACTIVE = "INA";
	private static final String VALUES[] = new String[] { ACTIVE, INACTIVE };
	
	public static void validate(String status) {
		if (!ArrayUtils.contains(Status.VALUES, status)) {
			throw new IllegalArgumentException("status must be any of "
					+ Arrays.toString(Status.VALUES));
		}
	}
}
