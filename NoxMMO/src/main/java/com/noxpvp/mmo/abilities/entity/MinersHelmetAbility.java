package com.noxpvp.mmo.abilities.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.bases.IntVector2;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutMultiBlockChange;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.utils.BlockChangeArray;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class MinersHelmetAbility extends BaseEntityAbility {

	public final static String ABILITY_NAME = "Miner's Helmet";
	public final static String PERM_NODE = "miners-helmet";
	
	private int duration;
	private int speed;
	
	/**
	 * Get the duration is seconds
	 * 
	 * @return Integer seconds
	 */
	public int getDuration() {return duration;}

	/**
	 * Sets the duration in seconds for this ability
	 * 
	 * @param duration
	 * @return MinersHelmetAbility This instance
	 */
	public MinersHelmetAbility setDuration(int duration) {this.duration = duration; return this;}

	/**
	 * Gets the speed in ticks for the block changer checks
	 * 
	 * @return Integer Speed in ticks
	 */
	public int getSpeed() {return speed;}

	/**
	 * Sets the speed in ticks for the block changer checks
	 * 
	 * @param speed
	 * @return MinersHelmetAbility This instance
	 */
	public MinersHelmetAbility setSpeed(int speed) {this.speed = speed; return this;}

	public MinersHelmetAbility(Entity e) {
		super(ABILITY_NAME, e);
		
		this.duration = 120;
		this.speed = 10;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
	
		new MinersHelmet(getEntity(), duration * (20 / speed)).runTaskTimer(NoxMMO.getInstance(), 0, speed);
		
		return true;
	}

	private class MinersHelmet extends BukkitRunnable{
	
		private Entity e;
		
		private int runs;
		private int runsLimit;
		
		private Block b;
		private Material oldBlock;
		private Location last;
		
		CommonPacket commonFakeBlock;
		NMSPacketPlayOutMultiBlockChange fakeBlock;
	
		public MinersHelmet(Entity e, int runsLimit) {
			this.e = e;
			
			this.runs = 0;
			
			this.runsLimit = runsLimit;
			this.last = e.getLocation();
			
			commonFakeBlock = new CommonPacket(PacketType.OUT_MULTI_BLOCK_CHANGE);
			fakeBlock = new NMSPacketPlayOutMultiBlockChange();
		}
	
		public void safeCancel() {
			try { cancel(); } catch (IllegalStateException e) {}
		}
		
		public void run() {
			
			Location loc = e.getLocation();
			
			if (runs > 0) {
				if (!((int) loc.getX() != (int) last.getX() || (int) loc.getZ() != (int) last.getZ() || (int) loc.getY() - 1 != (int) last.getY())) {
					runs++;
					return;
				}
				
				updateBlock(last, oldBlock);
			}
			
			if (runs > runsLimit){
				updateBlock(last, oldBlock);
				safeCancel();
				return;
			}
			
			if (e.isOnGround()) {
					loc.setY(loc.getY() - 1);
					b = e.getWorld().getBlockAt(loc);
					
					last = loc;
					oldBlock = b.getType();
					
					updateBlock(loc, Material.GLOWSTONE);
	
			} else {
				runs++;
				return;
			}		
			
			runs++;
		}
	
		private void updateBlock(Location loc, Material BlockType) {
			
			try {
				
				commonFakeBlock = new CommonPacket(PacketType.OUT_MULTI_BLOCK_CHANGE);
				fakeBlock = new NMSPacketPlayOutMultiBlockChange();
				
				BlockChangeArray change = new BlockChangeArray(1);
				
				change.getBlockChange(0).
				setRelativeX((int) loc.getX()).
				setRelativeZ((int) loc.getZ()).
				setAbsoluteY((int) loc.getY()).
				setBlockID(BlockType.getId());
				
				commonFakeBlock.write(fakeBlock.chunk, new IntVector2(last.getChunk()));
				commonFakeBlock.write(fakeBlock.blockCount, 1);
				commonFakeBlock.write(fakeBlock.blockData, change.toByteArray());
				
				PacketUtil.broadcastPacket(commonFakeBlock, false);
			}
			catch (IllegalArgumentException e) {e.printStackTrace();}
			catch (Exception e) {e.printStackTrace();}
			
		}
		
	}

}
