/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo.listeners;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.noxpvp.mmo.abilities.player.ClassArmorDisguiseAbility;
import com.noxpvp.mmo.abilities.player.SilentWalkingAbility;
import com.noxpvp.mmo.abilities.targeted.TargetAbility;

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
				new ClassArmorDisguiseAbility(p, packet).execute();
				
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
				arg0.setCancelled(new SilentWalkingAbility(p, packet).execute());
				
			}
			
		}
		
	}

}
