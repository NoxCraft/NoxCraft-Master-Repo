package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

/**
 * @author NoxPVP
 *
 */
public class BankShotAbility extends BasePlayerAbility implements IPVPAbility {
	
	private static final String ABILITY_NAME = "Bank Shot";
	public static final String PERM_NODE = "bank-shot";
	
	public void eventExecute(Arrow a){
		
		if (!this.arrows.contains(a))
			return;
		
		if (!(a.getShooter() instanceof Player))
			return;
		
		Entity e = null;
		
		for (Entity it :a.getNearbyEntities(20, 20, 20)){
			
			if (!(it instanceof LivingEntity || it == a)) continue;
			
			if (!hitPlayers && it instanceof Player) continue;

			if (!hitSelf && it == a.getShooter()) continue;

			if (!hitCreatures && it instanceof Creature) continue;
			
			Entity losChecker = a.getWorld().spawnEntity(a.getLocation(), EntityType.BAT);
			
			((LivingEntity)losChecker).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5, 1));
			
			if (!((LivingEntity) losChecker).hasLineOfSight(it)){
				losChecker.remove();
				continue;
			}
			losChecker.remove();
			
			e = it;
			break;
		}
		if (e == null)
			return;
		
		Location pLoc = a.getLocation();
		
		new ParticleRunner(ParticleType.explode, e, false, 0, 2, 3).start(0);
		a.setVelocity(e.getLocation().toVector().subtract(pLoc.toVector()));
		
		return;
	}
	
	private BaseMMOEventHandler<ProjectileHitEvent> hitHandler;
	private BaseMMOEventHandler<ProjectileLaunchEvent> launchHandler;
	
	private List<Arrow> arrows = new ArrayList<Arrow>();
	
	private boolean firing = false;
	private boolean active = false;
	
	private long firingTicks = 100L; //5 seconds default
	
	private int range = 20;
	private boolean hitPlayers = true;
	private boolean hitCreatures = true;
	private boolean hitSelf = false;
	private boolean singleShot = true;
	
	public long getFiringTicks() { return firingTicks; }
	public double getFiringSeconds() { return (firingTicks / 20); }
	
	public BankShotAbility setSingleShotMode(boolean single) { this.singleShot = single; return this; }
	public boolean isSingleShotMode() { return this.singleShot; }
	
	public BankShotAbility setFiringSeconds(double seconds) { return setFiringTicks(Math.round(seconds * 20)); }
	
	public BankShotAbility setFiringTicks(long l) { firingTicks = l; return this; }
	
	/**
	 * Sets wheter or not the listener is actively Listening.
	 * <br/>
	 * <b>This is mostly an internal method.</b>
	 * 
	 * @param active what to set to.
	 * @return this
	 */
	public BankShotAbility setActive(boolean active) {
		boolean changed = this.active != active;
		this.active = active;
		
		MasterListener m = NoxMMO.getInstance().getMasterListener();
		
		if (changed)
			if (active)
				m.registerHandler(hitHandler);
			else
				m.unregisterHandler(hitHandler);
		
		return this; 
	}
	
	/**
	 * Sets whether or not the bow shot listener is actively listening.
	 * <br/>
	 * <b>This is mostly an internal method.</b>
	 * 
	 * @param firing what to set to.
	 * @return this
	 */
	public BankShotAbility setFiring(boolean firing) {
		boolean changed = this.firing != firing;
		this.firing = firing;
		
		MasterListener m = NoxMMO.getInstance().getMasterListener();
		
		if (changed)
			if (firing)
				m.registerHandler(launchHandler);
			else
				m.unregisterHandler(launchHandler);
		
		return this; 
		
	}
	
	/**
	 * Tells whether or not we are actively using ability.
	 * @return true or false if we are listening for arrow hit events.
	 */
	public boolean isActive() { return active; }
	
	/**
	 * Tells whether or not we are actively shooting arrows bot.
	 * @return true or false if we are listing for bow shoot events.
	 */
	public boolean isFiring() { return firing; }
	
	
	/**
	 * 
	 * 
	 * @return Integer The currently set range for this ability instance
	 */
	public int getRange() {return range;}
	
	/**
	 * 
	 * 
	 * @param range The Integer range that the ricochet'd arrow should look for arrows target
	 * @return BankShotAbility This instance, used for chaining
	 */
	public BankShotAbility setRange(int range) {this.range = range; return this;}
	
	/**
	 * 
	 * 
	 * @return Boolean if the ricochet'd arrow will look for players as targets
	 */
	public boolean isHitPlayers() {return hitPlayers;}
	
	/**
	 * 
	 * 
	 * @param hitPlayers Boolean is the ricochet'd arrow should look for players as targets
	 * @return
	 */
	public BankShotAbility setHitPlayers(boolean hitPlayers) {this.hitPlayers = hitPlayers; return this;}
	
	/**
	 * 
	 * 
	 * @return Boolean If the ricochet'd arrow will look for Creature types as targets
	 */
	public boolean isHitCreatures() {return hitCreatures;}
	
	/**
	 * 
	 * 
	 * @param hitCreatures Boolean if the ricochet'd arrow should look for Creature types as targets
	 * @return
	 */
	public BankShotAbility setHitCreatures(boolean hitCreatures) {this.hitCreatures = hitCreatures; return this;}
	
	/**
	 * 
	 * 
	 * @return Boolean If the ricochet'd arrow can consider the shooter as arrows target
	 */
	public boolean isHitSelf() {return hitSelf;}
	
	/**
	 * 
	 * 
	 * @param hitSelf Boolean if the ricochet'd arrow should consider the shooter as arrows target
	 * @return BankShotAbility this instance, used for chaining
	 */
	public BankShotAbility setHitSelf(boolean hitSelf) {this.hitSelf = hitSelf; return this;}
	
	public BankShotAbility(Player player){
		super(ABILITY_NAME, player);
		
		hitHandler = new BaseMMOEventHandler<ProjectileHitEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileHitEvent").toString(),
				EventPriority.NORMAL, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public void execute(ProjectileHitEvent event) {
				if (event.getEntity().equals(BankShotAbility.this.arrows))
					BankShotAbility.this.eventExecute((Arrow) event.getEntity());
			}

			public Class<ProjectileHitEvent> getEventType() {
				return ProjectileHitEvent.class;
			}

			public String getEventName() {
				return "ProjectileHitEvent";
			}
		};
		
		launchHandler = new BaseMMOEventHandler<ProjectileLaunchEvent>(
			new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("ProjectileLaunchEvent").toString(), 
			EventPriority.MONITOR, 1) {
		
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<ProjectileLaunchEvent> getEventType() {
				return ProjectileLaunchEvent.class;
			}
			
			public String getEventName() {
				return "ProjectileLaunchEvent";
			}
			
			public void execute(ProjectileLaunchEvent event) {
				if (event.getEntityType() != EntityType.ARROW)
					return;
				
				Arrow arrow = (Arrow) event.getEntity();
				if (!(arrow.getShooter() instanceof Player))
					return;
				
				Player shooter = (Player) arrow.getShooter();
				if (!shooter.equals(BankShotAbility.this.getPlayer()))
					return;
				
				if (isSingleShotMode() && !arrows.isEmpty())
					arrows.clear();
				
				arrows.add(arrow);
				setActive(true);
				
				if (isSingleShotMode())
					setFiring(false);
			}
		};
	}

	public boolean execute() {
		if (!mayExecute())
			return false;

		if (!isFiring() && !isActive())
		{
			setFiring(true);
			
			MessageUtil.sendLocale(NoxMMO.getInstance(), getPlayer(), "ability.activated", "Bank Shot Ability");
			return true;
		} else {
			MessageUtil.sendLocale(NoxMMO.getInstance(), getPlayer(), "ability.already-active", ABILITY_NAME);
			return false;
		}
	}
	
}
