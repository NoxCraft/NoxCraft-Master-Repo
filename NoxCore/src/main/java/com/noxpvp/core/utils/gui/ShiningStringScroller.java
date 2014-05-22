package com.noxpvp.core.utils.gui;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;

public class ShiningStringScroller {
	private static final String regexCodes = "[&" + ChatColor.COLOR_CHAR + "][0-9a-frkmlno]";
	private static final String regexColors = "[&" + ChatColor.COLOR_CHAR + "][0-9a-fr]";
	private static final String regexFormats = "[&" + ChatColor.COLOR_CHAR + "][kmlno]";

	private String text;
	private String[] colors;
	private String curColor, curFormat;
	private int index;

	public ShiningStringScroller(String text) {
		this(text, (ChatColor[]) null);
	}

	/**
	 * @param text   The text to shine through
	 * @param colors a list of colors, if you don't want the nice default ones
	 * @throws IllegalArgumentException if the text is less than the length of provided colors, or 3 if no colors are provided
	 */
	public ShiningStringScroller(String text, @Nullable ChatColor... colors) {
		if (text == null || (text.length() < 3 || (colors != null && text.length() < colors.length)))
			throw new IllegalArgumentException("Text cannot be less than 3 or provided colors[] length");

		this.text = text;
		this.curColor = ChatColor.RESET.toString();
		this.curFormat = "";

		if (colors == null) {
			this.colors = new String[3];
			this.colors[0] = ChatColor.GOLD.toString();
			this.colors[1] = ChatColor.YELLOW.toString();
			this.colors[2] = ChatColor.RED.toString();
		} else {
			this.colors = new String[colors.length + 1];
			for (int i = 0; i < colors.length; i++)
				this.colors[i] = colors[i].toString();

			this.colors[colors.length - 1] = ChatColor.RESET.toString();
		}

		this.index = 0;
	}

	public void resetCurrent() {
		curColor = ChatColor.RESET.toString();
		curFormat = "";
	}

	public String shine() {
		StringBuilder text = new StringBuilder(this.text.toString());
		String shineColor;

		for (int i = 0, n = index; i <= colors.length; i++, n++) {

			if (i == colors.length) {
				index++;
				text.insert(n, curColor + curFormat);
				return text.toString();
			}

			shineColor = colors[i];

			if (shineColor == null)
				continue;

			if (n + 2 > text.length()) {
				index = 0;
				resetCurrent();
				return text.toString();
			}

			while (n <= text.length() && text.substring(n, n + 2).matches(regexCodes)) {
				String code = text.substring(n, n + 2);
				if (code.matches(regexColors)) {
					curColor = code;
					curFormat = "";
				} else if (code.matches(regexFormats)) {
					curFormat = code;
				}

				if (i == 0)
					index = index + 2;

				n = n + 2;
			}

			if (n > text.length()) {
				index = 0;
				resetCurrent();

				return text.toString();
			}

			text.insert(n, shineColor);
			n = n + 2;
		}

		index++;
		return text.toString();

	}

}
