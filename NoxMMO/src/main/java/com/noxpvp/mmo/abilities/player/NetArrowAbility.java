package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.BlockTimerRunnable;

public class NetArrowAbility extends BasePlayerAbility{
	
	static Map<String, NetArrowAbility> abilityCue = new HashMap<String, NetArrowAbility>();
	
	public final static String ABILITY_NAME = "Net Arrow";
	public final static String PERM_NODE = "net-arrow";
	
	/**
	 * Runs the event-side execution of this ability
	 * 
	 * @param p Player, normally arrow shooter from a projectile hit event
	 * @param loc The location to make the net, normally the location of an arrow
	 * @return boolean If the execution ran successfully
	 */
	public static boolean eventExecute(Player p, Location loc){
		String name = p.getName();
		
		if (abilityCue.containsKey(name))
			return false;
		
		NetArrowAbility ab = abilityCue.get(name);
		List<Block> net = new ArrayList<Block>();
		
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		int z = (int) loc.getZ();
		int size = ab.getSize();
		
		for (int by = y+(size/2); by > y-(size/2); by--){
			for (int bx = x+(size/2); bx > x-(size/2); bx--){
				for (int bz = z+(size/2); bz > z-(size/2); bz--){
					Block b = p.getWorld().getBlockAt(bx, by, bz);
					
					if (!isNetable(b.getType())) continue;
					
					b.setType(Material.WEB);
					net.add(b);
					
				}
			}
		}
		
		BlockTimerRunnable netRemover = new BlockTimerRunnable(net, Material.AIR, Material.WEB);
		netRemover.runTaskLater(NoxMMO.getInstance(), ab.getTime());
		return true;
	}
	private static boolean isNetable(Material type){
		switch(type){
			case AIR:
			case LONG_GRASS:
			case CROPS:
			case VINE:
			case WATER_LILY:
			case FLOWER_POT:
			case CARROT:
			case POTATO:
				return true;
			default:
				return false;
		}
	}
	
	private int size;
	private int time;

	/**
	 * Get the currently set size of the net
	 * 
	 * @return Integer The net size
	 */
	public int getSize() {return size;}

	/**
	 * Sets the size of the net
	 * 
	 * @param size The width of the net itself. This should be a odd number
	 * @return NetArrowAbility This instance
	 */
	public NetArrowAbility setSize(int size) {this.size = size; return this;}

	/**
	 * Gets the time the net will stay until removed
	 * 
	 * @return Integer Time in ticks
	 */
	public int getTime() {return time;}

	/**
	 * Sets the time in ticks that the net will stay
	 * 
	 * @param time The time in ticks
	 * @return NetArrowAbility This instance
	 */
	public NetArrowAbility setTime(int time) {this.time = time; return this;}

	public NetArrowAbility(Player player) {
		super(ABILITY_NAME, player);
		
		this.size = 3;
		this.time = 100;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final String pName = getPlayer().getName();
		
		NetArrowAbility.abilityCue.put(getPlayer().getName(), this);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
			
			public void run() {
				
				if (NetArrowAbility.abilityCue.containsKey(pName))
					NetArrowAbility.abilityCue.remove(pName);
				
			}
		}, 100);
		
		return true;
	}

}
