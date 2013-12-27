package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.listeners.BaseMMOEventHandler;

/**
 * @author NoxPVP
 *
 */
public class BankShotAbility extends BasePlayerAbility{
	
	public static final String PERM_NODE = "bankshot";
	private static final String ABILITTY_NAME = "Bank Shot";
	
	private static Map<Arrow, BankShotAbility> abilityQue = new HashMap<Arrow, BankShotAbility>();
	
	
	public void eventExecute(Arrow a){
		BankShotAbility ab = null;
		
		if (BankShotAbility.abilityQue.containsKey(a)) {
			ab = BankShotAbility.abilityQue.get(a);
		} else return;
		
		if (!(a.getShooter() instanceof Player))
			return;
		
		Entity e = null;
		
		for (Entity it :a.getNearbyEntities(20, 20, 20)){
			
			if (!(it instanceof LivingEntity || it == a)) continue;
			
			if (!ab.hitPlayers && it instanceof Player) continue;

			if (!ab.hitSelf && it == a.getShooter()) continue;

			if (!ab.hitCreatures && it instanceof Creature) continue;
			
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
		Location eLoc = e.getLocation();
		
		a.setVelocity(eLoc.toVector().subtract(pLoc.toVector()));
		
		return;
	}
	
	private BaseMMOEventHandler<ProjectileHitEvent> handler;
	private Arrow a;
	
	private int range;
	private boolean hitPlayers = true;
	private boolean hitCreatures = true;
	private boolean hitSelf = false;
	
	/**
	 * 
	 * 
	 * @return Integer The currently set range for this ability instance
	 */
	public int getRange() {return range;}
	
	/**
	 * 
	 * 
	 * @param range The Integer range that the ricochet'd arrow should look for a target
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
	 * @return Boolean If the ricochet'd arrow can consider the shooter as a target
	 */
	public boolean isHitSelf() {return hitSelf;}
	
	/**
	 * 
	 * 
	 * @param hitSelf Boolean if the ricochet'd arrow should consider the shooter as a target
	 * @return BankShotAbility this instance, used for chaining
	 */
	public BankShotAbility setHitSelf(boolean hitSelf) {this.hitSelf = hitSelf; return this;}
	
	/**
	 * 
	 * 
	 * @param player The Player type used for this ability instance
	 * @param proj The Arrow type projectile used for this ability instance
	 */
	public BankShotAbility(Player player, Arrow a){
		super(ABILITTY_NAME, player);
		
		handler = new BaseMMOEventHandler<ProjectileHitEvent>(
				new StringBuilder().append(player.getName()).append(ABILITTY_NAME).append("ProjectileHitEvent").toString(),
				EventPriority.NORMAL, 1) {

					@Override
					public boolean ignoreCancelled() {
						return true;
					}

					@Override
					public void execute(ProjectileHitEvent event) {
						if (event.getEntity().getType() != EntityType.ARROW) {
							return;
						} else BankShotAbility.this.eventExecute((Arrow) event.getEntity());
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
		
		this.a = a;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		abilityQue.put(this.a, this);
		
		return true;
	}
	
}
