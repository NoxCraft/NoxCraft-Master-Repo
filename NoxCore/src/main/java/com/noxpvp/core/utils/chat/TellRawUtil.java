package com.noxpvp.core.utils.chat;

import java.util.Collection;

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
			BOLD(1), UNDERLINE(2), ITALIC(4), STRIKETHROUGH(8), OBFUSCATED(16), OFF(32);

			private int value;

			FLAG(int value) {
				this.value = value;
			}

			public int getValue() {
				return value;
			}
			
			public static int getBits(int initial, FLAG... flags) {
				int bitFlag = initial;
				for (FLAG flag : flags)
					bitFlag |= flag.getValue();
				return bitFlag;
			}

			public static int getBits(FLAG... flags) {
				return getBits(0, flags);
			}
			
			public static FLAG valueOf(ChatColor color) {
				switch (color) {
					case BOLD:
						return BOLD;
					case ITALIC:
						return ITALIC;
					case MAGIC:
						return OBFUSCATED;
					case STRIKETHROUGH:
						return STRIKETHROUGH;
					case UNDERLINE:
						return UNDERLINE;
					default:
						return null;
				}
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
			
			bif(json, bits, 0);
			
//			json.key("extra").array();
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
		
		public JSONBuilder addEnclosedText(String text) {
			return addEnclosedText(null, text);
		}
		
		public JSONBuilder addEnclosedText(ChatColor color, String text) {
			return addEnclosedText(color, text, 0, 0);
		}
		
		public JSONBuilder addEnclosedText(ChatColor color, String text, FLAG... flags)
		{
			return addEnclosedText(color, text, FLAG.getBits(flags), 0);
		}
		
		public JSONBuilder addEnclosedText(ChatColor color, String text, FLAG[] onFlags, FLAG[] offFlags)
		{
			return addEnclosedText(color, text, FLAG.getBits(onFlags), FLAG.getBits(offFlags));
		}
		
		public JSONBuilder addEnclosedText(ChatColor color, String text, int onBits, int offBits)
		{
			TrackedJSONStringer b = getStringer();
			if (color != null && color.isFormat()) {
				onBits = FLAG.getBits(onBits, FLAG.valueOf(color));
				color = null;
			}
			if (b.isCurrentlyArray()) {
				b.object();
			} else if (b.isCurrentlyObject()) {
				b.key("extra").array();
				return addEnclosedText(color, text, onBits, offBits);
			}
			
			b.key("text").value(text);
			
			if (color != null)
				b.key("color").value(color);
			
			bif(b, onBits, offBits);
			if (!b.isCurrentlyObject())
				throw new IllegalStateException("Corrupted json code. Was expecting an open object.");
			
			b.endObject();
			
			return this;
		}
		
		public JSONBuilder addOpenText(String text) {
			return addOpenText(null, text);
		}
		
		public JSONBuilder addOpenText(ChatColor color, String text) {
			return addOpenText(color, text, 0, 0);
		}
		
		public JSONBuilder addOpenText(ChatColor color, String text, FLAG... flags) {
			return addOpenText(color, text, FLAG.getBits(flags), 0);
		}
		
		public JSONBuilder addOpenText(ChatColor color, String text, FLAG[] onFlags, FLAG[] offFlags) {
			return addOpenText(color, text, FLAG.getBits(onFlags), FLAG.getBits(offFlags));
		}
		
		public JSONBuilder addOpenText(ChatColor color, String text, int onBits, int offBits) {
			TrackedJSONStringer b = getStringer();
			if (color != null && color.isFormat()) {
				onBits = FLAG.getBits(onBits, FLAG.valueOf(color));
				color = null;
			}
			if (b.isCurrentlyArray()) {
				b.object();
			} else if (b.isCurrentlyObject()) {
				b.key("extra").array();
				return addEnclosedText(color, text, onBits, offBits);
			}
			
			b.key("text").value(text);
			
			if (color != null)
				b.key("color").value(color);
			
			bif(b, onBits, offBits);
			if (!b.isCurrentlyObject())
				throw new IllegalStateException("Corrupted json code. Was expecting an open object.");
			
			b.key("extra").array();
			children++;
			
			return this;
		}
		
		public JSONBuilder closeCurrentText() {
			if (children <= 0)
				return this;
			
			TrackedJSONStringer b = getStringer();
			if (!b.isCurrentlyArray())
				throw new IllegalStateException("Corrupted json code. Was expecting an open Array.");
			b.endArray();
			
			children--;
			return this;
		}
		
		
		/**
		 * BIF: Build Internal Format<br/><br/>
		 * <b>
		 * INTERNAL METHOD ONLY. DO NOT USE!!!!<br/>
		 * USING WILL RISK DAMAGING YOUR JSON TEXT!
		 * </b>
		 */
		private void bif(TrackedJSONStringer b, int onBits, int offBits) {
			int v = (offBits & FLAG.BOLD.getValue()) - (onBits & FLAG.BOLD.getValue());
			if (v == 1)
				b.key("bold").value(false);
			else if (v == -1)
				b.key("bold").value(true);
			
			v = (offBits & FLAG.ITALIC.getValue()) - (onBits & FLAG.ITALIC.getValue());
			if (v == 1)
				b.key("italic").value(false);
			else if (v == -1)
				b.key("italic").value(true);
			
			
			v = (offBits & FLAG.STRIKETHROUGH.getValue()) - (onBits & FLAG.STRIKETHROUGH.getValue());
			if (v == 1)
				b.key("strikethrough").value(false);
			else if (v == -1)
				b.key("strikethrough").value(true);
			
			
			v = (offBits & FLAG.UNDERLINE.getValue()) - (onBits & FLAG.UNDERLINE.getValue());
			if (v == 1)
				b.key("underlined").value(false);
			else if (v == -1)
				b.key("underlined").value(true);
			
			
			v = (offBits & FLAG.OBFUSCATED.getValue()) - (onBits & FLAG.OBFUSCATED.getValue());
			if (v == 1)
				b.key("obfuscated").value(false);
			else if (v == -1)
				b.key("obfuscated").value(true);
		}

		public JSONBuilder close() {
			getStringer().close();
			closed = true;
			
			return this;
		}
		
		public String getJson() {
			return getJson(true);
		}
		
		public String getJson(boolean autoClose) {
			if (!closed && autoClose)
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
	 * Much safer version of {@link TellRawUtil#sendRaw(Player, String)} that uses {@link JSONBuilder} objects instead.
	 * @param players to send to
	 * @param builder message to send
	 */
	public static void sendRaw(Player[] players, JSONBuilder builder) {
		sendRaw(players, builder.getJson());
	}
	
	/**
	 * Much safer version of {@link TellRawUtil#sendRaw(Player, String)} that uses {@link JSONBuilder} objects instead.
	 * @param players to send to
	 * @param builder message to send
	 */
	public static void sendRaw(Collection<Player> players, JSONBuilder builder) {
		sendRaw(players, builder.getJson());
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
	
	/**
	 * This will send json messages to player.
	 * <br>
	 * <b> This is not safe to use by itself. Please use {@link JSONBuilder}</b>
	 * 
	 * @param players to send to
	 * @param raw message to send
	 */
	public static void sendRaw(Player[] players, String raw) {
		NMSPacketPlayOutChat chat = new NMSPacketPlayOutChat();
		CommonPacket cp = new CommonPacket(chat);
		cp.write(chat.isFromServer, true);
		cp.write(chat.chatComponent, WrappedChatComponent.fromJson(raw));
		
		for (Player player: players)
			PacketUtil.sendPacket(player, cp);
	}
	
	/**
	 * This will send json messages to player.
	 * <br>
	 * <b> This is not safe to use by itself. Please use {@link JSONBuilder}</b>
	 * 
	 * @param players to send to
	 * @param raw message to send
	 */
	public static void sendRaw(Collection<Player> players, String raw) {
		NMSPacketPlayOutChat chat = new NMSPacketPlayOutChat();
		CommonPacket cp = new CommonPacket(chat);
		cp.write(chat.isFromServer, true);
		cp.write(chat.chatComponent, WrappedChatComponent.fromJson(raw));
		
		for (Player player: players)
			PacketUtil.sendPacket(player, cp);
	}
	
}
