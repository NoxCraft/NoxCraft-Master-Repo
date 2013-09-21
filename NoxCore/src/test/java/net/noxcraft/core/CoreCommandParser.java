package net.noxcraft.core;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;


import org.junit.Test;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.utils.CommandUtil;

public class CoreCommandParser {
	
	@Test(timeout=10000)
	public void testFlags()
	{
		String argline = "-p \"eating my\" Yoshi -a true -f ";
		Map<String, Object> expectedFlags = new HashMap<String, Object>();
		
		expectedFlags.put("p", "eating my");
		expectedFlags.put("a", "true");
		
		String expected = "Yoshi";
		
		Map<String, Object> flags = new LinkedHashMap<String, Object>();
		String[] args = argline.split(" ");
		String[] newArgs;
		newArgs = CommandUtil.parseFlags(flags, args);
		String newArgline = StringUtil.combine(" ", newArgs);
		
		Assert.assertEquals(newArgline, expected);
		for (String key: flags.keySet())
		{
			if (!expectedFlags.containsKey(key))
				fail("The flag map did not contain '" + key);
			if (!expectedFlags.get(key).equals(flags.get(key)))
				fail("The flag map contained wrong value for '" + key + "' They need '" + expectedFlags.get(key).toString() + "' But got '" + flags.get(key).toString() + "'");
		}
		
		
		
	}
}
