package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutNamedSoundEffect;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;

public class SilentWalkingAbility extends BasePlayerAbility implements PassiveAbility{

	public final static String ABILITY_NAME = "Silent Walking";
	public final static String PERM_NODE = "silent-walk";
	
	private CommonPacket packet;
	private NMSPacketPlayOutNamedSoundEffect nms;
	
	public SilentWalkingAbility(Player p, CommonPacket packet) {
		super(ABILITY_NAME, p);
		
		this.packet = packet;
	}

	public boolean execute() {
		nms = new NMSPacketPlayOutNamedSoundEffect();
		
		Player hearing = getPlayer();
		Location loc = new Location(getPlayer().getWorld(), packet.read(nms.x), packet.read(nms.y), packet.read(nms.z));
		
		double lowestDistance = Double.MAX_VALUE;
		Player closest = null;
		
		for (Player p : PlayerUtil.getNearbyPlayers(hearing, 50)) {
			double d = p.getLocation().distance(loc);
			
			if (d < lowestDistance) {
				lowestDistance = d;
				closest = p;
			}
		}
		
		if (closest == null || lowestDistance > 5) return false;
		
		if (VaultAdapter.permission.has(closest, NoxMMO.PERM_NODE + ".ability." + this.PERM_NODE)){
			return true;
		}
		
		return false;
		
	}

}
