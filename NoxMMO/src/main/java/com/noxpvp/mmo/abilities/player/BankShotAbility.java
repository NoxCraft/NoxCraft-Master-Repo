package com.noxpvp.mmo.abilities.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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

public class BankShotAbility extends BasePlayerAbility{
	
	private final static String ABILITTY_NAME = "Bank Shot";
	private Projectile p;
	private int range;
	private boolean hitPlayers = true;
	private boolean hitCreatures = true;
	private boolean hitSelf = false;
	
	public int getRange() {return range;}
	public BankShotAbility setRange(int range) {this.range = range; return this;}
	
	public boolean isHitPlayers() {return hitPlayers;}
	public BankShotAbility setHitPlayers(boolean hitPlayers) {this.hitPlayers = hitPlayers; return this;}
	
	public boolean isHitCreatures() {return hitCreatures;}
	public BankShotAbility setHitCreatures(boolean hitCreatures) {this.hitCreatures = hitCreatures; return this;}
	
	public boolean isHitSelf() {return hitSelf;}
	public BankShotAbility setHitSelf(boolean hitSelf) {this.hitSelf = hitSelf; return this;}
	
	public BankShotAbility(Player player, Projectile proj){
		super(ABILITTY_NAME, player);
		this.p = proj;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
			
		if (!(p.getShooter() instanceof Player))
			return false;
		
		Entity e = null;
		
		for (Entity it :p.getNearbyEntities(20, 20, 20)){
			
			if (!(it instanceof LivingEntity || it == p)) continue;
			
			if (!hitPlayers && it instanceof Player) continue;

			if (!hitSelf && it == p.getShooter()) continue;

			if (!hitCreatures && it instanceof Creature) continue;
			
			Entity losChecker = p.getWorld().spawnEntity(p.getLocation(), EntityType.BAT);
			
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
		
		Location pLoc = p.getLocation();
		Location eLoc = e.getLocation();
		Vector vector = eLoc.toVector().subtract(pLoc.toVector());
		
		p.getWorld().spawnArrow(pLoc, vector, (float) 3, (float) 0);
		p.remove();
		
		return true;
	}
	public boolean mayExecute() {
		if (getPlayer() == null)
			return false;
		if (this.p == null)
			return false;
		
		return true;
	}
}
