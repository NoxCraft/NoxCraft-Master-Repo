package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseRangedPlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.ExpandingDamageRunnable;
import com.noxpvp.mmo.runnables.SetVelocityRunnable;
import com.noxpvp.mmo.runnables.ShockWaveAnimation;
/**
 * @author NoxPVP
 *
 */
public class MassDestructionAbility extends BaseRangedPlayerAbility implements IPVPAbility {
	
	public static final String PERM_NODE = "mass-destruction";
	public static final String ABILITY_NAME = "Mass Destruction";
	
	@Override
	public String getDescription() {
		return "You leap high into the air causing the ground to shake when you land, dealing "
				+ getDamage() + " to all enemys within " + getRange() + " blocks";
	}
	
	private BaseMMOEventHandler<EntityDamageEvent> handler;
	private double hVelo = 1.5;
	private boolean isActive;
	
	private MassDestructionAbility setActive(boolean active) {
		boolean changed = this.isActive != active;
		this.isActive = active;
		
		if (changed)
			if (active)
				registerHandler(handler);
			else
				unregisterHandler(handler);
		
		return this; 
	}

	/**
	 * 
	 * @param velo Double velocity value for player upwards/downwards effect
	 * @return MassDestructionAbility This instance, used for chaining
	 */
	public MassDestructionAbility sethVelo(double velo) {this.hVelo = velo; return this;}
	
	/**
	 * 
	 * @return Double The current set velocity used for the player upwards/downwards effect
	 */
	public double gethVelo() {return this.hVelo;}

	public MassDestructionAbility(Player p) {
		this(p, 10);
	}
	
	/**
	 * 
	 * @param p The Player type user for this instance
	 */
	public MassDestructionAbility(Player p, double range){
		super(ABILITY_NAME, p);
		
		handler = new BaseMMOEventHandler<EntityDamageEvent>(
				new StringBuilder().append(p.getName()).append(ABILITY_NAME).append("EntityDamageEvent").toString(),
				EventPriority.MONITOR, 1) {
			
			public boolean ignoreCancelled() {
				return false;
			}
			
			public Class<EntityDamageEvent> getEventType() {
				return EntityDamageEvent.class;
			}
			
			public String getEventName() {
				return "EntityDamageEvent";
			}
			
			public void execute(EntityDamageEvent event) {
				if (event.getCause() != DamageCause.FALL || event.getEntityType() != EntityType.PLAYER)
					return;
				
				Player p = (Player) event.getEntity();
				
				if (p.equals(MassDestructionAbility.this.getPlayer())) {
					event.setCancelled(true);
					MassDestructionAbility.this.setActive(false).eventExecute();
				}
			}
		};
		
		this.isActive = false;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		NoxMMO instance = NoxMMO.getInstance();
		
		final Player p = getPlayer();
		
		Vector up = p.getLocation().getDirection();
		up.setY(gethVelo());
		Vector down = p.getLocation().getDirection();
		down.setY(-gethVelo() * 2);
		
		SetVelocityRunnable shootUp = new SetVelocityRunnable(getEntity(), up);
		SetVelocityRunnable shootDown = new SetVelocityRunnable(getEntity(), down);
		
		shootUp.runTask(instance);
		shootDown.runTaskLater(instance, 20);
		
		setActive(true);
	
		return true;
	}
	
	public void eventExecute() {
		
		Player p = getPlayer();
		Location pLoc = p.getLocation();
		
		int range = (int) Math.ceil(getRange());
		
		new ParticleRunner(ParticleType.largeexplode, pLoc.add(0, 1, 0), true, 10, 3, 1).start(0);
		new ShockWaveAnimation(pLoc, 1, range, 0.35, true).start(0);
		new ExpandingDamageRunnable(p, p.getLocation(), getDamage(), range, 1).start(0);
		
	}

}
