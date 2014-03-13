package com.noxpvp.core.data;

import org.bukkit.ChatColor;

import com.noxpvp.core.utils.chat.MessageUtil;

public class StringScroller{

	StringBuilder text;
	char cChar = ChatColor.COLOR_CHAR;
	String curColor;
	
	public StringScroller(String text) {
		this.text = new StringBuilder(text);
	}
	
	public String getString(){
		return text.toString();
	}
	
	public String scroll(){
		if (text.substring(0, 1).matches("[&" + cChar + "][0-9a-frklmo]")){
			curColor = text.substring(0, 1);
			MessageUtil.broadcast("matches");
			text.append(text.substring(0, 2)).delete(0, 2);
			
		} else {
			text.append(text.charAt(0)).deleteCharAt(0);
		}
		
		return ((curColor != null)? curColor : "") + text.toString();
	}

}
