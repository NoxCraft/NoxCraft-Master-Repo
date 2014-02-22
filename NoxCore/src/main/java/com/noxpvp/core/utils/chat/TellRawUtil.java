package com.noxpvp.core.utils.chat;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;

import com.noxpvp.core.json.TrackedJSONStringer;

public class TellRawUtil {

	/*
	 * 
	 * /tellraw @a { text:"TEST1", color:yellow, bold:true, underlined:true,
	 * italic:true, strikethrough:true, obfuscated:true }
	 */
	public static class JSONBuilder {
		public static enum FLAG {
			BOLD(1), UNDERLINE(2), ITALIC(4), STRIKETHROUGH(8), OBFUSCATED(16);

			private int value;

			FLAG(int value) {
				this.value = value;
			}

			public int getValue() {
				return value;
			}

			public static int getBits(FLAG[] flags) {
				int bitFlag = 0;
				for (FLAG flag : flags)
					bitFlag |= flag.getValue();
				return bitFlag;
			}
		}
		
		private TrackedJSONStringer json;
		private boolean closed = false;
		
		public JSONBuilder() {
			this(null);
		}
		
		public JSONBuilder(ChatColor color, FLAG... flags) {
			this(color, "", FLAG.getBits(flags));
		}
		
		public JSONBuilder(ChatColor color, int bits) {
			this(color, "", bits);
		}
		
		public JSONBuilder(ChatColor color, String initialText, FLAG... flags) {
			this(color, initialText, FLAG.getBits(flags));
		}
		
		public JSONBuilder(ChatColor color, @Nullable String initialText, int bits) {
			if (initialText == null)
				initialText = "";
			
			//Do not accept format codes. This will be deprecated!
			if (color.isFormat())
				color = null;
			
			json = new TrackedJSONStringer();
			
			json.object();
			json.key("text").value(initialText);
			
			if (color != null)
				json.key("color").value(color.name());
			
			json.key("extra").array();
		}
		
		private void closeCheck() {
			if (closed)
				throw new IllegalStateException("Json builder object is already closed. You must specify a new builder!");
		}
		
		public final boolean isClosed() { return closed; }
		
		public JSONBuilder close() {
			json.close();
			closed = true;
			
			return this;
		}
		
		public String getJson() {
			if (!closed)
				close();
			
			return json.toString();
		}
	}
	
}
