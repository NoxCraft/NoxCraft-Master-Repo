package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutNamedSoundEffect;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.utils.chat.MessageUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class SilentWalkingAbility extends BasePlayerAbility{

	public final static String ABILITY_NAME = "Silent Walking";
	public final static String PERM_NODE = "silent-walk";
	
	private CommonPacket packet;
	
	public SilentWalkingAbility(Player p, CommonPacket packet) {
		super(ABILITY_NAME, p);
		
		this.packet = packet;
	}

	public boolean execute() {
		
		if (!packet.read(PacketType.OUT_NAMED_SOUND_EFFECT.soundName).contains("step."))
			return false;
		
		Player hearing = getPlayer();
		Location loc = new Location(hearing.getWorld(),
				packet.read(PacketType.OUT_NAMED_SOUND_EFFECT.x) / 8,
				packet.read(PacketType.OUT_NAMED_SOUND_EFFECT.y) / 8, 
				packet.read(PacketType.OUT_NAMED_SOUND_EFFECT.z) / 8);
		
		double lowestDistance = 100;
		Player closest = null;
		
		for (Player p : PlayerUtil.getNearbyPlayers(hearing, 100)){
			if (p.equals(hearing)) continue;
			
			double d = p.getLocation().distance(loc);
			if (d < lowestDistance) {
				lowestDistance = d;
				closest = p;
			}
		}
		
		if (closest == null || lowestDistance > 6) {
			return false;
		}
		
		if (VaultAdapter.permission.has(closest, NoxMMO.PERM_NODE + ".ability." + SilentWalkingAbility.PERM_NODE)){
			return true;
		}
		
		return false;
		
	}

}
