package com.noxpvp.mmo.runnables;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.mmo.NoxMMO;

public class ShockWaveAnimation extends BukkitRunnable{
		private int shockSpeed;
		private int shockRange;
		private Vector shockVelo;
		private int i;
		private Block center;
		private HashSet<FallingBlock> novaBlocks;
		private List<Material> flowers;
		private int taskId;
        
        public ShockWaveAnimation(Location shockCenter, int shockSpeed, int shockRange, int shockVelo) {
        	this.shockSpeed = shockSpeed;
        	this.shockRange = shockRange;
        	this.shockVelo = new Vector().setY(shockVelo);
        	this.i = 0;
        	this.center = shockCenter.getBlock().getRelative(BlockFace.DOWN);
        	this.novaBlocks = new HashSet<FallingBlock>();
        	this.flowers = Arrays.asList(Material.LONG_GRASS, Material.RED_ROSE, Material.YELLOW_FLOWER, Material.CROPS, Material.DEAD_BUSH, Material.VINE, Material.SAPLING);
        	
        	taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(NoxMMO.getInstance(), this, 0, shockSpeed);
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
        
    	public void start() {
			runTaskTimer(NoxMMO.getInstance(), 0, shockSpeed);
		}
    	
        @SuppressWarnings("deprecation")
		public void run() {
            // remove old blocks
            for (FallingBlock block : novaBlocks) {
            	block.setDropItem(false);
            }
            novaBlocks.clear();
            
            i += 1;
            if (i <= shockRange) {
                // do next ring
                int bx = center.getX();
                int y = center.getY();
                int bz = center.getZ();
                for (int x = bx - i; x <= bx + i; x++) {
                    for (int z = bz - i; z <= bz + i; z++) {
                        if (Math.abs(x-bx) == i || Math.abs(z-bz) == i) {
                        	Block b = center.getWorld().getBlockAt(x,y+3,z);//+3 - max height above location
                            
                            while(!isThrowable(b.getType()) && b.getLocation().getY() >= (center.getY()-3)){
                            	b = b.getRelative(BlockFace.DOWN);
                            }
                            if (flowers.contains(b.getType())) {b.breakNaturally();}
                            if (b.getType() == Material.GRASS) {b.setType(Material.DIRT);}
                            
                            final FallingBlock nb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
                            final FallingBlock nb2 = b.getWorld().spawnFallingBlock(b.getRelative(0, 1, 0).getLocation(), b.getType(), b.getData());
                            nb.setVelocity(shockVelo);
                            nb2.setVelocity(shockVelo);
                            novaBlocks.add(nb);
                            novaBlocks.add(nb2);
                            
                        }
                    }
                }
            } else if (i > shockRange+1) {
            	// stop if done
                cancel();
                }
            }
        }