package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.core.utils.StaticEffects;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.UnregisterMMOHandlerRunnable;

public class SkullSmasherAbility extends BasePlayerAbility implements PVPAbility {
	
	public static final String ABILITY_NAME = "Skull Smasher";
	public static final String PERM_NODE = "skull-smasher";
	
	private BaseMMOEventHandler<EntityDamageByEntityEvent> handler;
	private double range;

	/**
	 * gets the currently set range for this ability
	 * 
	 * @return double The Ranged
	 */
	public double getRange() {return range;}

	/**
	 * Sets the range for this ability
	 * 
	 * @param range The range
	 * @return SkullSmasherAbility This instance
	 */
	public SkullSmasherAbility setRange(double range) {this.range = range; return this;}

	/**
	 * 
	 * @param player The user of the ability instance
	 */
	public SkullSmasherAbility(Player player, double range){
		super(ABILITY_NAME, player);
		
		this.handler = new BaseMMOEventHandler<EntityDamageByEntityEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("EntityDamageByEntityEvent").toString(),
				EventPriority.MONITOR, 1) {
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<EntityDamageByEntityEvent> getEventType() {
				return EntityDamageByEntityEvent.class;
			}
			
			public String getEventName() {
				return "EntityDamageByEntityEvent";
			}
			
			public void execute(EntityDamageByEntityEvent event) {
				if (!event.getDamager().equals(SkullSmasherAbility.this.getPlayer()))
					return;
				
				double damage = event.getDamage();
				double range = SkullSmasherAbility.this.range;
				Player attacker = (Player) event.getDamager();
				
				for (Entity it : getPlayer().getNearbyEntities(range, range, range)){
					if (!(it instanceof Damageable) || it.equals(attacker)) continue;
					
					((Damageable) it).damage(damage - (damage / 4), attacker);
					StaticEffects.SkullBreak((LivingEntity) it);
				}
			}
		};
		
		this.range = range;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		PlayerClass pClass = PlayerManager.getInstance().getPlayer(getPlayer()).getPrimaryClass();
		
		int length = (20 * (pClass.getTotalLevel())) / 16;
		
		registerHandler(handler);
		new UnregisterMMOHandlerRunnable(handler).runTaskLater(NoxMMO.getInstance(), length);
		
		return true;
	}
	
}
