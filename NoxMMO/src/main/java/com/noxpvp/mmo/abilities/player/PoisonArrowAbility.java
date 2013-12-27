package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.listeners.BaseMMOEventHandler;

public class PoisonArrowAbility extends BasePlayerAbility{
	
	static Map<String, PoisonArrowAbility> abilityCue = new HashMap<String, PoisonArrowAbility>();
	
	private final static String ABILITY_NAME = "Poison Arrow";
	public final static String PERM_NODE = "poison-arrow";
	
	private BaseMMOEventHandler<EntityDamageByEntityEvent> handler;
	private int amplifier;
	private int duration;
	
	/**
	 * Runs the event-side execution of this ability
	 * 
	 * @param player The Player - normally arrow shooter from a damage event
	 * @param target The target - normally the living entity from a damage event
	 * @return boolean If the execution ran successfully
	 */
	private void eventExecute(Player player, LivingEntity target){
		String name = player.getName();
				
		if (abilityCue.containsKey(name))
			return;
		
		PoisonArrowAbility a = abilityCue.get(name);
		
		target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, a.duration, a.amplifier));
		return;
	}

	/**
	 * Get the currently set poison effect amplifier
	 * 
	 * @return Integer The poison amplifier
	 */
	public int getAmplifier() {return amplifier;}

	/**
	 * Sets the poison effect amplifier
	 * 
	 * @param size The poison effect amplifier
	 * @return NetArrowAbility This instance
	 */
	public PoisonArrowAbility setSize(int size) {this.amplifier = size; return this;}

	/**
	 * Gets the duration of the poison effect
	 * 
	 * @return Integer The duration
	 */
	public int getTime() {return duration;}

	/**
	 * Sets the duration in ticks of the poison amplifier
	 * 
	 * @param duration The duration
	 * @return NetArrowAbility This instance
	 */
	public PoisonArrowAbility setDuration(int duration) {this.duration = duration; return this;}

	public PoisonArrowAbility(Player player) {
		super(ABILITY_NAME, player);
		
		handler = new BaseMMOEventHandler<EntityDamageByEntityEvent>(new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("EntityDamageByEntityEvent").toString(), EventPriority.NORMAL, 1) {

			@Override
			public boolean ignoreCancelled() {
				return true;
			}

			@Override
			public void execute(EntityDamageByEntityEvent event) {
				if (!(event.getEntity() instanceof LivingEntity))
					return;
				
				if (event.getDamager().getType() != EntityType.ARROW)
					return;
				
				LivingEntity e = (LivingEntity) event.getEntity();
				Arrow a = (Arrow) event.getDamager();		
				
				if (a.getShooter().getType() != EntityType.PLAYER)
					return;
				
				LivingEntity s = a.getShooter();
				String name = ((Player) s).getName();
				
				if (ExplosiveArrowAbility.abilityCue.containsKey(name)) {
					PoisonArrowAbility.this.eventExecute((Player) s, e);

				} else return;
			}

			@Override
			public Class<EntityDamageByEntityEvent> getEventType() {
				return EntityDamageByEntityEvent.class;
			}

			@Override
			public String getEventName() {
				return "EntityDamageByEntityEvent";
			}
		};
		
		this.amplifier = 3;
		this.duration = 75;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final String pName = getPlayer().getName();
		
		PoisonArrowAbility.abilityCue.put(pName, this);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				
				if (PoisonArrowAbility.abilityCue.containsKey(pName))
					PoisonArrowAbility.abilityCue.remove(pName);
				
			}
		}, 100);
		
		return true;
	}

}
