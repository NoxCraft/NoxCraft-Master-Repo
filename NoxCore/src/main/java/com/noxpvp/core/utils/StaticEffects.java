package com.noxpvp.core.utils;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;


public class StaticEffects {

	public static void BloodEffect(Entity e){
		BloodEffect(e, NoxCore.getInstance());
	}
	
	public static void BloodEffect(Entity e, NoxPlugin plugin){
		Location loc = e.getLocation();
		if (e instanceof Player)
			loc.setY(loc.getY() + 1.7);
		
		new EffectsRunnable(Arrays.asList("blockdust_152_0"), false, loc, .12F, 25, 1, null).runTask(plugin);
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
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.volume, 100F);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.pitch, 0);
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
	 * Freezes the entity (jump and movement) for the set ticks
	 * 
	 * @param e
	 * @param ticks
	 */
	public static void Freeze(LivingEntity e, int ticks){
		PotionEffect walking = new PotionEffect(PotionEffectType.SPEED, ticks, -255, false),
					jumping = new PotionEffect(PotionEffectType.JUMP, ticks, -255, false);
		
		e.addPotionEffect(walking, true);
		e.addPotionEffect(jumping, true);
		
	}
}
