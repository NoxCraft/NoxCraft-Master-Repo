package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.listeners.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class SeveringStrikesAbility extends BasePlayerAbility{
	
	public static final String ABILITY_NAME = "Severing Strikes";
	public static final String PERM_NODE = "severing-strikes";
	
	public static Map<String, SeveringStrikesAbility> strikers = new HashMap<String, SeveringStrikesAbility>();
	
	public void eventExecute(Player attacker, Damageable target){
		
		new DamageRunnable(target, attacker, 1*(1+((bleed / 20) / 6)), (bleed / 20) / 3).
				runTaskTimer(NoxMMO.getInstance(), 30, 30);
		
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
		
		this.bleed = (20 * pClass.getTotalLevels()) / 16;
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				if (SeveringStrikesAbility.strikers.containsKey(name))
					SeveringStrikesAbility.strikers.remove(name);
				
			}
		}, bleed);
		
		return true;
	}
	
}
