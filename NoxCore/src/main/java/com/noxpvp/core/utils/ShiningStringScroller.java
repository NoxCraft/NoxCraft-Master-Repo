package com.noxpvp.core.utils;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;

public class ShiningStringScroller {
	private final static String regexColors = "[&" + ChatColor.COLOR_CHAR + "][0-9a-frkmlo]";
	
	private String text;
	private String[] colors;
	private int index;
	
	public ShiningStringScroller(String text){
		this(text, null);
	}
	
	/**
	 * 
	 * @param text The text to shine through
	 * @param colors a list of colors, if you don't want the nice default ones
	 * 
	 * @throws IllegalArgumentException if the text is less than the length of provided colors, or 3 if no colors are provided
	 */
	public ShiningStringScroller(String text, @Nullable ChatColor... colors) {
		if (text == null || (text.length() < 3 || (colors != null && text.length() < colors.length)))
			throw new IllegalArgumentException("Text cannot be less than 3 or provided colors[] length");
		
		this.text = text;
		
		if (colors == null){
			this.colors = new String[4];
			this.colors[0] = ChatColor.GOLD.toString();
			this.colors[1] = ChatColor.YELLOW.toString();
			this.colors[2] = ChatColor.RED.toString();
			this.colors[3] = ChatColor.RESET.toString();
		} else {
			this.colors = new String[colors.length + 1];
			for (int i = 0; i < colors.length; i++)
				this.colors[i] = colors[i].toString();
			
			this.colors[colors.length - 1] = ChatColor.RESET.toString();
		}
			
		this.index = 0;
	}
	
	public String shine(){
		StringBuilder text = new StringBuilder(this.text.toString());
		String newColor;
		
		for (int i = 0, n = index; i < colors.length; i++, n++){
			newColor = colors[i];
			
			if (newColor == null)
				continue;
			
			if (n + 2 > text.length()){
				index = 0;
				colors[3] = ChatColor.RESET.toString();
				return text.toString();
			}
			
			while (text.substring(n, n + 2).matches(regexColors)){//TODO support color WITH bold and stuff
				colors[3] = text.substring(n, n + 2);
				if (i == (colors.length - 1))
					newColor = colors[3];
				
				if (i == 0)
					index = index + 2;
				
				n = n + 2;
			}
			
			if (n > text.length()){
				index = 0;
				colors[3] = ChatColor.RESET.toString();
				return text.toString();
			}
			
			text.insert(n, newColor);
			n = n + 2;
		}
		
		index++;
		return text.toString();
		
	}

}
