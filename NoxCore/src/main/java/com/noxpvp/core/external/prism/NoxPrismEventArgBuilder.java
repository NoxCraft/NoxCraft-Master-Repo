package com.noxpvp.core.external.prism;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.bergerkiller.bukkit.common.utils.StringUtil;

public class NoxPrismEventArgBuilder {
	private static final String listKW = "FORCELIST";
	
	private StringBuilder s;
	
	public NoxPrismEventArgBuilder() {
		s = new StringBuilder();
	}
	
	public void withArg(String key, String arg) {
		s.append(key + ":" + arg + ",");
	}
	
	public void withList(String key, String... args) {
		HashSet<String> newArgs = new HashSet<String>();
		
		for (String s : args)
			newArgs.add(s);
		
		if (newArgs.size() > 0)
			withList(key, newArgs);		
	}
	
	public void withList(String key, Collection<String> args) {
		s.append(listKW + key + ":" + StringUtil.join("|", args) + ",");
	}
	
	public String build() {
		if (s.charAt(s.length() - 1) == ',')
			s.deleteCharAt(s.length() - 1);
		
		return s.toString();
	}
	
	public static Map<String, Object> getMapFromBuilder(String message) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		final String orig = message;
		String[] splits = orig.split(",");
		
		for (int i = 0; i < splits.length; i++) {
			String pair = splits[i];
			
			String[] keyValue = pair.split(":");
			
			String[] chanceArray;
			if (keyValue[0].startsWith(listKW)) {
				chanceArray = keyValue[1].split("\\|");
				ret.put(keyValue[0].replace(listKW, ""), chanceArray);
			} else 
				ret.put(keyValue[0], keyValue[1]);
		}
		
		return ret;
	}

}
