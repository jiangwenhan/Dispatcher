package com.ku6.cdn.dispatcher.common.util;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

public class TypeUtil {
	
	public static long castToLong(Object o) {
		Number n = (Number) o;
		return n.longValue();
	}
	
	public static String castToString(Object o) {
		return (String) o;
	}
	
	public static int castToStoreType(String type) {
		if (type.equalsIgnoreCase("group_cold")) {
			return COMMON_GROUP_COLD;
		} else if (type.equalsIgnoreCase("node_cold")) {
			return COMMON_NODE_COLD;
		} else if (type.equalsIgnoreCase("node_hot")) {
			return COMMON_NODE_HOT;
		} else if (type.equalsIgnoreCase("store")) {
			return COMMON_STORE;
		}
		return COMMON_STORE_UNKNOWN;
	}

}
