package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class BankShotAbility extends BasePlayerAbility{
	
	public final static String PERM_NODE = "bankshot";
	private final static String ABILITTY_NAME = "Bank Shot";
	private Projectile a;
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
	public BankShotAbility(Player player, Arrow proj){
		super(ABILITTY_NAME, player);
		this.a = proj;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
			
		if (!(a.getShooter() instanceof Player))
			return false;
		
		Entity e = null;
		
		for (Entity it :a.getNearbyEntities(20, 20, 20)){
			
			if (!(it instanceof LivingEntity || it == a)) continue;
			
			if (!hitPlayers && it instanceof Player) continue;

			if (!hitSelf && it == a.getShooter()) continue;

			if (!hitCreatures && it instanceof Creature) continue;
			
			Entity losChecker = a.getWorld().spawnEntity(a.getLocation(), EntityType.BAT);
			
			((LivingEntity)losChecker).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20, 1));
			
			if (!((LivingEntity) losChecker).hasLineOfSight(it)){
				losChecker.remove();
				continue;
			}
			losChecker.remove();
			
			e = it;
			break;
		}
		if (e == null)
			return false;
		
		Location pLoc = a.getLocation();
		Location eLoc = e.getLocation();
		Vector vector = eLoc.toVector().subtract(pLoc.toVector());
		
		Arrow a2 = a.getWorld().spawnArrow(pLoc, vector, (float) 3, (float) 0);
		a2.setShooter(getPlayer());
		a.remove();
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		if (getPlayer() == null)
			return false;
		if (this.a == null)
			return false;
		
		return true;
	}
}
