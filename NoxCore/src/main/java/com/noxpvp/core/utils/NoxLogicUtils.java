package com.noxpvp.core.utils;

public class NoxLogicUtils {
	public static boolean anyNull(Object... objs)
	{
		for (Object ob : objs)
			if (ob == null)
				return true;
		return false;
	}
}
