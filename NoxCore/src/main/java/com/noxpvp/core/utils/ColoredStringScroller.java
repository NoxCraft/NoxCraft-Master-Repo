package com.noxpvp.core.utils;

import org.bukkit.ChatColor;

import com.noxpvp.core.utils.chat.MessageUtil;

public class ColoredStringScroller{

	StringBuilder text;
	String curColor;
	String curFormat;
	char cChar;
	
	public ColoredStringScroller(String text) {
		this.text = new StringBuilder(text);
		
		cChar = ChatColor.COLOR_CHAR;
		curColor = "";
		curFormat = "";
	}
	
	public String getString(){
		return text.toString();
	}
	
	public String setCurrent(String code){
		if (code.matches("[&" + cChar + "][0-9a-fr]")){
			curFormat = "";
			return (curColor = code);
			
		} else if (code.matches("[&" + cChar + "][kmlno]")){
			return (curFormat = code);
			
		}
		
		return "";	
	}
	
	public String scroll(){
		while (text.substring(0, 2).matches("[&" + cChar + "][0-9a-frkmlno]")){
			text.append(setCurrent(text.substring(0, 2))).delete(0, 2);
			
		}
		
		text.append(text.charAt(0)).deleteCharAt(0);
		return MessageUtil.parseColor(curColor + curFormat + text.toString());
	}

}
