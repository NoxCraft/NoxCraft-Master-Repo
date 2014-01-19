package com.noxpvp.core.utils;

import java.util.Collection;

public class NoxLogicUtils {
	public static boolean anyNull(Collection<?> col)
	{
		if (col == null)
			return true;
		
		for (Object ob : col)
			if (ob == null)
				return true;
		return false;
	}
	
	public static boolean anyNull(Object... objs)
	{
		if (objs == null)
			return true;
		
		for (Object ob : objs)
			if (ob == null)
				return true;
		return false;
	}
}
