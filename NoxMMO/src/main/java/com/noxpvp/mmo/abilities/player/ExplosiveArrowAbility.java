package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.listeners.BaseMMOEventHandler;

public class ExplosiveArrowAbility extends BasePlayerAbility{
	
	static Map<String, ExplosiveArrowAbility> abilityCue = new HashMap<String, ExplosiveArrowAbility>();
	
	private final static String ABILITY_NAME = "Explosive Arrow";
	public final static String PERM_NODE = "explosive-arrow";
	
	/**
	 * Runs the event-side execution of this ability
	 * 
	 * @param player The Player - normally arrow shooter from a projectile hit event
	 * @param loc The location - normally the hit block from a projectile hit event
	 * @return boolean If the execution ran successfully
	 */
	private void eventExecute(Player player, Location loc, float power) {
		
		player.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, false, false);
		return;
	}
	
	private BaseMMOEventHandler<ProjectileHitEvent> handler;
	private float power;

	/**
	 * Gets the current power of the explosion
	 * 
	 * @return Float The power
	 */
	public float getPower() {return power;}

	/**
	 * Sets the power of the explosion
	 * 
	 * @param power The power
	 * @return NetArrowAbility This instance
	 */
	public ExplosiveArrowAbility setPower(float power) {this.power = power; return this;}

	public ExplosiveArrowAbility(Player player) {
		super(ABILITY_NAME, player);
		
		handler = new BaseMMOEventHandler<ProjectileHitEvent>(new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileHitEvent").toString(), EventPriority.NORMAL, 1) {

			@Override
			public boolean ignoreCancelled() {
				return true;
			}

			@Override
			public void execute(ProjectileHitEvent event) {
				if (event.getEntityType() != EntityType.ARROW)
					return;
				
				Arrow a = (Arrow) event.getEntity();
				
				if (a.getShooter().getType() != EntityType.PLAYER)
					return;
				
				String name = ((Player) a.getShooter()).getName();
				ExplosiveArrowAbility ab = null;
				
				if (ExplosiveArrowAbility.abilityCue.containsKey(name)) {
					ab = ExplosiveArrowAbility.abilityCue.get(name);
					ExplosiveArrowAbility.abilityCue.remove(name);
					
					ExplosiveArrowAbility.this.eventExecute(ab.getPlayer(), a.getLocation(), ab.power);

				} else return;
			}

			@Override
			public Class<ProjectileHitEvent> getEventType() {
				return ProjectileHitEvent.class;
			}

			@Override
			public String getEventName() {
				return "ProjectileHitEvent";
			}
		};
		
		this.power = 4f;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final String pName = getPlayer().getName();
		
		ExplosiveArrowAbility.abilityCue.put(getPlayer().getName(), this);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				
				if (ExplosiveArrowAbility.abilityCue.containsKey(pName))
					ExplosiveArrowAbility.abilityCue.remove(pName);
				
			}
		}, 100);
		
		return true;
	}

}
