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

package com.noxpvp.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.dsh105.holoapi.HoloAPI;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.packet.ParticleRunner;


public class StaticEffects {

	private static List<LivingEntity> frozenEntitys = new ArrayList<LivingEntity>();
	
	public static void DamageAmountParticle(LivingEntity e, double damage){
		DamageAmountParticle(e.getEyeLocation(), damage);
	}
	
	public static void DamageAmountParticle(Location loc, double damage){
		HoloAPI.getManager().createSimpleHologram(loc, 5, true, ChatColor.RED + String.format("%.1f", damage));
	}
	
	public static void SkullBreak(LivingEntity e){
		SkullBreak(e.getEyeLocation(), NoxCore.getInstance());
	}
	
	public static void SkullBreak(Location loc, NoxPlugin plugin){
		new ParticleRunner("blockcrack_155_0", loc, false, .1F, 20, 2).start(0);
	}
	
	public static void BloodEffect(Entity e){
		BloodEffect(e, NoxCore.getInstance());
	}
	
	public static void BloodEffect(Entity e, NoxPlugin plugin){
		new ParticleRunner("blockdust_35_14", e, false, .12F, 15, 1).runTaskTimer(plugin, 0, 0);
	}
	
	public static void BroadcastSound(Entity e, Sound sound){
		BroadcastSound(e.getLocation(), sound);
	}
	
	public static void BroadcastSound(Location loc, Sound sound){
		loc.getWorld().playSound(loc, sound, 10, 0);
	}
	
	public static void PlaySound(Player p, Sound sound){
		PlaySound(p, p.getLocation(), sound.name());
	}
	
	public static void playSound(Player p, String sound){
		PlaySound(p, p.getLocation(), sound);
	}
	
	public static void PlaySound(Player p, Location loc, String sound){
		CommonPacket packet = new CommonPacket(PacketType.OUT_NAMED_SOUND_EFFECT);
		
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.soundName, sound);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.volume, 100.0F);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.pitch, 2);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.x, (int) loc.getX());
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.y, (int) loc.getY());
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.z, (int) loc.getZ());
		
		/*
		 * Apparently only /some/ packets need this for coords...
		 */
//		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.x, (int) loc.getX() * 32);
//		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.y, (int) loc.getY() * 32);
//		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.z, (int) loc.getZ() * 32);
		
		PacketUtil.sendPacket(p, packet, false);
	}

	/**
	 * Freezes the entity (jump and movement) for the set ticks.
	 * If the entity has already been and is still frozen, will return false
	 * 
	 * @param e
	 * @param ticks
	 * 
	 * @return Boolean if freezing was successful
	 */
	public static boolean Freeze(final LivingEntity e, int ticks){
		if (frozenEntitys.contains(e))
			return false;
		
		final List<PotionEffect> preserve = new ArrayList<PotionEffect>();
		for (PotionEffect effect : e.getActivePotionEffects()){
			preserve.add(new PotionEffect(
					effect.getType(),
					effect.getDuration() - ticks,
					effect.getAmplifier(),
					effect.isAmbient()));
		}
		
		PotionEffect walking = new PotionEffect(PotionEffectType.SPEED, ticks, -50, false),
					jumping = new PotionEffect(PotionEffectType.JUMP, ticks, -50, false);
		
		e.addPotionEffect(walking, true);
		e.addPotionEffect(jumping, true);
		
		
		final NoxListener<NoxPlugin> freezeRemovalPrevention = new NoxListener<NoxPlugin>(NoxCore.getInstance()) {
			
			@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
			public void onPotionDrink(PlayerItemConsumeEvent event){
				if (!e.equals(event.getPlayer()) || event.getItem().getType() != Material.POTION)
					return;
				
				if (!frozenEntitys.contains(e)){
					this.unregister();
					return;
				}
				
				event.setCancelled(true);	
			}
			
			@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
			public void onPotionSplash(PotionSplashEvent event){
				if (!event.getAffectedEntities().contains(e))
					return;
				
				if (!frozenEntitys.contains(e)){
					this.unregister();
					return;
				}
				try {
					event.getAffectedEntities().remove(e);
					event.setCancelled(true);
				} catch (UnsupportedOperationException e) {}
					
			}
		};
		
		Bukkit.getScheduler().runTaskLater(NoxCore.getInstance(), new Runnable() {
			
			public void run() {
				if (e == null || !e.isValid()){
					return;
				}
				
				for (PotionEffect effect : preserve)
					if (effect.getDuration() > 0)
						e.addPotionEffect(effect, true);
				
				if (StaticEffects.frozenEntitys.contains(e))
					StaticEffects.frozenEntitys.remove(e);
			
				freezeRemovalPrevention.unregister();
			}
		}, ticks);
		
		
		freezeRemovalPrevention.register();
		
		return frozenEntitys.add(e);
	}
}
