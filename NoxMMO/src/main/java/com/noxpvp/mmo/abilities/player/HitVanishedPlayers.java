package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.utils.Vector3D;

public class HitVanishedPlayers extends BasePlayerAbility{
	
	private double range = 3.5;
	private Player e = null;
	
	/**
	 * @author Connor Stone
	 * 
	 * @return double - The currently set range for target distance
	 */
	public double getRange() {return range;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param range - The double range to look for targets
	 * @return HitVanishedPlayers - This instance, used for chaining
	 */
	public HitVanishedPlayers setRange(double range) {this.range = range; return this;}
	
	/**
	 * @author Connor Stone
	 * CREDIT: Comphenix @ bukkit forums
	 * 
	 * @param player - The Player type user for this ability instance
	 */
	public HitVanishedPlayers(Player player){
		super("Hit Vanished Players", player);
	}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Boolean - If this ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		for (Entity it : p.getNearbyEntities(range, range, range)){
			
			if (!(it instanceof Player)) continue;
			if (((Player)it).canSee(p)) continue;
			
			this.e = (Player) it;
			break;
		}
		if (this.e == null)
			return false;
		
		Location observerPos = p.getEyeLocation();
		Vector3D observerDir = new Vector3D(observerPos.getDirection());

		Vector3D observerStart = new Vector3D(observerPos);
		Vector3D observerEnd = observerStart.add(observerDir.multiply(range));
		
		Vector3D targetPos = new Vector3D(e.getLocation());
		Vector3D minimum = targetPos.add(-0.5, 0, -0.5); 
		Vector3D maximum = targetPos.add(0.5, 1.67, 0.5); 

		if (hasIntersection(observerStart, observerEnd, minimum, maximum)) {
			p.showPlayer(e);
			e.damage(1, p);
		}
		
		return true;
	}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Boolean - If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}
	
	private boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
		final double epsilon = 0.0001f;
 
		Vector3D d = p2.subtract(p1).multiply(0.5);
		Vector3D e = max.subtract(min).multiply(0.5);
		Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
		Vector3D ad = d.abs();
 
		if (Math.abs(c.x) > e.x + ad.x)
			return false;
		if (Math.abs(c.y) > e.y + ad.y)
			return false;
		if (Math.abs(c.z) > e.z + ad.z)
			return false;
 
		if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
			return false;
		if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
			return false;
		if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
			return false;
 
		return true;
	}

}
