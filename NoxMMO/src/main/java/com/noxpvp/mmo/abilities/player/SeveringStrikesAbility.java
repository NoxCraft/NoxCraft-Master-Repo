package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class SeveringStrikesAbility extends BasePlayerAbility{
	
	public static final String ABILITY_NAME = "Severing Strikes";
	public static final String PERM_NODE = "severing-strikes";
	
	private static Map<String, SeveringStrikesAbility> strikers = new HashMap<String, SeveringStrikesAbility>();
	private static Map<Damageable, BleedRunnable> bleeders = new HashMap<Damageable, SeveringStrikesAbility.BleedRunnable>();
	
	private int bleedLength;
	
	public static boolean eventExecute(Player attacker, Damageable target){
		if (!strikers.containsKey(attacker.getName()))
			return false;
		
		SeveringStrikesAbility a = strikers.get(attacker.getName());
		
		BleedRunnable bleeder = new BleedRunnable(target, attacker, 1, a.bleedLength / 20);
		bleeder.runTaskTimer(NoxMMO.getInstance(), 10, 30);
		
		if (SeveringStrikesAbility.bleeders.containsKey(target)){
			SeveringStrikesAbility.bleeders.get(target).safeCancel();
		}
		SeveringStrikesAbility.bleeders.put(target, bleeder);
		
		return true;
	}
	
	/**
	 * 
	 * @param player The user of the ability instance
	 */
	public SeveringStrikesAbility(Player player){
		super(ABILITY_NAME, player);
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final String name = getPlayer().getName();
		
		SeveringStrikesAbility.strikers.put(name, this);
		PlayerClass pClass = NoxMMO.getInstance().getPlayerManager().getMMOPlayer(getPlayer()).getMainPlayerClass();
		
		this.bleedLength = 20 * ((pClass.getLevel() * pClass.getTierLevel()) / 16);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				if (SeveringStrikesAbility.strikers.containsKey(name))
					SeveringStrikesAbility.strikers.remove(name);
				
			}
		}, bleedLength);
		
		return true;
	}
	
	private static class BleedRunnable extends BukkitRunnable{
		private Damageable target;
		private Entity attacker;
		
		private Double damage;
		private int runLimit;
		private int runs;
		
		public BleedRunnable(Damageable target, Entity attacker, double damage, int runs){
			this.target = target;
			this.attacker = attacker;
			
			this.damage = damage;
			this.runLimit = runs;
			this.runs = 0;
		}
		
		public void safeCancel(){try {cancel();} catch (IllegalStateException e) {}}
		
		public void run(){
			if (runs++ > runLimit || target == null || attacker == null) {safeCancel(); return;}
			
			target.damage(damage, attacker);
		}
	}

	
}
