package com.ku6.cdn.dispatcher.common.util;

public class TypeUtil {
	
	public static long castToLong(Object o) {
		Number n = (Number) o;
		return n.longValue();
	}
	
	public static String castToString(Object o) {
		return (String) o;
	}

}
