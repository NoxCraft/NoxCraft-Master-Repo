package com.noxpvp.mmo.abilities.player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.listeners.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.EffectsRunnable;

public class SeveringStrikesAbility extends BasePlayerAbility{
	
	public static final String ABILITY_NAME = "Severing Strikes";
	public static final String PERM_NODE = "severing-strikes";
	
	public static Map<String, SeveringStrikesAbility> strikers = new HashMap<String, SeveringStrikesAbility>();
	public static Map<Damageable, BleedRunnable> bleeders = new HashMap<Damageable, SeveringStrikesAbility.BleedRunnable>();
	
	public void eventExecute(Player attacker, Damageable target){
		
		BleedRunnable bleeder = new BleedRunnable(target, attacker, 1*(1+((bleed / 20) / 6)), (bleed / 20) / 3);
		
		if (SeveringStrikesAbility.bleeders.containsKey(target)){
			SeveringStrikesAbility.bleeders.get(target).safeCancel();
		}
		SeveringStrikesAbility.bleeders.put(target, bleeder);
		
		bleeder.runTaskTimer(NoxMMO.getInstance(), 30, 30);
		
		return;
	}
	
	private BaseMMOEventHandler<EntityDamageByEntityEvent> handler;
	private int bleed;
	
	/**
	 * 
	 * @param player The user of the ability instance
	 */
	public SeveringStrikesAbility(Player player){
		super(ABILITY_NAME, player);
		
		this.handler = new BaseMMOEventHandler<EntityDamageByEntityEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("EntityDamageByEntityEvent").toString(),
				EventPriority.MONITOR, 1) {
			
			@Override
			public boolean ignoreCancelled() {
				return true;
			}
			
			@Override
			public Class<EntityDamageByEntityEvent> getEventType() {
				return EntityDamageByEntityEvent.class;
			}
			
			@Override
			public String getEventName() {
				return "EntityDamageByEntityEvent";
			}
			
			@Override
			public void execute(EntityDamageByEntityEvent event) {
				if (event.getDamager().equals(SeveringStrikesAbility.this.getPlayer()) && event.getEntity() instanceof Damageable)
					SeveringStrikesAbility.this.eventExecute((Player) event.getDamager(), (Damageable) event.getEntity());
			}
		};
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final String name = getPlayer().getName();
		
		SeveringStrikesAbility.strikers.put(name, this);
		PlayerClass pClass = NoxMMO.getInstance().getPlayerManager().getMMOPlayer(getPlayer()).getMainPlayerClass();
		
		this.bleed = 20 * ((pClass.getLevel() * pClass.getTierLevel()) / 16);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				if (SeveringStrikesAbility.strikers.containsKey(name))
					SeveringStrikesAbility.strikers.remove(name);
				
			}
		}, bleed);
		
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
			if (runs++ > runLimit || target == null || attacker == null || target.isDead()) {safeCancel(); return;}
			
			target.damage(damage, attacker);
			new EffectsRunnable(Arrays.asList("blockcrack_30_0"), target.getLocation(), .1F, 20, false, false, null).runTask(NoxMMO.getInstance());	
		}
	}

	
}
