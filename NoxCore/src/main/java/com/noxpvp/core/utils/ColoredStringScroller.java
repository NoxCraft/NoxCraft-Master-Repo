package com.noxpvp.core.utils;

import org.bukkit.ChatColor;

public class ColoredStringScroller{

	StringBuilder text;
	String curColor;
	char cChar;
	
	public ColoredStringScroller(String text) {
		this.text = new StringBuilder(text);
		
		cChar = ChatColor.COLOR_CHAR;
		curColor = "";
	}
	
	public String getString(){
		return text.toString();
	}
	
	public String scroll(){
		if (text.substring(0, 2).matches("[&" + cChar + "][0-9a-frkmlo]")){
			curColor = text.substring(0, 2);
			text.append(curColor + text.charAt(2)).delete(0, 3);
			
		} else {
			text.append(text.charAt(0)).deleteCharAt(0);
		}
		
		return curColor + text.toString();
	}

}
