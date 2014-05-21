package com.noxpvp.mmo.listeners;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.noxpvp.mmo.abilities.player.ClassArmorDisguisePlayerAbility;
import com.noxpvp.mmo.abilities.player.SilentWalkingPlayerAbility;

public class PacketListeners {

/*	public class PlayerAnimationListener implements PacketListener {

		public void onPacketReceive(PacketReceiveEvent arg0) {
			
			CommonPacket packet = arg0.getPacket();
			Player p = arg0.getPlayer();
			
			if (p == null) return;
			
			new TargetAbility(p).setRange(75).execute();
			
		}

		public void onPacketSend(PacketSendEvent arg0) {}
		
	}*/
	
	public class EntityEquipmentListener implements PacketListener {

		public void onPacketReceive(PacketReceiveEvent arg0) {
			
			CommonPacket packet = arg0.getPacket();
			Player p = arg0.getPlayer();
			
			if (p != null){
				
				/*
				 * class armor disguises
				 */
				new ClassArmorDisguisePlayerAbility(p, packet).execute();
				
			}
		}

		public void onPacketSend(PacketSendEvent arg0) {}
		
	}

	public class WorldSoundListener implements PacketListener{
		
		public void onPacketReceive(PacketReceiveEvent arg0) {}
		
		public void onPacketSend(PacketSendEvent arg0) {
			
			CommonPacket packet = arg0.getPacket();
			Player p = arg0.getPlayer();
			
			if (p != null){
				
				/*
				 * silent walking ability
				 */
				arg0.setCancelled(new SilentWalkingPlayerAbility(p, packet).execute());
				
			}
			
		}
		
	}

}
