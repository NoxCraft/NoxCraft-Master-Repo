package com.noxpvp.core.utils;

import java.util.Collection;

public class NoxLogicUtils {
	public static boolean anyNull(Object... objs)
	{
		for (Object ob : objs)
			if (ob == null)
				return true;
		return false;
	}
	
	public static boolean anyNull(Collection<?> col)
	{
		for (Object ob : col)
			if (ob == null)
				return true;
		return false;
	}
}
