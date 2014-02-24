package com.noxpvp.core.utils.chat;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutChat;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
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
		private int children = 0;
		
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
			if (color != null && color.isFormat())
				color = null;
			
			json = new TrackedJSONStringer();
			
			json.object();
			json.key("text").value(initialText);
			
			if (color != null)
				json.key("color").value(color.name());
			
			if ((bits & FLAG.BOLD.getValue()) == 1)
				json.key("bold").value(true);
			if ((bits & FLAG.ITALIC.getValue()) == 1)
				json.key("italic").value(true);
			if ((bits & FLAG.STRIKETHROUGH.getValue()) == 1)
				json.key("strikethrough").value(true);
			if ((bits & FLAG.UNDERLINE.getValue()) == 1)
				json.key("underlined").value(true);
			if ((bits & FLAG.OBFUSCATED.getValue()) == 1)
				json.key("obfuscated").value(true);
			
			json.key("extra").array();
		}
		
		private void closeCheck() {
			if (closed)
				throw new IllegalStateException("Json builder object is already closed. You must specify a new builder!");
		}
		
		public final boolean isClosed() { return closed; }
		
		/**
		 * USE THIS METHOD FOR EVERY OPERATION ON STRINGER!
		 * @return safe json or throws IllegalStateException for builder already being closed.
		 * @throws IllegalStateException
		 */
		protected TrackedJSONStringer getStringer() {
			closeCheck();
			return json;
		}
		
		public JSONBuilder close() {
			getStringer().close();
			closed = true;
			
			return this;
		}
		
		public String getJson() {
			if (!closed)
				close();
			
			return json.toString(); //Only time we bypass getStringer()
		}
	}
	
	/**
	 * Much safer version of {@link TellRawUtil#sendRaw(Player, String)} that uses {@link JSONBuilder} objects instead.
	 * @param player to send to
	 * @param builder message to send
	 */
	public static void sendRaw(Player player, JSONBuilder builder) {
		sendRaw(player, builder.getJson());
	}
	
	/**
	 * This will send json messages to player.
	 * <br>
	 * <b> This is not safe to use by itself. Please use {@link JSONBuilder}</b>
	 * 
	 * @param player to send to
	 * @param raw message to send
	 */
	public static void sendRaw(Player player, String raw) {
		NMSPacketPlayOutChat chat = new NMSPacketPlayOutChat();
		CommonPacket cp = new CommonPacket(chat);
		cp.write(chat.isFromServer, true);
		cp.write(chat.chatComponent, WrappedChatComponent.fromJson(raw));
		PacketUtil.sendPacket(player, cp);
	}
	
}
