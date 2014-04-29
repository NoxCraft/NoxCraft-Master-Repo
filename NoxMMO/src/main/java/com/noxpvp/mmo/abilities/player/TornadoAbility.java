package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;

public class TornadoAbility extends BasePlayerAbility implements PVPAbility {

	public final static String ABILITY_NAME = "Tornado";
	public final static String PERM_NODE = "tornado";
	
	private Location loc;
	private int amount_of_blocks; 
	private Material material;
	private long time;
	
	private Vector direction;
	
	/**
	 * Spawns a tornado at the given location l.
	 * 
	 * @param location
	 *            - Location to spawn the tornado.
	 * @param material
	 *            - The base material for the tornado.
	 * @param blockLimit
	 *            - The max amount of blocks that can exist in the tornado.
	 * @param direction
	 *            - The direction the tornado should move in.
	 * @param time
	 *            - The amount of ticks the tornado should be alive.
	 */
	public TornadoAbility(Player p, Location loc, Material type, int blockLimit, Vector direction, long time) {
		super(ABILITY_NAME, p);
		
		this.loc = loc;
		this.amount_of_blocks = blockLimit;
		this.material = type;
		this.time = time;
		
		this.direction = direction;
		
	}

	public boolean execute() {
		
		return new TornadoAbility(getPlayer(), loc, material, amount_of_blocks, direction, time).execute();
		
	}

}
