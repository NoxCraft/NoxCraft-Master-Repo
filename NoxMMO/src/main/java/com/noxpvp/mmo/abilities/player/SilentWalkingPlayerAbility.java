package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class SilentWalkingPlayerAbility extends BasePlayerAbility{

	public static final String ABILITY_NAME = "Silent Walking";
	public static final String PERM_NODE = "silent-walk";
	
	private CommonPacket packet;
	
	public SilentWalkingPlayerAbility(Player p, CommonPacket packet) {
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

		return VaultAdapter.permission.has(closest, NoxMMO.PERM_NODE + ".ability." + SilentWalkingPlayerAbility.PERM_NODE);

	}

}
