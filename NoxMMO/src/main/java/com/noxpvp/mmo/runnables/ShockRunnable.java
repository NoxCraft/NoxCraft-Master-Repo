package com.noxpvp.mmo.runnables;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.NoxMMO;

public class ShockRunnable implements Runnable{
	private int shockRange;
	private Vector shockVelo;
	private double percentThrown;
	private Block center;
	private HashSet<FallingBlock> shockBlocks;
	private List<Material> flowers;
	
	public ShockRunnable(Location shockCenter, int shockRange, double shockVelo, double percentThrown, int delay) {
		this.center = shockCenter.getBlock();
		this.shockRange = shockRange;
		this.shockVelo = new Vector().zero();
		this.percentThrown = percentThrown;
		this.shockBlocks = new HashSet<FallingBlock>();
		this.flowers = Arrays.asList(Material.LONG_GRASS, Material.RED_ROSE, Material.YELLOW_FLOWER, Material.CROPS, Material.DEAD_BUSH, Material.VINE, Material.SAPLING);
		
		Bukkit.getServer().getScheduler().runTaskLater(NoxMMO.getInstance(), this, delay);
	}
	
	private boolean isThrowable(Material type){
		switch(type){
			case AIR:
			case SAPLING:
			case POWERED_RAIL:
			case DETECTOR_RAIL:
			case TORCH:
			case FIRE:
			case REDSTONE_WIRE:
			case CROPS:
			case LADDER:
			case RAILS:
			case LEVER:
			case REDSTONE_TORCH_OFF:
			case REDSTONE_TORCH_ON:
			case STONE_BUTTON:
			case SUGAR_CANE_BLOCK:
			case PORTAL:
			case DIODE_BLOCK_OFF:
			case DIODE_BLOCK_ON:
			case PUMPKIN_STEM:
			case MELON_STEM:
			case VINE:
			case WATER_LILY:
			case NETHER_WARTS:
			case ENDER_PORTAL:
			case COCOA:
			case TRIPWIRE_HOOK:
			case TRIPWIRE:
			case FLOWER_POT:
			case CARROT:
			case POTATO:
			case WOOD_BUTTON:
			case SKULL:
			case REDSTONE_COMPARATOR_OFF:
			case REDSTONE_COMPARATOR_ON:
			case ACTIVATOR_RAIL:
			case CARPET:
			case LEAVES:
				return false;
			default:
				return true;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
		
		int bx = center.getX();
		int y = center.getY();
		int bz = center.getZ();
		
		for (int x = bx - shockRange; x <= bx + shockRange; x++) {
			for (int z = bz - shockRange; z <= bz + shockRange; z++) {
				Block b = center.getWorld().getBlockAt(x,y+3,z);
				
				while(!isThrowable(b.getType()) && b.getLocation().getY() >= (center.getY()-3)){
					b = b.getRelative(BlockFace.DOWN);
				}
				
				if (Math.random() * 100 > percentThrown) continue;
				
				if (flowers.contains(b.getType())) {b.breakNaturally();}
				if (b.getType() == Material.GRASS) {b.setType(Material.DIRT);}
				
				final FallingBlock nb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
				nb.setVelocity(shockVelo.setY(RandomUtils.nextDouble() + 0.45));
				nb.setDropItem(false);
				shockBlocks.add(nb);
			}
		}

		for (FallingBlock block : shockBlocks) {
			block.setDropItem(false);
		}
	}
}