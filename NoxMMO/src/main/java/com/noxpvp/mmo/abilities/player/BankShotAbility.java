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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class BankShotAbility extends BasePlayerAbility{
	
	public static final String PERM_NODE = "bankshot";
	private static final String ABILITTY_NAME = "Bank Shot";
	
	private static Map<String, Arrow> abilityQue = new HashMap<String, Arrow>();
	
	private Arrow a;
	
	private int range;
	private boolean hitPlayers = true;
	private boolean hitCreatures = true;
	private boolean hitSelf = false;
	
	public static boolean eventExecute(String name, Arrow a){
		BankShotAbility ab = null;
		
		if (!(a.getShooter() instanceof Player))
			return false;
		
		Entity e = null;
		
		for (Entity it :a.getNearbyEntities(20, 20, 20)){
			
			if (!(it instanceof LivingEntity || it == a)) continue;
			
			if (!ab.hitPlayers && it instanceof Player) continue;

			if (!ab.hitSelf && it == a.getShooter()) continue;

			if (!ab.hitCreatures && it instanceof Creature) continue;
			
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
		
		Arrow a2 = a.getWorld().spawnArrow(pLoc, eLoc.toVector().subtract(pLoc.toVector()), (float) 3, (float) 0);
		a2.setShooter(ab.getPlayer());
		a.remove();
		
		return true;
	}
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
		
		this.a = a;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		final String name = p.getName();
		
		abilityQue.put(name, this.a);
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}
}
