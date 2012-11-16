package com.siberhus.mailberry.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

public class StringArrayUtils {
	
	public static boolean isEmpty(String[] array){
		if (ObjectUtils.isEmpty(array)) {
			return true;
		}
		for(String elem: array){
			if(StringUtils.isNotEmpty(elem)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean isBlank(String[] array){
		if (ObjectUtils.isEmpty(array)) {
			return true;
		}
		for(String elem: array){
			if(StringUtils.isNotBlank(elem)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param array the original String array
	 * @return the resulting array (of the same size) with trimmed elements.
	 * if array is empty (length=0 or null), return null
	 */
	public static String[] trimToNull(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return null;
		}
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			result[i] = (element != null ? StringUtils.trimToNull(element) : null);
		}
		return result;
	}
	
	/**
	 * 
	 * @param array the original String array
	 * @return the resulting array (of the same size) with trimmed elements.
	 * if array is empty (length=0 or null), return null
	 */
	public static String[] trim(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return null;
		}
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			result[i] = (element != null ? element.trim() : null);
		}
		return result;
	}
}
