package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class SoulStealAbility extends BasePlayerAbility{
	
	public final static String PERM_NODE = "soulsteal";
	private final static String ABILITY_NAME = "SoulSteal";
	private LivingEntity target;
	private int duration;
	
	public LivingEntity getE() {return target;}
	public SoulStealAbility setE(LivingEntity e) {this.target = e; return this;}
	
	public int getDuration() {return duration;}
	public SoulStealAbility setDuration(int duration) {this.duration = duration; return this;}
	
	public SoulStealAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));
		
		return true;
	}
	
	public boolean mayExecute() {
		if (getPlayer() == null)
			return false;
		if (getE() == null)
			return false;
		
		return true;
	}

	
}
