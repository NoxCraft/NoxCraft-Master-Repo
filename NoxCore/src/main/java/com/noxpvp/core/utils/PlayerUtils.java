package com.noxpvp.core.utils;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

public class PlayerUtils {
	
	public static boolean hasItems(Player p, ItemStack item){
		return hasItems(p.getInventory(), item);
	}
	
	public static boolean hasItems(Inventory inv, ItemStack item){
		Material type = item.getType();
		int totals = 0;
		
		for(ItemStack s : inv.getContents()){
			if (s.getType() != type) continue;
			
			totals += s.getAmount();
			
		}
		
		return totals >= item.getAmount();
		
	}
	
	/**
	 * @author burgerkiller - https://forums.bukkit.org/threads/determine-whether-the-player-can-see-a-mob.56129/
	 * 
	 */
	public static class LineOfSightUtil{
		public static Block getTarget(Location from, int distance, byte... transparentTypeIds) {
			if (transparentTypeIds == null || transparentTypeIds.length == 0) {
				return getTarget(from, distance, (Set<Byte>) null);
			} else {
				Set<Byte> types = new HashSet<Byte>(transparentTypeIds.length);
				for (byte b : transparentTypeIds) types.add(b);
				return getTarget(from, distance, types);
			}
		}
		public static Block getTarget(Location from, int distance, Set<Byte> transparentTypeIds) {
			BlockIterator itr = new BlockIterator(from, 0, distance);
			while (itr.hasNext()) {
				Block block = itr.next();
				int id = block.getTypeId();
				if (transparentTypeIds == null) {
					if (id == 0) continue;
				} else if (transparentTypeIds.contains((byte) id)) {
					continue;
				}
				return block;
			}
			return null;
		}
		public static Block getTarget(LivingEntity from, int distance, Set<Byte> transparentTypeIds) {
			Location from2 = from.getEyeLocation();
			from2.setPitch(0);
			from2.setYaw(0);
			
			return getTarget(from2, distance, transparentTypeIds);
		}
		public static Block getTarget(LivingEntity from, int distance, byte... transparentTypeIds) {
			return getTarget(from.getEyeLocation(), distance, transparentTypeIds);
		}
	    public static boolean canSee(LivingEntity from, Location to, Set<Byte> transparentTypeIds) {
	        return getTarget(from, (int) Math.ceil(from.getLocation().distance(to)), transparentTypeIds) == null;
	    }
		public static boolean canSee(LivingEntity from, Location to, byte... transparentTypeIds) {
			return getTarget(from, (int) Math.ceil(from.getLocation().distance(to)), transparentTypeIds) == null;
		}
		
	}

}
